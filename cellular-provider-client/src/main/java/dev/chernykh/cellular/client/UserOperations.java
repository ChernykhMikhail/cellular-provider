package dev.chernykh.cellular.client;

import java.util.List;

/**
 * Interface provides operations to manage users.
 */
public interface UserOperations {

    /**
     * Add a new user.
     *
     * @param name     the full user name
     * @param tariffId the user's tariff id
     * @return the added user
     */
    UserDto add(String name, long tariffId);

    /**
     * Get a list of existing users. Optionally takes two params for filtering aim.
     *
     * @param name     a partial or full user name
     * @param tariffId a user's tariff id
     * @return the list of found users
     */
    List<UserDto> getAll(String name, long tariffId);

    /**
     * Delete a user by id.
     *
     * @param id the user id
     */
    void delete(long id);

    /**
     * Update a user with a specified id.
     *
     * @param id       the user id
     * @param name     the user full name
     * @param tariffId the user tariff id
     * @return the updated user
     */
    UserDto update(long id, String name, long tariffId);
}
