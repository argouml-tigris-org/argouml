package org.omg.uml.behavioralelements.statemachines;

/**
 * StateVertex object instance interface.
 */
public interface StateVertex extends org.omg.uml.foundation.core.ModelElement {
    /**
     * Returns the value of reference container.
     * @return Value of reference container.
     */
    public org.omg.uml.behavioralelements.statemachines.CompositeState getContainer();
    /**
     * Sets the value of reference container. See {@link #getContainer} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setContainer(org.omg.uml.behavioralelements.statemachines.CompositeState newValue);
    /**
     * Returns the value of reference outgoing.
     * @return Value of reference outgoing.
     */
    public java.util.Collection getOutgoing();
    /**
     * Returns the value of reference incoming.
     * @return Value of reference incoming.
     */
    public java.util.Collection getIncoming();
}
