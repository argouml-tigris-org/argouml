package org.omg.uml.behavioralelements.commonbehavior;

/**
 * Stimulus object instance interface.
 */
public interface Stimulus extends org.omg.uml.foundation.core.ModelElement {
    /**
     * Returns the value of reference argument.
     * @return Value of reference argument.
     */
    public java.util.Collection getArgument();
    /**
     * Returns the value of reference sender.
     * @return Value of reference sender.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Instance getSender();
    /**
     * Sets the value of reference sender. See {@link #getSender} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setSender(org.omg.uml.behavioralelements.commonbehavior.Instance newValue);
    /**
     * Returns the value of reference receiver.
     * @return Value of reference receiver.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Instance getReceiver();
    /**
     * Sets the value of reference receiver. See {@link #getReceiver} for description 
     * on the reference.
     * @param newValue New value to be set.
     */
    public void setReceiver(org.omg.uml.behavioralelements.commonbehavior.Instance newValue);
    /**
     * Returns the value of reference communicationLink.
     * @return Value of reference communicationLink.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Link getCommunicationLink();
    /**
     * Sets the value of reference communicationLink. See {@link #getCommunicationLink} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setCommunicationLink(org.omg.uml.behavioralelements.commonbehavior.Link newValue);
    /**
     * Returns the value of reference dispatchAction.
     * @return Value of reference dispatchAction.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Action getDispatchAction();
    /**
     * Sets the value of reference dispatchAction. See {@link #getDispatchAction} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setDispatchAction(org.omg.uml.behavioralelements.commonbehavior.Action newValue);
}
