package dev.chernykh.api.notice;


import dev.chernykh.api.tariff.model.Tariff;

public interface NoticeTextGenerator {

    String generateNotice(Tariff tariffs);
}
