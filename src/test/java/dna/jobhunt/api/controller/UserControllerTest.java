package dna.jobhunt.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dna.jobhunt.api.dto.UserDTO;
import dna.jobhunt.api.dto.UserDetailsDTO;
import dna.jobhunt.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    private final static String USERS_BASE_PATH = "/api/users";
    private final static String USER_SPECIFIC_PATH = USERS_BASE_PATH + "/{id}";

    private final static LocalDateTime CREATION_DATE_1 = LocalDateTime.of(2019, 11, 10,0,0);
    private final static int USER_ID_1 = 1;
    private final static String USERNAME_1 = "test";
    private final static String PASSWORD_1 = "pass";
    private final static LocalDateTime CREATION_DATE_2 = LocalDateTime.of(2019, 11, 14,0,0);
    private final static int USER_ID_2 = 2;
    private final static String USERNAME_2 = "user";
    private final static String PASSWORD_2 = "pass1";
    private final static String USERNAME_BLANK_ERROR_MSG = "Username cannot be blank.";
    private final static String PASSWORD_BLANK_ERROR_MSG = "Password cannot be blank.";

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @DisplayName("It should return 200 and user list.")
    @Test
    public void getAllUsers() throws Exception {
        final var users = createMockUserList();
        when(userService.getUsers()).thenReturn(users);

        mockMvc.perform(get(USERS_BASE_PATH).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", equalTo(USER_ID_1)))
                .andExpect(jsonPath("$.[0].username", equalTo(USERNAME_1)))
                .andExpect(jsonPath("$.[0].password", equalTo(PASSWORD_1)))
                .andExpect(jsonPath("$.[0].creationDate", equalTo(dateTimeFormatter.format(CREATION_DATE_1))))
                .andExpect(jsonPath("$.[1].id", equalTo(USER_ID_2)))
                .andExpect(jsonPath("$.[1].username", equalTo(USERNAME_2)))
                .andExpect(jsonPath("$.[1].password", equalTo(PASSWORD_2)))
                .andExpect(jsonPath("$.[1].creationDate", equalTo(dateTimeFormatter.format(CREATION_DATE_2))));
    }

    @DisplayName("It should return 200 and existing user.")
    @Test
    public void getUserExisting() throws Exception {
        final var user = createMockUser();
        when(userService.getUserById(USER_ID_1)).thenReturn(Optional.of(user));

        mockMvc.perform(get(USER_SPECIFIC_PATH, USER_ID_1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(USER_ID_1)))
                .andExpect(jsonPath("$.username", equalTo(USERNAME_1)))
                .andExpect(jsonPath("$.password", equalTo(PASSWORD_1)))
                .andExpect(jsonPath("$.creationDate", equalTo(dateTimeFormatter.format(CREATION_DATE_1))));
    }

    @DisplayName("It should return 404 for not existing user.")
    @Test
    public void getUserNotExisting() throws Exception {
        when(userService.getUserById(USER_ID_1)).thenReturn(Optional.empty());

        mockMvc.perform(get(USER_SPECIFIC_PATH, USER_ID_1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @DisplayName("It should return 204 for successful deleting existing user.")
    @Test
    public void deleteUser() throws Exception {
        mockMvc.perform(delete(USER_SPECIFIC_PATH, USER_ID_1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(USER_ID_1);
    }

    @DisplayName("It should return 200 and updated user.")
    @Test
    public void updateUser() throws Exception {
        final var userDetailsDTOArgumentCaptor = ArgumentCaptor.forClass(UserDetailsDTO.class);
        final var user = createMockUser();
        when(userService.updateUser(any(UserDetailsDTO.class), eq(USER_ID_1))).thenReturn(Optional.of(user));

        mockMvc.perform(patch(USER_SPECIFIC_PATH, USER_ID_1)
                .content(objectMapper.writeValueAsString(createMockUserDetails()))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(USER_ID_1)))
                .andExpect(jsonPath("$.username", equalTo(USERNAME_1)))
                .andExpect(jsonPath("$.password", equalTo(PASSWORD_1)))
                .andExpect(jsonPath("$.creationDate", equalTo(dateTimeFormatter.format(CREATION_DATE_1))));

        verify(userService, times(1)).updateUser(userDetailsDTOArgumentCaptor.capture(), eq(USER_ID_1));
        UserDetailsDTO userDetailsDTO = userDetailsDTOArgumentCaptor.getValue();
        assertThat(userDetailsDTO.getUsername(), equalTo(USERNAME_1));
        assertThat(userDetailsDTO.getPassword(), equalTo(PASSWORD_1));
    }

    @DisplayName("It should return 404 and do not update user.")
    @Test
    public void updateUserNotFound() throws Exception {
        final var userDetailsDTOArgumentCaptor = ArgumentCaptor.forClass(UserDetailsDTO.class);
        final var user = createMockUser();
        when(userService.updateUser(any(UserDetailsDTO.class), eq(USER_ID_1))).thenReturn(Optional.empty());

        mockMvc.perform(patch(USER_SPECIFIC_PATH, USER_ID_1)
                .content(objectMapper.writeValueAsString(createMockUserDetails()))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound());

        verify(userService, times(1)).updateUser(userDetailsDTOArgumentCaptor.capture(), eq(USER_ID_1));
        UserDetailsDTO userDetailsDTO = userDetailsDTOArgumentCaptor.getValue();
        assertThat(userDetailsDTO.getUsername(), equalTo(USERNAME_1));
        assertThat(userDetailsDTO.getPassword(), equalTo(PASSWORD_1));
    }

    @DisplayName("It should return 201 and newly created user.")
    @Test
    public void createUser() throws Exception {
        final var userDetailsDTOArgumentCaptor = ArgumentCaptor.forClass(UserDetailsDTO.class);
        final var user = createMockUser();
        when(userService.createUser(any(UserDetailsDTO.class))).thenReturn(user);

        mockMvc.perform(post(USERS_BASE_PATH)
                .content(objectMapper.writeValueAsString(createMockUserDetails()))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", equalTo(USER_ID_1)))
                .andExpect(jsonPath("$.username", equalTo(USERNAME_1)))
                .andExpect(jsonPath("$.password", equalTo(PASSWORD_1)))
                .andExpect(jsonPath("$.creationDate", equalTo(dateTimeFormatter.format(CREATION_DATE_1))));

        verify(userService, times(1)).createUser(userDetailsDTOArgumentCaptor.capture());
        UserDetailsDTO userDetailsDTO = userDetailsDTOArgumentCaptor.getValue();
        assertThat(userDetailsDTO.getUsername(), equalTo(USERNAME_1));
        assertThat(userDetailsDTO.getPassword(), equalTo(PASSWORD_1));
    }

    @DisplayName("It should return 400 and error list.")
    @Test
    public void createUserValidationError() throws Exception {
        mockMvc.perform(post(USERS_BASE_PATH)
                .content(objectMapper.writeValueAsString(new UserDetailsDTO()))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$", hasItem(PASSWORD_BLANK_ERROR_MSG)))
                .andExpect(jsonPath("$", hasItem(USERNAME_BLANK_ERROR_MSG)));

        verify(userService, never()).createUser(any());
    }

    private List<UserDTO> createMockUserList() {
        final var user1 = createMockUser();
        final var user2 = UserDTO.userDTOBuilder()
                .creationDate(CREATION_DATE_2)
                .id(USER_ID_2)
                .username(USERNAME_2)
                .password(PASSWORD_2)
                .build();
        return List.of(user1, user2);
    }

    private UserDTO createMockUser() {
        return UserDTO.userDTOBuilder()
                .creationDate(CREATION_DATE_1)
                .id(USER_ID_1)
                .username(USERNAME_1)
                .password(PASSWORD_1)
                .build();
    }

    private UserDetailsDTO createMockUserDetails() {
        return UserDetailsDTO.builder()
                .username(USERNAME_1)
                .password(PASSWORD_1)
                .build();
    }
}