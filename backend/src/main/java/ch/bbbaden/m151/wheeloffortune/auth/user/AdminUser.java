package ch.bbbaden.m151.wheeloffortune.auth.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "admin_user")
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, name = "password_salt")
    private String passwordSalt;

    public AdminUser(String username, String password, String passwordSalt) {
        this.username = username;
        this.password = password;
        this.passwordSalt = passwordSalt;
    }
}
