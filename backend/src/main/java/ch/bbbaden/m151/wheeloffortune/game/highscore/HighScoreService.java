package ch.bbbaden.m151.wheeloffortune.game.highscore;

import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenService;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityNotFoundException;
import ch.bbbaden.m151.wheeloffortune.util.LocalDateTimeParser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class HighScoreService {

    private final SecurityTokenService securityTokenService;
    private final HighScoreRepo repo;

    /**
     * @return a list of all the {@link HighScoreDTO}s in the DB, sorted decreasing by achievedAt.
     */
    public List<HighScoreDTO> getAllSortedByScoreAsDto(){
        return StreamSupport.stream(repo.findAllByOrderByScoreDesc().spliterator(), false)
                .map(highScore -> new HighScoreDTO(
                        highScore.getId(),
                        highScore.getScore(),
                        highScore.getUsername(),
                        LocalDateTimeParser.dateToString(highScore.getAchievedAt())))
                .collect(Collectors.toList());
    }

    /**
     * Only cares about the username and the score in the {@link HighScoreDTO}. The other values are generated.
     * (id -> JPA, <strong>achievedAt -> NOW</strong>)
     * @param toAdd the DTO to read the values from
     * @return the DTO representing the entity created in the database
     */
    public HighScoreDTO addNew(HighScoreDTO toAdd){
        HighScore highScoreToAdd = new HighScore();
        highScoreToAdd.setScore(toAdd.getScore());
        highScoreToAdd.setUsername(toAdd.getUsername());
        highScoreToAdd.setAchievedAt(LocalDateTime.now());

        HighScore createdHighScore = repo.save(highScoreToAdd);
        return new HighScoreDTO(createdHighScore.getId(),
                createdHighScore.getScore(),
                createdHighScore.getUsername(),
                LocalDateTimeParser.dateToString(createdHighScore.getAchievedAt()));
    }

    /**
     * @param securityTokenString the {@link ch.bbbaden.m151.wheeloffortune.auth.token.SecurityToken} token that is
     *                            used to authenticate the request
     * @param dtoToEdit the DTO to edit (all values, except the id, will be applied in the DB)
     * @throws ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.InvalidatedSecurityTokenException when the
     * given token is invalid
     * @throws ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.SecurityTokenNotFoundException when the given
     * token doesn't exist.
     * @throws EntityNotFoundException when no entity with the id in the DTO exists
     */
    public void edit(String securityTokenString, HighScoreDTO dtoToEdit){
        if(securityTokenService.isTokenValid(securityTokenString)){
            HighScore highScore = repo.findById(dtoToEdit.getId()).orElseThrow(() ->
                    new EntityNotFoundException(HighScore.class.getSimpleName(), dtoToEdit.getId()));

            highScore.setScore(dtoToEdit.getScore());
            highScore.setUsername(dtoToEdit.getUsername());
            highScore.setAchievedAt(LocalDateTimeParser.stringToDate(dtoToEdit.getAchievedAt()));

            repo.save(highScore);
        }
    }

    /**
     * @param securityTokenString the {@link ch.bbbaden.m151.wheeloffortune.auth.token.SecurityToken} token that is
     *                            used to authenticate the request
     * @param id the id of the {@link HighScore} to delete
     * @throws ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.InvalidatedSecurityTokenException when the
     * given token is invalid
     * @throws ch.bbbaden.m151.wheeloffortune.errorhandling.exception.auth.SecurityTokenNotFoundException when the given
     * token doesn't exist.
     * @throws EntityNotFoundException when no entity with the id in the DTO exists
     */
    public void delete(String securityTokenString, int id){
        if(securityTokenService.isTokenValid(securityTokenString)) {
            HighScore highScore = repo.findById(id).orElseThrow(() ->
                    new EntityNotFoundException(HighScore.class.getSimpleName(), id));
            repo.delete(highScore);
        }
    }

    public Optional<HighScore> getByUsername(String username){
        return repo.findHighScoreByUsername(username);
    }
}
