package ch.bbbaden.m151.wheeloffortune.game.candidate;

import ch.bbbaden.m151.wheeloffortune.game.data.WebEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Candidate implements WebEntity<Integer, CandidateDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String username;

    public Candidate(String username) {
        this.username = username;
    }

    @Override
    public CandidateDTO parseToDTO() {
        return new CandidateDTO(getId(), getUsername());
    }
}
