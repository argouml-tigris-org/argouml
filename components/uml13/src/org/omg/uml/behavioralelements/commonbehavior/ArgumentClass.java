package org.omg.uml.behavioralelements.commonbehavior;

/**
 * Argument class proxy interface.
 */
public interface ArgumentClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Argument createArgument();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param value 
     * @return The created instance object.
     */
    public Argument createArgument(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, org.omg.uml.foundation.datatypes.Expression value);
}
