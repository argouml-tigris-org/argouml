package org.omg.uml.behavioralelements.statemachines;

/**
 * Event object instance interface.
 */
public interface Event extends org.omg.uml.foundation.core.ModelElement {
    /**
     * Returns the value of reference parameter.
     * @return Value of reference parameter.
     */
    public java.util.List getParameter();
    /**
     * Returns the value of reference state.
     * @return Value of reference state.
     */
    public java.util.Collection getState();
    /**
     * Returns the value of reference transition.
     * @return Value of reference transition.
     */
    public java.util.Collection getTransition();
}
