package org.omg.uml.behavioralelements.commonbehavior;

/**
 * LinkEnd object instance interface.
 */
public interface LinkEnd extends org.omg.uml.foundation.core.ModelElement {
    /**
     * Returns the value of reference instance.
     * @return Value of reference instance.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Instance getInstance();
    /**
     * Sets the value of reference instance. See {@link #getInstance} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setInstance(org.omg.uml.behavioralelements.commonbehavior.Instance newValue);
    /**
     * Returns the value of reference link.
     * @return Value of reference link.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Link getLink();
    /**
     * Sets the value of reference link. See {@link #getLink} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setLink(org.omg.uml.behavioralelements.commonbehavior.Link newValue);
    /**
     * Returns the value of reference associationEnd.
     * @return Value of reference associationEnd.
     */
    public org.omg.uml.foundation.core.AssociationEnd getAssociationEnd();
    /**
     * Sets the value of reference associationEnd. See {@link #getAssociationEnd} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setAssociationEnd(org.omg.uml.foundation.core.AssociationEnd newValue);
    /**
     * Returns the value of reference qualifiedValue.
     * @return Value of reference qualifiedValue.
     */
    public java.util.Collection getQualifiedValue();
}
