package org.omg.uml.behavioralelements.commonbehavior;

/**
 * Link object instance interface.
 */
public interface Link extends org.omg.uml.foundation.core.ModelElement {
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
     * Returns the value of reference connection.
     * @return Value of reference connection.
     */
    public java.util.Collection getConnection();
    /**
     * Returns the value of reference stimulus.
     * @return Value of reference stimulus.
     */
    public java.util.Collection getStimulus();
}
