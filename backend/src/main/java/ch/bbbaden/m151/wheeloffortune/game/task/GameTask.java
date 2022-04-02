package ch.bbbaden.m151.wheeloffortune.game.task;

import ch.bbbaden.m151.wheeloffortune.game.entity.Game;

public interface GameTask {

    /**
     * Handles a task in the game <br>
     * NOTE: when implementing do NOT SAVE in repo
     * @param game the game to handle the task in
     * @return the same game entity with changed values (NOT SAVED IN REPO)
     */
    Game execute(Game game);
}
