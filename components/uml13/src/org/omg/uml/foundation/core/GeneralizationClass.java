package org.omg.uml.foundation.core;

/**
 * Generalization class proxy interface.
 */
public interface GeneralizationClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Generalization createGeneralization();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param discriminator 
     * @return The created instance object.
     */
    public Generalization createGeneralization(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, java.lang.String discriminator);
}
