package org.omg.uml.behavioralelements.collaborations;

/**
 * Interaction object instance interface.
 */
public interface Interaction extends org.omg.uml.foundation.core.ModelElement {
    /**
     * Returns the value of reference message.
     * @return Value of reference message.
     */
    public java.util.Collection getMessage();
    /**
     * Returns the value of reference context.
     * @return Value of reference context.
     */
    public org.omg.uml.behavioralelements.collaborations.Collaboration getContext();
    /**
     * Sets the value of reference context. See {@link #getContext} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setContext(org.omg.uml.behavioralelements.collaborations.Collaboration newValue);
}
