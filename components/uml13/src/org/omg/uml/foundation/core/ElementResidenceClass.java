package org.omg.uml.foundation.core;

/**
 * ElementResidence class proxy interface.
 */
public interface ElementResidenceClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public ElementResidence createElementResidence();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param visibility 
     * @return The created instance object.
     */
    public ElementResidence createElementResidence(org.omg.uml.foundation.datatypes.VisibilityKind visibility);
}
