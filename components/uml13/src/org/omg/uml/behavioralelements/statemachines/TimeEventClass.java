package org.omg.uml.behavioralelements.statemachines;

/**
 * TimeEvent class proxy interface.
 */
public interface TimeEventClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public TimeEvent createTimeEvent();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param when 
     * @return The created instance object.
     */
    public TimeEvent createTimeEvent(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, org.omg.uml.foundation.datatypes.TimeExpression when);
}
