package org.omg.uml.behavioralelements.commonbehavior;

/**
 * Stimulus class proxy interface.
 */
public interface StimulusClass extends javax.jmi.reflect.RefClass {
    /**
     * The default factory operation used to create an instance object.
     * @return The created instance object.
     */
    public Stimulus createStimulus();
    /**
     * Creates an instance object having attributes initialized by the passed 
     * values.
     * @param name 
     * @param visibility 
     * @param isSpecification 
     * @return The created instance object.
     */
    public Stimulus createStimulus(java.lang.String name, org.omg.uml.foundation.datatypes.VisibilityKind visibility, boolean isSpecification);
}
