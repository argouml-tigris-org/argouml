package org.omg.uml.behavioralelements.collaborations;

/**
 * A_base_associationEndRole association proxy interface.
 */
public interface ABaseAssociationEndRole extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param base Value of the first association end.
     * @param associationEndRole Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.AssociationEnd base, org.omg.uml.behavioralelements.collaborations.AssociationEndRole associationEndRole);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param base Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.AssociationEnd getBase(org.omg.uml.behavioralelements.collaborations.AssociationEndRole associationEndRole);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param associationEndRole Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getAssociationEndRole(org.omg.uml.foundation.core.AssociationEnd base);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param base Value of the first association end.
     * @param associationEndRole Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.AssociationEnd base, org.omg.uml.behavioralelements.collaborations.AssociationEndRole associationEndRole);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param base Value of the first association end.
     * @param associationEndRole Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.AssociationEnd base, org.omg.uml.behavioralelements.collaborations.AssociationEndRole associationEndRole);
}
