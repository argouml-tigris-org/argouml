package org.omg.uml.behavioralelements.collaborations;

/**
 * Collaboration object instance interface.
 */
public interface Collaboration extends org.omg.uml.foundation.core.GeneralizableElement, org.omg.uml.foundation.core.Namespace {
    /**
     * Returns the value of reference interaction.
     * @return Value of reference interaction.
     */
    public java.util.Collection getInteraction();
    /**
     * Returns the value of reference representedClassifier.
     * @return Value of reference representedClassifier.
     */
    public org.omg.uml.foundation.core.Classifier getRepresentedClassifier();
    /**
     * Sets the value of reference representedClassifier. See {@link #getRepresentedClassifier} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setRepresentedClassifier(org.omg.uml.foundation.core.Classifier newValue);
    /**
     * Returns the value of reference representedOperation.
     * @return Value of reference representedOperation.
     */
    public org.omg.uml.foundation.core.Operation getRepresentedOperation();
    /**
     * Sets the value of reference representedOperation. See {@link #getRepresentedOperation} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setRepresentedOperation(org.omg.uml.foundation.core.Operation newValue);
    /**
     * Returns the value of reference constrainingElement.
     * @return Value of reference constrainingElement.
     */
    public java.util.Collection getConstrainingElement();
}
