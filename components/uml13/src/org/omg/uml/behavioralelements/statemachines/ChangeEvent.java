package org.omg.uml.behavioralelements.statemachines;

/**
 * ChangeEvent object instance interface.
 */
public interface ChangeEvent extends org.omg.uml.behavioralelements.statemachines.Event {
    /**
     * Returns the value of attribute changeExpression.
     * @return Value of attribute changeExpression.
     */
    public org.omg.uml.foundation.datatypes.BooleanExpression getChangeExpression();
    /**
     * Sets the value of changeExpression attribute. See {@link #getChangeExpression} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setChangeExpression(org.omg.uml.foundation.datatypes.BooleanExpression newValue);
}
