package org.omg.uml.behavioralelements.collaborations;

/**
 * Message class proxy interface.
 */
public interface MessageClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Message createMessage();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @return The created instance object.
     */
    public Message createMessage(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification);
}
