package org.omg.uml.behavioralelements.activitygraphs;

/**
 * SubactivityState class proxy interface.
 */
public interface SubactivityStateClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public SubactivityState createSubactivityState();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param isConcurrent 
     * @param isDynamic 
     * @param dynamicArguments 
     * @param dynamicMultiplicity 
     * @return The created instance object.
     */
    public SubactivityState createSubactivityState(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, boolean isConcurrent, boolean isDynamic, org.omg.uml.foundation.datatypes.ArgListsExpression dynamicArguments, org.omg.uml.foundation.datatypes.Multiplicity dynamicMultiplicity);
}
