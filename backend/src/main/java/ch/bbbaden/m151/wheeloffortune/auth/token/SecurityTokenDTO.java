package ch.bbbaden.m151.wheeloffortune.auth.token;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@ToString
public class SecurityTokenDTO {
    public static final String DATE_FORMAT_PATTERN = "yyyy/MM/dd hh:mm:ss";

    private final String token;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = DATE_FORMAT_PATTERN)
    private final LocalDateTime expiresAt;

    public static SecurityTokenDTO fromToken(SecurityToken t){
        return new SecurityTokenDTO(t.getToken(), t.getExpiresAt());
    }
}
