package org.omg.uml.behavioralelements.activitygraphs;

/**
 * A_classifierInState_inState association proxy interface.
 */
public interface AClassifierInStateInState extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param classifierInState Value of the first association end.
     * @param inState Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.activitygraphs.ClassifierInState classifierInState, org.omg.uml.behavioralelements.statemachines.State inState);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param classifierInState Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getClassifierInState(org.omg.uml.behavioralelements.statemachines.State inState);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param inState Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getInState(org.omg.uml.behavioralelements.activitygraphs.ClassifierInState classifierInState);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param classifierInState Value of the first association end.
     * @param inState Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.activitygraphs.ClassifierInState classifierInState, org.omg.uml.behavioralelements.statemachines.State inState);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param classifierInState Value of the first association end.
     * @param inState Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.activitygraphs.ClassifierInState classifierInState, org.omg.uml.behavioralelements.statemachines.State inState);
}
