package org.omg.uml.behavioralelements.statemachines;

/**
 * SynchState class proxy interface.
 */
public interface SynchStateClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public SynchState createSynchState();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param bound 
     * @return The created instance object.
     */
    public SynchState createSynchState(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, int bound);
}
