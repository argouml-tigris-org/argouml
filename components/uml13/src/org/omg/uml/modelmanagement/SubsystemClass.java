package org.omg.uml.modelmanagement;

/**
 * Subsystem class proxy interface.
 */
public interface SubsystemClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Subsystem createSubsystem();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param isRoot 
     * @param isLeaf 
     * @param isAbstract 
     * @param isInstantiable 
     * @return The created instance object.
     */
    public Subsystem createSubsystem(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, boolean isRoot, boolean isLeaf, boolean isAbstract, boolean isInstantiable);
}
