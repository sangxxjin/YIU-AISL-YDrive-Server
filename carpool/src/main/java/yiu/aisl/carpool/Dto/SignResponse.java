package yiu.aisl.carpool.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yiu.aisl.carpool.domain.User;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignResponse {

  private String email;

  private String name;

  private String phone;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getHome() {
    return home;
  }

  public void setHome(String home) {
    this.home = home;
  }

  public String getCarNum() {
    return carNum;
  }

  public void setCarNum(String carNum) {
    this.carNum = carNum;
  }

  public TokenDto getToken() {
    return token;
  }

  public void setToken(TokenDto token) {
    this.token = token;
  }

  public String getFcmToken() {
    return fcmToken;
  }

  public void setFcmToken(String fcmToken) {
    this.fcmToken = fcmToken;
  }

  private String home;

  private String carNum;

  private TokenDto token;

  private String fcmToken;

  public SignResponse(User user) {
    this.email = user.getEmail();
    this.name = user.getName();
    this.phone = user.getPhone();
    this.home = user.getHome();
    this.carNum = user.getCarNum();
    this.fcmToken = user.getFcmToken();
  }
}