package org.omg.uml.behavioralelements.activitygraphs;

/**
 * A_activityGraph_partition association proxy interface.
 */
public interface AActivityGraphPartition extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param activityGraph Value of the first association end.
     * @param partition Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.activitygraphs.ActivityGraph activityGraph, org.omg.uml.behavioralelements.activitygraphs.Partition partition);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param activityGraph Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.activitygraphs.ActivityGraph getActivityGraph(org.omg.uml.behavioralelements.activitygraphs.Partition partition);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param partition Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getPartition(org.omg.uml.behavioralelements.activitygraphs.ActivityGraph activityGraph);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param activityGraph Value of the first association end.
     * @param partition Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.activitygraphs.ActivityGraph activityGraph, org.omg.uml.behavioralelements.activitygraphs.Partition partition);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param activityGraph Value of the first association end.
     * @param partition Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.activitygraphs.ActivityGraph activityGraph, org.omg.uml.behavioralelements.activitygraphs.Partition partition);
}
