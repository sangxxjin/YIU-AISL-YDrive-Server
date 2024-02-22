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
public class CarpoolResponse {
    private int carpoolNum;
    private String start;
    private String end;
    private LocalDateTime date;
    private int memberNum;
    private String email;
    private int checkNum;
    private LocalDateTime createdAt;

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

    public int getCarpoolNum() {
        return carpoolNum;
    }

    public void setCarpoolNum(int carpoolNum) {
        this.carpoolNum = carpoolNum;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(int checkNum) {
        this.checkNum = checkNum;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
