package org.omg.uml.behavioralelements.collaborations;

/**
 * A_message_communicationConnection association proxy interface.
 */
public interface AMessageCommunicationConnection extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param message Value of the first association end.
     * @param communicationConnection Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.collaborations.Message message, org.omg.uml.behavioralelements.collaborations.AssociationRole communicationConnection);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param message Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getMessage(org.omg.uml.behavioralelements.collaborations.AssociationRole communicationConnection);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param communicationConnection Required value of the second association 
     * end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.collaborations.AssociationRole getCommunicationConnection(org.omg.uml.behavioralelements.collaborations.Message message);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param message Value of the first association end.
     * @param communicationConnection Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.collaborations.Message message, org.omg.uml.behavioralelements.collaborations.AssociationRole communicationConnection);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param message Value of the first association end.
     * @param communicationConnection Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.collaborations.Message message, org.omg.uml.behavioralelements.collaborations.AssociationRole communicationConnection);
}
