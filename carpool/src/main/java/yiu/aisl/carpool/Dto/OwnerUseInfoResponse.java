package yiu.aisl.carpool.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yiu.aisl.carpool.domain.Carpool;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnerUseInfoResponse {
    private String start;
    private String end;
    private LocalDateTime date;

    public OwnerUseInfoResponse(Carpool carpool) {
        this.start = carpool.getStart();
        this.end = carpool.getEnd();
        this.date = carpool.getDate();
    }
}
