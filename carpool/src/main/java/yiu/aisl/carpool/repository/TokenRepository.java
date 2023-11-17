package yiu.aisl.carpool.repository;

import org.springframework.data.repository.CrudRepository;
import yiu.aisl.carpool.domain.Token;

// CRUDRepository를 확장하면 save, findBy 등등의 동작을 수행할 수 있다.
public interface TokenRepository extends CrudRepository<Token, String> {
}