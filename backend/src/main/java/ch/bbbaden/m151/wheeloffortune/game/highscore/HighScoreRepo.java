package ch.bbbaden.m151.wheeloffortune.game.highscore;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface HighScoreRepo extends CrudRepository<HighScore, Integer> {
    Iterable<HighScore> findAllByOrderByScoreDesc();
    Optional<HighScore> findHighScoreByUsername(String username);
}
