package dev.chernykh.cellular.api.tariff;

import dev.chernykh.cellular.api.tariff.model.Tariff;
import dev.chernykh.cellular.api.tariff.service.TariffService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TariffController.class)
public class TariffControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TariffService tariffService;

    @Test
    public void shouldActivateExistingTariffAndReturnSuccess() throws Exception {
        given(tariffService.activate("Tariff 2"))
                .willReturn(true);

        mvc.perform(
                get("/tariffs/activate")
                        .param("tariffName", "Tariff 2"))
                .andExpect(status().isOk());

        verify(tariffService, times(1)).activate("Tariff 2");
    }

    @Test
    public void shouldReturnErrorUponActivatingNotExistingTariff() throws Exception {
        given(tariffService.activate("Tariff 8"))
                .willReturn(false);

        mvc.perform(
                get("/tariffs/activate")
                        .param("tariffName", "Tariff 8"))
                .andExpect(status().isNotFound());

        verify(tariffService, times(1)).activate("Tariff 8");
    }

    @Test
    public void shouldDeactivateExistingTariffAndReturnSuccess() throws Exception {
        given(tariffService.deactivate("Tariff 3"))
                .willReturn(true);

        mvc.perform(
                get("/tariffs/deactivate")
                        .param("tariffName", "Tariff 3"))
                .andExpect(status().isOk());

        verify(tariffService, times(1)).deactivate("Tariff 3");
    }

    @Test
    public void shouldReturnErrorUponDeactivatingNotExistingTariff() throws Exception {
        given(tariffService.deactivate("Tariff 10"))
                .willReturn(false);

        mvc.perform(
                get("/tariffs/deactivate")
                        .param("tariffName", "Tariff 10"))
                .andExpect(status().isNotFound());

        verify(tariffService, times(1)).deactivate("Tariff 10");
    }

    @Test
    public void shouldReturnAllTariffs() throws Exception {
        given(tariffService.getTariffs()).willReturn(asList(
                Tariff.from("Tariff 1", 2.34, 1.43),
                Tariff.from("Tariff 2", 1.65, 1.76)
        ));

        mvc.perform(
                get("/tariffs")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
                .andExpect(jsonPath("$[0].tariffName", is("Tariff 1")))
                .andExpect(jsonPath("$[0].active", is(false)))
//                .andExpect(jsonPath("$[0].options", empty()))

                .andExpect(jsonPath("$[1].id", notNullValue()))
                .andExpect(jsonPath("$[1].tariffName", is("Tariff 2")))
                .andExpect(jsonPath("$[1].active", is(false)));
//                .andExpect(jsonPath("$[1].options", empty()));

        verify(tariffService, times(1)).getTariffs();
    }

    @Test
    public void shouldReturnTariffsChangedWithinPeriod() throws Exception {
        given(tariffService.getTariffsChangedWithinPeriod("2017-11-01", "2017-12-01"))
                .willReturn(asList(
                        Tariff.from("Tariff 3", 1.12, 1.54),
                        Tariff.from("Tariff 11", 1.65, 1.02)
                ));

        mvc.perform(
                get("/tariffs")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("from", "2017-11-01")
                        .param("to", "2017-12-01"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", notNullValue()))
//                .andExpect(jsonPath("$[0].options.dateOfChange", greaterThan(LocalDate.from(2017,11,1))))
//                .andExpect(jsonPath("$[1].options.dateOfChange", lessThan(LocalDate.from(2017,12,1))))
                .andExpect(jsonPath("$[1].id", notNullValue()));

        verify(tariffService, times(1))
                .getTariffsChangedWithinPeriod("2017-11-01", "2017-12-01");
    }

    @Test
    public void shouldReturnTariffById() throws Exception {
        given(tariffService.getTariffById(3L))
                .willReturn(Optional.of(
                        Tariff.from("Tariff 3", 1.89, 2.32)
                ));

        mvc.perform(
                get("/tariffs/3")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.tariffName", is("Tariff 3")))
                .andExpect(jsonPath("$.active", is(false)));
//                .andExpect(jsonPath("$.options", empty()));

        verify(tariffService, times(1)).getTariffById(3L);
    }

    @Test
    public void shouldReturnErrorIfTariffHasNotBeenFound() throws Exception {
        given(tariffService.getTariffById(10L))
                .willReturn(Optional.empty());

        mvc.perform(
                get("/tariffs/10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(tariffService, times(1)).getTariffById(10L);
    }

    @Test
    public void shouldSaveTariffThenReturnThisOneBack() throws Exception {
        given(tariffService.createTariff("Tariff 5", BigDecimal.valueOf(1.23), BigDecimal.valueOf(0.98)))
                .willReturn(
                        Tariff.from("Tariff 5", 1.23, 0.98)
                );

        mvc.perform(
                post("/tariffs")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("tariffName", "Tariff 5")
                        .param("callPrice", "1.23")
                        .param("smsPrice", "0.98"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.tariffName", is("Tariff 5")))
                .andExpect(jsonPath("$.active", is(false)));
//                .andExpect(jsonPath("$.options", empty()));

        verify(tariffService, times(1))
                .createTariff("Tariff 5", BigDecimal.valueOf(1.23), BigDecimal.valueOf(0.98));

    }

    @Test
    public void shouldDeleteTariffById() throws Exception {
        doNothing()
                .when(tariffService).deleteTariff(6L);

        mvc.perform(delete("/tariffs/6"))
                .andExpect(status().isNoContent());

        verify(tariffService, times(1)).deleteTariff(6L);
    }
}