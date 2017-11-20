package dev.chernykh.cellular.api.notice;


import dev.chernykh.cellular.api.tariff.model.Tariff;

public interface NoticeTextGenerator {

    String generateNotice(Tariff tariffs);
}
