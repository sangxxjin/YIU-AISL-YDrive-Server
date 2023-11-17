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

  private String home;

  private String carNum;

  private TokenDto token;

  public SignResponse(User user) {
    this.email = user.getEmail();
    this.name = user.getName();
    this.phone = user.getPhone();
    this.home = user.getHome();
    this.carNum = user.getCarNum();
  }
}