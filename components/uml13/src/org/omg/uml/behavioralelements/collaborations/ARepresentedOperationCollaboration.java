package org.omg.uml.behavioralelements.collaborations;

/**
 * A_representedOperation_collaboration association proxy interface.
 */
public interface ARepresentedOperationCollaboration extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param representedOperation Value of the first association end.
     * @param collaboration Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.Operation representedOperation, org.omg.uml.behavioralelements.collaborations.Collaboration collaboration);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param representedOperation Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.Operation getRepresentedOperation(org.omg.uml.behavioralelements.collaborations.Collaboration collaboration);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param collaboration Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getCollaboration(org.omg.uml.foundation.core.Operation representedOperation);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param representedOperation Value of the first association end.
     * @param collaboration Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.Operation representedOperation, org.omg.uml.behavioralelements.collaborations.Collaboration collaboration);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param representedOperation Value of the first association end.
     * @param collaboration Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.Operation representedOperation, org.omg.uml.behavioralelements.collaborations.Collaboration collaboration);
}
