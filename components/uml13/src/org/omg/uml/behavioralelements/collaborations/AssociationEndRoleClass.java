package org.omg.uml.behavioralelements.collaborations;

/**
 * AssociationEndRole class proxy interface.
 */
public interface AssociationEndRoleClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public AssociationEndRole createAssociationEndRole();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @param isNavigable 
     * @param ordering 
     * @param aggregation 
     * @param targetScope 
     * @param multiplicity 
     * @param changeability 
     * @param collaborationMultiplicity 
     * @return The created instance object.
     */
    public AssociationEndRole createAssociationEndRole(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification, boolean isNavigable, org.omg.uml.foundation.datatypes.OrderingKind ordering, org.omg.uml.foundation.datatypes.AggregationKind aggregation, org.omg.uml.foundation.datatypes.ScopeKind targetScope, org.omg.uml.foundation.datatypes.Multiplicity multiplicity, org.omg.uml.foundation.datatypes.ChangeableKind changeability, org.omg.uml.foundation.datatypes.Multiplicity collaborationMultiplicity);
}
