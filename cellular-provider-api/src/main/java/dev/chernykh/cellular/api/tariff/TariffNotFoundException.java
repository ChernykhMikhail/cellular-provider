package dev.chernykh.cellular.api.tariff;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TariffNotFoundException extends RuntimeException {

    public TariffNotFoundException(Long id) {
        super("could not find tariff '" + id + "'.");
    }
}
