package yiu.aisl.carpool.Dto;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnerReviewedRequest {
  private int ReviewedNum;
  private int carpoolNum;
  private int star;
  private String review;
  private Date createdAt;
  private String email;
}
