package org.omg.uml.behavioralelements.usecases;

/**
 * ExtensionPoint class proxy interface.
 */
public interface ExtensionPointClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public ExtensionPoint createExtensionPoint();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param location 
     * @return The created instance object.
     */
    public ExtensionPoint createExtensionPoint(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, java.lang.String location);
}
