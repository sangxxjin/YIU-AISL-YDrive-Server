package yiu.aisl.carpool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.carpool.domain.GuestReviewed;

public interface GuestReviewdRepository extends JpaRepository<GuestReviewed, String> {

}
