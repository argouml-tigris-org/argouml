package org.omg.uml.behavioralelements.activitygraphs;

/**
 * A_contents_partition association proxy interface.
 */
public interface AContentsPartition extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param contents Value of the first association end.
     * @param partition Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.ModelElement contents, org.omg.uml.behavioralelements.activitygraphs.Partition partition);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param contents Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getContents(org.omg.uml.behavioralelements.activitygraphs.Partition partition);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param partition Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getPartition(org.omg.uml.foundation.core.ModelElement contents);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param contents Value of the first association end.
     * @param partition Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.ModelElement contents, org.omg.uml.behavioralelements.activitygraphs.Partition partition);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param contents Value of the first association end.
     * @param partition Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.ModelElement contents, org.omg.uml.behavioralelements.activitygraphs.Partition partition);
}
