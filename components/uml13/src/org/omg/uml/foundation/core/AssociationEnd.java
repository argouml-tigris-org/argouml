package org.omg.uml.foundation.core;

/**
 * AssociationEnd object instance interface.
 */
public interface AssociationEnd extends org.omg.uml.foundation.core.ModelElement {
    /**
     * Returns the value of attribute isNavigable.
     * @return Value of attribute isNavigable.
     */
    public boolean isNavigable();
    /**
     * Sets the value of isNavigable attribute. See {@link #isNavigable} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setNavigable(boolean newValue);
    /**
     * Returns the value of attribute ordering.
     * @return Value of attribute ordering.
     */
    public org.omg.uml.foundation.datatypes.OrderingKind getOrdering();
    /**
     * Sets the value of ordering attribute. See {@link #getOrdering} for description 
     * on the attribute.
     * @param newValue New value to be set.
     */
    public void setOrdering(org.omg.uml.foundation.datatypes.OrderingKind newValue);
    /**
     * Returns the value of attribute aggregation.
     * @return Value of attribute aggregation.
     */
    public org.omg.uml.foundation.datatypes.AggregationKind getAggregation();
    /**
     * Sets the value of aggregation attribute. See {@link #getAggregation} for 
     * description on the attribute.
     * @param newValue New value to be set.
     */
    public void setAggregation(org.omg.uml.foundation.datatypes.AggregationKind newValue);
    /**
     * Returns the value of attribute targetScope.
     * @return Value of attribute targetScope.
     */
    public org.omg.uml.foundation.datatypes.ScopeKind getTargetScope();
    /**
     * Sets the value of targetScope attribute. See {@link #getTargetScope} for 
     * description on the attribute.
     * @param newValue New value to be set.
     */
    public void setTargetScope(org.omg.uml.foundation.datatypes.ScopeKind newValue);
    /**
     * Returns the value of attribute multiplicity.
     * @return Value of attribute multiplicity.
     */
    public org.omg.uml.foundation.datatypes.Multiplicity getMultiplicity();
    /**
     * Sets the value of multiplicity attribute. See {@link #getMultiplicity} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setMultiplicity(org.omg.uml.foundation.datatypes.Multiplicity newValue);
    /**
     * Returns the value of attribute changeability.
     * @return Value of attribute changeability.
     */
    public org.omg.uml.foundation.datatypes.ChangeableKind getChangeability();
    /**
     * Sets the value of changeability attribute. See {@link #getChangeability} 
     * for description on the attribute.
     * @param newValue New value to be set.
     */
    public void setChangeability(org.omg.uml.foundation.datatypes.ChangeableKind newValue);
    /**
     * Returns the value of reference association.
     * @return Value of reference association.
     */
    public org.omg.uml.foundation.core.UmlAssociation getAssociation();
    /**
     * Sets the value of reference association. See {@link #getAssociation} for 
     * description on the reference.
     * @param newValue New value to be set.
     */
    public void setAssociation(org.omg.uml.foundation.core.UmlAssociation newValue);
    /**
     * Returns the value of reference qualifier.
     * @return Value of reference qualifier.
     */
    public java.util.List getQualifier();
    /**
     * Returns the value of reference type.
     * @return Value of reference type.
     */
    public org.omg.uml.foundation.core.Classifier getType();
    /**
     * Sets the value of reference type. See {@link #getType} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setType(org.omg.uml.foundation.core.Classifier newValue);
    /**
     * Returns the value of reference specification.
     * @return Value of reference specification.
     */
    public java.util.Collection getSpecification();
}
