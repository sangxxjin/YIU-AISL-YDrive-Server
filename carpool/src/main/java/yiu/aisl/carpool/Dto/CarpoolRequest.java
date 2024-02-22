package yiu.aisl.carpool.Dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

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
    private LocalDateTime createdAt;

    public int getCarpoolNum() {
        return carpoolNum;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public String getEmail() {
        return email;
    }

    public int getCheckNum() {
        return checkNum;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
