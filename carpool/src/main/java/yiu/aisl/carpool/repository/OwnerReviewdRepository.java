package yiu.aisl.carpool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.carpool.domain.OwnerReviewed;


public interface OwnerReviewdRepository extends JpaRepository<OwnerReviewed, String> {

}
