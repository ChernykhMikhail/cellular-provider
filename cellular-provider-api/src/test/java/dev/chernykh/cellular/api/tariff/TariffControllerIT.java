package dev.chernykh.cellular.api.tariff;

import dev.chernykh.cellular.api.RestApiApplication;
import dev.chernykh.cellular.api.tariff.model.Tariff;
import dev.chernykh.cellular.api.tariff.service.TariffService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RestApiApplication.class)
public class TariffControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @MockBean
    private TariffService tariffService;

    private Tariff tariff;
    private String jsonTariff;
    private List<Tariff> tariffs;
    private String jsonTariffs;

    @Before
    public void setUp() throws Exception {

        mvc = webAppContextSetup(webApplicationContext).build();

        tariff = new Tariff(1L, "Tariff 1", true, null);
        jsonTariff = "{\"id\": 1, \"tariffName\": \"Tariff 1\", \"active\": true, \"options\": []}";
        tariffs = Stream.of(
                tariff = new Tariff(1L, "Tariff 1", true, new ArrayList<>()),
                tariff = new Tariff(2L, "Tariff 2", true, new ArrayList<>())
        ).collect(toList());
        //language=JSON
        jsonTariffs = "[\n" +
                "  {\"id\": 1, \"tariffName\": \"Tariff 1\", \"active\": true, \"options\": []},\n" +
                "  {\"id\": 2, \"tariffName\": \"Tariff 2\", \"active\": true, \"options\": []}\n" +
                "]";
    }

    @Test
    public void getTariffsAndExpectSuccess() throws Exception {
        given(tariffService.getAllTariffs()).willReturn(tariffs);
        mvc.perform(get("/tariffs").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(content().json(jsonTariffs));
    }

    @Test
    public void getTariffAndExpectSuccess() throws Exception {
        given(tariffService.getTariffById(tariff.getId())).willReturn(Optional.of(tariff));
        mvc.perform(get("/tariffs/"+tariff.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(tariff.getId().intValue())))
                .andExpect(jsonPath("$.tariffName", is(tariff.getTariffName())))
                .andExpect(jsonPath("$.active", is(tariff.isActive())));
    }

    @Test
    public void getWrongTariffAndExpectFailure() throws Exception {
        given(tariffService.getTariffById(tariff.getId())).willThrow(TariffNotFoundException.class);
        mvc.perform(get("/tariffs/"+tariff.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateTariffsAndExpectSuccess() throws Exception {

        mvc.perform(put("/tariffs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTariffs))
                .andExpect(status().isCreated());
    }

    @Test
    public void saveTariffAndExpectSuccess() throws Exception {

        mvc.perform(post("/tariffs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTariff))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTariffAndExpectSuccess() throws Exception {
        mvc.perform(delete("/tariffs/"+tariff.getId()))
                .andExpect(status().isNoContent());
    }
}