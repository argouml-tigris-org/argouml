package org.omg.uml.behavioralelements.statemachines;

/**
 * CallEvent object instance interface.
 */
public interface CallEvent extends org.omg.uml.behavioralelements.statemachines.Event {
    /**
     * Returns the value of reference operation.
     * @return Value of reference operation.
     */
    public org.omg.uml.foundation.core.Operation getOperation();
    /**
     * Sets the value of reference operation. See {@link #getOperation} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setOperation(org.omg.uml.foundation.core.Operation newValue);
}
