package yiu.aisl.carpool.Dto;

import lombok.Getter;
import lombok.Setter;
import yiu.aisl.carpool.domain.Carpool;

import java.util.Date;

@Getter
@Setter
public class WaitRequest {
    private int waitNum;
    private Carpool carpoolNum;
    private String guest;
    private String owner;
    private int checkNum;
    private Date createdAt;
}
