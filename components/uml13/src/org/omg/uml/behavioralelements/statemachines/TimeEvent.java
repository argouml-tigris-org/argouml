package org.omg.uml.behavioralelements.statemachines;

/**
 * TimeEvent object instance interface.
 */
public interface TimeEvent extends org.omg.uml.behavioralelements.statemachines.Event {
    /**
     * Returns the value of attribute when.
     * @return Value of attribute when.
     */
    public org.omg.uml.foundation.datatypes.TimeExpression getWhen();
    /**
     * Sets the value of when attribute. See {@link #getWhen} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setWhen(org.omg.uml.foundation.datatypes.TimeExpression newValue);
}
