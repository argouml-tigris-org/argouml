package org.omg.uml.behavioralelements.statemachines;

/**
 * A_signal_occurrence association proxy interface.
 */
public interface ASignalOccurrence extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param signal Value of the first association end.
     * @param occurrence Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.commonbehavior.Signal signal, org.omg.uml.behavioralelements.statemachines.SignalEvent occurrence);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param signal Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Signal getSignal(org.omg.uml.behavioralelements.statemachines.SignalEvent occurrence);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param occurrence Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getOccurrence(org.omg.uml.behavioralelements.commonbehavior.Signal signal);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param signal Value of the first association end.
     * @param occurrence Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.commonbehavior.Signal signal, org.omg.uml.behavioralelements.statemachines.SignalEvent occurrence);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param signal Value of the first association end.
     * @param occurrence Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.commonbehavior.Signal signal, org.omg.uml.behavioralelements.statemachines.SignalEvent occurrence);
}
