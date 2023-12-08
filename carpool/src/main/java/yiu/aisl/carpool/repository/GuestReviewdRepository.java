package yiu.aisl.carpool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.carpool.domain.GuestReviewed;

import java.util.List;

public interface GuestReviewdRepository extends JpaRepository<GuestReviewed, String> {
    List<GuestReviewed> findByEmail(String email);
}
