package org.omg.uml.behavioralelements.usecases;

/**
 * Extend class proxy interface.
 */
public interface ExtendClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Extend createExtend();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param condition 
     * @return The created instance object.
     */
    public Extend createExtend(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, org.omg.uml.foundation.datatypes.BooleanExpression condition);
}
