package dev.chernykh.cellular.shell;

import dev.chernykh.cellular.client.TariffClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class TariffCommand {

    private final TariffClient tariffClient;

    @Autowired
    public TariffCommand(TariffClient tariffClient) {
        this.tariffClient = tariffClient;
    }

    @ShellMethod("Display list of the tariff plans.")
    public void tariffs() {
        tariffClient.getAll().forEach(System.out::println);
    }

    @ShellMethod("Update the tariffs plans.")
    public void update(@ShellOption(help = "If key specified, update tariffs randomly, otherwise all") boolean r) {
        if (r) {
            tariffClient.updateRandomly();
        } else {
            tariffClient.updateAll();
        }
    }

    @ShellMethod("Check for tariff updates for given period")
    public void check(@ShellOption String from, @ShellOption String to) {
        tariffClient.checkUpdateForPeriod(from, to)
                .forEach(System.out::println);
    }
}
