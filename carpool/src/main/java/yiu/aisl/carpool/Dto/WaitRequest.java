package yiu.aisl.carpool.Dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import yiu.aisl.carpool.domain.Carpool;

@Getter
@Setter
public class WaitRequest {
    private int waitNum;
    private Carpool carpoolNum;
    private String guest;
    private String owner;
    private int checkNum;
    private LocalDateTime createdAt;

    public int getWaitNum() {
        return waitNum;
    }

    public Carpool getCarpoolNum() {
        return carpoolNum;
    }

    public String getGuest() {
        return guest;
    }

    public String getOwner() {
        return owner;
    }

    public int getCheckNum() {
        return checkNum;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
