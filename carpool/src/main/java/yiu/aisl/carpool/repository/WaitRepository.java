package yiu.aisl.carpool.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yiu.aisl.carpool.domain.Carpool;
import yiu.aisl.carpool.domain.Wait;

public interface WaitRepository extends JpaRepository<Wait, String> {
  Optional<Wait> findByCarpoolNum_CarpoolNumAndWaitNum(Integer carpoolNum, Integer waitNum);

  Optional<Wait> findByOwnerAndCarpoolNum_CarpoolNumAndWaitNum(String owner, Integer carpoolNum, Integer waitNum);

  boolean existsByCarpoolNumAndGuest(Carpool carpool, String email);

  @Query("SELECT w FROM Wait w JOIN FETCH w.carpoolNum c WHERE w.checkNum = 3 AND w.guest = :email")
  List<Wait> findByCheckNumAndGuestWithCarpool(@Param("email") String email);
}