package org.omg.uml.behavioralelements.statemachines;

/**
 * SubmachineState object instance interface.
 */
public interface SubmachineState extends org.omg.uml.behavioralelements.statemachines.CompositeState {
    /**
     * Returns the value of reference submachine.
     * @return Value of reference submachine.
     */
    public org.omg.uml.behavioralelements.statemachines.StateMachine getSubmachine();
    /**
     * Sets the value of reference submachine. See {@link #getSubmachine} for 
     * description on the reference.
     * @param newValue New value to be set.
     */
    public void setSubmachine(org.omg.uml.behavioralelements.statemachines.StateMachine newValue);
}
