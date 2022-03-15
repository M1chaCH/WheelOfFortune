package ch.bbbaden.m151.wheeloffortune.errorhandling.advice;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.AccountNotFoundException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.BadCredentialsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CredentialsAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public String badCredentials(BadCredentialsException exception){
        return exception.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccountNotFoundException.class)
    public String accountNotFound(AccountNotFoundException exception){
        return exception.getMessage();
    }
}
