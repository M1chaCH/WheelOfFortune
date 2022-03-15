package ch.bbbaden.m151.wheeloffortune.game.candidate;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CandidateRepo extends CrudRepository<Candidate, Integer> {

    Optional<Candidate> findCandidateByUsername(String username);
}
