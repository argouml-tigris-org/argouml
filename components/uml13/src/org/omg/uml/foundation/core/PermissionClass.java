package org.omg.uml.foundation.core;

/**
 * Permission class proxy interface.
 */
public interface PermissionClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Permission createPermission();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @return The created instance object.
     */
    public Permission createPermission(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification);
}
