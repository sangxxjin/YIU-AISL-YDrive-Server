package yiu.aisl.carpool.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.carpool.domain.Carpool;
import yiu.aisl.carpool.domain.User;

public interface CarpoolRepository extends JpaRepository<Carpool, String> {
  Optional<Carpool> findByCarpoolNum(int carpoolNum);
  Optional<Carpool> findByCarpoolNumAndEmail(int carpoolNum, String email);
}
