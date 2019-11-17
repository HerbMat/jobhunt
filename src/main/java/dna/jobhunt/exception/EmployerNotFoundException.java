package dna.jobhunt.exception;

import lombok.Getter;

/**
 * Exception for managing errors with missing user with given username in application
 */
public class EmployerNotFoundException extends RuntimeException {

    /**
     * missing user username.
     */
    @Getter
    private final String username;

    public EmployerNotFoundException(String message, String username) {
        super(message);
        this.username = username;
    }

    public EmployerNotFoundException(String username) {
        this(String.format("User with id %s does not exists", username), username);
    }


}
