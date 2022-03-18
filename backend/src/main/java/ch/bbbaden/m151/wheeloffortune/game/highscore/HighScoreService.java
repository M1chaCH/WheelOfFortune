package ch.bbbaden.m151.wheeloffortune.game.highscore;

import ch.bbbaden.m151.wheeloffortune.auth.token.SecurityTokenService;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityAlreadyExistsException;
import ch.bbbaden.m151.wheeloffortune.game.candidate.CandidateService;
import ch.bbbaden.m151.wheeloffortune.game.data.GenericAuthenticatedEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HighScoreService extends GenericAuthenticatedEntityService<Integer, HighScoreDTO, HighScore, HighScoreRepo> {

    private final CandidateService candidateService;

    @Autowired
    public HighScoreService(SecurityTokenService securityTokenService, HighScoreRepo repo, CandidateService candidateService) {
        super(securityTokenService, repo);
        this.candidateService = candidateService;
    }

    /**
     * @param candidateId the id of the candidate in search
     * @return a list of SighScores for a candidate
     * @throws ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityNotFoundException when candidate
     * does not exist
     */
    public List<HighScore> getByCandidateSortedByDate(Integer candidateId){
        return repo.findHighScoresByCandidateOrderByAchievedAt(candidateService.getById(candidateId));
    }

    /**
     * checks if entity id is new and saves in db
     * @param toAdd the {@link HighScore} to save in the database
     * @throws EntityAlreadyExistsException when entity already exists in the db
     */
    public HighScore addNew(HighScore toAdd) {
        if(repo.findById(toAdd.getId()).isPresent())
            throw new EntityAlreadyExistsException(toAdd.getClass().getName(), toAdd.getId());

        return repo.save(toAdd);
    }

    /**
     * like {@link #addNew(HighScore)} but parses result to DTO
     */
    public HighScoreDTO addNew(HighScoreDTO toAdd){
        return this.addNew(toAdd.parseToEntity()).parseToDTO();
    }
}
