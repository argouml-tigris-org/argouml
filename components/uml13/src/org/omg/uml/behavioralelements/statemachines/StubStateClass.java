package org.omg.uml.behavioralelements.statemachines;

/**
 * StubState class proxy interface.
 */
public interface StubStateClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public StubState createStubState();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param referenceState 
     * @return The created instance object.
     */
    public StubState createStubState(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, java.lang.String referenceState);
}
