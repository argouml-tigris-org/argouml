package org.omg.uml.behavioralelements.commonbehavior;

/**
 * A_link_connection association proxy interface.
 */
public interface ALinkConnection extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param link Value of the first association end.
     * @param connection Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.commonbehavior.Link link, org.omg.uml.behavioralelements.commonbehavior.LinkEnd connection);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param link Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Link getLink(org.omg.uml.behavioralelements.commonbehavior.LinkEnd connection);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param connection Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getConnection(org.omg.uml.behavioralelements.commonbehavior.Link link);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param link Value of the first association end.
     * @param connection Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.commonbehavior.Link link, org.omg.uml.behavioralelements.commonbehavior.LinkEnd connection);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param link Value of the first association end.
     * @param connection Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.commonbehavior.Link link, org.omg.uml.behavioralelements.commonbehavior.LinkEnd connection);
}
