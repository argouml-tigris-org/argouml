package org.omg.uml.behavioralelements.statemachines;

/**
 * CompositeState class proxy interface.
 */
public interface CompositeStateClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public CompositeState createCompositeState();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param isConcurrent 
     * @return The created instance object.
     */
    public CompositeState createCompositeState(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, boolean isConcurrent);
}
