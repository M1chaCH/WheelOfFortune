package ch.bbbaden.m151.wheeloffortune.auth.api;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginRequestDTO {
    private String username;
    private String password;
}
