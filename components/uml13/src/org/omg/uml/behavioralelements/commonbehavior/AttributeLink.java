package org.omg.uml.behavioralelements.commonbehavior;

/**
 * AttributeLink object instance interface.
 */
public interface AttributeLink extends org.omg.uml.foundation.core.ModelElement {
    /**
     * Returns the value of reference attribute.
     * @return Value of reference attribute.
     */
    public org.omg.uml.foundation.core.Attribute getAttribute();
    /**
     * Sets the value of reference attribute. See {@link #getAttribute} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setAttribute(org.omg.uml.foundation.core.Attribute newValue);
    /**
     * Returns the value of reference value.
     * @return Value of reference value.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Instance getValue();
    /**
     * Sets the value of reference value. See {@link #getValue} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setValue(org.omg.uml.behavioralelements.commonbehavior.Instance newValue);
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
     * Returns the value of reference linkEnd.
     * @return Value of reference linkEnd.
     */
    public org.omg.uml.behavioralelements.commonbehavior.LinkEnd getLinkEnd();
    /**
     * Sets the value of reference linkEnd. See {@link #getLinkEnd} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setLinkEnd(org.omg.uml.behavioralelements.commonbehavior.LinkEnd newValue);
}
