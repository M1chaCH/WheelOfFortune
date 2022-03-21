package ch.bbbaden.m151.wheeloffortune.game.highscore;

import org.springframework.data.repository.CrudRepository;

public interface HighScoreRepo extends CrudRepository<HighScore, Integer> {
    Iterable<HighScore> findAllByOrderByScoreDesc();
}
