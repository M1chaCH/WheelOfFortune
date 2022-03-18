package ch.bbbaden.m151.wheeloffortune.game.highscore;

import ch.bbbaden.m151.wheeloffortune.game.candidate.Candidate;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HighScoreRepo extends CrudRepository<HighScore, Integer> {
    List<HighScore> findHighScoresByCandidateOrderByAchievedAt(Candidate candidate);
}
