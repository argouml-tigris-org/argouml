package org.omg.uml.behavioralelements.statemachines;

/**
 * A_state_deferrableEvent association proxy interface.
 */
public interface AStateDeferrableEvent extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param state Value of the first association end.
     * @param deferrableEvent Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.statemachines.State state, org.omg.uml.behavioralelements.statemachines.Event deferrableEvent);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param state Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getState(org.omg.uml.behavioralelements.statemachines.Event deferrableEvent);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param deferrableEvent Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getDeferrableEvent(org.omg.uml.behavioralelements.statemachines.State state);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param state Value of the first association end.
     * @param deferrableEvent Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.statemachines.State state, org.omg.uml.behavioralelements.statemachines.Event deferrableEvent);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param state Value of the first association end.
     * @param deferrableEvent Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.statemachines.State state, org.omg.uml.behavioralelements.statemachines.Event deferrableEvent);
}
