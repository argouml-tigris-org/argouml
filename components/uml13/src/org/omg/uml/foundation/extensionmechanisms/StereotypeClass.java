package org.omg.uml.foundation.extensionmechanisms;

/**
 * Stereotype class proxy interface.
 */
public interface StereotypeClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Stereotype createStereotype();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param isRoot 
     * @param isLeaf 
     * @param isAbstract 
     * @param icon 
     * @param baseClass 
     * @return The created instance object.
     */
    public Stereotype createStereotype(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, boolean isRoot, boolean isLeaf, boolean isAbstract, java.lang.String icon, java.lang.String baseClass);
}
