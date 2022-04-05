package ch.bbbaden.m151.wheeloffortune.game.highscore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class HighScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private int playedRounds;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private LocalDateTime achievedAt;

    public HighScore(int score, int playedRounds, String username, LocalDateTime achievedAt) {
        this.score = score;
        this.playedRounds = playedRounds;
        this.username = username;
        this.achievedAt = achievedAt;
    }
}
