package yiu.aisl.carpool.Dto;

import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStateDto {
  private int state;

}
