package yiu.aisl.carpool.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CarpoolRequest {
    private int carpoolNum;
    private String start;
    private String end;
    private Date date;
    private int memberNum;
    private String email;
    private int checkNum;
    private Date createdAt;
}
