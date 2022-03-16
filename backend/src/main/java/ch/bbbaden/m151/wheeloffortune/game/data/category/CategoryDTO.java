package ch.bbbaden.m151.wheeloffortune.game.data.category;

import ch.bbbaden.m151.wheeloffortune.game.data.WebDto;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CategoryDTO implements WebDto<Integer, Category> {
    private Integer id;
    private String name;

    @Override
    public Category parseToEntity() {
        Category category = new Category(getName());
        category.setId(getId());
        return category;
    }
}
