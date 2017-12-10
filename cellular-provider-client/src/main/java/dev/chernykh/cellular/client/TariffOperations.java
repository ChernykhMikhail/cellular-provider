package dev.chernykh.cellular.client;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Provides operations to manage tariffs.
 */
public interface TariffOperations {

    /**
     * Activates tariff with given name.
     *
     * @param name the tariff name. Must not be empty
     * @return {@code true} if tariff was activated successfully; {@code false} otherwise
     */
    boolean activate(String name);

    /**
     * Creates a new tariff with basic options.
     *
     * @param name      the tariff name
     * @param callPrice the call voice price
     * @param smsPrice  the sms price
     * @return the created tariff
     */
    String create(String name, BigDecimal callPrice, BigDecimal smsPrice);

    /**
     * Deactivates tariff with given name.
     *
     * @param name the tariff name
     * @return {@code true} if tariff was deactivated successfully; {@code false} otherwise
     */
    boolean deactivate(String name);

    /**
     * Gets all existing tariffs.
     *
     * @return the list of tariffs
     */
    List<String> getAll();

    /**
     * Gets all tariffs which was changed within given period.
     *
     * @param from the date of beginning period
     * @param to   the date of end period
     * @return list of changed tariffs
     */
    List<String> getAllChangedWithinPeriod(LocalDate from, LocalDate to);

    /**
     * Gets tariff with the specified name.
     *
     * @param name the tariff name. Must not be empty
     * @return the tariff
     */
    String getByName(String name);

    /**
     * Removes tariff with the specified name.
     *
     * @param name the tariff name. Must not be empty
     */
    void remove(String name);

    /**
     * Updates tariff with specified name.
     *
     * @param name      the tariff name. Must not be empty
     * @param callPrice the new call price
     * @param smsPrice  the new sms price
     * @return the updated tariff
     */
    String update(String name, BigDecimal callPrice, BigDecimal smsPrice);


}