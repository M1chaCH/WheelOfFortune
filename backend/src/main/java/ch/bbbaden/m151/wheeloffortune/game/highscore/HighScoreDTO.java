package ch.bbbaden.m151.wheeloffortune.game.highscore;

import ch.bbbaden.m151.wheeloffortune.game.candidate.CandidateDTO;
import ch.bbbaden.m151.wheeloffortune.game.data.WebDto;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class HighScoreDTO implements WebDto<Integer, HighScore> {

    private Integer id;
    private int score;
    private LocalDateTime achievedAt;
    private CandidateDTO candidateDTO;

    @Override
    public HighScore parseToEntity() {
        HighScore highScore = new HighScore(score, achievedAt, candidateDTO.parseToEntity());
        highScore.setId(id);
        return highScore;
    }
}
