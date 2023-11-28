package yiu.aisl.carpool.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yiu.aisl.carpool.Dto.GuestReviewedRequest;
import yiu.aisl.carpool.Dto.OwnerReviewedRequest;
import yiu.aisl.carpool.repository.OwnerReviewdRepository;
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


}
