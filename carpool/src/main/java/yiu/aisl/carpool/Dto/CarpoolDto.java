package yiu.aisl.carpool.Dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yiu.aisl.carpool.domain.Carpool;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarpoolDto {
  private String start;
  private String end;
  private LocalDateTime date;
  private int memberNum;
}
