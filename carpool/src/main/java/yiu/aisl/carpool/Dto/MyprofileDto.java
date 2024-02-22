package yiu.aisl.carpool.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyprofileDto {
  private String name;
  private String phone;
  private String home;
  private String carNum;

  public void setName(String name) {
    this.name = name;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setHome(String home) {
    this.home = home;
  }

  public void setCarNum(String carNum) {
    this.carNum = carNum;
  }

  public String getName() {
    return name;
  }

  public String getPhone() {
    return phone;
  }

  public String getHome() {
    return home;
  }

  public String getCarNum() {
    return carNum;
  }
}
