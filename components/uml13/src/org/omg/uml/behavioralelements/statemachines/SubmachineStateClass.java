package org.omg.uml.behavioralelements.statemachines;

/**
 * SubmachineState class proxy interface.
 */
public interface SubmachineStateClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public SubmachineState createSubmachineState();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param isConcurrent 
     * @return The created instance object.
     */
    public SubmachineState createSubmachineState(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, boolean isConcurrent);
}
