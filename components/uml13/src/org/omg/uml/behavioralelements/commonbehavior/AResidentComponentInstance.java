package org.omg.uml.behavioralelements.commonbehavior;

/**
 * A_resident_componentInstance association proxy interface.
 */
public interface AResidentComponentInstance extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param resident Value of the first association end.
     * @param componentInstance Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.commonbehavior.Instance resident, org.omg.uml.behavioralelements.commonbehavior.ComponentInstance componentInstance);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param resident Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getResident(org.omg.uml.behavioralelements.commonbehavior.ComponentInstance componentInstance);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param componentInstance Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.commonbehavior.ComponentInstance getComponentInstance(org.omg.uml.behavioralelements.commonbehavior.Instance resident);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param resident Value of the first association end.
     * @param componentInstance Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.commonbehavior.Instance resident, org.omg.uml.behavioralelements.commonbehavior.ComponentInstance componentInstance);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param resident Value of the first association end.
     * @param componentInstance Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.commonbehavior.Instance resident, org.omg.uml.behavioralelements.commonbehavior.ComponentInstance componentInstance);
}
