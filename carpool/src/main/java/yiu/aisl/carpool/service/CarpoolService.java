package yiu.aisl.carpool.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import yiu.aisl.carpool.Dto.CarpoolDto;
import yiu.aisl.carpool.Dto.CarpoolRequest;
import yiu.aisl.carpool.Dto.WaitDto;
import yiu.aisl.carpool.Dto.WaitRequest;
import yiu.aisl.carpool.domain.Carpool;
import yiu.aisl.carpool.domain.User;
import yiu.aisl.carpool.domain.Wait;
import yiu.aisl.carpool.exception.CustomException;
import yiu.aisl.carpool.exception.ErrorCode;
import yiu.aisl.carpool.repository.CarpoolRepository;
import yiu.aisl.carpool.repository.UserRepository;
import yiu.aisl.carpool.repository.WaitRepository;
import yiu.aisl.carpool.security.CustomUserDetails;
import yiu.aisl.carpool.security.JwtProvider;


@Service
@Transactional
@RequiredArgsConstructor
public class CarpoolService {
  private final CarpoolRepository carpoolRepository;
  private final JwtProvider jwtProvider;
  private final HttpServletRequest httpServletRequest;
  private final UserRepository userRepository;
  private final WaitRepository waitRepository;

  public boolean create(CarpoolRequest request) throws Exception {
    if (request.getMemberNum() == 0) {
      throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
    } else if (request.getStart().isEmpty() || request.getEnd().isEmpty() || (request.getDate() == null)) {
      throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
    }
    try {
      LocalDateTime createdAt = LocalDateTime.now();
      String authHeader = httpServletRequest.getHeader("Authorization");
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        String email = jwtProvider.getEmail(token);

        // 사용자 정보 조회
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
          if (user.get().getStatus() == 0) {
            throw new IllegalArgumentException("차주 모드로 변경해 주세요.");
          }
          else {
            Carpool carpool = Carpool.builder()
                .carpoolNum(request.getCarpoolNum())
                .start(request.getStart())
                .end(request.getEnd())
                .date(request.getDate())
                .checkNum(request.getCheckNum())
                .memberNum(request.getMemberNum())
                .email(email)
                .createdAt(createdAt)
                .build();
            carpoolRepository.save(carpool);
          }
        }else throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
      }
    } catch (DataIntegrityViolationException e) {
      System.out.println(e.getMessage());
      throw new IllegalArgumentException("잘못된 요청입니다.");
    }
    return true;
  }

  public boolean apply(WaitRequest request, Integer carpoolNum) throws Exception {
    try {
      LocalDateTime createdAt = LocalDateTime.now();
      String authHeader = httpServletRequest.getHeader("Authorization");

      if(authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        String email = jwtProvider.getEmail(token);  // 현재 로그인한 사용자의 이메일
        Optional<Carpool> carpoolOptional = carpoolRepository.findByCarpoolNum(carpoolNum);
        if(carpoolOptional.isPresent()) {
          Carpool carpool = carpoolOptional.get();
          String ownerEmail = carpool.getEmail();  // 게시물을 작성한 사용자의 이메일
          if(carpool.getCheckNum() == 3) {
            throw new CustomException(ErrorCode.Application_Deadline); // 신청이 마감된 경우
          }

          // 사용자가 이미 해당 carpoolNum에 대해 신청한 기록이 있는지 확인
          boolean alreadyApplied = waitRepository.existsByCarpoolNumAndGuest(carpool, email);

          if (!alreadyApplied) {
            if (!email.equals(ownerEmail)) {
              int memberNum = carpool.getMemberNum();
              if (memberNum > 0) {
                Wait wait = Wait.builder()
                    .waitNum(request.getWaitNum())
                    .carpoolNum(carpool)
                    .guest(email)
                    .owner(ownerEmail)
                    .checkNum(request.getCheckNum())
                    .createdAt(createdAt)
                    .build();
                waitRepository.save(wait);
              } else {
                throw new CustomException(ErrorCode.Number_Of_Applications_Exceeded);
              }
            } else {
              throw new CustomException(ErrorCode.Post_Written_By_Me);
            }
          } else {
            throw new CustomException(ErrorCode.Already_Applied);
          }
        }
      }
    } catch (DataIntegrityViolationException e) {
      System.out.println(e.getMessage());
      throw new IllegalArgumentException("잘못된 요청입니다.");
    }
    return true;
  }
  public void decide(CustomUserDetails userDetails, Integer carpoolNum, Integer waitNum, WaitDto waitDto){
    String email = userDetails.getUser().getEmail();
    Optional<Wait> waitOptional = waitRepository.findByOwnerAndCarpoolNum_CarpoolNumAndWaitNum(email,carpoolNum,waitNum);
    Optional<Carpool> carpoolOptional = carpoolRepository.findByCarpoolNum(carpoolNum);
    Carpool carpool = carpoolOptional.get();
    if(carpool.getMemberNum() == 0) throw new CustomException(ErrorCode.Number_Of_Applications_Exceeded);
    if (waitOptional.isPresent()){
      Wait wait = waitOptional.get();
      if(wait.getCheckNum() == 1) {
        throw new CustomException(ErrorCode.Already_Accept);
      }
      wait.setCheckNum(waitDto.getCheckNum());
      if (waitDto.getCheckNum()==1)
        // 게시물의 신청 인원 수 - 1
        carpool.setMemberNum(carpool.getMemberNum() - 1);
        if(carpool.getMemberNum() == 0) {
          carpool.setCheckNum(2);
        } else carpool.setCheckNum(1);
      waitRepository.save(wait);
      carpoolRepository.save(carpool);
    } else {
      throw new IllegalArgumentException("찾을수가 없습니다.");
    }
  }
  public void deny(CustomUserDetails userDetails, Integer carpoolNum, Integer waitNum, WaitDto waitDto){
    String email = userDetails.getUser().getEmail();
    Optional<Wait> waitOptional = waitRepository.findByOwnerAndCarpoolNum_CarpoolNumAndWaitNum(email,carpoolNum,waitNum);
    Optional<Carpool> carpoolOptional = carpoolRepository.findByCarpoolNum(carpoolNum);
    Carpool carpool = carpoolOptional.get();
    if (waitOptional.isPresent()){
      Wait wait = waitOptional.get();
      wait.setCheckNum(2);
      waitRepository.save(wait);
    } else {
      throw new IllegalArgumentException("찾을수가 없습니다.");
    }
  }
  public void carpoolFinish(CustomUserDetails userDetails, Integer carpoolNum) {
    String email = userDetails.getUser().getEmail();
    Optional<Carpool> carpoolOptional = carpoolRepository.findByCarpoolNumAndEmail(carpoolNum, email);
    if (carpoolOptional.isPresent()) {
      Carpool carpool = carpoolOptional.get();
      // 자신의 게시물이 맞으면 해당 게시물에 대한 모든 wait 엔티티 가져오기
      List<Wait> waits = waitRepository.findByCarpoolNum(carpool);
      if(carpool.getCheckNum() == 2) {
        carpool.setCheckNum(3);
        carpoolRepository.save(carpool);
      }
      for (Wait wait : waits) {
        // 자신의 checkNum이 1인 경우에 checkNum을 3으로 변경
        if (wait.getCheckNum() == 1) {
          wait.setCheckNum(3);
          waitRepository.save(wait);
        }
      }
    } else {
      throw new IllegalArgumentException("찾을 수 없거나 권한이 없습니다.");
    }
  }

  public void carpoolAcceptFinish(CustomUserDetails userDetails, Integer carpoolNum) {
    String email = userDetails.getUser().getEmail();
    Optional<Carpool> carpoolOptional = carpoolRepository.findByCarpoolNumAndEmail(carpoolNum, email);
    if (carpoolOptional.isPresent()) {
      Carpool carpool = carpoolOptional.get();
      // 자신의 게시물이 맞으면 해당 게시물에 대한 모든 wait 엔티티 가져오기
      List<Wait> waits = waitRepository.findByCarpoolNum(carpool);
      if(carpool.getCheckNum() == 1) {
        carpool.setCheckNum(2);
        carpoolRepository.save(carpool);
      }
    } else {
      throw new IllegalArgumentException("찾을 수 없거나 권한이 없습니다.");
    }
  }

  public void update(CustomUserDetails userDetails, Integer carpoolNum, CarpoolDto carpoolDto) {
    String email = userDetails.getUser().getEmail();
    if(carpoolDto.getMemberNum() == 0) {
      throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
    }
    if(carpoolDto.getStart().isEmpty() || carpoolDto.getEnd().isEmpty() || carpoolDto.getDate() == null) {
      throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
    }
    Optional<Carpool> carpoolOptional = carpoolRepository.findByCarpoolNumAndEmail(carpoolNum,
        email);
    if (carpoolOptional.isPresent()) {
      Carpool carpool = carpoolOptional.get();
      // 업데이트 로직을 수행하고 저장
      carpool.setStart(carpoolDto.getStart());
      carpool.setEnd(carpoolDto.getEnd());
      carpool.setDate(carpoolDto.getDate());
      carpool.setMemberNum(carpoolDto.getMemberNum());
      carpoolRepository.save(carpool);
    } else {
      throw new IllegalArgumentException("찾을수가 없습니다.");
    }
  }

  public void delete(CustomUserDetails userDetails, Integer carpoolNum) {
    String email = userDetails.getUser().getEmail();
    Optional<Carpool> carpoolOptional = carpoolRepository.findByCarpoolNumAndEmail(carpoolNum,
        email);

    if (carpoolOptional.isPresent()) {
      Carpool carpool = carpoolOptional.get();

      // 현재 시간
      LocalDateTime now = LocalDateTime.now();

      // 차이 계산
      long hoursDifference = ChronoUnit.HOURS.between(now, carpool.getDate());

      // 차이가 12시간 이상이면 삭제
      if (Math.abs(hoursDifference) >= 12) {
        carpoolRepository.delete(carpool);
      } else {
        throw new IllegalArgumentException("12시간 이전에 생성된 카풀만 삭제할 수 있습니다.");
      }
    } else {
      throw new IllegalArgumentException("찾을수가 없습니다.");
    }
  }
}
