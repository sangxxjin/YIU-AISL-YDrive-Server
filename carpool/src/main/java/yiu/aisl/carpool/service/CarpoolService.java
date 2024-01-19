package yiu.aisl.carpool.service;

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
import yiu.aisl.carpool.Dto.WaitRequest;
import yiu.aisl.carpool.domain.Carpool;
import yiu.aisl.carpool.domain.User;
import yiu.aisl.carpool.domain.Wait;
import yiu.aisl.carpool.exception.CustomException;
import yiu.aisl.carpool.exception.ErrorCode;
import yiu.aisl.carpool.repository.CarpoolRepository;
import yiu.aisl.carpool.repository.WaitRepository;
import yiu.aisl.carpool.security.CustomUserDetails;


@Service
@Transactional
@RequiredArgsConstructor
public class CarpoolService {

  private final CarpoolRepository carpoolRepository;
  private final WaitRepository waitRepository;

  public boolean create(CarpoolRequest request, CustomUserDetails customUserDetails) {
    if (request.getMemberNum() <= 0) {
      throw new CustomException(ErrorCode.INVALID_MEMBER_NUM);
    } else if (request.getStart().isEmpty() || request.getEnd().isEmpty()
        || request.getDate() == null) {
      throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
    }

    User user = customUserDetails.getUser();
    if (user.getStatus() == 0) {
      throw new CustomException(ErrorCode.CHANGE_TO_DRIVER_MODE_REQUIRED);
    }

    try {
      LocalDateTime createdAt = LocalDateTime.now();

      Carpool carpool = Carpool.builder()
          .carpoolNum(request.getCarpoolNum())
          .start(request.getStart())
          .end(request.getEnd())
          .date(request.getDate())
          .checkNum(request.getCheckNum())
          .memberNum(request.getMemberNum())
          .email(user.getEmail())
          .createdAt(createdAt)
          .build();

      carpoolRepository.save(carpool);
    } catch (DataIntegrityViolationException e) {
      System.out.println(e.getMessage());
      throw new IllegalArgumentException("잘못된 요청입니다.");
    }
    return true;
  }

  public boolean apply(WaitRequest request, Integer carpoolNum,
      CustomUserDetails customUserDetails) {
    try {
      LocalDateTime createdAt = LocalDateTime.now();

      User currentUser = customUserDetails.getUser();
      String email = currentUser.getEmail();  // 현재 로그인한 사용자의 이메일

      Optional<Carpool> carpoolOptional = carpoolRepository.findByCarpoolNum(carpoolNum);

      if (carpoolOptional.isPresent()) {
        Carpool carpool = carpoolOptional.get();

        if (carpool.getCheckNum() == 2 || carpool.getCheckNum() == 3) {
          throw new CustomException(ErrorCode.APPLICATION_DEADLINE); // 신청이 마감된 경우
        }

        if (!email.equals(carpool.getEmail())) {
          boolean alreadyApplied = waitRepository.existsByCarpoolNumAndGuest(carpool, email);

          if (!alreadyApplied) {
            int memberNum = carpool.getMemberNum();

            if (memberNum > 0) {
              Wait wait = Wait.builder()
                  .waitNum(request.getWaitNum())
                  .carpoolNum(carpool)
                  .guest(email)
                  .owner(carpool.getEmail())
                  .checkNum(request.getCheckNum())
                  .createdAt(createdAt)
                  .build();

              waitRepository.save(wait);
            } else {
              throw new CustomException(ErrorCode.NUMBER_OF_APPLICATIONS_EXCEEDED);
            }
          } else {
            throw new CustomException(ErrorCode.ALREADY_APPLIED);
          }
        } else {
          throw new CustomException(ErrorCode.POST_WRITTEN_BY_ME);
        }
      }
    } catch (DataIntegrityViolationException e) {
      System.out.println(e.getMessage());
      throw new IllegalArgumentException("잘못된 요청입니다.");
    }
    return true;
  }

  public void accept(CustomUserDetails userDetails, Integer carpoolNum, Integer waitNum) {
    String email = userDetails.getUser().getEmail();

    Wait wait = waitRepository.findByOwnerAndCarpoolNum_CarpoolNumAndWaitNum(email, carpoolNum,
            waitNum)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));

    Carpool carpool = carpoolRepository.findByCarpoolNum(carpoolNum)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));

    if (carpool.getMemberNum() == 0) {
      throw new CustomException(ErrorCode.NUMBER_OF_APPLICATIONS_EXCEEDED);
    }

    if (wait.getCheckNum() == 1) {
      throw new CustomException(ErrorCode.ALREADY_ACCEPT);
    }

    // 수락 처리
    wait.setCheckNum(1);
    carpool.setMemberNum(carpool.getMemberNum() - 1);
