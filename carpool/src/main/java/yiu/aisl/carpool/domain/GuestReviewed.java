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
@Table(name="GuestReviewed")
public class GuestReviewed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int guestReviewedNum;

    @ManyToOne
    @JoinColumn(nullable = false, name="Wait")
    private Wait waitNum;

    @Column
    private int star;

    @Column
    private String review;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String email;

    public int getGuestReviewedNum() {
        return guestReviewedNum;
    }

    public void setGuestReviewedNum(int guestReviewedNum) {
        this.guestReviewedNum = guestReviewedNum;
    }

    public Wait getWaitNum() {
        return waitNum;
    }

    public void setWaitNum(Wait waitNum) {
        this.waitNum = waitNum;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}