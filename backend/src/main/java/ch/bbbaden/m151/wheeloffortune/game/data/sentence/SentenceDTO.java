package ch.bbbaden.m151.wheeloffortune.game.data.sentence;

import ch.bbbaden.m151.wheeloffortune.game.data.WebDto;
import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryDTO;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SentenceDTO implements WebDto<Integer, Sentence> {
    private Integer id;
    private String sentence;
    private CategoryDTO categoryDTO;

    @Override
    public Sentence parseToEntity() {
        Sentence sentenceEntity = new Sentence(getSentence(), getCategoryDTO().parseToEntity());
        sentenceEntity.setId(getId());
        return sentenceEntity;
    }
}
