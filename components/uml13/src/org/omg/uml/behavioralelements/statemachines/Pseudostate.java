package org.omg.uml.behavioralelements.statemachines;

/**
 * Pseudostate object instance interface.
 */
public interface Pseudostate extends org.omg.uml.behavioralelements.statemachines.StateVertex {
    /**
     * Returns the value of attribute kind.
     * @return Value of attribute kind.
     */
    public org.omg.uml.foundation.datatypes.PseudostateKind getKind();
    /**
     * Sets the value of kind attribute. See {@link #getKind} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setKind(org.omg.uml.foundation.datatypes.PseudostateKind newValue);
}
