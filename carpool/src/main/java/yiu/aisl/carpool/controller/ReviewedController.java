package yiu.aisl.carpool.controller;

import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.carpool.Dto.GuestReviewedRequest;
import yiu.aisl.carpool.Dto.OwnerReviewedRequest;
import yiu.aisl.carpool.repository.OwnerReviewdRepository;
import yiu.aisl.carpool.security.CustomUserDetails;
import yiu.aisl.carpool.service.ReviewedService;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class ReviewedController {

  private final OwnerReviewdRepository ownerReviewdRepository;
  private final ReviewedService reviewedService;

  @PostMapping("/carpool/{carpoolNum}/wait/{waitNum}/owner/reviewed")
  public ResponseEntity<Boolean> ownerReviewed(@PathVariable int carpoolNum, @PathVariable int waitNum,
      @RequestBody OwnerReviewedRequest request ,@AuthenticationPrincipal CustomUserDetails customUserDetails)
      throws Exception {
    return new ResponseEntity<>(reviewedService.ownerReviewed(carpoolNum, waitNum, request, customUserDetails),
        HttpStatus.OK);
  }


  @PostMapping("/carpool/{carpoolNum}/wait/{waitNum}/guest/reviewed")
  public ResponseEntity<Boolean> guestReviewed(@PathVariable int carpoolNum, @PathVariable int waitNum,
      @RequestBody GuestReviewedRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails)
      throws Exception {
    return new ResponseEntity<>(reviewedService.guestReviewed(carpoolNum,waitNum,request, customUserDetails), HttpStatus.OK);
  }

  @GetMapping("/list/review/owner/{email}")
  public ResponseEntity<Object> getOwnerReview(@PathVariable String email, @AuthenticationPrincipal CustomUserDetails userDetails) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
    return new ResponseEntity<>(reviewedService.getOwnerReview(email, userDetails), headers, HttpStatus.OK);
  }

  @GetMapping("/list/review/guest/{email}")
  public ResponseEntity<Object> getGuestReview(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable String email) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
    return new ResponseEntity<>(reviewedService.getGuestReview(userDetails, email), headers, HttpStatus.OK);
  }
}
