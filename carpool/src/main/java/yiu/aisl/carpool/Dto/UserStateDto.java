package yiu.aisl.carpool.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStateDto {
  private int state;
  private String email;

  public void setState(int state) {
    this.state = state;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getState() {
    return state;
  }

  public String getEmail() {
    return email;
  }
}
