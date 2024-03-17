package yiu.aisl.carpool.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import yiu.aisl.carpool.exception.CustomException;
import yiu.aisl.carpool.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender emailSender;
  private final StringRedisTemplate redisTemplate;

  private String authNum;

  // 인증번호 6자리 생성
  public void createCode() {
    Random random = new Random();
    StringBuilder key = new StringBuilder();

    for (int i = 0; i < 6; i++) {
      // 0~9 사이의 값을 랜덤하게 받아옴
      int digit = random.nextInt(10);
      key.append(digit);
    }

    authNum = key.toString();
  }

  // 메일 양식 작성
  public MimeMessage createEmailForm(String email) throws MessagingException {
    // 코드 생성
    createCode();
    String setFrom = "callikys@naver.com";
    String toEmail = email;
    String title = "YDRIVE 이메일 인증";

    jakarta.mail.internet.MimeMessage message = emailSender.createMimeMessage();
    message.addRecipients(MimeMessage.RecipientType.TO, toEmail);
    message.setSubject(title);

    // 메일 내용 설정
    String msgOfEmail = "";
    msgOfEmail += "<div style='margin:20px;'>";
    msgOfEmail += "<h1> 안녕하세요 YDRIVE 입니다. </h1>";
    msgOfEmail += "<br>";
    msgOfEmail += "<p>아래 코드를 입력해주세요<p>";
    msgOfEmail += "<br>";
    msgOfEmail += "<p>감사합니다.<p>";
    msgOfEmail += "<br>";
    msgOfEmail += "<div align='center' style='border:1px solid black; font-family:verdana';>";
    msgOfEmail += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
    msgOfEmail += "<div style='font-size:130%'>";
    msgOfEmail += "CODE : <strong>";
    msgOfEmail += authNum + "</strong><div><br/> ";
    msgOfEmail += "</div>";

    message.setFrom(setFrom);
    message.setText(msgOfEmail, "utf-8", "html");
    return message;
  }

  // 실제 메일 전송
  public String sendEmail(String email) throws MessagingException {
    email = email + "@yiu.ac.kr";
    MimeMessage emailForm = createEmailForm(email);
    emailSender.send(emailForm);
    // Redis에 이메일 주소랑 함께 인증 코드 저장 10분간 저장 변경가능
    redisTemplate.opsForValue().set(email, authNum, 3, TimeUnit.MINUTES);
    return authNum;
  }

  // 이메일 검증
  public boolean emailTrue(String email, String userAuthNum) {
    if (userAuthNum.isEmpty()) {
      throw new CustomException(ErrorCode.INSUFFICIENT_DATA);
    }
    String storedAuthNum = redisTemplate.opsForValue().get(email);
    if (storedAuthNum != null && storedAuthNum.equals(userAuthNum)) {
      return true; // 코드 인증 성공
    } else {
      throw new CustomException(ErrorCode.INVALID_EMAIL_VERIFICATION_CODE);
    }
  }
}
