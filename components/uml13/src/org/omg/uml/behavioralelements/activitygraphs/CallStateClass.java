package org.omg.uml.behavioralelements.activitygraphs;

/**
 * CallState class proxy interface.
 */
public interface CallStateClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public CallState createCallState();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param isDynamic 
     * @param dynamicArguments 
     * @param dynamicMultiplicity 
     * @return The created instance object.
     */
    public CallState createCallState(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, boolean isDynamic, org.omg.uml.foundation.datatypes.ArgListsExpression dynamicArguments, org.omg.uml.foundation.datatypes.Multiplicity dynamicMultiplicity);
}
