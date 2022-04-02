package ch.bbbaden.m151.wheeloffortune.game.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CorrectQuestionRequest {
    private boolean selectedAnswerOne;
    private int amount;
}
