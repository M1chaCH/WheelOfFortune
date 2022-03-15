package ch.bbbaden.m151.wheeloffortune.errorhandling.advice;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.InvalidatedSecurityTokenException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.SecurityTokenNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SecurityTokenAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(SecurityTokenNotFoundException.class)
    public String tokenNotFound(SecurityTokenNotFoundException exception){
        return exception.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidatedSecurityTokenException.class)
    public String invalidatedToken(InvalidatedSecurityTokenException exception){
        return exception.getMessage();
    }
}