package org.omg.uml.foundation.core;

/**
 * A_association_connection association proxy interface.
 */
public interface AAssociationConnection extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param association Value of the first association end.
     * @param connection Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.UmlAssociation association, org.omg.uml.foundation.core.AssociationEnd connection);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param association Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.UmlAssociation getAssociation(org.omg.uml.foundation.core.AssociationEnd connection);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param connection Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getConnection(org.omg.uml.foundation.core.UmlAssociation association);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param association Value of the first association end.
     * @param connection Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.UmlAssociation association, org.omg.uml.foundation.core.AssociationEnd connection);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param association Value of the first association end.
     * @param connection Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.UmlAssociation association, org.omg.uml.foundation.core.AssociationEnd connection);
}
