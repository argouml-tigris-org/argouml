package org.omg.uml.behavioralelements.activitygraphs;

/**
 * ActionState object instance interface.
 */
public interface ActionState extends org.omg.uml.behavioralelements.statemachines.SimpleState {
    /**
     * Returns the value of attribute isDynamic.
     * @return Value of attribute isDynamic.
     */
    public boolean isDynamic();
    /**
     * Sets the value of isDynamic attribute. See {@link #isDynamic} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setDynamic(boolean newValue);
    /**
     * Returns the value of attribute dynamicArguments.
     * @return Value of attribute dynamicArguments.
     */
    public org.omg.uml.foundation.datatypes.ArgListsExpression getDynamicArguments();
    /**
     * Sets the value of dynamicArguments attribute. See {@link #getDynamicArguments} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setDynamicArguments(org.omg.uml.foundation.datatypes.ArgListsExpression newValue);
    /**
     * Returns the value of attribute dynamicMultiplicity.
     * @return Value of attribute dynamicMultiplicity.
     */
    public org.omg.uml.foundation.datatypes.Multiplicity getDynamicMultiplicity();
    /**
     * Sets the value of dynamicMultiplicity attribute. See {@link #getDynamicMultiplicity} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setDynamicMultiplicity(org.omg.uml.foundation.datatypes.Multiplicity newValue);
}
