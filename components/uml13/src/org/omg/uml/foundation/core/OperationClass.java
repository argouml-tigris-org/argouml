package org.omg.uml.foundation.core;

/**
 * Operation class proxy interface.
 */
public interface OperationClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Operation createOperation();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param ownerScope 
     * @param isQuery 
     * @param concurrency 
     * @param isRoot 
     * @param isLeaf 
     * @param isAbstract 
     * @param specification 
     * @return The created instance object.
     */
    public Operation createOperation(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, org.omg.uml.foundation.datatypes.ScopeKind ownerScope, boolean isQuery, org.omg.uml.foundation.datatypes.CallConcurrencyKind concurrency, boolean isRoot, boolean isLeaf, boolean isAbstract, java.lang.String specification);
}
