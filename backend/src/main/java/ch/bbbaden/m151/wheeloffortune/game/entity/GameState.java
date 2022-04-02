package ch.bbbaden.m151.wheeloffortune.game.entity;

import lombok.*;

import java.util.List;

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
        REPLAY,
        DELETE,
        MESSAGE,
        SENTENCE_COMPLETED,
    }

    private State state;
    private List<Task> availableTasks;
    private List<TaskParameter> taskParameters;

    public Object getTaskParameterValue(Task key){
        for (TaskParameter taskParameter : taskParameters) {
            if(taskParameter.getKey().equals(key))
                return taskParameter.getValue();
        }
        return null;
    }
}
