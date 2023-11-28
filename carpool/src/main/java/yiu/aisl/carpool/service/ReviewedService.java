package yiu.aisl.carpool.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yiu.aisl.carpool.Dto.GuestReviewedRequest;
import yiu.aisl.carpool.Dto.OwnerReviewedRequest;
import yiu.aisl.carpool.domain.GuestReviewed;
import yiu.aisl.carpool.domain.OwnerReviewed;
import yiu.aisl.carpool.domain.Wait;
import yiu.aisl.carpool.repository.CarpoolRepository;
import yiu.aisl.carpool.repository.GuestReviewdRepository;
import yiu.aisl.carpool.repository.OwnerReviewdRepository;
import yiu.aisl.carpool.repository.WaitRepository;
import yiu.aisl.carpool.security.JwtProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewedService {

  private final HttpServletRequest httpServletRequest;
  private final JwtProvider jwtProvider;
  private final OwnerReviewdRepository ownerReviewdRepository;
  private final GuestReviewdRepository guestReviewdRepository;
  private final CarpoolRepository carpoolRepository;
  private final WaitRepository waitRepository;

  public boolean ownerReviewed(int carpoolNum, int waitNum, OwnerReviewedRequest request) throws Exception {
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
      Date date = new Date(System.currentTimeMillis());
      String authHeader = httpServletRequest.getHeader("Authorization");

      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        String email = jwtProvider.getEmail(token);

        // wait 테이블에서 carpoolNum과 waitNum을 이용하여 해당하는 wait 정보 조회
        Optional<Wait> waitOptional = waitRepository.findByCarpoolNum_CarpoolNumAndWaitNum(carpoolNum, waitNum);
        if (!waitOptional.isPresent()) {
          throw new Exception("게시물을 찾을 수 없습니다.");
        }
        Wait wait = waitOptional.get();

        // checkNum이 1인지 확인하고 현재 사용자가 guest인지 확인하여 리뷰 작성 허용 여부 판단
        if (wait.getCheckNum() != 1 || !wait.getGuest().equals(email)) {
          throw new Exception("리뷰를 작성할 수 없습니다.");
        }

        // 리뷰 작성 처리
        OwnerReviewed ownerReviewed = OwnerReviewed.builder()
            .carpoolNum(wait.getCarpoolNum())
            .star(request.getStar())
            .review(request.getReview())
            .createdAt(date)
            .email(wait.getOwner())
            .build();

        ownerReviewdRepository.save(ownerReviewed);
      } else {
        throw new Exception("사용자 토큰이 다릅니다.");
      }
    } catch (Exception e) {
      throw new Exception("리뷰 작성에 실패했습니다.");
    }
    return true; // 리뷰 작성 성공
  }



  public boolean guestReviewed(int carpoolNum, int waitNum, GuestReviewedRequest request) throws Exception {
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
      Date date = new Date(System.currentTimeMillis());
      String authHeader = httpServletRequest.getHeader("Authorization");

      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        String email = jwtProvider.getEmail(token);

        // wait 테이블에서 carpoolNum과 waitNum을 이용하여 해당하는 wait 정보 조회
        Optional<Wait> waitOptional = waitRepository.findByCarpoolNum_CarpoolNumAndWaitNum(carpoolNum, waitNum);
        if (!waitOptional.isPresent()) {
          throw new Exception("게시물을 찾을 수 없습니다.");
        }
        Wait wait = waitOptional.get();

        // checkNum이 1인지 확인하고 현재 사용자가 owner인지 확인하여 리뷰 작성 허용 여부 판단
        if (wait.getCheckNum() != 1 || !wait.getOwner().equals(email)) {
          throw new Exception("리뷰를 작성할 수 없습니다.");
        }

        // 리뷰 작성 처리
        GuestReviewed guestReviewed = GuestReviewed.builder()
            .waitNum(wait)
            .star(request.getStar())
            .review(request.getReview())
            .createdAt(date)
            .email(wait.getGuest())
            .build();

        guestReviewdRepository.save(guestReviewed);
      } else {
        throw new Exception("사용자 토큰이 다릅니다.");
      }
    } catch (Exception e) {
      throw new Exception("리뷰 작성에 실패했습니다.");
    }
    return true; // 리뷰 작성 성공
  }
}
