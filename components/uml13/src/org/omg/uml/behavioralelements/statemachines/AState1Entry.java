package org.omg.uml.behavioralelements.statemachines;

/**
 * A_state1_entry association proxy interface.
 */
public interface AState1Entry extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param state1 Value of the first association end.
     * @param entry Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.statemachines.State state1, org.omg.uml.behavioralelements.commonbehavior.Action entry);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param state1 Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.statemachines.State getState1(org.omg.uml.behavioralelements.commonbehavior.Action entry);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param entry Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Action getEntry(org.omg.uml.behavioralelements.statemachines.State state1);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param state1 Value of the first association end.
     * @param entry Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.statemachines.State state1, org.omg.uml.behavioralelements.commonbehavior.Action entry);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param state1 Value of the first association end.
     * @param entry Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.statemachines.State state1, org.omg.uml.behavioralelements.commonbehavior.Action entry);
}
