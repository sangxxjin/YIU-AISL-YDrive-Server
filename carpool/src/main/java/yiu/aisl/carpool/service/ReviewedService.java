package yiu.aisl.carpool.service;


import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import yiu.aisl.carpool.Dto.GuestReviewedRequest;
import yiu.aisl.carpool.Dto.GuestReviewedResponse;
import yiu.aisl.carpool.Dto.OwnerReviewedRequest;
import yiu.aisl.carpool.Dto.OwnerReviewedResponse;
import yiu.aisl.carpool.domain.Carpool;
import yiu.aisl.carpool.domain.GuestReviewed;
import yiu.aisl.carpool.domain.OwnerReviewed;
import yiu.aisl.carpool.domain.Wait;
import yiu.aisl.carpool.exception.CustomException;
import yiu.aisl.carpool.exception.ErrorCode;
import yiu.aisl.carpool.repository.CarpoolRepository;
import yiu.aisl.carpool.repository.GuestReviewdRepository;
import yiu.aisl.carpool.repository.OwnerReviewdRepository;
import yiu.aisl.carpool.repository.WaitRepository;
import yiu.aisl.carpool.security.CustomUserDetails;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewedService {

  private final OwnerReviewdRepository ownerReviewdRepository;
  private final GuestReviewdRepository guestReviewdRepository;
  private final WaitRepository waitRepository;
  private final CarpoolRepository carpoolRepository;

  public boolean ownerReviewed(int carpoolNum, int waitNum, OwnerReviewedRequest request, CustomUserDetails customUserDetails) {
    if(request.getReview().isEmpty() || request.getStar() == 0) {
      throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
    }
    try {
      LocalDateTime createdAt = LocalDateTime.now();
      String email = customUserDetails.getUser().getEmail();

        // wait 테이블에서 carpoolNum과 waitNum을 이용하여 해당하는 wait 정보 조회
        Optional<Wait> waitOptional = waitRepository.findByCarpoolNum_CarpoolNumAndWaitNum(carpoolNum, waitNum);
        if (waitOptional.isEmpty()) {
          throw new CustomException(ErrorCode.NOT_EXIST);
        }
        Wait wait = waitOptional.get();
        // checkNum이 3인지 확인하고 현재 사용자가 guest인지 확인하여 리뷰 작성 허용 여부 판단
        if (wait.getCheckNum() != 3 || !wait.getGuest().equals(email)) {
          throw new CustomException(ErrorCode.REVIEW_WRITE_NOT_ALLOWED);
        }

        // 리뷰 작성 처리
        OwnerReviewed ownerReviewed = OwnerReviewed.builder()
            .carpoolNum(wait.getCarpoolNum())
            .star(request.getStar())
            .review(request.getReview())
            .createdAt(createdAt)
            .email(wait.getOwner())
            .build();

        wait.setCheckNum(4);

        ownerReviewdRepository.save(ownerReviewed);

    } catch (Exception e) {
      throw new IllegalArgumentException("리뷰를 작성할 수 없습니다.", e);

    }
    return true;
  }



  public boolean guestReviewed(int carpoolNum, int waitNum, GuestReviewedRequest request, CustomUserDetails customUserDetails) {
    if(request.getReview().isEmpty() || request.getStar() == 0) {
      throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
    }
    try {
      LocalDateTime createdAt = LocalDateTime.now();
      String email = customUserDetails.getUser().getEmail();


        // wait 테이블에서 carpoolNum과 waitNum을 이용하여 해당하는 wait 정보 조회
        Optional<Wait> waitOptional = waitRepository.findByCarpoolNum_CarpoolNumAndWaitNum(carpoolNum, waitNum);
        if (waitOptional.isEmpty()) {
          throw new CustomException(ErrorCode.NOT_EXIST);
        }
        Wait wait = waitOptional.get();

        // checkNum이 3인지 확인하고 현재 사용자가 owner인지 확인하여 리뷰 작성 허용 여부 판단
        if (wait.getCheckNum() != 3 || !wait.getOwner().equals(email)) {
          throw new CustomException(ErrorCode.REVIEW_WRITE_NOT_ALLOWED);
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
    } catch (Exception e) {
      throw new IllegalArgumentException("리뷰를 작성할 수 없습니다.", e);
    }
    return true;
  }

  public List<GuestReviewedResponse> getGuestReview(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam String email) {
    List<GuestReviewed> myReviews = guestReviewdRepository.findByEmail(email);
    return myReviews.stream()
            .map(GuestReviewedResponse::new)
            .collect(Collectors.toList());
  }

  public List<OwnerReviewedResponse> getOwnerReview(@RequestParam int carpoolNum, @AuthenticationPrincipal CustomUserDetails userDetails) {
    String userEmail = userDetails.getUser().getEmail();
    Optional<Carpool> carpoolOptional = carpoolRepository.findByCarpoolNum(carpoolNum);

    if(carpoolOptional.isEmpty()) {
      return Collections.emptyList();
    }
    String authorEmail = carpoolOptional.get().getEmail();

    List<OwnerReviewed> authorReviews = ownerReviewdRepository.findByEmail(authorEmail);
    return authorReviews.stream()
            .map(OwnerReviewedResponse::new)
            .collect(Collectors.toList());
  }
}
