package yiu.aisl.carpool.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Wait")
public class Wait {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int waitNum;

    @ManyToOne
    @JoinColumn(name="carpoolNum", referencedColumnName="carpoolNum")
    private Carpool carpoolNum;

    @Column(nullable = false)
    private String guest;

    @Column(nullable = false)
    private String owner;

    @Column(nullable = false)
    private int checkNum;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    public int getWaitNum() {
        return waitNum;
    }

    public void setWaitNum(int waitNum) {
        this.waitNum = waitNum;
    }

    public Carpool getCarpoolNum() {
        return carpoolNum;
    }

    public void setCarpoolNum(Carpool carpoolNum) {
        this.carpoolNum = carpoolNum;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

