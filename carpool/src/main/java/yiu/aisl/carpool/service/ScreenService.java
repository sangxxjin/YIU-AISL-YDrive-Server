package yiu.aisl.carpool.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.carpool.Dto.CarpoolResponse;
import yiu.aisl.carpool.domain.Carpool;
import yiu.aisl.carpool.domain.Wait;
import yiu.aisl.carpool.exception.CustomException;
import yiu.aisl.carpool.exception.ErrorCode;
import yiu.aisl.carpool.repository.CarpoolRepository;

import java.util.List;
import java.util.stream.Collectors;
import yiu.aisl.carpool.repository.WaitRepository;
import yiu.aisl.carpool.security.CustomUserDetails;

@Service
@Transactional
@RequiredArgsConstructor
public class ScreenService {

  private final CarpoolRepository carpoolRepository;
  private final WaitRepository waitRepository;

  public List<CarpoolResponse> getCarpool() {
    LocalDateTime currentDateTime = LocalDateTime.now();
    return carpoolRepository.findAll().stream()
        .filter(carpool -> carpool.getDate().isAfter(currentDateTime))
        .map(CarpoolResponse::new)
        .collect(Collectors.toList());
  }

  public List<CarpoolResponse> getMyCarpool(CustomUserDetails customUserDetails) {
    String userEmail = customUserDetails.getUser().getEmail();
    List<Carpool> myCarpools = carpoolRepository.findByEmail(userEmail);
    return myCarpools.stream()
        .map(CarpoolResponse::new)
        .collect(Collectors.toList());
  }

  public List<Wait> getWaitList(Integer carpoolNum, CustomUserDetails userDetails) {

    String email = userDetails.getUser().getEmail();
    Optional<Carpool> carpoolOptional = carpoolRepository.findByCarpoolNumAndEmail(carpoolNum,
        email);

    if (carpoolOptional.isPresent()) {
      Carpool carpool = carpoolOptional.get();
      List<Wait> waits = waitRepository.findByCarpoolNum(carpool);

      return waits.stream()
          .filter(wait -> wait.getCheckNum() == 0)
          .collect(Collectors.toList());
    } else {
      throw new CustomException(ErrorCode.NOT_EXIST);
    }
  }

  public List<Wait> getWaitListAll(Integer carpoolNum, CustomUserDetails userDetails) {

    String email = userDetails.getUser().getEmail();
    Optional<Carpool> carpoolOptional = carpoolRepository.findByCarpoolNumAndEmail(carpoolNum,
            email);

    if (carpoolOptional.isPresent()) {
      Carpool carpool = carpoolOptional.get();
      List<Wait> waits = waitRepository.findByCarpoolNum(carpool);

      return waits.stream()
              .filter(wait -> wait.getCheckNum() == 3)
              .collect(Collectors.toList());
    } else {
      throw new CustomException(ErrorCode.NOT_EXIST);
    }
  }

  public List<Wait> getMyWaitList(CustomUserDetails userDetails) {
    String email = userDetails.getUser().getEmail();
    List<Wait> waits = waitRepository.findByGuest(email);
    return waits.stream()
            .collect(Collectors.toList());
  }
}