package org.omg.uml.modelmanagement;

/**
 * ElementImport class proxy interface.
 */
public interface ElementImportClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public ElementImport createElementImport();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param visibility 
     * @param alias 
     * @return The created instance object.
     */
    public ElementImport createElementImport(org.omg.uml.foundation.datatypes.VisibilityKind visibility, java.lang.String alias);
}
