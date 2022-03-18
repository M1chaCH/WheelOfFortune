package ch.bbbaden.m151.wheeloffortune.game.highscore;

import ch.bbbaden.m151.wheeloffortune.game.candidate.Candidate;
import ch.bbbaden.m151.wheeloffortune.game.data.WebEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class HighScore implements WebEntity<Integer, HighScoreDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private int score;
    @Column(nullable = false)
    private LocalDateTime achievedAt;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "candidate", nullable = false)
    private Candidate candidate;

    public HighScore(int score, LocalDateTime achievedAt, Candidate candidate) {
        this.score = score;
        this.achievedAt = achievedAt;
        this.candidate = candidate;
    }

    @Override
    public HighScoreDTO parseToDTO() {
        return new HighScoreDTO(id, score, achievedAt, candidate.parseToDTO());
    }
}
