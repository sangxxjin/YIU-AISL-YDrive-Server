package yiu.aisl.carpool.Dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yiu.aisl.carpool.domain.Carpool;

import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarpoolResponse {
    private int carpoolNum;
    private String start;
    private String end;
    private LocalDateTime date;
    private int memberNum;
    private String email;
    private int checkNum;
    private Date createdAt;

    public CarpoolResponse(Carpool carpool) {
        this.carpoolNum = carpool.getCarpoolNum();
        this.start = carpool.getStart();
        this.end = carpool.getEnd();
        this.date = carpool.getDate();
        this.memberNum = carpool.getMemberNum();
        this.email = carpool.getEmail();
        this.checkNum = carpool.getCheckNum();
        this.createdAt = carpool.getCreatedAt();
    }
}
