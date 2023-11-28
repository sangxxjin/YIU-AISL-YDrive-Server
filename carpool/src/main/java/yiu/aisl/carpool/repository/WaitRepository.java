package yiu.aisl.carpool.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import yiu.aisl.carpool.domain.Carpool;
import yiu.aisl.carpool.domain.Wait;

public interface WaitRepository extends JpaRepository<Wait, String> {
  Optional<Wait> findByCarpoolNum_CarpoolNumAndWaitNum(Integer carpoolNum, Integer waitNum);

  Optional<Wait> findByOwnerAndCarpoolNum_CarpoolNumAndWaitNum(String owner, Integer carpoolNum,
      Integer waitNum);

}
