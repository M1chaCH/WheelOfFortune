package ch.bbbaden.m151.wheeloffortune.errorhandling.exception.entity;

import ch.bbbaden.m151.wheeloffortune.errorhandling.exception.BasicWheelOfFortuneException;

public class EntityInvalidException extends BasicWheelOfFortuneException {
    public EntityInvalidException(String entityName, Object entityId) {
        super(EntityInvalidException.class, "entity: [ " + entityName + " | id:" + entityId + " ] is invalid");
    }
}
