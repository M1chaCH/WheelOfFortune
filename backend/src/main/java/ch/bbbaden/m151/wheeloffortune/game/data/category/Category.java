package ch.bbbaden.m151.wheeloffortune.game.data.category;

import ch.bbbaden.m151.wheeloffortune.game.data.WebEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
