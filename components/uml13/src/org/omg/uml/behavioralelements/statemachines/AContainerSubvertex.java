package org.omg.uml.behavioralelements.statemachines;

/**
 * A_container_subvertex association proxy interface.
 */
public interface AContainerSubvertex extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param container Value of the first association end.
     * @param subvertex Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.statemachines.CompositeState container, org.omg.uml.behavioralelements.statemachines.StateVertex subvertex);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param container Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.statemachines.CompositeState getContainer(org.omg.uml.behavioralelements.statemachines.StateVertex subvertex);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param subvertex Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getSubvertex(org.omg.uml.behavioralelements.statemachines.CompositeState container);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param container Value of the first association end.
     * @param subvertex Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.statemachines.CompositeState container, org.omg.uml.behavioralelements.statemachines.StateVertex subvertex);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param container Value of the first association end.
     * @param subvertex Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.statemachines.CompositeState container, org.omg.uml.behavioralelements.statemachines.StateVertex subvertex);
}
