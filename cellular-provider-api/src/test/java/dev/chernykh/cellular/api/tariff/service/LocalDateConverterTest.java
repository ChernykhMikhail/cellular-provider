package dev.chernykh.cellular.api.tariff.service;

import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class LocalDateConverterTest {

    private LocalDateConverter localDateConverter;

    @Before
    public void setUp() throws Exception {
        localDateConverter = new LocalDateConverter();
    }

    @Test
    public void convertToDatabaseColumn() throws Exception {

        Date expectedDate = localDateConverter.convertToDatabaseColumn(LocalDate.parse("2017-11-12"));
        Date actualDate = Date.valueOf("2017-11-12");

        assertEquals(actualDate, expectedDate);
    }

    @Test
    public void convertToEntityAttribute() throws Exception {

        LocalDate expectedDate = localDateConverter.convertToEntityAttribute(Date.valueOf("2017-11-01"));
        LocalDate currentDate = LocalDate.parse("2017-11-01");

        assertEquals(currentDate, expectedDate);
    }

}