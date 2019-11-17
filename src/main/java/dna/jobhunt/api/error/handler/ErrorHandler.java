package dna.jobhunt.api.error.handler;

import dna.jobhunt.exception.EmployerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * It handles all validation exceptions.
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    private static final String EMPLOYER_NOT_FOUND_FOR_USERNAME_MSG = "Employer username must belong to existing user.";

    /**
     * It handles all errors with bad user input besides request body elements.
     *
     * @param ex handled exception
     * @return api response with status code and collection of error messages
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public List<String> handleConstraintViolationException(final ConstraintViolationException ex) {
        log.info("Handling input validation exceptions: {}", ex.getMessage());
        return ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    /**
     * It handles all errors with bad user input in request body.
     *
     * @param ex handled exception
     * @return api response with status code and collection of error messages
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        log.error("Handling input validation exceptions: {}", ex.getMessage());
        return ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
    }

    /**
     * It handles error with missing employer for given username.
     *
     * @param ex handled exception
     * @return api response with status code and collection with single error message.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmployerNotFoundException.class)
    public List<String> handleEmployerNotFoundException(final EmployerNotFoundException ex) {
        log.error("Handling user not found exception: {}", ex.getMessage());
        return List.of(EMPLOYER_NOT_FOUND_FOR_USERNAME_MSG);
    }
}
