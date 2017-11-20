package dev.chernykh.cellular.shell;

import dev.chernykh.cellular.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class UserCommand {

    private final UserClient userClient;

    @Autowired
    public UserCommand(UserClient userClient) {
        this.userClient = userClient;
    }

    @ShellMethod("Display users list.")
    public void users() {
        userClient.getUsers().forEach(System.out::println);
    }
}
