package org.omg.uml.behavioralelements.commonbehavior;

/**
 * Reception class proxy interface.
 */
public interface ReceptionClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Reception createReception();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param ownerScope 
     * @param isQuery 
     * @param specification 
     * @param isRoot 
     * @param isLeaf 
     * @param isAbstract 
     * @return The created instance object.
     */
    public Reception createReception(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, org.omg.uml.foundation.datatypes.ScopeKind ownerScope, boolean isQuery, java.lang.String specification, boolean isRoot, boolean isLeaf, boolean isAbstract);
}
