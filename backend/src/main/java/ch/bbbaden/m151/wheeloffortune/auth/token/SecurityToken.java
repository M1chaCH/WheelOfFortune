package ch.bbbaden.m151.wheeloffortune.auth.token;

import ch.bbbaden.m151.wheeloffortune.auth.user.AdminUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "security_token")
public class SecurityToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AdminUser admin;

    public SecurityToken(String token, LocalDateTime createdAt, AdminUser admin) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = createdAt.plusMinutes(SecurityTokenService.TOKEN_EXPIRE_TIME_MIN);
        this.admin = admin;
    }
}
