package org.omg.uml.behavioralelements.commonbehavior;

/**
 * A_slot_instance association proxy interface.
 */
public interface ASlotInstance extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param slot Value of the first association end.
     * @param instance Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.commonbehavior.AttributeLink slot, org.omg.uml.behavioralelements.commonbehavior.Instance instance);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param slot Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getSlot(org.omg.uml.behavioralelements.commonbehavior.Instance instance);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param instance Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Instance getInstance(org.omg.uml.behavioralelements.commonbehavior.AttributeLink slot);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param slot Value of the first association end.
     * @param instance Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.commonbehavior.AttributeLink slot, org.omg.uml.behavioralelements.commonbehavior.Instance instance);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param slot Value of the first association end.
     * @param instance Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.commonbehavior.AttributeLink slot, org.omg.uml.behavioralelements.commonbehavior.Instance instance);
}
