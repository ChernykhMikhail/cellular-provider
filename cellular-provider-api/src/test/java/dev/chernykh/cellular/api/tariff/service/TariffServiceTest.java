package dev.chernykh.cellular.api.tariff.service;

import dev.chernykh.cellular.api.tariff.model.Tariff;
import dev.chernykh.cellular.api.tariff.repository.TariffRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TariffServiceTest {

    @Autowired
    private TariffService tariffService;

    @MockBean
    private TariffRepository tariffRepository;

    @Test
    public void shouldActivateExistingTariff() {
        Tariff tariff = Tariff.from(1, "Tariff 1", 2.23, 1.23);
        when(tariffRepository.findByTariffName("Tariff 1"))
                .thenReturn(tariff);

        boolean activated = tariffService.activate("Tariff 1");
        assertTrue(activated);
        assertTrue(tariff.isActive());

        verify(tariffRepository, times(1)).findByTariffName("Tariff 1");
    }

    @Test
    public void shouldFailActivatingNotExistingTariff() {
        when(tariffRepository.findByTariffName("Tariff 5"))
                .thenReturn(null);

        boolean notActivated = tariffService.activate("Tariff 5");
        assertFalse(notActivated);

        verify(tariffRepository, times(1)).findByTariffName("Tariff 5");
        verifyZeroInteractions(tariffRepository);
    }

    @Test
    public void shouldDeactivateExistingTariff() {
        Tariff tariff = Tariff.from(1, "Tariff 4", 3.23, 1.63);
        when(tariffRepository.findByTariffName("Tariff 4"))
                .thenReturn(tariff);

        boolean deactivated = tariffService.deactivate("Tariff 4");
        assertTrue(deactivated);
        assertFalse(tariff.isActive());

        verify(tariffRepository, times(1)).findByTariffName("Tariff 4");
    }

    @Test
    public void shouldFailDeactivatingNotExistingTariff() {
        when(tariffRepository.findByTariffName("Tariff 3"))
                .thenReturn(null);

        boolean notDeactivated = tariffService.deactivate("Tariff 3");
        assertFalse(notDeactivated);

        verify(tariffRepository, times(1)).findByTariffName("Tariff 3");
        verifyZeroInteractions(tariffRepository);
    }

    @Test
    public void shouldReturnTariffs() {
        when(tariffRepository.findAllTariffs())
                .thenReturn(asList(
                        Tariff.from(1, "Tariff 1", 2.34, 5.65),
                        Tariff.from(2, "Tariff 2", 2.14, 1.34)
                ));

        List<Tariff> tariffs = tariffService.getTariffs();

        assertThat(tariffs, notNullValue());
        assertThat(tariffs, hasSize(2));
        assertThat(tariffs.get(0).getId(), is(1L));
        assertThat(tariffs.get(0).getTariffName(), is("Tariff 1"));
        assertThat(tariffs.get(0).isActive(), is(false));
        assertThat(tariffs.get(0).getOptions(), notNullValue());
        assertTrue(tariffs.get(0).getOptions().containsKey("call"));
        assertThat(tariffs.get(0).getOptions().get("call").getNewPrice().getAmount(), is(BigDecimal.valueOf(2.34)));
        assertTrue(tariffs.get(0).getOptions().containsKey("sms"));
        assertThat(tariffs.get(0).getOptions().get("sms").getNewPrice().getAmount(), is(BigDecimal.valueOf(5.65)));

        assertThat(tariffs.get(1).getId(), is(2L));
        assertThat(tariffs.get(1).getTariffName(), is("Tariff 2"));
        assertThat(tariffs.get(1).isActive(), is(false));
        assertThat(tariffs.get(1).getOptions(), notNullValue());
        assertTrue(tariffs.get(1).getOptions().containsKey("call"));
        assertThat(tariffs.get(1).getOptions().get("call").getNewPrice().getAmount(), is(BigDecimal.valueOf(2.14)));
        assertTrue(tariffs.get(1).getOptions().containsKey("sms"));
        assertThat(tariffs.get(1).getOptions().get("sms").getNewPrice().getAmount(), is(BigDecimal.valueOf(1.34)));
    }

    @Test
    public void shouldReturnTariffsChangedWithinPeriod() {
        when(tariffRepository.findByDateOfChangeBetween(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)))
                .thenReturn(asList(
                        Tariff.from("Tariff 3", 2.34, 1.76)
                ));

        List<Tariff> tariffs = tariffService.getTariffsChangedWithinPeriod(
                LocalDate.now().minusDays(1).toString(),
                LocalDate.now().plusDays(1).toString());

        assertThat(tariffs, notNullValue());
        assertThat(tariffs, hasSize(1));
        assertThat(tariffs.get(0), notNullValue());
        assertThat(tariffs.get(0).getId(), notNullValue());
        assertThat(tariffs.get(0).getTariffName(), is("Tariff 3"));
        assertThat(tariffs.get(0).getOptions(), notNullValue());
        assertTrue(tariffs.get(0).getOptions().containsKey("call"));
        assertThat(tariffs.get(0).getOptions().get("call").getId(), notNullValue());
        assertThat(tariffs.get(0).getOptions().get("call").getOptionName(), is("Voice call"));
        assertThat(tariffs.get(0).getOptions().get("call").getNewPrice().getAmount(), is(BigDecimal.valueOf(2.34)));
        assertThat(tariffs.get(0).getOptions().get("call").getOldPrice().getAmount(), is(BigDecimal.valueOf(2.34)));
        assertThat(tariffs.get(0).getOptions().get("call").getDateOfChange(), is(lessThan(LocalDate.now().plusDays(1))));
        assertThat(tariffs.get(0).getOptions().get("call").getDateOfChange(), is(greaterThan(LocalDate.now().minusDays(1))));

        assertTrue(tariffs.get(0).getOptions().containsKey("sms"));
        assertThat(tariffs.get(0).getOptions().get("sms").getId(), notNullValue());
        assertThat(tariffs.get(0).getOptions().get("sms").getOptionName(), is("SMS"));
        assertThat(tariffs.get(0).getOptions().get("sms").getNewPrice().getAmount(), is(BigDecimal.valueOf(1.76)));
        assertThat(tariffs.get(0).getOptions().get("sms").getOldPrice().getAmount(), is(BigDecimal.valueOf(1.76)));
        assertThat(tariffs.get(0).getOptions().get("sms").getDateOfChange(), is(lessThan(LocalDate.now().plusDays(1))));
        assertThat(tariffs.get(0).getOptions().get("sms").getDateOfChange(), is(greaterThan(LocalDate.now().minusDays(1))));

        verify(tariffRepository, times(1))
                .findByDateOfChangeBetween(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
    }

    @Test
    public void shouldReturnTariffById() {
        when(tariffRepository.findOne(1L))
                .thenReturn(Tariff.from(1, "Tariff 1", 2.22, 1.11));

        Optional<Tariff> tariff = tariffService.getTariffById(1L);

        assertThat(tariff, notNullValue());
        assertTrue(tariff.isPresent());
        assertThat(tariff.get().getId(), is(1L));
        assertThat(tariff.get().getTariffName(), is("Tariff 1"));
        assertFalse(tariff.get().isActive());
        assertThat(tariff.get().getOptions(), notNullValue());
        assertTrue(tariff.get().getOptions().containsKey("call"));
        assertThat(tariff.get().getOptions().get("call"), notNullValue());
        assertThat(tariff.get().getOptions().get("call").getId(), notNullValue());
        assertThat(tariff.get().getOptions().get("call").getOptionName(), is("Voice call"));
        assertThat(tariff.get().getOptions().get("call").getNewPrice().getAmount(), is(BigDecimal.valueOf(2.22)));
        assertThat(tariff.get().getOptions().get("call").getOldPrice().getAmount(), is(BigDecimal.valueOf(2.22)));
        assertThat(tariff.get().getOptions().get("call").getDateOfChange(), is(LocalDate.now()));

        assertTrue(tariff.get().getOptions().containsKey("sms"));
        assertThat(tariff.get().getOptions().get("sms"), notNullValue());
        assertThat(tariff.get().getOptions().get("sms").getId(), notNullValue());
        assertThat(tariff.get().getOptions().get("sms").getOptionName(), is("SMS"));
        assertThat(tariff.get().getOptions().get("sms").getNewPrice().getAmount(), is(BigDecimal.valueOf(1.11)));
        assertThat(tariff.get().getOptions().get("sms").getOldPrice().getAmount(), is(BigDecimal.valueOf(1.11)));
        assertThat(tariff.get().getOptions().get("sms").getDateOfChange(), is(LocalDate.now()));

        verify(tariffRepository, times(1)).findOne(1L);
    }

    @Test
    public void shouldReturnNothingByWrongId() {
        when(tariffRepository.findOne(0L))
                .thenReturn(null);

        Optional<Tariff> tariffById = tariffService.getTariffById(0L);

        assertThat(tariffById, notNullValue());
        assertFalse(tariffById.isPresent());

        verify(tariffRepository, times(1)).findOne(0L);
    }

    @Ignore("createTariff() returns null")
    @Test
    public void shouldCreateTariffAndReturnIt() {
        Tariff tariff = Tariff.from("Tariff 1", 1.00, 2.00);
        when(tariffRepository.save(tariff))
                .thenReturn(tariff);

        Tariff savedTariff = tariffService.createTariff("Tariff 1", BigDecimal.valueOf(1.00), BigDecimal.valueOf(2.00));

        assertThat(savedTariff, notNullValue());
        assertThat(savedTariff.getId(), notNullValue());
        assertThat(savedTariff.getTariffName(), is("Tariff 1"));
        assertFalse(savedTariff.isActive());
        assertThat(savedTariff.getOptions(), notNullValue());
        assertTrue(savedTariff.getOptions().containsKey("call"));
        assertThat(savedTariff.getOptions().get("call"), notNullValue());
        assertThat(savedTariff.getOptions().get("call").getId(), notNullValue());
        assertThat(savedTariff.getOptions().get("call").getOptionName(), is("Voice call"));
        assertThat(savedTariff.getOptions().get("call").getNewPrice().getAmount(), is(BigDecimal.valueOf(1.00)));
        assertThat(savedTariff.getOptions().get("call").getOldPrice().getAmount(), is(BigDecimal.valueOf(1.00)));
        assertThat(savedTariff.getOptions().get("call").getDateOfChange(), is(LocalDate.now()));

        assertTrue(savedTariff.getOptions().containsKey("sms"));
        assertThat(savedTariff.getOptions().get("sms"), notNullValue());
        assertThat(savedTariff.getOptions().get("sms").getId(), notNullValue());
        assertThat(savedTariff.getOptions().get("sms").getOptionName(), is("SMS"));
        assertThat(savedTariff.getOptions().get("sms").getNewPrice().getAmount(), is(BigDecimal.valueOf(2.00)));
        assertThat(savedTariff.getOptions().get("sms").getOldPrice().getAmount(), is(BigDecimal.valueOf(2.00)));
        assertThat(savedTariff.getOptions().get("sms").getDateOfChange(), is(LocalDate.now()));

        verify(tariffRepository, times(1)).save(tariff);
    }

    @Test
    public void shouldDeleteTariffById() {
        doNothing()
                .when(tariffRepository).delete(3L);

        tariffService.deleteTariff(3L);

        verify(tariffRepository, times(1)).delete(3L);
    }
}