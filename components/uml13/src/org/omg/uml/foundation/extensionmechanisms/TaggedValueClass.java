package org.omg.uml.foundation.extensionmechanisms;

/**
 * TaggedValue class proxy interface.
 */
public interface TaggedValueClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public TaggedValue createTaggedValue();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param tag 
     * @param value 
     * @return The created instance object.
     */
    public TaggedValue createTaggedValue(java.lang.String tag, java.lang.String value);
}
