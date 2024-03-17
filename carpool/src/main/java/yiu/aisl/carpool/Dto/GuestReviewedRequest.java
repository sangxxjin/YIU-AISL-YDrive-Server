package yiu.aisl.carpool.Dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestReviewedRequest {
  private int ReviewedNum;
  private int waitNum;
  private int star;
  private String review;
  private LocalDateTime createdAt;
  private String email;

  public void setReviewedNum(int reviewedNum) {
    ReviewedNum = reviewedNum;
  }

  public void setWaitNum(int waitNum) {
    this.waitNum = waitNum;
  }

  public void setStar(int star) {
    this.star = star;
  }

  public void setReview(String review) {
    this.review = review;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getReviewedNum() {
    return ReviewedNum;
  }

  public int getWaitNum() {
    return waitNum;
  }

  public int getStar() {
    return star;
  }

  public String getReview() {
    return review;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public String getEmail() {
    return email;
  }
}
