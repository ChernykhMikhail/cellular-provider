package dev.chernykh.cellular.api.tariff.repository;

import dev.chernykh.cellular.api.tariff.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
}
