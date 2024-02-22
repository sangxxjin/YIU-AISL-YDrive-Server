package yiu.aisl.carpool.Dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yiu.aisl.carpool.domain.OwnerReviewed;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnerReviewedResponse {
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

    private int star;
    private String review;

    public OwnerReviewedResponse(OwnerReviewed ownerReviewed) {
        this.star = ownerReviewed.getStar();
        this.review = ownerReviewed.getReview();
    }
}
