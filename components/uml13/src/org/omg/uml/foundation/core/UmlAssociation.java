package org.omg.uml.foundation.core;

/**
 * Association object instance interface.
 */
public interface UmlAssociation extends org.omg.uml.foundation.core.GeneralizableElement, org.omg.uml.foundation.core.Relationship {
    /**
     * Returns the value of reference connection.
     * @return Value of reference connection.
     */
    public java.util.Collection getConnection();
}
