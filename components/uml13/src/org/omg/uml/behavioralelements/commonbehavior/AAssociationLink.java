package org.omg.uml.behavioralelements.commonbehavior;

/**
 * A_association_link association proxy interface.
 */
public interface AAssociationLink extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param association Value of the first association end.
     * @param link Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.UmlAssociation association, org.omg.uml.behavioralelements.commonbehavior.Link link);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param association Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.UmlAssociation getAssociation(org.omg.uml.behavioralelements.commonbehavior.Link link);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param link Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getLink(org.omg.uml.foundation.core.UmlAssociation association);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param association Value of the first association end.
     * @param link Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.UmlAssociation association, org.omg.uml.behavioralelements.commonbehavior.Link link);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param association Value of the first association end.
     * @param link Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.UmlAssociation association, org.omg.uml.behavioralelements.commonbehavior.Link link);
}
