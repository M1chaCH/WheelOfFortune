package ch.bbbaden.m151.wheeloffortune.errorhandling.advice;

import ch.bbbaden.m151.wheeloffortune.errorhandling.ErrorMessageDTO;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.AuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AuthAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthAdvice.class);

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthException.class)
    public ErrorMessageDTO tokenNotFound(AuthException exception){
        LOGGER.warn("error caught: {}", exception.getMessage());
        return new ErrorMessageDTO(exception.getMessage());
    }
}
