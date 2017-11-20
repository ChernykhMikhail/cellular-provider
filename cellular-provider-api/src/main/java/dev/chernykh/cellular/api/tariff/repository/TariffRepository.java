package dev.chernykh.cellular.api.tariff.repository;

import dev.chernykh.cellular.api.tariff.model.Tariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TariffRepository extends JpaRepository<Tariff, Long> {

    @Query("SELECT DISTINCT t FROM Tariff t JOIN FETCH t.options")
    List<Tariff> findAllTariffs();

    @Query("select distinct t from Tariff t join fetch t.options o where o.dateOfChange between :fr and :to")
    List<Tariff> findByDateOfChangeBetween(@Param("fr") LocalDate fr, @Param("to") LocalDate to);
}
