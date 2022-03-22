package ch.bbbaden.m151.wheeloffortune.game.data.question;

import ch.bbbaden.m151.wheeloffortune.game.data.WebEntity;
import ch.bbbaden.m151.wheeloffortune.game.data.category.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Question implements WebEntity<Integer, QuestionDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String question;
    @Column(nullable = false)
    private String answerOne;
    @Column(nullable = false)
    private String answerTwo;
    @Column(nullable = false)
    private boolean answerOneCorrect;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn ( name = "category", nullable = false    )
    private Category category;

    public Question(String question, String answerOne, String answerTwo, boolean answerOneCorrect, Category category) {
        this.question = question;
        this.answerOne = answerOne;
        this.answerTwo = answerTwo;
        this.answerOneCorrect = answerOneCorrect;
        this.category = category;
    }

    @Override
    public QuestionDTO parseToDTO() {
        return new QuestionDTO(id, question, answerOne, answerTwo, answerOneCorrect, category.parseToDTO());
    }
}
