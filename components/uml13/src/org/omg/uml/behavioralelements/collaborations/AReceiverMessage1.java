package org.omg.uml.behavioralelements.collaborations;

/**
 * A_receiver_message1 association proxy interface.
 */
public interface AReceiverMessage1 extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param receiver Value of the first association end.
     * @param message1 Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.collaborations.ClassifierRole receiver, org.omg.uml.behavioralelements.collaborations.Message message1);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param receiver Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.collaborations.ClassifierRole getReceiver(org.omg.uml.behavioralelements.collaborations.Message message1);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param message1 Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getMessage1(org.omg.uml.behavioralelements.collaborations.ClassifierRole receiver);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param receiver Value of the first association end.
     * @param message1 Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.collaborations.ClassifierRole receiver, org.omg.uml.behavioralelements.collaborations.Message message1);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param receiver Value of the first association end.
     * @param message1 Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.collaborations.ClassifierRole receiver, org.omg.uml.behavioralelements.collaborations.Message message1);
}
