package org.omg.uml.foundation.core;

/**
 * Dependency object instance interface.
 */
public interface Dependency extends org.omg.uml.foundation.core.Relationship {
    /**
     * Returns the value of reference client.
     * @return Value of reference client.
     */
    public java.util.Collection getClient();
    /**
     * Returns the value of reference supplier.
     * @return Value of reference supplier.
     */
    public java.util.Collection getSupplier();
}
