package org.omg.uml.foundation.core;

/**
 * Attribute class proxy interface.
 */
public interface AttributeClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Attribute createAttribute();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param ownerScope 
     * @param multiplicity 
     * @param changeability 
     * @param targetScope 
     * @param initialValue 
     * @return The created instance object.
     */
    public Attribute createAttribute(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, org.omg.uml.foundation.datatypes.ScopeKind ownerScope, org.omg.uml.foundation.datatypes.Multiplicity multiplicity, org.omg.uml.foundation.datatypes.ChangeableKind changeability, org.omg.uml.foundation.datatypes.ScopeKind targetScope, org.omg.uml.foundation.datatypes.Expression initialValue);
}
