package org.omg.uml.foundation.core;

/**
 * Flow object instance interface.
 */
public interface Flow extends org.omg.uml.foundation.core.Relationship {
    /**
     * Returns the value of reference target.
     * @return Value of reference target.
     */
    public java.util.Collection getTarget();
    /**
     * Returns the value of reference source.
     * @return Value of reference source.
     */
    public java.util.Collection getSource();
}
