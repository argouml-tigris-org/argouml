package org.omg.uml.foundation.core;

/**
 * Constraint class proxy interface.
 */
public interface ConstraintClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Constraint createConstraint();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param body 
     * @return The created instance object.
     */
    public Constraint createConstraint(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, org.omg.uml.foundation.datatypes.BooleanExpression body);
}
