package ch.bbbaden.m151.wheeloffortune.game.data.question;

import ch.bbbaden.m151.wheeloffortune.game.data.WebDto;
import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryDTO;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class QuestionDTO implements WebDto<Integer, Question> {
    private Integer id;
    private String question;
    private String answerOne;
    private String answerTwo;
    private boolean answerOneCorrect;
    private CategoryDTO categoryDTO;

    @Override
    public Question parseToEntity() {
        Question questionEntity = new Question(getQuestion(),
                getAnswerOne(),
                getAnswerTwo(),
                isAnswerOneCorrect(),
                getCategoryDTO().parseToEntity());
        questionEntity.setId(getId());
        return questionEntity;
    }
}
