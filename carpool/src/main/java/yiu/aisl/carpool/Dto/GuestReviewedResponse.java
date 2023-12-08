package yiu.aisl.carpool.Dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yiu.aisl.carpool.domain.GuestReviewed;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GuestReviewedResponse {
    private int star;
    private String review;

    public GuestReviewedResponse(GuestReviewed guestReviewed) {
        this.star = guestReviewed.getStar();
        this.review = guestReviewed.getReview();
    }
}
