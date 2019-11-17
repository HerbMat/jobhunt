package dna.jobhunt.api.controller;

import dna.jobhunt.api.dto.UserDTO;
import dna.jobhunt.api.dto.UserDetailsDTO;
import dna.jobhunt.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Api(value="User Controller", tags = "user")
@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @ApiOperation(value = "Retrieve all existing users from database", response = Collection.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieved users successfully."),
            @ApiResponse(code = 500, message = "Internal service error.")
    })
    @GetMapping
    public Collection<UserDTO> getAllUsers() {
        log.debug("Resolving get all users call");

        return userService.getUsers();
    }

    @ApiOperation(value = "Retrieve user for given id", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retrieved user successfully."),
            @ApiResponse(code = 400, message = "Validation error."),
            @ApiResponse(code = 404, message = "User not exist"),
            @ApiResponse(code = 500, message = "Internal service error.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(
            @ApiParam(value = "Id of user", example = "1")
            @PathVariable("id")
                    Integer id) {
        log.debug("Received request to retrieve user with id {}", id);

        return ResponseEntity.of(userService.getUserById(id));
    }

    @ApiOperation(value = "Retrieve user for given id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Deleted user successfully."),
            @ApiResponse(code = 400, message = "Validation error."),
            @ApiResponse(code = 404, message = "User not exist"),
            @ApiResponse(code = 500, message = "Internal service error.")
    })
    @DeleteMapping("/{id}")
    public void deleteUser(
            @ApiParam(value = "Id of user", example = "1")
            @PathVariable("id")
                    Integer id) {
        log.debug("Received request to delete user with id {}", id);

        userService.deleteUser(id);
    }

    @ApiOperation(value = "Update user with given id", response = UserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated user successfully."),
            @ApiResponse(code = 400, message = "Validation error."),
            @ApiResponse(code = 404, message = "User not exist"),
            @ApiResponse(code = 500, message = "Internal service error.")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @ApiParam(value = "Id of user", example = "1")
            @PathVariable("id")
                    Integer id,
            @ApiParam(value = "User details to update")
            @RequestBody
                    UserDetailsDTO userDetailsDTO
            ) {
        log.debug("Received request to update user with id {} with data {}", id, userDetailsDTO);

        return ResponseEntity.of(userService.updateUser(userDetailsDTO, id));
    }

    @ApiOperation(value = "Create user with given data", response = UserDTO.class)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created successfully."),
            @ApiResponse(code = 400, message = "Validation error."),
            @ApiResponse(code = 500, message = "Internal service error.")
    })
    @PostMapping
    public UserDTO createUser(
            @ApiParam(value = "New User details")
            @RequestBody
            @Valid
                    UserDetailsDTO userDetailsDTO
    ) {
        log.debug("Received request to create new user with data {}", userDetailsDTO);

        return userService.createUser(userDetailsDTO);
    }
}
