package org.omg.uml.behavioralelements.usecases;

/**
 * UseCaseInstance class proxy interface.
 */
public interface UseCaseInstanceClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public UseCaseInstance createUseCaseInstance();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @return The created instance object.
     */
    public UseCaseInstance createUseCaseInstance(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification);
}
