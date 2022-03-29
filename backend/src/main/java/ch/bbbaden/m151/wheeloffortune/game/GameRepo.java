package ch.bbbaden.m151.wheeloffortune.game;

import ch.bbbaden.m151.wheeloffortune.game.entity.Game;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class GameRepo {
    private static final GameRepo INSTANCE = new GameRepo();

    private final Map<String, Game> games = new HashMap<>();

    private GameRepo() { }

    public static GameRepo getInstance(){
        return INSTANCE;
    }

    public Optional<Game> getGameById(String id){
        return Optional.ofNullable(games.get(id));
    }

    public Optional<Game> getByUsername(String username){
        return games.values().stream().filter(gameDTO -> gameDTO.getUsername().equals(username)).findAny();
    }

    public void save(Game gameDTO){
        games.put(gameDTO.getGameId(), gameDTO);
    }

    public void delete(String id){
        games.remove(id);
    }
}
