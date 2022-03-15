package ch.bbbaden.m151.wheeloffortune.errorhandling.advice;

import ch.bbbaden.m151.wheeloffortune.errorhandling.ErrorMessageDTO;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.BasicWheelOfFortuneException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BasicWheelOfFortuneAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicWheelOfFortuneAdvice.class);

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BasicWheelOfFortuneException.class)
    public ErrorMessageDTO alreadyExists(BasicWheelOfFortuneException e){
        LOGGER.warn("error caught: {}", e.getMessage());
        return new ErrorMessageDTO(e.getMessage());
    }
}
