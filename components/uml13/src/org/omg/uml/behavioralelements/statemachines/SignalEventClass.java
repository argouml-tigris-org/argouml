package org.omg.uml.behavioralelements.statemachines;

/**
 * SignalEvent class proxy interface.
 */
public interface SignalEventClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public SignalEvent createSignalEvent();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @return The created instance object.
     */
    public SignalEvent createSignalEvent(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification);
}
