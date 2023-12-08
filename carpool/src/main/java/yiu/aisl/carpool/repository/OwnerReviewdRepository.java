package yiu.aisl.carpool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.carpool.domain.OwnerReviewed;

import java.util.List;


public interface OwnerReviewdRepository extends JpaRepository<OwnerReviewed, String> {
    List<OwnerReviewed> findByEmail(String email);
}
