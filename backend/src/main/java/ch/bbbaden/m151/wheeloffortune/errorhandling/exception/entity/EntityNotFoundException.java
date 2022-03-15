package ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.BasicWheelOfFortuneException;

public class EntityNotFoundException extends BasicWheelOfFortuneException {
    public EntityNotFoundException(String entityName, String entityId){
        super(EntityNotFoundException.class, "entity: [ " + entityName + " | id:" + entityId + " ] does not exist");
    }
}
