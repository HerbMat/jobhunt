package dna.jobhunt.service.impl;

import dna.jobhunt.api.dto.UserDTO;
import dna.jobhunt.api.dto.UserDetailsDTO;
import dna.jobhunt.api.mapper.UserMapper;
import dna.jobhunt.domain.User;
import dna.jobhunt.repository.UserRepository;
import dna.jobhunt.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

/**
 * Implementation of {@link UserService}
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDTO createUser(final UserDetailsDTO userDetailsDTO) {
        log.debug("Creating user with data {}.", userDetailsDTO);
        final var user = userMapper.toUser(userDetailsDTO);
        user.setCreationDate(LocalDateTime.now());

        return userMapper.toUserDTO(userRepository.save(user));
    }

    /**
     * Updates only set fields in {@link UserDetailsDTO}.
     * {@inheritDoc}
     */
    @Override
    public Optional<UserDTO> updateUser(final UserDetailsDTO userDetailsDTO, final Integer id) {
        final var user = userRepository.findById(id);
        user.ifPresent(userEntity -> updateUserWithData(userEntity, userDetailsDTO));

        return user.map(userMapper::toUserDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<UserDTO> getUserById(final Integer id) {
        log.debug("Retrieving user with id {}", id);

        return userRepository.findById(id)
                .map(userMapper::toUserDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findOneByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<UserDTO> getUsers() {
        log.debug("Retrieving collection of all existing user");

        return userMapper.toUserDTOList(userRepository.findAll());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUser(final Integer id) {
        log.debug("Deleting user user with id {}", id);
        userRepository.deleteById(id);
        log.debug("Deleted user with id {}", id);
    }

    private void updateUserWithData(User user, UserDetailsDTO userDetailsDTO) {
        log.debug("Updating user {} with data {}", user, userDetailsDTO);
        if (userDetailsDTO == null) {
            return;
        }
        if (null != userDetailsDTO.getPassword()) {
            user.setPassword(userDetailsDTO.getPassword());
        }
        if (null != userDetailsDTO.getUsername()) {
            user.setUsername(userDetailsDTO.getUsername());
        }
        log.debug("Saving user with new data {}", user);

        userRepository.save(user);
    }
}
