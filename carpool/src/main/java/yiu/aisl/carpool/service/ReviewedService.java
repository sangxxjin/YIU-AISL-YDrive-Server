package yiu.aisl.carpool.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import yiu.aisl.carpool.Dto.GuestReviewedRequest;
import yiu.aisl.carpool.Dto.GuestReviewedResponse;
import yiu.aisl.carpool.Dto.OwnerReviewedRequest;
import yiu.aisl.carpool.Dto.OwnerReviewedResponse;
import yiu.aisl.carpool.domain.GuestReviewed;
import yiu.aisl.carpool.domain.OwnerReviewed;
import yiu.aisl.carpool.domain.Wait;
import yiu.aisl.carpool.repository.CarpoolRepository;
import yiu.aisl.carpool.repository.GuestReviewdRepository;
import yiu.aisl.carpool.repository.OwnerReviewdRepository;
import yiu.aisl.carpool.repository.WaitRepository;
import yiu.aisl.carpool.security.CustomUserDetails;
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
      LocalDateTime createdAt = LocalDateTime.now();
      String authHeader = httpServletRequest.getHeader("Authorization");

      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        String email = jwtProvider.getEmail(token);

        // wait 테이블에서 carpoolNum과 waitNum을 이용하여 해당하는 wait 정보 조회
        Optional<Wait> waitOptional = waitRepository.findByCarpoolNum_CarpoolNumAndWaitNum(carpoolNum, waitNum);
        if (waitOptional.isEmpty()) {
          throw new IllegalArgumentException("게시물을 찾을 수 없습니다.");
        }
        Wait wait = waitOptional.get();

        // checkNum이 1인지 확인하고 현재 사용자가 guest인지 확인하여 리뷰 작성 허용 여부 판단
        if (wait.getCheckNum() != 1 || !wait.getGuest().equals(email)) {
          throw new IllegalArgumentException("리뷰를 작성할 수 없습니다.");
        }
        int starValue = request.getStar();
        if (starValue < 1 || starValue > 5) {
          throw new IllegalArgumentException("start 값은 1에서 5 사이어야 합니다.");
        }

        // 리뷰 작성 처리
        OwnerReviewed ownerReviewed = OwnerReviewed.builder()
            .carpoolNum(wait.getCarpoolNum())
            .star(request.getStar())
            .review(request.getReview())
            .createdAt(createdAt)
            .email(wait.getOwner())
            .build();

        ownerReviewdRepository.save(ownerReviewed);
      } else {
        throw new IllegalArgumentException("사용자 토큰이 다릅니다.");
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("리뷰 작성에 실패했습니다.");
    }
    return true; // 리뷰 작성 성공
  }



  public boolean guestReviewed(int carpoolNum, int waitNum, GuestReviewedRequest request) throws Exception {
    try {
      LocalDateTime createdAt = LocalDateTime.now();
      String authHeader = httpServletRequest.getHeader("Authorization");

      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        String email = jwtProvider.getEmail(token);

        // wait 테이블에서 carpoolNum과 waitNum을 이용하여 해당하는 wait 정보 조회
        Optional<Wait> waitOptional = waitRepository.findByCarpoolNum_CarpoolNumAndWaitNum(carpoolNum, waitNum);
        if (waitOptional.isEmpty()) {
          throw new IllegalArgumentException("게시물을 찾을 수 없습니다.");
        }
        Wait wait = waitOptional.get();

        // checkNum이 1인지 확인하고 현재 사용자가 owner인지 확인하여 리뷰 작성 허용 여부 판단
        if (wait.getCheckNum() != 1 || !wait.getOwner().equals(email)) {
          throw new IllegalArgumentException("리뷰를 작성할 수 없습니다.");
        }
        int starValue = request.getStar();
        if (starValue < 1 || starValue > 5) {
          throw new IllegalArgumentException("start 값은 1에서 5 사이어야 합니다.");
        }

        // 리뷰 작성 처리
        GuestReviewed guestReviewed = GuestReviewed.builder()
            .waitNum(wait)
            .star(request.getStar())
            .review(request.getReview())
            .createdAt(createdAt)
            .email(wait.getGuest())
            .build();

        guestReviewdRepository.save(guestReviewed);
      } else {
        throw new IllegalArgumentException("사용자 토큰이 다릅니다.");
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("리뷰 작성에 실패했습니다.");
    }
    return true; // 리뷰 작성 성공
  }

  public List<GuestReviewedResponse> getGuestReview(@AuthenticationPrincipal CustomUserDetails userDetails) {
    String userEmail = userDetails.getUser().getEmail();
    List<GuestReviewed> myReviews = guestReviewdRepository.findByEmail(userEmail);
    return myReviews.stream()
            .map(GuestReviewedResponse::new)
            .collect(Collectors.toList());
  }

  public List<OwnerReviewedResponse> getOwnerReview(@AuthenticationPrincipal CustomUserDetails userDetails) {
    String userEmail = userDetails.getUser().getEmail();
    List<OwnerReviewed> myReviews = ownerReviewdRepository.findByEmail(userEmail);
    return myReviews.stream()
            .map(OwnerReviewedResponse::new)
            .collect(Collectors.toList());
  }
}
