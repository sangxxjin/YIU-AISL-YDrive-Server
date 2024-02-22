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
@Table(name="OwnerReviewed")
public class OwnerReviewed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int ownerReviewedNum;

    @ManyToOne
    @JoinColumn(nullable = false, name="Carpool")
    private Carpool carpoolNum;

    @Column
    private int star;

    @Column
    private String review;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String email;

    public int getOwnerReviewedNum() {
        return ownerReviewedNum;
    }

    public void setOwnerReviewedNum(int ownerReviewedNum) {
        this.ownerReviewedNum = ownerReviewedNum;
    }

    public Carpool getCarpoolNum() {
        return carpoolNum;
    }

    public void setCarpoolNum(Carpool carpoolNum) {
        this.carpoolNum = carpoolNum;
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
