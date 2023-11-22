package yiu.aisl.carpool.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import yiu.aisl.carpool.Dto.CarpoolDto;
import yiu.aisl.carpool.Dto.CarpoolRequest;
import yiu.aisl.carpool.domain.Carpool;
import yiu.aisl.carpool.repository.CarpoolRepository;
import yiu.aisl.carpool.repository.UserRepository;
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

  public boolean create(CarpoolRequest request) throws Exception {
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
      Date date = new Date(System.currentTimeMillis());
      String authHeader = httpServletRequest.getHeader("Authorization");
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        String email = jwtProvider.getEmail(token);
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

  public void update(CustomUserDetails userDetails, int carpoolNum, CarpoolDto carpoolDto) {
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

  public void delete(CustomUserDetails userDetails, int carpoolNum) {
    String email = userDetails.getUser().getEmail();
    Optional<Carpool> carpoolOptional = carpoolRepository.findByCarpoolNumAndEmail(carpoolNum,
        email);

    if (carpoolOptional.isPresent()) {
      carpoolRepository.delete(carpoolOptional.get());
    } else {
      throw new IllegalArgumentException("찾을수가 없습니다.");
    }
  }
}
