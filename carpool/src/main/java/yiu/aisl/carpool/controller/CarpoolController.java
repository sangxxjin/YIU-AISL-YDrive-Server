package yiu.aisl.carpool.controller;

import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yiu.aisl.carpool.Dto.CarpoolDto;
import yiu.aisl.carpool.Dto.CarpoolRequest;
import yiu.aisl.carpool.Dto.StationResponse;
import yiu.aisl.carpool.Dto.WaitRequest;
import yiu.aisl.carpool.domain.Wait;
import yiu.aisl.carpool.repository.CarpoolRepository;
import yiu.aisl.carpool.security.CustomUserDetails;
import yiu.aisl.carpool.service.CarpoolService;
import yiu.aisl.carpool.service.ScreenService;

@RestController
@RequestMapping("/carpool")
@RequiredArgsConstructor
public class CarpoolController {
  private final CarpoolRepository carpoolRepository;
  private final CarpoolService carpoolService;
  private final ScreenService screenService;

  @PostMapping("/create")
  public ResponseEntity<Boolean> carpoolCreate(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CarpoolRequest request)
          throws Exception {
    return new ResponseEntity<>(carpoolService.create(request, customUserDetails), HttpStatus.OK);
  }

  @GetMapping("/mycarpool")
  public ResponseEntity<Object> mycarpool(@AuthenticationPrincipal CustomUserDetails customUserDetails){
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
    return new ResponseEntity<>(screenService.getMyCarpool(customUserDetails), headers, HttpStatus.OK);
  }

  @PutMapping("/update/{carpoolNum}")
  public ResponseEntity<Object> carpoolUpdate(
          @AuthenticationPrincipal CustomUserDetails customUserDetails,
          @PathVariable Integer carpoolNum,
          @RequestBody CarpoolDto carpoolDto) {
    carpoolService.update(customUserDetails, carpoolNum, carpoolDto);
    return ResponseEntity.ok("업데이트 성공");
  }

  @PutMapping("/accept/{carpoolNum}/{waitNum}")
  public ResponseEntity<Object> waitDecide(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                           @PathVariable Integer carpoolNum, @PathVariable Integer waitNum) {
    carpoolService.accept(customUserDetails, carpoolNum, waitNum);
    return ResponseEntity.ok("수락 성공");
  }

  @PutMapping("/deny/{carpoolNum}/{waitNum}")
  public ResponseEntity<Object> waitDeny(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                         @PathVariable Integer carpoolNum, @PathVariable Integer waitNum) {
    carpoolService.deny(customUserDetails, carpoolNum, waitNum);
    return ResponseEntity.ok("거절 성공");
  }

  @DeleteMapping("/delete/{carpoolNum}")
  public ResponseEntity<Object> carpoolDelete(
          @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Integer carpoolNum) {
    carpoolService.delete(customUserDetails, carpoolNum);
    return ResponseEntity.ok("삭제 성공");
  }

  @PostMapping("/apply/{carpoolNum}")
  public ResponseEntity<Boolean> carpoolApply(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody WaitRequest request, @PathVariable Integer carpoolNum)
          throws  Exception {
    return new ResponseEntity<>(carpoolService.apply(request, carpoolNum, customUserDetails), HttpStatus.OK);
  }

  @PostMapping("/{carpoolNum}/finish")
  public ResponseEntity<String> carpoolFinish(
          @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Integer carpoolNum) {
    carpoolService.carpoolFinish(customUserDetails, carpoolNum);
    return ResponseEntity.ok("도착");
  }

  @PutMapping("/{carpoolNum}/acceptFinish")
  public ResponseEntity<String> carpoolAcceptFinish(
          @AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Integer carpoolNum) {
    carpoolService.carpoolAcceptFinish(customUserDetails, carpoolNum);
    return ResponseEntity.ok("모집 완료");
  }

  @GetMapping("/{carpoolNum}/apply-list")
  public List<Wait> getWaitList(@PathVariable Integer carpoolNum,@AuthenticationPrincipal CustomUserDetails customUserDetails) {
    return screenService.getWaitList(carpoolNum, customUserDetails);
  }

  @GetMapping("/cityList")
  public List<StationResponse> getCityList() {
    return carpoolService.getCityList();
  }
}