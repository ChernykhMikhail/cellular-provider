package dev.chernykh.cellular.shell;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

/**
 * A set of shell commands to manage tariffs.
 */
@ShellComponent
class TariffCommands {

    public static final int MAX_FULL_NAME_LENGTH = 255;

    private Map<String, Map<String, Object>> tariffs = new HashMap<>();

    /**
     * Activate given tariff.
     *
     * @param name tariff name. Must not be empty
     * @return the command output
     */
    @ShellMethod("Activate tariff.")
    String activateTariff(
            @ShellOption(help = "Tariff name") @NotBlank @Length(max = MAX_FULL_NAME_LENGTH) String name) {

        Map<String, Object> current = tariffs.get(name);
        if (current == null) {
            return "Tariff \'" + name + "\' not found.";
        }

        HashMap<String, Object> updated = Maps.newHashMap(current);
        updated.replace("active", true);
        tariffs.replace(name, ImmutableMap.copyOf(updated));

        return "Tariff activated: " + updated;
    }

    /**
     * Create a new tariff that includes 2 basic options (call and SMS).
     *
     * @param name      tariff name. Must not be empty
     * @param callPrice price of calls. Must be {@link BigDecimal} value
     * @param smsPrice  price of SMS. Must be {@link BigDecimal} value
     * @param active    optional flag that indicates activeness of tariff
     * @return the command output
     */
    @ShellMethod("Create new basic tariff.")
    String createTariff(
            @ShellOption(help = "Tariff name") @NotBlank @Length(max = MAX_FULL_NAME_LENGTH) String name,
            @Digits(integer = 5, fraction = 2) BigDecimal callPrice,
            @Digits(integer = 5, fraction = 2) BigDecimal smsPrice,
            boolean active) {

        Map<String, Object> options = ImmutableMap.of(
                "SMS", ImmutableMap.of(
                        "oldPrice", smsPrice,
                        "newPrice", smsPrice,
                        "dateOfChange", LocalDate.now()),
                "Call", ImmutableMap.of(
                        "oldPrice", callPrice,
                        "newPrice", callPrice,
                        "dateOfChange", LocalDate.now())
        );

        ImmutableMap<String, Object> tariff = ImmutableMap.of(
                "name", name,
                "options", options,
                "dateOfChange", LocalDate.now(),
                "active", active);
        tariffs.put(name, tariff);

        return "New tariff created: " + tariff;
    }

    /**
     * Deactivate given tariff.
     *
     * @param name tariff name. Must not be empty
     * @return the command output
     */
    @ShellMethod("Deactivate tariff.")
    String deactivateTariff(
            @ShellOption(help = "Tariff name") @NotBlank @Length(max = MAX_FULL_NAME_LENGTH) String name) {

        Map<String, Object> current = tariffs.get(name);

        if (current == null) {
            return "Tariff \'" + name + "\' not found.";
        }

        HashMap<String, Object> updated = Maps.newHashMap(current);
        updated.replace("active", false);
        tariffs.replace(name, ImmutableMap.copyOf(updated));

        return "Tariff deactivated: " + updated;
    }

    /**
     * Show tariffs list.
     */
    @ShellMethod("Show list of the tariff plans.")
    String listTariffs() {
        String result = tariffs.values()
                .stream()
                .map(Object::toString)
                .collect(joining("\n"));
        return result.isEmpty() ? "Nothing found." : result;
    }

    /**
     * Show tariffs changed within given period.
     *
     * @param from date of beginning period. Must not be empty
     * @param to   date of end period. Must not be empty
     * @return the command output
     */
    @ShellMethod("Show tariffs changed within period.")
    String listTariffsChangedWithinPeriod(
            @ShellOption(help = "Date of beginning period") @DateTimeFormat(iso = DATE) @NotNull @Past LocalDate from,
            @ShellOption(help = "Date of end period") @DateTimeFormat(iso = DATE) @NotNull LocalDate to) {

        if (from.isAfter(to) || to.isBefore(from)) {
            return "Date of end period less than date of beginning";
        }

        String result = tariffs.values()
                .stream()
                .filter(tariff -> {
                    LocalDate dateOfChange = (LocalDate) tariff.get("dateOfChange");
                    return !dateOfChange.isBefore(from) && !dateOfChange.isAfter(to);
                })
                .map(Object::toString)
                .collect(joining("\n"));

        return result.isEmpty() ? "Nothing has been found." : result;
    }

    /**
     * Show a tariff by given name if it exists.
     *
     * @param name tariff name. Must not be empty
     * @return the command output
     */
    @ShellMethod("Show tariff by name.")
    String showTariff(
            @ShellOption(help = "Tariff name") @NotBlank @Length(max = MAX_FULL_NAME_LENGTH) String name) {
        return Optional.ofNullable(tariffs.get(name))
                .map(Object::toString)
                .orElse("Tariff " + name + " has not been found.");
    }

    /**
     * Delete a tariff with given name if it exists.
     *
     * @param name tariff name. Must not be empty
     * @return the command output
     */
    @ShellMethod("Remove tariff by name.")
    String removeTariff(
            @ShellOption(help = "Tariff name") @NotBlank @Length(max = MAX_FULL_NAME_LENGTH) String name) {
        return Optional.ofNullable(tariffs.remove(name))
                .map(tariff -> "Tariff " + tariff + " has been deleted successfully.")
                .orElse("Tariff " + name + " has not been found.");

    }

    /**
     * Update a tariff by given name if it exists.
     *
     * @param name      must not be empty
     * @param callPrice price of call. Must not be empty
     * @param smsPrice  price of SMS. Must not be empty
     * @return the command output
     */
    @ShellMethod("Update tariff plans.")
    String updateTariff(
            @ShellOption("Tariff name") @NotBlank @Length(max = MAX_FULL_NAME_LENGTH) String name,
            @ShellOption("SMS price") @Digits(integer = 5, fraction = 2) BigDecimal callPrice,
            @ShellOption("Call price") @Digits(integer = 5, fraction = 2) BigDecimal smsPrice) {

        Map<String, Object> tariff = tariffs.get(name);

        if (tariff == null) {
            return "Tariff \'" + name + "\' has not been found.";
        }

        HashMap<String, Object> updatedTariff = Maps.newHashMap(tariff);

        updatedTariff.replace("dateOfChange", LocalDate.now());

        Map<String, Object> currentOptions = (Map<String, Object>) updatedTariff.get("options");

        Map<String, Object> sms = (Map<String, Object>) currentOptions.get("SMS");
        Map<String, Object> call = (Map<String, Object>) currentOptions.get("Call");

        HashMap<String, Object> updatedSms = Maps.newHashMap(sms);
        updatedSms.replace("oldPrice", sms.get("newPrice"));
        updatedSms.replace("newPrice", smsPrice);
        updatedSms.replace("dateOfChange", LocalDate.now());

        Map<String, Object> updatedCall = Maps.newHashMap(call);
        updatedCall.replace("oldPrice", call.get("newPrice"));
        updatedCall.replace("newPrice", callPrice);
        updatedCall.replace("dateOfChange", LocalDate.now());

        Map<String, Object> updatedOptions = new HashMap<>();
        updatedOptions.put("Call", ImmutableMap.copyOf(updatedCall));
        updatedOptions.put("SMS", ImmutableMap.copyOf(updatedSms));


        updatedTariff.replace("options", ImmutableMap.copyOf(updatedOptions));

        return "Updated tariff: " + updatedTariff;
    }
}