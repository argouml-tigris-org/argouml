package org.omg.uml.behavioralelements.statemachines;

/**
 * A_state2_exit association proxy interface.
 */
public interface AState2Exit extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param state2 Value of the first association end.
     * @param exit Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.statemachines.State state2, org.omg.uml.behavioralelements.commonbehavior.Action exit);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param state2 Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.statemachines.State getState2(org.omg.uml.behavioralelements.commonbehavior.Action exit);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param exit Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Action getExit(org.omg.uml.behavioralelements.statemachines.State state2);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param state2 Value of the first association end.
     * @param exit Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.statemachines.State state2, org.omg.uml.behavioralelements.commonbehavior.Action exit);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param state2 Value of the first association end.
     * @param exit Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.statemachines.State state2, org.omg.uml.behavioralelements.commonbehavior.Action exit);
}
