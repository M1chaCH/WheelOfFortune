package ch.bbbaden.m151.wheeloffortune.game.data;

public interface WebDto<I, E> extends CrudEntity<I>{
    E parseToEntity();
}
