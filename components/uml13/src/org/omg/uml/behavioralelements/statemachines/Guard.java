package org.omg.uml.behavioralelements.statemachines;

/**
 * Guard object instance interface.
 */
public interface Guard extends org.omg.uml.foundation.core.ModelElement {
    /**
     * Returns the value of attribute expression.
     * @return Value of attribute expression.
     */
    public org.omg.uml.foundation.datatypes.BooleanExpression getExpression();
    /**
     * Sets the value of expression attribute. See {@link #getExpression} for 
     * description on the attribute.
     * @param newValue New value to be set.
     */
    public void setExpression(org.omg.uml.foundation.datatypes.BooleanExpression newValue);
    /**
     * Returns the value of reference transition.
     * @return Value of reference transition.
     */
    public org.omg.uml.behavioralelements.statemachines.Transition getTransition();
    /**
     * Sets the value of reference transition. See {@link #getTransition} for 
     * description on the reference.
     * @param newValue New value to be set.
     */
    public void setTransition(org.omg.uml.behavioralelements.statemachines.Transition newValue);
}
