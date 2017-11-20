package dev.chernykh.api.tariff.service;

import dev.chernykh.api.tariff.model.Tariff;
import dev.chernykh.api.tariff.repository.TariffRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TariffService {
    private final TariffRepository tariffRepository;

    @Autowired
    public TariffService(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    public List<Tariff> getAllTariffs() {
        return tariffRepository.findAllTariffs();
    }

    public void updateTariffs(@NonNull List<Tariff> tariffs) {
        tariffRepository.save(tariffs);
    }

    public List<Tariff> getAllTariffPlanWhereDateOfChangeBetween(@NonNull String from, @NonNull String to) {
        return tariffRepository.findByDateOfChangeBetween(LocalDate.parse(from), LocalDate.parse(to));
    }

    public Optional<Tariff> getTariffById(@NonNull Long id) {
        return Optional.ofNullable(tariffRepository.findOne(id));
    }

    public void saveTariff(@NonNull Tariff tariff) {
        tariffRepository.save(tariff);

    }

    public void deleteTariff(@NonNull Long id) {
        tariffRepository.delete(id);
    }
}
