package ch.bbbaden.m151.wheeloffortune.dto;

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
