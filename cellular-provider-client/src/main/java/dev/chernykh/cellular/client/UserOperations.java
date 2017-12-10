package dev.chernykh.cellular.client;

import java.util.List;

/**
 * Provides the operations to manage users.
 */
public interface UserOperations {

    /**
     * Adds a new user.
     *
     * @param name     the user name
     * @param tariffId the tariff id
     * @return the added user
     */
    UserDto add(String name, long tariffId);

    /**
     * Gets the list of existing users. Optionally takes two params for filtering aim.
     *
     * @param name     the partial or full user name
     * @param tariffId the user's tariff id
     * @return the list of found users
     */
    List<UserDto> getAll(String name, long tariffId);

    /**
     * Deletes user by id.
     *
     * @param id user's id
     */
    void delete(long id);

    /**
     * Updates the user with specified id.
     *
     * @param id       the user's id
     * @param name     the user's full name
     * @param tariffId user's tariff id
     * @return the updated user
     */
    UserDto update(long id, String name, long tariffId);
}
