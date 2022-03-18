package ch.bbbaden.m151.wheeloffortune.game.candidate;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityAlreadyExistsException;
import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CandidateService {

    private final CandidateRepo repo;

    /**
     * @param username the username to search the candidate by.
     * @return a {@link Candidate} with the given username
     * @throws EntityNotFoundException when no Candidate with given username was found.
     */
    public Candidate getByUsername(String username){
        return repo.findCandidateByUsername(username).orElseThrow(() ->
                new EntityNotFoundException(Candidate.class.getSimpleName(), username));
    }

    /**
     * checks if no candidate with this username exists
     * and saves a new candidate with the username stored in the DTO
     * @param candidate the {@link CandidateDTO} to store
     * @return the saved {@link Candidate}
     * @throws EntityAlreadyExistsException when the username is already in use
     */
    public Candidate saveCandidate(CandidateDTO candidate){
        Optional<Candidate> c = repo.findCandidateByUsername(candidate.getUsername());
        if(c.isPresent())
            throw new EntityAlreadyExistsException(Candidate.class.getSimpleName(), c.get().getId());

        return repo.save(candidate.parseToEntity());
    }
}
