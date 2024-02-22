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