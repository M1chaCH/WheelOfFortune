package ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.BasicWheelOfFortuneException;

public class EntityAlreadyExistsException extends BasicWheelOfFortuneException {
    public EntityAlreadyExistsException(String entityName, Object entityId){
        super(EntityAlreadyExistsException.class, "entity: [ " + entityName + " | id:" + entityId + " ] already exists");
    }
}
