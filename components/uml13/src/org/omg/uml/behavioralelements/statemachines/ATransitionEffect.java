package org.omg.uml.behavioralelements.statemachines;

/**
 * A_transition_effect association proxy interface.
 */
public interface ATransitionEffect extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param transition Value of the first association end.
     * @param effect Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.statemachines.Transition transition, org.omg.uml.behavioralelements.commonbehavior.Action effect);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param transition Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.statemachines.Transition getTransition(org.omg.uml.behavioralelements.commonbehavior.Action effect);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param effect Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Action getEffect(org.omg.uml.behavioralelements.statemachines.Transition transition);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param transition Value of the first association end.
     * @param effect Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.statemachines.Transition transition, org.omg.uml.behavioralelements.commonbehavior.Action effect);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param transition Value of the first association end.
     * @param effect Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.statemachines.Transition transition, org.omg.uml.behavioralelements.commonbehavior.Action effect);
}
