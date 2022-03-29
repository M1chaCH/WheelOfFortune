package ch.bbbaden.m151.wheeloffortune.game.entity;

import lombok.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class GameState {
    public enum State{
        PLAY,
        FORCED,
        END
    }

    public enum Task {
        SPIN,
        GUESS_CONSONANT,
        BUY_VOWEL,
        SOLVE_PUZZLE,
        LEAVE,
        RISK,
        BANKRUPT,
        HP_DEATH,
        REPLAY
    }

    private State state;
    private List<Task> availableTasks;
    private Map<Task, Object> taskParameters;
}
