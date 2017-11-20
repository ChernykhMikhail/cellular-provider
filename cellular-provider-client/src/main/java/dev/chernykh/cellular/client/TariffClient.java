package dev.chernykh.cellular.client;

import dev.chernykh.cellular.api.tariff.model.Option;
import dev.chernykh.cellular.api.tariff.model.Tariff;
import dev.chernykh.cellular.api.user.User;
import lombok.NonNull;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Component
public class TariffClient implements TariffOperations {

    private final RestTemplate restTemplate;

    private String rootUrl = "http://localhost:8090";

    @Autowired
    public TariffClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder
                .rootUri(rootUrl)
                .build();
    }

    @Override
    public List<Tariff> getAll() {
        ParameterizedTypeReference<List<Tariff>> tariffListRef =
                new ParameterizedTypeReference<List<Tariff>>() {
                };
        ResponseEntity<List<Tariff>> exchange =
                restTemplate.exchange("/tariffs", HttpMethod.GET, null, tariffListRef);
        return exchange.getBody();

    }

    @Override
    public void updateAll() {

        List<Tariff> tariffs = getAll();

        tariffs.stream()
                .map(Tariff::getOptions)
                .flatMap(List::stream)
                .forEach(this::updatePrice);

        ParameterizedTypeReference<List<Tariff>> tariffListRef =
                new ParameterizedTypeReference<List<Tariff>>() {
                };
        ResponseEntity<List<Tariff>> exchange =
                restTemplate.exchange("/tariffs", HttpMethod.POST, new HttpEntity(tariffs), tariffListRef);
    }

    @Override
    public void updateRandomly() {

        List<Tariff> tariffs = getAll();

        List<Option> options = tariffs
                .stream()
                .map(Tariff::getOptions)
                .flatMap(List::stream)
                .collect(toList());

        Random random = new Random();
        int streamSize = random.nextInt(options.size());

        random.ints(streamSize, 0, options.size())
                .distinct()
                .sorted()
                .forEach(index -> updatePrice(options.get(index)));

        ParameterizedTypeReference<List<Tariff>> tariffListRef =
                new ParameterizedTypeReference<List<Tariff>>() {
                };
        ResponseEntity<List<Tariff>> exchange =
                restTemplate.exchange("/tariffs", HttpMethod.POST, new HttpEntity(tariffs), tariffListRef);

    }

    @Override
    public List<String> checkUpdateForPeriod(@NonNull String from, @NonNull String to) {

        Map<String, String> uriVars = new HashMap<>();
        uriVars.put("from", from);
        uriVars.put("to", to);

        ParameterizedTypeReference<List<Tariff>> tariffsRef = new ParameterizedTypeReference<List<Tariff>>() {
        };
        ResponseEntity<List<Tariff>> changedTariffs =
                restTemplate.exchange("/tariffs/from/{from}/to/{to}", HttpMethod.GET, null, tariffsRef, uriVars);

        List<String> notices = new ArrayList<>();

        changedTariffs.getBody().forEach(tariff -> {

            ParameterizedTypeReference<List<User>> usersRef = new ParameterizedTypeReference<List<User>>() {
            };
            ResponseEntity<List<User>> response =
                    restTemplate.exchange("/users/by-tariff/" + tariff.getId(), HttpMethod.GET, null, usersRef);

            List<User> users = response.getBody();
            if (users.size() > 0) {
                ParameterizedTypeReference<String> noticeRef = new ParameterizedTypeReference<String>() {
                };
                ResponseEntity<String> notice = restTemplate.exchange("/notice", HttpMethod.POST, new HttpEntity<>(tariff), noticeRef);
                notices.add(notice.getBody());
            }
        });

        return notices;
    }

    private Option updatePrice(Option option) {
        Random random = new Random();
        option.setOldPrice(option.getNewPrice());

        double newAmount = (10 + random.nextInt(1990)) / 100.0;

        option.setNewPrice(Money.of(CurrencyUnit.ofNumericCode(643), BigDecimal.valueOf(newAmount)));
        option.setDateOfChange(LocalDate.now());
        return option;
    }
}
