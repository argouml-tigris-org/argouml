package org.omg.uml.foundation.core;

/**
 * Abstraction class proxy interface.
 */
public interface AbstractionClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Abstraction createAbstraction();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param mapping 
     * @return The created instance object.
     */
    public Abstraction createAbstraction(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, org.omg.uml.foundation.datatypes.MappingExpression mapping);
}
