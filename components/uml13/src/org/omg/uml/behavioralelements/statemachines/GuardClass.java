package org.omg.uml.behavioralelements.statemachines;

/**
 * Guard class proxy interface.
 */
public interface GuardClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Guard createGuard();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param expression 
     * @return The created instance object.
     */
    public Guard createGuard(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, org.omg.uml.foundation.datatypes.BooleanExpression expression);
}
