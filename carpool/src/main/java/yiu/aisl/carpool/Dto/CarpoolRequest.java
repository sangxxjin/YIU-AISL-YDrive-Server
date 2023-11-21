package yiu.aisl.carpool.Dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CarpoolRequest {
    private int carpoolNum;
    private String start;
    private String end;
    private LocalDateTime date;
    private int memberNum;
    private String email;
    private int checkNum;
    private Date createdAt;
}
