package org.omg.uml.behavioralelements.commonbehavior;

/**
 * DestroyAction class proxy interface.
 */
public interface DestroyActionClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public DestroyAction createDestroyAction();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param recurrence 
     * @param target 
     * @param isAsynchronous 
     * @param script 
     * @return The created instance object.
     */
    public DestroyAction createDestroyAction(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, org.omg.uml.foundation.datatypes.IterationExpression recurrence, org.omg.uml.foundation.datatypes.ObjectSetExpression target, boolean isAsynchronous, org.omg.uml.foundation.datatypes.ActionExpression script);
}
