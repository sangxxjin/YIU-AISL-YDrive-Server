package yiu.aisl.carpool.Dto;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerReviewedRequest {
  private int ReviewedNum;
  private int carpoolNum;
  private int star;
  private String review;
  private LocalDateTime createdAt;
  private String email;

  public int getReviewedNum() {
    return ReviewedNum;
  }

  public int getCarpoolNum() {
    return carpoolNum;
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
