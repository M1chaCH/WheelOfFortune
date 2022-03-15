package ch.bbbaden.m151.wheeloffortune.game.data;

public interface WebEntity<I, D> extends CrudEntity<I>{
    /**
     * creates a {@link D} from itself
     * @return a new {@link D} containing the values of itself
     */
    D parseToDTO();
}
