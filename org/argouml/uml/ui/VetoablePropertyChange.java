package org.argouml.uml.ui;

/**
 * A class implementing this interface states it can veto a property change.
 * Normally vetoing is done with throwing a PropertyVetoException. Due to the
 * generic invocation mechanisme for setmethods in the UMLxxxProperty this is not 
 * possible to use.
 * @author jaap.branderhorst@xs4all.nl
 * @since ArgoUML 0.11.1
 * @see UMLTextProperty#setProperty for an example of its use in setting properties
 * @see org.argouml.ui.ArgoDiagram for an example implementing this interface
 */
public interface VetoablePropertyChange {
    /**
     * Checks for veto's on proposed changes. 
     * @param propertyName The property to check for.
     * @param args arguments with which the setmethod will be invoked if this method returns false
     * @return boolean false if no veto, true if veto
     */
	public boolean vetoCheck(String propertyName, Object[] args);
    /**
     * Gets the specific errormessage if a veto is given by vetoCheck
     * @param propertyName the property for which the veto was given
     * @return String the specific errormessage
     */
	public String getVetoMessage(String propertyName);

}
