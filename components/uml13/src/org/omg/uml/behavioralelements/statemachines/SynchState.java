package org.omg.uml.behavioralelements.statemachines;

/**
 * SynchState object instance interface.
 */
public interface SynchState extends org.omg.uml.behavioralelements.statemachines.StateVertex {
    /**
     * Returns the value of attribute bound.
     * @return Value of attribute bound.
     */
    public int getBound();
    /**
     * Sets the value of bound attribute. See {@link #getBound} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setBound(int newValue);
}
