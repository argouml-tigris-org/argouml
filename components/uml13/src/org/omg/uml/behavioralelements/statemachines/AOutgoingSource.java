package org.omg.uml.behavioralelements.statemachines;

/**
 * A_outgoing_source association proxy interface.
 */
public interface AOutgoingSource extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param outgoing Value of the first association end.
     * @param source Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.statemachines.Transition outgoing, org.omg.uml.behavioralelements.statemachines.StateVertex source);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param outgoing Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getOutgoing(org.omg.uml.behavioralelements.statemachines.StateVertex source);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param source Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.statemachines.StateVertex getSource(org.omg.uml.behavioralelements.statemachines.Transition outgoing);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param outgoing Value of the first association end.
     * @param source Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.statemachines.Transition outgoing, org.omg.uml.behavioralelements.statemachines.StateVertex source);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param outgoing Value of the first association end.
     * @param source Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.statemachines.Transition outgoing, org.omg.uml.behavioralelements.statemachines.StateVertex source);
}
