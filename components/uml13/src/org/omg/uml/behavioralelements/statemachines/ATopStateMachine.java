package org.omg.uml.behavioralelements.statemachines;

/**
 * A_top_stateMachine association proxy interface.
 */
public interface ATopStateMachine extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param top Value of the first association end.
     * @param stateMachine Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.statemachines.State top, org.omg.uml.behavioralelements.statemachines.StateMachine stateMachine);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param top Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.statemachines.State getTop(org.omg.uml.behavioralelements.statemachines.StateMachine stateMachine);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param stateMachine Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.statemachines.StateMachine getStateMachine(org.omg.uml.behavioralelements.statemachines.State top);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param top Value of the first association end.
     * @param stateMachine Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.statemachines.State top, org.omg.uml.behavioralelements.statemachines.StateMachine stateMachine);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param top Value of the first association end.
     * @param stateMachine Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.statemachines.State top, org.omg.uml.behavioralelements.statemachines.StateMachine stateMachine);
}
