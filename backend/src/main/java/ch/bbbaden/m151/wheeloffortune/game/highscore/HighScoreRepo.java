package ch.bbbaden.m151.wheeloffortune.game.highscore;

import ch.bbbaden.m151.wheeloffortune.game.candidate.Candidate;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HighScoreRepo extends CrudRepository<HighScore, Integer> {

    List<HighScore> findHighScoresByCandidate(Candidate candidate);
    List<HighScore> findHighScoresByAchievedAt(LocalDateTime date);
}
