package yiu.aisl.carpool.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yiu.aisl.carpool.domain.Station;

public interface StationRepository extends JpaRepository<Station, Integer> {
    @Override
    List<Station> findAll();

    @Query("SELECT DISTINCT s.city FROM Station s")
    List<String> findDistinctCities();

    @Query("SELECT DISTINCT s.district FROM Station s")
    List<String> findDistinctDistricts();
}