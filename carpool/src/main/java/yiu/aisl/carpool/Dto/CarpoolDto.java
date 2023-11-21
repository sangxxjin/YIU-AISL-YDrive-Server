package yiu.aisl.carpool.Dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarpoolDto {
  private String start;
  private String end;
  private Date date;
  private int memberNum;


}
