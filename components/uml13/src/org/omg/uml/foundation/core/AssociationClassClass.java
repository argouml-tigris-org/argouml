package org.omg.uml.foundation.core;

/**
 * AssociationClass class proxy interface.
 */
public interface AssociationClassClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public AssociationClass createAssociationClass();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param isRoot 
     * @param isLeaf 
     * @param isAbstract 
     * @param isActive 
     * @return The created instance object.
     */
    public AssociationClass createAssociationClass(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, boolean isRoot, boolean isLeaf, boolean isAbstract, boolean isActive);
}
