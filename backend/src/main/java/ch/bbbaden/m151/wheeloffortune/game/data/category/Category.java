package ch.bbbaden.m151.wheeloffortune.game.data.category;

import ch.bbbaden.m151.wheeloffortune.game.data.WebEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Category implements WebEntity<Integer, CategoryDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    public Category(String name) {
        this.name = name;
    }

    @Override
    public CategoryDTO parseToDTO() {
        return new CategoryDTO(id, name);
    }

    public static Category fromDTO(CategoryDTO dto){
        Category category = new Category(dto.getName());
        category.setId(dto.getId());
        return category;
    }
}
