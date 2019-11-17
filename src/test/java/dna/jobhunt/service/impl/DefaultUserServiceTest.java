package dna.jobhunt.service.impl;

import dna.jobhunt.api.dto.UserDTO;
import dna.jobhunt.api.dto.UserDetailsDTO;
import dna.jobhunt.api.mapper.UserMapper;
import dna.jobhunt.domain.User;
import dna.jobhunt.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultUserServiceTest {

    private static final String TEST_USERNAME = "test";
    private static final String TEST_PASSWORD = "pass";
    private static final int TEST_USER_ID = 1;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private DefaultUserService userService;

    @DisplayName("It should create new user successfully.")
    @Test
    public void createUser() {
        final var userDetails = createMockUserDetailsDTo();
        final var mappedUser = new User();
        final var createdUserDTO = new UserDTO();
        when(userMapper.toUser(userDetails)).thenReturn(mappedUser);
        when(userRepository.save(mappedUser)).thenReturn(mappedUser);
        when(userMapper.toUserDTO(mappedUser)).thenReturn(createdUserDTO);

        final var result = userService.createUser(userDetails);

        assertThat(result, equalTo(createdUserDTO));
        assertThat(mappedUser.getCreationDate(), notNullValue());
        verify(userRepository, times(1)).save(mappedUser);
        verify(userMapper, times(1)).toUser(userDetails);
        verify(userMapper, times(1)).toUserDTO(mappedUser);
    }

    @DisplayName("It should update only username.")
    @Test
    public void updateUserWithUsername() {
        final var userDetails = UserDetailsDTO.builder()
                .username(TEST_USERNAME)
                .build();
        final User capturedUser = performBasicUpdateUserTest(userDetails);
        assertThat(capturedUser.getPassword(), nullValue());
        assertThat(capturedUser.getUsername(), equalTo(TEST_USERNAME));
    }


    @DisplayName("It should update only password.")
    @Test
    public void updateUserWithPassword() {
        final var userDetails = UserDetailsDTO.builder()
                .password(TEST_PASSWORD)
                .build();
        final User capturedUser = performBasicUpdateUserTest(userDetails);
        assertThat(capturedUser.getUsername(), nullValue());
        assertThat(capturedUser.getPassword(), equalTo(TEST_PASSWORD));
    }

    @DisplayName("It should update username and password.")
    @Test
    public void updateUserWithUsernameAndPassword() {
        final var userDetails = UserDetailsDTO.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build();
        final User capturedUser = performBasicUpdateUserTest(userDetails);
        assertThat(capturedUser.getPassword(), equalTo(TEST_PASSWORD));
        assertThat(capturedUser.getUsername(), equalTo(TEST_USERNAME));
    }

    @DisplayName("It should not update not existing user.")
    @Test
    public void updateUserFail() {
        final var userDetails = UserDetailsDTO.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build();
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.empty());

        final var result = userService.updateUser(userDetails, TEST_USER_ID);

        assertThat(result, equalTo(Optional.empty()));
        verify(userMapper, never()).toUser(userDetails);
        verify(userMapper, never()).toUserDTO(any(User.class));
        verify(userRepository, never()).save(any(User.class));
    }

    private User performBasicUpdateUserTest(UserDetailsDTO userDetails) {
        final var userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        final var mappedUser = new User();
        final var createdUserDTO = new UserDTO();
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(mappedUser));
        when(userRepository.save(mappedUser)).thenReturn(mappedUser);
        when(userMapper.toUserDTO(mappedUser)).thenReturn(createdUserDTO);

        final var result = userService.updateUser(userDetails, TEST_USER_ID);

        assertThat(result.get(), equalTo(createdUserDTO));
        verify(userMapper, times(1)).toUserDTO(mappedUser);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        return userArgumentCaptor.getValue();
    }

    private UserDetailsDTO createMockUserDetailsDTo() {
        return UserDetailsDTO.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build();
    }
}
