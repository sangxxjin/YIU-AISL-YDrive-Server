package yiu.aisl.carpool.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.carpool.domain.Carpool;

public interface CarpoolRepository extends JpaRepository<Carpool, String> {
  Optional<Carpool> findByCarpoolNum(Integer carpoolNum);
  Optional<Carpool> findByCarpoolNumAndEmail(Integer carpoolNum, String email);
}
