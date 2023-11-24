package yiu.aisl.carpool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.carpool.domain.Wait;

public interface WaitRepository extends JpaRepository<Wait, String> {
}
