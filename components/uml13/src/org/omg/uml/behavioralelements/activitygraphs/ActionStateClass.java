package org.omg.uml.behavioralelements.activitygraphs;

/**
 * ActionState class proxy interface.
 */
public interface ActionStateClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public ActionState createActionState();
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
    public ActionState createActionState(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, boolean isDynamic, org.omg.uml.foundation.datatypes.ArgListsExpression dynamicArguments, org.omg.uml.foundation.datatypes.Multiplicity dynamicMultiplicity);
}
