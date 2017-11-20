package dev.chernykh.cellular.api.notice;

import dev.chernykh.cellular.api.tariff.model.Tariff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class NoticeService implements NoticeTextGenerator {

    private final TemplateLoader templateLoader;

    @Autowired
    public NoticeService(TemplateLoader templateLoader) {
        this.templateLoader = templateLoader;
    }

    @Override
    public String generateNotice(@NotNull Tariff tariff) {
        return templateLoader.load(tariff);
    }
}
