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

    public void setCarpoolNum(int carpoolNum) {
        this.carpoolNum = carpoolNum;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCheckNum(int checkNum) {
        this.checkNum = checkNum;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
