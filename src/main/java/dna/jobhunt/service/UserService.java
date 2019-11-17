package dna.jobhunt.service;

import dna.jobhunt.api.dto.UserDTO;
import dna.jobhunt.api.dto.UserDetailsDTO;
import dna.jobhunt.domain.User;

import java.util.Collection;
import java.util.Optional;

/**
 * Service for managing {@link dna.jobhunt.domain.User}
 */
public interface UserService {

    /**
     * Create new user with given details.
     *
     * @param userDetailsDTO new user details.
     *
     * @return new user data.
     */
    UserDTO createUser(final UserDetailsDTO userDetailsDTO);

    /**
     * Update existing user with given data.
     *
     * @param userDetailsDTO new user details.
     * @param id existing user id
     *
     * @return updated existing user fo given id.
     */
    Optional<UserDTO> updateUser(final UserDetailsDTO userDetailsDTO, final Integer id);

    /**
     * Get user data for given id.
     *
     * @param id existing user id
     *
     * @return user data for given id.
     */
    Optional<UserDTO> getUserById(final Integer id);

    /**
     * Get user data with for given username.
     *
     * @param username existing user id
     *
     * @return user data for given username.
     */
    Optional<User> getUserByUsername(final String username);

    /**
     * Get all existing users in the system.
     *
     * @return collection of existing users.
     */
    Collection<UserDTO> getUsers();

    /**
     * Delete user with given id.
     *
     * @param id existing user id.
     */
    void deleteUser(final Integer id);
}
