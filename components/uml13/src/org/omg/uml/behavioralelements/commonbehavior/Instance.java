package org.omg.uml.behavioralelements.commonbehavior;

/**
 * Instance object instance interface.
 */
public interface Instance extends org.omg.uml.foundation.core.ModelElement {
    /**
     * Returns the value of reference classifier.
     * @return Value of reference classifier.
     */
    public java.util.Collection getClassifier();
    /**
     * Returns the value of reference attributeLink.
     * @return Value of reference attributeLink.
     */
    public java.util.Collection getAttributeLink();
    /**
     * Returns the value of reference linkEnd.
     * @return Value of reference linkEnd.
     */
    public java.util.Collection getLinkEnd();
    /**
     * Returns the value of reference slot.
     * @return Value of reference slot.
     */
    public java.util.Collection getSlot();
    /**
     * Returns the value of reference stimulus1.
     * @return Value of reference stimulus1.
     */
    public java.util.Collection getStimulus1();
    /**
     * Returns the value of reference stimulus2.
     * @return Value of reference stimulus2.
     */
    public java.util.Collection getStimulus2();
    /**
     * Returns the value of reference stimulus3.
     * @return Value of reference stimulus3.
     */
    public java.util.Collection getStimulus3();
    /**
     * Returns the value of reference componentInstance.
     * @return Value of reference componentInstance.
     */
    public org.omg.uml.behavioralelements.commonbehavior.ComponentInstance getComponentInstance();
    /**
     * Sets the value of reference componentInstance. See {@link #getComponentInstance} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setComponentInstance(org.omg.uml.behavioralelements.commonbehavior.ComponentInstance newValue);
}
