package dev.chernykh.cellular.shell;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.joining;

/**
 * A set of shell commands to manage users.
 */
@ShellComponent
public class UserCommands {

    private static final int MAX_FULL_NAME_LENGTH = 255;

    private Map<Long, Map<String, Object>> users = new HashMap<>();

    private AtomicLong idGenerator = new AtomicLong();

    /**
     * Add a new user.
     *
     * @param fullName the full name.
     * @param tariffId the id of a tariff used by the user.
     * @return the command output.
     */
    @ShellMethod("Add new user.")
    String addUser(
            @ShellOption(help = "The full name.") @NotBlank @Length(max = MAX_FULL_NAME_LENGTH) String fullName,
            @ShellOption(help = "The id of a tariff used by the user.") long tariffId) {

        long id = idGenerator.incrementAndGet();

        ImmutableMap<String, Object> user = ImmutableMap.of("id", id, "fullName", fullName, "tariffId", tariffId);
        users.put(id, user);

        return "New user added: " + user;
    }

    /**
     * List users with optional filtering.
     *
     * @param filter the SpEL expression to which the users should conform
     * @return the command output. One user per line.
     */
    @ShellMethod("List users with optional filtering.")
    String listUsers(@ShellOption(defaultValue = "") String filter) {

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(filter);
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.addPropertyAccessor(new MapAccessor());
        boolean isApplyFilter = !filter.isEmpty();

        String result = users.values()
                .stream()
                .filter(user -> isApplyFilter ? expression.getValue(context, user, Boolean.class) : true)
                .map(Object::toString)
                .collect(joining("\n"));
        return result.isEmpty() ? "No users." : result;
    }

    /**
     * List users by given tariff id.
     *
     * @param tariff tariff id. Must not be empty
     * @return the command output
     */
    @ShellMethod("Show users by tariff id.")
    String listUsersByTariff(@ShellOption(help = "User's tariff id") long tariff) {
        String result = users.values()
                .stream()
                .filter(user -> user.get("tariffId").equals(tariff))
                .map(Object::toString)
                .collect(joining("\n"));
        return result.isEmpty() ? "No one is found." : result;
    }

    /**
     * Deletes user with given its id if one exists.
     *
     * @param id user id. Must not be empty
     * @return the command output
     */
    @ShellMethod("Remove user by specified id.")
    String removeUser(@ShellOption(help = "User id") long id) {
        return Optional.ofNullable(users.remove(id))
                .map(user -> "Removed ser: " + user)
                .orElse("No user with id: " + id);
    }

    /**
     * Updates details of given user if one exists.
     *
     * @param id       user id. Must not be empty
     * @param name     new user name. Must no be empty
     * @param tariffId new user's tariff. Must not be empty
     * @return the command output
     */
    @ShellMethod("Make change to user details.")
    String updateUser(
            @ShellOption(help = "User id") long id,
            @ShellOption(help = "User full name") @NotBlank @Length(max = MAX_FULL_NAME_LENGTH) String name,
            @ShellOption(help = "User's tariff id") long tariffId) {

        Map<String, Object> user = users.get(id);
        if (user == null) {
            return "No user with id: " + id;
        }

        HashMap<String, Object> updatedUser = Maps.newHashMap(user);
        updatedUser.replace("name", name);
        updatedUser.replace("tariffId", tariffId);

        users.replace(id, ImmutableMap.copyOf(updatedUser));

        return "Updated user: " + updatedUser;
    }
}
