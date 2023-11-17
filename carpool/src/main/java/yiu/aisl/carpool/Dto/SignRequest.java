package yiu.aisl.carpool.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignRequest {

  private String email;

  private String name;

  private String phone;

  private String home;

  private String pwd;

  private String carNum;

}