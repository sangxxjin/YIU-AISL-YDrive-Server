package yiu.aisl.carpool.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.carpool.Dto.*;
import yiu.aisl.carpool.repository.UserRepository;
import yiu.aisl.carpool.security.CustomUserDetails;
import yiu.aisl.carpool.service.EmailService;
import yiu.aisl.carpool.service.UserService;

import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserRepository userRepository;
  private final UserService userService;
  private final EmailService emailService;

  @PostMapping(value="/join", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<Boolean> join(SignRequest request) throws Exception {
    return new ResponseEntity<>(userService.join(request), HttpStatus.OK);
  }
  @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<SignResponse> login(SignRequest request) throws Exception {
    return new ResponseEntity<>(userService.login(request), HttpStatus.OK);
  }

  @GetMapping("/user/get")
  public ResponseEntity<SignResponse> getUser(@RequestParam String email) throws Exception {
    return new ResponseEntity<>( userService.getUser(email), HttpStatus.OK);
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
  @PostMapping(value="/join/emailCheck", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public String mailConfirm(EmailCheckReq emailDto) throws MessagingException, UnsupportedEncodingException {

    String authCode = emailService.sendEmail(emailDto.getEmail());
    return authCode;
  }

  @ResponseBody
  @PostMapping("/join/emailCheckTrue")
  public boolean emailTrue(@RequestBody EmailCheckReq emailDto) {
    boolean emailCheckTrue = emailService.emailTrue(emailDto.getAuthNum());
    return emailCheckTrue;
  }

  @PostMapping(value="/user/changepwd", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public ResponseEntity<Boolean> join(PwdRequest request) throws Exception {
    return new ResponseEntity<>(userService.changePwd(request), HttpStatus.OK);
  }
  @GetMapping("/myprofile")
  public ResponseEntity<Object> myprofile(@AuthenticationPrincipal CustomUserDetails userDetails) {
    return new ResponseEntity<>(userService.getProfile(userDetails), HttpStatus.OK);
  }

  @PutMapping("/myprofile/update")
  public ResponseEntity<Object> myprofileUpdate(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody MyprofileDto myprofileDto) {
    return new ResponseEntity<>(userService.profileUpdate(userDetails, myprofileDto), HttpStatus.OK);
  }
}