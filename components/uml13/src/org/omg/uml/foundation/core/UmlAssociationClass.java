package org.omg.uml.foundation.core;

/**
 * Association class proxy interface.
 */
public interface UmlAssociationClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public UmlAssociation createUmlAssociation();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param isRoot 
     * @param isLeaf 
     * @param isAbstract 
     * @return The created instance object.
     */
    public UmlAssociation createUmlAssociation(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, boolean isRoot, boolean isLeaf, boolean isAbstract);
}
