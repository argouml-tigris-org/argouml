package org.omg.uml.behavioralelements.statemachines;

/**
 * ChangeEvent class proxy interface.
 */
public interface ChangeEventClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public ChangeEvent createChangeEvent();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param changeExpression 
     * @return The created instance object.
     */
    public ChangeEvent createChangeEvent(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, org.omg.uml.foundation.datatypes.BooleanExpression changeExpression);
}
