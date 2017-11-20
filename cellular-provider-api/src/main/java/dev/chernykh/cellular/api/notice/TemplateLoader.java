package dev.chernykh.cellular.api.notice;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import dev.chernykh.cellular.api.tariff.model.Tariff;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;

@Component
public class TemplateLoader {

    private final static String TEMPLATE_FILE_NAME = "template.mustache";

    public String load(Tariff tariff) {
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        Mustache mustache = mustacheFactory.compile(TEMPLATE_FILE_NAME);
        StringWriter stringWriter = new StringWriter();
        String noticeText = mustache.execute(stringWriter, tariff).toString();
        try {
            stringWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return noticeText;
    }
}
