package org.omg.uml.behavioralelements.collaborations;

/**
 * A_associationEndRole_availableQualifier association proxy interface.
 */
public interface AAssociationEndRoleAvailableQualifier extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param associationEndRole Value of the first association end.
     * @param availableQualifier Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.collaborations.AssociationEndRole associationEndRole, org.omg.uml.foundation.core.Attribute availableQualifier);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param associationEndRole Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getAssociationEndRole(org.omg.uml.foundation.core.Attribute availableQualifier);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param availableQualifier Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getAvailableQualifier(org.omg.uml.behavioralelements.collaborations.AssociationEndRole associationEndRole);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param associationEndRole Value of the first association end.
     * @param availableQualifier Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.collaborations.AssociationEndRole associationEndRole, org.omg.uml.foundation.core.Attribute availableQualifier);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param associationEndRole Value of the first association end.
     * @param availableQualifier Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.collaborations.AssociationEndRole associationEndRole, org.omg.uml.foundation.core.Attribute availableQualifier);
}
