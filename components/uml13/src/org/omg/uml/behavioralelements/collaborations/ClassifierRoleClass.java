package org.omg.uml.behavioralelements.collaborations;

/**
 * ClassifierRole class proxy interface.
 */
public interface ClassifierRoleClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public ClassifierRole createClassifierRole();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param isRoot 
     * @param isLeaf 
     * @param isAbstract 
     * @param multiplicity 
     * @return The created instance object.
     */
    public ClassifierRole createClassifierRole(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, boolean isRoot, boolean isLeaf, boolean isAbstract, org.omg.uml.foundation.datatypes.Multiplicity multiplicity);
}
