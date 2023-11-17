package yiu.aisl.carpool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import yiu.aisl.carpool.domain.User;

import java.util.Optional;

@Transactional
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByEmail(String email);
}

