package yiu.aisl.carpool.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yiu.aisl.carpool.Dto.EmailCheckReq;
import yiu.aisl.carpool.Dto.SignRequest;
import yiu.aisl.carpool.Dto.SignResponse;
import yiu.aisl.carpool.Dto.TokenDto;
import yiu.aisl.carpool.repository.UserRepository;
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

  @PostMapping("/join")
  public ResponseEntity<Boolean> join(@RequestBody SignRequest request) throws Exception {
    return new ResponseEntity<>(userService.join(request), HttpStatus.OK);
  }
  @PostMapping(value = "/login")
  public ResponseEntity<SignResponse> login(@RequestBody SignRequest request) throws Exception {
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
  @PostMapping("/join/emailCheck")
  public String mailConfirm(@RequestBody EmailCheckReq emailDto) throws MessagingException, UnsupportedEncodingException {

    String authCode = emailService.sendEmail(emailDto.getEmail());
    return authCode;
  }
}