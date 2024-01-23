package yiu.aisl.carpool.controller;

import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.carpool.Dto.*;
import yiu.aisl.carpool.repository.UserRepository;
import yiu.aisl.carpool.repository.WaitRepository;
import yiu.aisl.carpool.security.CustomUserDetails;
import yiu.aisl.carpool.service.EmailService;
import yiu.aisl.carpool.service.UserService;

import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final EmailService emailService;

  @PostMapping("/join")
  public ResponseEntity<Boolean> join(@RequestBody SignRequest request) throws Exception {
    return new ResponseEntity<>(userService.join(request), HttpStatus.OK);
  }
  @PostMapping("/login")
  public ResponseEntity<SignResponse> login(@RequestBody SignRequest request) throws Exception {
    return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
  }

  @GetMapping("/user/get")
  public ResponseEntity<SignResponse> getUser(@RequestParam String email) throws Exception {
    return new ResponseEntity<>( userService.getUser(email), HttpStatus.OK);
  }
  @GetMapping("/user/getStatus")
  public ResponseEntity<Object> getUserStatus(@AuthenticationPrincipal CustomUserDetails userDetails) throws Exception{
    return new ResponseEntity<>(userService.getUserStatus(userDetails),HttpStatus.OK);
  }

  @GetMapping("/admin/get")
  public ResponseEntity<SignResponse> getUserForAdmin(@RequestParam String email) throws Exception {
    return new ResponseEntity<>( userService.getUser(email), HttpStatus.OK);
  }
  @GetMapping("/refresh")
  public ResponseEntity<TokenDto> refresh(@RequestBody TokenDto token) throws Exception {
    return new ResponseEntity<>( userService.refreshAccessToken(token), HttpStatus.OK);
  }

  @ResponseBody
  @PostMapping("/join/emailCheck")
  public String mailConfirm(@RequestBody EmailCheckReq emailDto) throws MessagingException, UnsupportedEncodingException {

    String authCode = emailService.sendEmail(emailDto.getEmail());
    return authCode;
  }

  @ResponseBody
  @PostMapping("/join/emailCheckTrue")
  public boolean emailTrue(@RequestBody EmailCheckReq emailDto) {
    boolean emailCheckTrue = emailService.emailTrue(emailDto.getAuthNum());
    return emailCheckTrue;
  }

  @PostMapping("/user/changepwd")
  public ResponseEntity<Boolean> join(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody PwdRequest request) throws Exception {
    return new ResponseEntity<>(userService.changePwd(customUserDetails, request), HttpStatus.OK);
  }
  @GetMapping("/myprofile")
  public ResponseEntity<Object> myprofile(@AuthenticationPrincipal CustomUserDetails userDetails) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
    return new ResponseEntity<>(userService.getProfile(userDetails), headers, HttpStatus.OK);
  }

  @PutMapping("/myprofile/update")
  public ResponseEntity<Object> myprofileUpdate(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody MyprofileDto myprofileDto) {
    return new ResponseEntity<>(userService.profileUpdate(userDetails, myprofileDto), HttpStatus.OK);
  }

  @DeleteMapping("/myprofile/delete")
  public ResponseEntity<Object> userDelete(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody SignRequest request){
    userService.delete(userDetails,request);
    return ResponseEntity.ok("회원탈퇴 성공");
  }
  @PutMapping("/myprofile/ownerMode")
  public ResponseEntity<Object> ownerMode(@AuthenticationPrincipal CustomUserDetails userDetails) {
    userService.ownerMode(userDetails);
    return ResponseEntity.ok("차주 모드 변환(게시물 작성 가능)");
  }
  @PutMapping("/myprofile/guestMode")
  public ResponseEntity<Object> guestMode(@AuthenticationPrincipal CustomUserDetails userDetails) {
    userService.guestMode(userDetails);
    return ResponseEntity.ok("탑승자 변환");
  }

  @GetMapping("/list/guest")
  public ResponseEntity<Object> getGuestList(@AuthenticationPrincipal CustomUserDetails userDetails) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
    return new ResponseEntity<>(userService.getGuestList(userDetails), headers, HttpStatus.OK);
  }

  @GetMapping("/list/owner")
  public ResponseEntity<Object> getOwnerList(@AuthenticationPrincipal CustomUserDetails userDetails) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
    return new ResponseEntity<>(userService.getOwnerList(userDetails), headers, HttpStatus.OK);
  }
}