package yiu.aisl.carpool.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
import yiu.aisl.carpool.repository.CarpoolRepository;
import yiu.aisl.carpool.repository.UserRepository;
import yiu.aisl.carpool.repository.WaitRepository;
import yiu.aisl.carpool.security.CustomUserDetails;
import yiu.aisl.carpool.security.JwtProvider;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
      Date date = new Date(System.currentTimeMillis());
      String authHeader = httpServletRequest.getHeader("Authorization");
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        String email = jwtProvider.getEmail(token);

        // 사용자 정보 조회
        Optional<User> user = userRepository.findByEmail(email);

        // 여기서 carNum이 null이면 예외 발생
        if (user.get().getStatus() == 0) {
          throw new Exception("carNum이 없어서 게시글을 작성할 수 없습니다.");
        }

        Carpool carpool = Carpool.builder()
            .carpoolNum(request.getCarpoolNum())
            .start(request.getStart())
            .end(request.getEnd())
            .date(request.getDate())
            .checkNum(request.getCheckNum())
            .memberNum(request.getMemberNum())
            .email(email)
            .createdAt(date)
            .build();
        carpoolRepository.save(carpool);
      }
    } catch (DataIntegrityViolationException e) {
      System.out.println(e.getMessage());
      throw new Exception("잘못된 요청입니다.");
    }
    return true;
  }

  public boolean apply(WaitRequest request, Integer carpoolNum) throws Exception {
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
      Date date = new Date(System.currentTimeMillis());
      String authHeader = httpServletRequest.getHeader("Authorization");

      if(authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        String email = jwtProvider.getEmail(token);  // 현재 로그인한 사용자의 이메일
        Optional<Carpool> carpoolOptional = carpoolRepository.findByCarpoolNum(carpoolNum);
        if(carpoolOptional.isPresent()) {
          Carpool carpool = carpoolOptional.get();
          String ownerEmail = carpool.getEmail();  // 게시물을 작성한 사용자의 이메일

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
                    .createdAt(date)
                    .build();
                waitRepository.save(wait);
                // 게시물의 신청 인원 수 - 1
                carpool.setMemberNum(memberNum - 1);
                carpoolRepository.save(carpool);
              } else {
                throw new Exception("신청 인원 초과!!!!!");
              }
            } else {
              throw new Exception("본인이 작성한 게시글에는 신청할 수 없음!!!!!");
            }
          } else {
            throw new Exception("이미 해당 게시물에 대해 신청한 기록이 있습니다.");
          }
        }
      }
    } catch (DataIntegrityViolationException e) {
      System.out.println(e.getMessage());
      throw new Exception("잘못된 요청입니다.");
    }
    return true;
  }
  public boolean decide(CustomUserDetails userDetails, Integer carpoolNum, Integer waitNum, WaitDto waitDto){
    String email = userDetails.getUser().getEmail();
    Optional<Wait> waitOptional = waitRepository.findByOwnerAndCarpoolNum_CarpoolNumAndWaitNum(email,carpoolNum,waitNum);
    if (waitOptional.isPresent()){
      Wait wait = waitOptional.get();
      wait.setCheckNum(waitDto.getCheckNum());
      waitRepository.save(wait);
    }else {
      throw new IllegalArgumentException("찾을수가 없습니다.");
    }
    return true;
  }

  public void update(CustomUserDetails userDetails, Integer carpoolNum, CarpoolDto carpoolDto) {
    String email = userDetails.getUser().getEmail();
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
