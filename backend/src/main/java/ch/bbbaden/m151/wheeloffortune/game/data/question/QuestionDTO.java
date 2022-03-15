package ch.bbbaden.m151.wheeloffortune.game.data.question;

import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryDTO;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class QuestionDTO {
    private Integer id;
    private String question;
    private String answerOne;
    private String answerTwo;
    private CategoryDTO categoryDTO;
}
