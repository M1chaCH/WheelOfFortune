package ch.bbbaden.m151.wheeloffortune.game.highscore;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class HighScoreDTO {
    private int id;
    private int score;
    private int playedRounds;
    private String username;
    private String achievedAt;
}
