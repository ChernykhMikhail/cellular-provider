package dev.chernykh.cellular.api.tariff.service;

import dev.chernykh.cellular.api.tariff.model.Option;
import dev.chernykh.cellular.api.tariff.model.Tariff;
import dev.chernykh.cellular.api.tariff.repository.TariffRepository;
import org.hibernate.validator.constraints.NotBlank;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
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

    /**
     * Activate a tariff with given name if exists.
     *
     * @param name the tariff name
     * @return {@code true} if tariff was activated otherwise {@code false}
     */
    public boolean activate(@NotNull @NotBlank String name) {
        Tariff tariff = tariffRepository.findByTariffName(name);
        if (tariff != null) {
            tariff.setActive(true);
            tariffRepository.save(tariff);
            return true;
        }
        return false;
    }

    /**
     * Deactivate a tariff with given name if exists.
     *
     * @param name the tariff name
     * @return {@code true} if tariff was deactivated otherwise {@code false}
     */
    public boolean deactivate(@NotNull @NotBlank String name) {
        Tariff tariff = tariffRepository.findByTariffName(name);
        if (tariff != null) {
            tariff.setActive(false);
            tariffRepository.save(tariff);
            return true;
        }
        return false;
    }

    /**
     * Retrieve all tariffs.
     *
     * @return the list from tariffs
     */
    public List<Tariff> getTariffs() {
        return tariffRepository.findAllTariffs();
    }

    /**
     * Retrieve tariffs changed within period.
     *
     * @param from the date from the beginning period
     * @param to   the date from the end period
     * @return the list from changed tariffs
     */
    public List<Tariff> getTariffsChangedWithinPeriod(
            @NotNull @NotBlank String from,
            @NotNull @NotBlank String to) {
        LocalDate beginDate = LocalDate.parse(from);
        LocalDate endDate = LocalDate.parse(to);
        if (beginDate.isAfter(endDate)) {
//            Optional.empty() or Exception
        }
        return tariffRepository.findByDateOfChangeBetween(beginDate, endDate);
    }

    /**
     * Retrieve a tariff by id.
     *
     * @param id the tariff id
     * @return the retrieved tariff if one exists
     */
    public Optional<Tariff> getTariffById(Long id) {
        return Optional.ofNullable(tariffRepository.findOne(id));
    }

    /**
     * Create a new or updates the existing tariff.
     *
     * @param name      the tariff name
     * @param callPrice the price from call
     * @param smsPrice  the price from sms
     */
    public Tariff createTariff(
            @NotNull @NotBlank String name,
            @NotNull @Digits(integer = 5, fraction = 2) BigDecimal callPrice,
            @NotNull @Digits(integer = 5, fraction = 2) BigDecimal smsPrice) {
        Tariff tariff = tariffRepository.findByTariffName(name);
        if (tariff != null) {
            Money oldSmsPrice = tariff.getOptions().get("SMS").getNewPrice();
            tariff.getOptions().get("SMS").setNewPrice(Money.of(CurrencyUnit.ofNumericCode(643), smsPrice));
            tariff.getOptions().get("SMS").setOldPrice(oldSmsPrice);
            tariff.getOptions().get("SMS").setDateOfChange(LocalDate.now());

            Money oldVoiceCallPrice = tariff.getOptions().get("Voice call").getNewPrice();
            tariff.getOptions().get("Voice call").setNewPrice(Money.of(CurrencyUnit.ofNumericCode(643), callPrice));
            tariff.getOptions().get("Voice call").setOldPrice(oldVoiceCallPrice);
            tariff.getOptions().get("Voice call").setDateOfChange(LocalDate.now());
        } else {
            tariff = new Tariff();
            tariff.setTariffName(name);
            Money sms = Money.of(CurrencyUnit.ofNumericCode(643), smsPrice);
            Money call = Money.of(CurrencyUnit.ofNumericCode(643), callPrice);

            tariff.getOptions().put("SMS", new Option(null, "SMS", sms, sms, LocalDate.now()));
            tariff.getOptions().put("Voice call", new Option(null, "Voice call", call, call, LocalDate.now()));
        }
        return tariffRepository.save(tariff);
    }

    /**
     * Delete a tariff by id.
     *
     * @param id the tariff id
     */
    public void deleteTariff(Long id) {
        tariffRepository.delete(id);
    }
}
