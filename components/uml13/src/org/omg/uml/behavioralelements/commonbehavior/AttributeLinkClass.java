package org.omg.uml.behavioralelements.commonbehavior;

/**
 * AttributeLink class proxy interface.
 */
public interface AttributeLinkClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public AttributeLink createAttributeLink();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @return The created instance object.
     */
    public AttributeLink createAttributeLink(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification);
}
