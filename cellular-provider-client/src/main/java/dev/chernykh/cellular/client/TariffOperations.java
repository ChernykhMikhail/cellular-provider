package dev.chernykh.cellular.client;

import java.util.List;

public interface TariffOperations {

//    List<Tariff> getAll();

    void updateAll();

    void updateRandomly();

    List<String> checkUpdateForPeriod(String from, String to);
}
