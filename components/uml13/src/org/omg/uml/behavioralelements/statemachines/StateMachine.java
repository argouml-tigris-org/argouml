package org.omg.uml.behavioralelements.statemachines;

/**
 * StateMachine object instance interface.
 */
public interface StateMachine extends org.omg.uml.foundation.core.ModelElement {
    /**
     * Returns the value of reference context.
     * @return Value of reference context.
     */
    public org.omg.uml.foundation.core.ModelElement getContext();
    /**
     * Sets the value of reference context. See {@link #getContext} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setContext(org.omg.uml.foundation.core.ModelElement newValue);
    /**
     * Returns the value of reference top.
     * @return Value of reference top.
     */
    public org.omg.uml.behavioralelements.statemachines.State getTop();
    /**
     * Sets the value of reference top. See {@link #getTop} for description on 
     * the reference.
     * @param newValue New value to be set.
     */
    public void setTop(org.omg.uml.behavioralelements.statemachines.State newValue);
    /**
     * Returns the value of reference transitions.
     * @return Value of reference transitions.
     */
    public java.util.Collection getTransitions();
    /**
     * Returns the value of reference subMachineState.
     * @return Value of reference subMachineState.
     */
    public java.util.Collection getSubMachineState();
}
