package org.omg.uml.behavioralelements.commonbehavior;

/**
 * A_actualArgument_action association proxy interface.
 */
public interface AActualArgumentAction extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param actualArgument Value of the first association end.
     * @param action Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.commonbehavior.Argument actualArgument, org.omg.uml.behavioralelements.commonbehavior.Action action);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param actualArgument Required value of the first association end.
     * @return List of related objects.
     */
    public java.util.List getActualArgument(org.omg.uml.behavioralelements.commonbehavior.Action action);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param action Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Action getAction(org.omg.uml.behavioralelements.commonbehavior.Argument actualArgument);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param actualArgument Value of the first association end.
     * @param action Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.commonbehavior.Argument actualArgument, org.omg.uml.behavioralelements.commonbehavior.Action action);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param actualArgument Value of the first association end.
     * @param action Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.commonbehavior.Argument actualArgument, org.omg.uml.behavioralelements.commonbehavior.Action action);
}
