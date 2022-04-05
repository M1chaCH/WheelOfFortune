package ch.bbbaden.m151.wheeloffortune.game.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class TaskParameter {
    private GameState.Task key;
    private Object value;
}
