package org.omg.uml.behavioralelements.statemachines;

/**
 * A_event_parameter association proxy interface.
 */
public interface AEventParameter extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param event Value of the first association end.
     * @param parameter Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.statemachines.Event event, org.omg.uml.foundation.core.Parameter parameter);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param event Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.statemachines.Event getEvent(org.omg.uml.foundation.core.Parameter parameter);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param parameter Required value of the second association end.
     * @return List of related objects.
     */
    public java.util.List getParameter(org.omg.uml.behavioralelements.statemachines.Event event);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param event Value of the first association end.
     * @param parameter Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.statemachines.Event event, org.omg.uml.foundation.core.Parameter parameter);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param event Value of the first association end.
     * @param parameter Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.statemachines.Event event, org.omg.uml.foundation.core.Parameter parameter);
}
