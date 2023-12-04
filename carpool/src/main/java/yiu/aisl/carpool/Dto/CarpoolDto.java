package yiu.aisl.carpool.Dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yiu.aisl.carpool.domain.Carpool;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarpoolDto {
  private String start;
  private String end;
  private LocalDateTime date;
  private int memberNum;

  public CarpoolDto(Carpool carpool) {
    this.start = carpool.getStart();
    this.end = carpool.getEnd();
    this.date = carpool.getDate();
    this.memberNum = carpool.getMemberNum();
  }
}
