package org.omg.uml.foundation.core;

/**
 * Classifier object instance interface.
 */
public interface Classifier extends org.omg.uml.foundation.core.GeneralizableElement, org.omg.uml.foundation.core.Namespace {
    /**
     * Returns the value of reference feature.
     * @return Value of reference feature.
     */
    public java.util.List getFeature();
    /**
     * Returns the value of reference participant.
     * @return Value of reference participant.
     */
    public java.util.Collection getParticipant();
    /**
     * Returns the value of reference powertypeRange.
     * @return Value of reference powertypeRange.
     */
    public java.util.Collection getPowertypeRange();
}
