package org.omg.uml.behavioralelements.commonbehavior;

/**
 * A_context_raisedSignal association proxy interface.
 */
public interface AContextRaisedSignal extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param context Value of the first association end.
     * @param raisedSignal Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.BehavioralFeature context, org.omg.uml.behavioralelements.commonbehavior.Signal raisedSignal);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param context Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getContext(org.omg.uml.behavioralelements.commonbehavior.Signal raisedSignal);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param raisedSignal Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getRaisedSignal(org.omg.uml.foundation.core.BehavioralFeature context);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param context Value of the first association end.
     * @param raisedSignal Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.BehavioralFeature context, org.omg.uml.behavioralelements.commonbehavior.Signal raisedSignal);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param context Value of the first association end.
     * @param raisedSignal Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.BehavioralFeature context, org.omg.uml.behavioralelements.commonbehavior.Signal raisedSignal);
}
