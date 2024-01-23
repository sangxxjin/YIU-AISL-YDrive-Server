package yiu.aisl.carpool.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yiu.aisl.carpool.domain.Carpool;

public interface CarpoolRepository extends JpaRepository<Carpool, String> {
  Optional<Carpool> findByCarpoolNum(Integer carpoolNum);
  Optional<Carpool> findByCarpoolNumAndEmail(Integer carpoolNum, String email);
  List<Carpool> findByEmail(String email);

  @Query("SELECT c FROM Carpool c WHERE c.email = :email AND c.checkNum = 3")
  List<Carpool> findByCheckNumAndOwnerWithCarpool(@Param("email") String email);
}
