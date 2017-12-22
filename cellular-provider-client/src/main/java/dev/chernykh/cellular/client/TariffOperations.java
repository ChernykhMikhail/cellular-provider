package dev.chernykh.cellular.client;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Provides operations to manage tariffs.
 */
public interface TariffOperations {

    /**
     * Activate a tariff with given name.
     *
     * @param name the tariff name. Must not be empty
     * @return {@code true} if tariff was activated successfully; {@code false} otherwise
     */
    boolean activate(String name);

    /**
     * Create a new tariff with basic options.
     *
     * @param name      the tariff name
     * @param callPrice a call voice price
     * @param smsPrice  an sms price
     * @return the created tariff
     */
    String create(String name, BigDecimal callPrice, BigDecimal smsPrice);

    /**
     * Deactivate a tariff with given name.
     *
     * @param name the tariff name
     * @return {@code true} if tariff was deactivated successfully; {@code false} otherwise
     */
    boolean deactivate(String name);

    /**
     * Get all existing tariffs.
     *
     * @return the list of tariffs
     */
    List<String> getAll();

    /**
     * Get all tariffs which were changed within given period.
     *
     * @param from a date of beginning period
     * @param to   a date of end period
     * @return list of changed tariffs
     */
    List<String> getAllChangedWithinPeriod(LocalDate from, LocalDate to);

    /**
     * Get a tariff with the specified name.
     *
     * @param name the tariff name. Must not be empty
     * @return the tariff
     */
    String getByName(String name);

    /**
     * Remove a tariff with specified name.
     *
     * @param name the tariff name. Must not be empty
     */
    void remove(String name);

    /**
     * Update a tariff with specified name.
     *
     * @param name      the tariff name. Must not be empty
     * @param callPrice a new call price
     * @param smsPrice  a new sms price
     * @return the updated tariff
     */
    String update(String name, BigDecimal callPrice, BigDecimal smsPrice);


}