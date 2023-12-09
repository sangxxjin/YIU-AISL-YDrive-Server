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
      @RequestBody OwnerReviewedRequest request)
      throws Exception {
    return new ResponseEntity<>(reviewedService.ownerReviewed(carpoolNum, waitNum, request),
        HttpStatus.OK);
  }


  @PostMapping("/carpool/{carpoolNum}/wait/{waitNum}/guest/reviewed")
  public ResponseEntity<Boolean> guestReviewed(@PathVariable int carpoolNum, @PathVariable int waitNum,
      @RequestBody GuestReviewedRequest request)
      throws Exception {
    return new ResponseEntity<>(reviewedService.guestReviewed(carpoolNum,waitNum,request), HttpStatus.OK);
  }

  @GetMapping("/list/review/owner")
  public ResponseEntity<Object> getOwnerReview(@AuthenticationPrincipal CustomUserDetails userDetails) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
    return new ResponseEntity<>(reviewedService.getOwnerReview(userDetails), headers, HttpStatus.OK);
  }

  @GetMapping("/list/review/guest")
  public ResponseEntity<Object> getGuestReview(@AuthenticationPrincipal CustomUserDetails userDetails) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
    return new ResponseEntity<>(reviewedService.getGuestReview(userDetails), headers, HttpStatus.OK);
  }
}
