package dev.chernykh.cellular.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TariffClient implements TariffOperations {

    private final RestTemplate restTemplate;

    public TariffClient(RestTemplateBuilder builder) {
        this.restTemplate = builder
                .rootUri("http://localhost:8090")
                .build();
    }

    @Override
    public boolean activate(String name) {
        return false;
    }

    @Override
    public String create(String name, BigDecimal callPrice, BigDecimal smsPrice) {
        return null;
    }

    @Override
    public boolean deactivate(String name) {
        return false;
    }

    @Override
    public List<String> getAll() {
        return null;
    }

    @Override
    public List<String> getAllChangedWithinPeriod(LocalDate from, LocalDate to) {
        return null;
    }

    @Override
    public String getByName(String name) {
        return null;
    }

    @Override
    public void remove(String name) {
        restTemplate.delete("/tariffs");
    }

    @Override
    public String update(String name, BigDecimal callPrice, BigDecimal smsPrice) {
        return null;
    }
}
