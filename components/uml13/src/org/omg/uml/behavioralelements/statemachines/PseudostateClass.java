package org.omg.uml.behavioralelements.statemachines;

/**
 * Pseudostate class proxy interface.
 */
public interface PseudostateClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Pseudostate createPseudostate();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param kind 
     * @return The created instance object.
     */
    public Pseudostate createPseudostate(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, org.omg.uml.foundation.datatypes.PseudostateKind kind);
}
