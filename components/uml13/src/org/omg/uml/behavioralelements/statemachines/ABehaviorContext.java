package org.omg.uml.behavioralelements.statemachines;

/**
 * A_behavior_context association proxy interface.
 */
public interface ABehaviorContext extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param behavior Value of the first association end.
     * @param context Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.statemachines.StateMachine behavior, org.omg.uml.foundation.core.ModelElement context);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param behavior Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getBehavior(org.omg.uml.foundation.core.ModelElement context);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param context Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.ModelElement getContext(org.omg.uml.behavioralelements.statemachines.StateMachine behavior);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param behavior Value of the first association end.
     * @param context Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.statemachines.StateMachine behavior, org.omg.uml.foundation.core.ModelElement context);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param behavior Value of the first association end.
     * @param context Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.statemachines.StateMachine behavior, org.omg.uml.foundation.core.ModelElement context);
}
