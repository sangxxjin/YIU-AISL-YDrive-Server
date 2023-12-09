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
import yiu.aisl.carpool.Dto.WaitDto;
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
  public ResponseEntity<Boolean> carpoolCreate(@RequestBody CarpoolRequest request)
          throws Exception {
    return new ResponseEntity<>(carpoolService.create(request), HttpStatus.OK);
  }

  @GetMapping("/mycarpool")
  public ResponseEntity<Object> mycarpool(@AuthenticationPrincipal CustomUserDetails customUserDetails){
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
    return new ResponseEntity<>(screenService.getMyCarpool(customUserDetails), headers, HttpStatus.OK);
  }

  @PutMapping("/update/{carpoolNum}")
  public ResponseEntity<Object> carpoolUpdate(
          @AuthenticationPrincipal CustomUserDetails userDetails,
          @PathVariable Integer carpoolNum,
          @RequestBody CarpoolDto carpoolDto) {
    carpoolService.update(userDetails, carpoolNum, carpoolDto);
    return ResponseEntity.ok("업데이트 성공");
  }

  @PutMapping("/accept/{carpoolNum}/{waitNum}")
  public ResponseEntity<Object> waitDecide(@AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable Integer carpoolNum, @PathVariable Integer waitNum,
      @RequestBody WaitDto waitDto) {
    carpoolService.decide(userDetails, carpoolNum, waitNum, waitDto);
    return ResponseEntity.ok("결정 성공");
  }

  @DeleteMapping("/delete/{carpoolNum}")
  public ResponseEntity<Object> carpoolDelete(
          @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Integer carpoolNum) {
    carpoolService.delete(userDetails, carpoolNum);
    return ResponseEntity.ok("삭제 성공");
  }

  @PostMapping("/apply/{carpoolNum}")
  public ResponseEntity<Boolean> carpoolApply(@RequestBody WaitRequest request, @PathVariable Integer carpoolNum)
          throws  Exception {
    return new ResponseEntity<>(carpoolService.apply(request, carpoolNum), HttpStatus.OK);
  }

  @PostMapping("/{carpoolNum}/finish")
  public ResponseEntity<String> carpoolFinish(
      @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Integer carpoolNum) {
    carpoolService.carpoolFinish(userDetails, carpoolNum);
    return ResponseEntity.ok("도착");
  }

  @GetMapping("/{carpoolNum}/apply-list")
  public List<Wait> getWaitList(@PathVariable Integer carpoolNum,@AuthenticationPrincipal CustomUserDetails userDetails) {
    return screenService.getWaitList(carpoolNum, userDetails);
  }
}