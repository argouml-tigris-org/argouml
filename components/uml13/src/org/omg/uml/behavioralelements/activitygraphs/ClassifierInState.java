package org.omg.uml.behavioralelements.activitygraphs;

/**
 * ClassifierInState object instance interface.
 */
public interface ClassifierInState extends org.omg.uml.foundation.core.Classifier {
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
     * Returns the value of reference inState.
     * @return Value of reference inState.
     */
    public java.util.Collection getInState();
}
