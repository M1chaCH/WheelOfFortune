package ch.bbbaden.m151.wheeloffortune.game.data.sentence;

import ch.bbbaden.m151.wheeloffortune.game.data.category.CategoryDTO;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SentenceDTO {
    private Integer id;
    private String sentence;
    private CategoryDTO categoryDTO;
}
