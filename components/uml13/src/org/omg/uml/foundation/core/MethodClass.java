package org.omg.uml.foundation.core;

/**
 * Method class proxy interface.
 */
public interface MethodClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Method createMethod();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param ownerScope 
     * @param isQuery 
     * @param body 
     * @return The created instance object.
     */
    public Method createMethod(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, org.omg.uml.foundation.datatypes.ScopeKind ownerScope, boolean isQuery, org.omg.uml.foundation.datatypes.ProcedureExpression body);
}
