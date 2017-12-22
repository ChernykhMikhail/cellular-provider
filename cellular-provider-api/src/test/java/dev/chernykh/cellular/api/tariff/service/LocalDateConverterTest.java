package dev.chernykh.cellular.api.tariff.service;

import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class LocalDateConverterTest {

    private LocalDateConverter localDateConverter;

    @Before
    public void setUp() {
        localDateConverter = new LocalDateConverter();
    }

    @Test
    public void shouldConvertToDatabaseColumn() {

        Date expectedDate = localDateConverter.convertToDatabaseColumn(LocalDate.parse("2017-11-12"));
        Date actualDate = Date.valueOf("2017-11-12");

        assertThat(expectedDate, notNullValue());
        assertThat(actualDate, notNullValue());
        assertEquals(actualDate, expectedDate);
    }

    @Test
    public void shouldConvertToEntityAttribute() {

        LocalDate expectedDate = localDateConverter.convertToEntityAttribute(Date.valueOf("2017-11-01"));
        LocalDate actualDate = LocalDate.parse("2017-11-01");

        assertThat(expectedDate, notNullValue());
        assertThat(actualDate, notNullValue());
        assertEquals(actualDate, expectedDate);
    }

}