//    carpool.setCheckNum(carpool.getMemberNum() == 0 ? 2 : 1);

    waitRepository.save(wait);
    carpoolRepository.save(carpool);
  }

  public void deny(CustomUserDetails userDetails, Integer carpoolNum, Integer waitNum) {
    String email = userDetails.getUser().getEmail();

    Wait wait = waitRepository.findByOwnerAndCarpoolNum_CarpoolNumAndWaitNum(email, carpoolNum,
            waitNum)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));

    wait.setCheckNum(2);
    waitRepository.save(wait);
  }

  public void carpoolFinish(CustomUserDetails userDetails, Integer carpoolNum) {
    String email = userDetails.getUser().getEmail();

    Carpool carpool = carpoolRepository.findByCarpoolNumAndEmail(carpoolNum, email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));

    if (carpool.getCheckNum() == 2) {
      carpool.setCheckNum(3);
      carpoolRepository.save(carpool);

      // 해당 carpool에 대한 모든 wait 엔티티 가져오기
      List<Wait> waits = waitRepository.findByCarpoolNum(carpool);
      for (Wait wait : waits) {
        // 자신의 checkNum이 1인 경우에 checkNum을 3으로 변경
        if (wait.getCheckNum() == 1) {
          wait.setCheckNum(3);
          waitRepository.save(wait);
        }
      }
    } else {
      throw new CustomException(ErrorCode.NOT_EXIST);
    }
  }


  public void carpoolAcceptFinish(CustomUserDetails userDetails, Integer carpoolNum) {
    String email = userDetails.getUser().getEmail();

    Carpool carpool = carpoolRepository.findByCarpoolNumAndEmail(carpoolNum, email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));
    if (carpool.getCheckNum() == 1) {
      carpool.setCheckNum(2);
      carpoolRepository.save(carpool);
    }
  }

  public void update(CustomUserDetails userDetails, Integer carpoolNum, CarpoolDto carpoolDto) {
    String email = userDetails.getUser().getEmail();

    // 데이터가 충분하지 않은 경우
    if (carpoolDto.getMemberNum() == 0 || carpoolDto.getStart().isEmpty() || carpoolDto.getEnd()
        .isEmpty() || carpoolDto.getDate() == null) {
      throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
    }

    // 카풀 정보 조회
    Carpool carpool = carpoolRepository.findByCarpoolNumAndEmail(carpoolNum, email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));

    // 현재 시간
    LocalDateTime now = LocalDateTime.now();

    // 차이 계산
    long hoursDifference = ChronoUnit.HOURS.between(now, carpool.getDate());

    // 차이가 12시간 미만이면 예외 발생
    if (Math.abs(hoursDifference) < 12) {
      throw new CustomException(ErrorCode.CANNOT_DELETE_CARPOOL_HOUR);
    }

    // Wait 엔터티 검사
    List<Wait> waitingList = waitRepository.findByCarpoolNumAndCheckNum(carpool, 0);
    List<Wait> acceptedList = waitRepository.findByCarpoolNumAndCheckNum(carpool, 1);

    // 대기중이거나 수락한 사람이 있으면 삭제불가
    if (!waitingList.isEmpty() || !acceptedList.isEmpty()) {
      throw new CustomException(ErrorCode.CANNOT_DELETE_CARPOOL_WITH_WAITING);
    }

    // 카풀 정보 업데이트
    carpool.setStart(carpoolDto.getStart());
    carpool.setEnd(carpoolDto.getEnd());
    carpool.setDate(carpoolDto.getDate());
    carpool.setMemberNum(carpoolDto.getMemberNum());
    carpoolRepository.save(carpool);
  }


  public void delete(CustomUserDetails userDetails, Integer carpoolNum) {
    String email = userDetails.getUser().getEmail();

    Carpool carpool = carpoolRepository.findByCarpoolNumAndEmail(carpoolNum, email)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST));

    // 현재 시간
    LocalDateTime now = LocalDateTime.now();

    // 차이 계산
    long hoursDifference = ChronoUnit.HOURS.between(now, carpool.getDate());

    // 차이가 12시간 미만이면 예외 발생
    if (Math.abs(hoursDifference) < 12) {
      throw new CustomException(ErrorCode.CANNOT_DELETE_CARPOOL_HOUR);
    }

    // Wait 엔터티 검사
    List<Wait> waitingList = waitRepository.findByCarpoolNumAndCheckNum(carpool, 0);
    List<Wait> acceptedList = waitRepository.findByCarpoolNumAndCheckNum(carpool, 1);

    if (!waitingList.isEmpty() || !acceptedList.isEmpty()) {
      throw new CustomException(ErrorCode.CANNOT_DELETE_CARPOOL_WITH_WAITING);
    }
    waitRepository.deleteAllByCarpoolNum(carpool);

    // 삭제 가능한 경우 삭제
    carpoolRepository.delete(carpool);
  }
}
