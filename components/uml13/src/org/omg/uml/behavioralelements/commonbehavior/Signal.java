package org.omg.uml.behavioralelements.commonbehavior;

/**
 * Signal object instance interface.
 */
public interface Signal extends org.omg.uml.foundation.core.Classifier {
    /**
     * Returns the value of reference reception.
     * @return Value of reference reception.
     */
    public java.util.Collection getReception();
    /**
     * Returns the value of reference context.
     * @return Value of reference context.
     */
    public java.util.Collection getContext();
    /**
     * Returns the value of reference sendAction.
     * @return Value of reference sendAction.
     */
    public java.util.Collection getSendAction();
}
