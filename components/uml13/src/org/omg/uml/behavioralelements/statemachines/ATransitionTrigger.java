package org.omg.uml.behavioralelements.statemachines;

/**
 * A_transition_trigger association proxy interface.
 */
public interface ATransitionTrigger extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param transition Value of the first association end.
     * @param trigger Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.statemachines.Transition transition, org.omg.uml.behavioralelements.statemachines.Event trigger);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param transition Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getTransition(org.omg.uml.behavioralelements.statemachines.Event trigger);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param trigger Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.statemachines.Event getTrigger(org.omg.uml.behavioralelements.statemachines.Transition transition);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param transition Value of the first association end.
     * @param trigger Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.statemachines.Transition transition, org.omg.uml.behavioralelements.statemachines.Event trigger);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param transition Value of the first association end.
     * @param trigger Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.statemachines.Transition transition, org.omg.uml.behavioralelements.statemachines.Event trigger);
}
