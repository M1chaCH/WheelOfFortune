package ch.bbbaden.m151.wheeloffortune.game.candidate;

import ch.bbbaden.m151.wheeloffortune.game.data.WebDto;
import lombok.*;

/**
 * a DTO for {@link Candidate}<br>
 * !IMPORTANT: the id is ignored by {@link #parseToEntity()}!
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CandidateDTO implements WebDto<Integer, Candidate> {

    private Integer id;
    private String username;

    /**
     * parses this to a {@link Candidate}<br>
     * !NOTE id is ignored & will be generated by JPA!
     * @return a new {@link Candidate} with the username stored in this DTO
     */
    @Override
    public Candidate parseToEntity() {
        return new Candidate(getUsername());
    }
}
