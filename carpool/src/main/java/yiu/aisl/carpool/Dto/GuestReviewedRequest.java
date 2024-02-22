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
