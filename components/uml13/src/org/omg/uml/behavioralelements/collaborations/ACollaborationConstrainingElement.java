package org.omg.uml.behavioralelements.collaborations;

/**
 * A_collaboration_constrainingElement association proxy interface.
 */
public interface ACollaborationConstrainingElement extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param collaboration Value of the first association end.
     * @param constrainingElement Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.collaborations.Collaboration collaboration, org.omg.uml.foundation.core.ModelElement constrainingElement);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param collaboration Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getCollaboration(org.omg.uml.foundation.core.ModelElement constrainingElement);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param constrainingElement Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getConstrainingElement(org.omg.uml.behavioralelements.collaborations.Collaboration collaboration);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param collaboration Value of the first association end.
     * @param constrainingElement Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.collaborations.Collaboration collaboration, org.omg.uml.foundation.core.ModelElement constrainingElement);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param collaboration Value of the first association end.
     * @param constrainingElement Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.collaborations.Collaboration collaboration, org.omg.uml.foundation.core.ModelElement constrainingElement);
}
