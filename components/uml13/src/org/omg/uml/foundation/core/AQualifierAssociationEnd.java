package org.omg.uml.foundation.core;

/**
 * A_qualifier_associationEnd association proxy interface.
 */
public interface AQualifierAssociationEnd extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param qualifier Value of the first association end.
     * @param associationEnd Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.Attribute qualifier, org.omg.uml.foundation.core.AssociationEnd associationEnd);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param qualifier Required value of the first association end.
     * @return List of related objects.
     */
    public java.util.List getQualifier(org.omg.uml.foundation.core.AssociationEnd associationEnd);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param associationEnd Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.AssociationEnd getAssociationEnd(org.omg.uml.foundation.core.Attribute qualifier);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param qualifier Value of the first association end.
     * @param associationEnd Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.Attribute qualifier, org.omg.uml.foundation.core.AssociationEnd associationEnd);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param qualifier Value of the first association end.
     * @param associationEnd Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.Attribute qualifier, org.omg.uml.foundation.core.AssociationEnd associationEnd);
}
