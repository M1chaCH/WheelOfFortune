package ch.bbbaden.m151.wheeloffortune.game.data.sentence;

import ch.bbbaden.m151.wheeloffortune.game.data.WebEntity;
import ch.bbbaden.m151.wheeloffortune.game.data.category.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Sentence implements WebEntity<Integer, SentenceDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String sentence;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "category", nullable = false)
    private Category category;

    public Sentence(String sentence, Category category) {
        this.sentence = sentence;
        this.category = category;
    }

    @Override
    public SentenceDTO parseToDTO() {
        return new SentenceDTO(id, sentence, category.parseToDTO());
    }
}
