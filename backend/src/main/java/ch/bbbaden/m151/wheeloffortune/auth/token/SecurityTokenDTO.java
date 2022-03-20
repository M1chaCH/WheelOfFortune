package ch.bbbaden.m151.wheeloffortune.auth.token;

import ch.bbbaden.m151.wheeloffortune.util.LocalDateTimeParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class SecurityTokenDTO {
    public static final String DATE_FORMAT_PATTERN = "yyyy/MM/dd hh:mm:ss";

    private final String token;
    private final String expiresAt;

    public static SecurityTokenDTO fromToken(SecurityToken t){
        return new SecurityTokenDTO(t.getToken(), LocalDateTimeParser.dateToString(t.getExpiresAt()));
    }
}
