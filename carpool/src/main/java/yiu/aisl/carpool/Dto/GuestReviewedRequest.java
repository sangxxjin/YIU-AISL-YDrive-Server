package yiu.aisl.carpool.Dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestReviewedRequest {
  private int ReviewedNum;
  private int waitNum;
  private int star;
  private String review;
  private Date createdAt;
  private String email;

}
