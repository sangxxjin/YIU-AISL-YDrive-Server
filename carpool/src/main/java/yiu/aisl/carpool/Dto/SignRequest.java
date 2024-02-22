package yiu.aisl.carpool.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignRequest {

  private String email;

  private String name;

  private String phone;

  private String city;

  public void setEmail(String email) {
    this.email = email;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public void setCarNum(String carNum) {
    this.carNum = carNum;
  }

  public void setFcmToken(String fcmToken) {
    this.fcmToken = fcmToken;
  }

  private String district;

  private String pwd;

  private String carNum;

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public String getPhone() {
    return phone;
  }

  public String getCity() {
    return city;
  }

  public String getDistrict() {
    return district;
  }

  public String getPwd() {
    return pwd;
  }

  public String getCarNum() {
    return carNum;
  }

  public String getFcmToken() {
    return fcmToken;
  }

  private String fcmToken;

}