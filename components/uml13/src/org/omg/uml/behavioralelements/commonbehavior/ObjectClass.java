package org.omg.uml.behavioralelements.commonbehavior;

/**
 * Object class proxy interface.
 */
public interface ObjectClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Object createObject();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @return The created instance object.
     */
    public Object createObject(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification);
}
