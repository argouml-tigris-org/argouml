package org.omg.uml.foundation.core;

/**
 * Parameter class proxy interface.
 */
public interface ParameterClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Parameter createParameter();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param defaultValue 
     * @param kind 
     * @return The created instance object.
     */
    public Parameter createParameter(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, org.omg.uml.foundation.datatypes.Expression defaultValue, org.omg.uml.foundation.datatypes.ParameterDirectionKind kind);
}
