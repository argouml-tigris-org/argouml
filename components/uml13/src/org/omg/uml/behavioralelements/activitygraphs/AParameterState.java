package org.omg.uml.behavioralelements.activitygraphs;

/**
 * A_parameter_state association proxy interface.
 */
public interface AParameterState extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param parameter Value of the first association end.
     * @param state Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.Parameter parameter, org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState state);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param parameter Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getParameter(org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState state);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param state Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getState(org.omg.uml.foundation.core.Parameter parameter);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param parameter Value of the first association end.
     * @param state Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.Parameter parameter, org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState state);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param parameter Value of the first association end.
     * @param state Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.Parameter parameter, org.omg.uml.behavioralelements.activitygraphs.ObjectFlowState state);
}
