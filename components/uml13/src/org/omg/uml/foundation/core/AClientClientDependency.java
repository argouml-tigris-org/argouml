package org.omg.uml.foundation.core;

/**
 * A_client_clientDependency association proxy interface.
 */
public interface AClientClientDependency extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param client Value of the first association end.
     * @param clientDependency Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.ModelElement client, org.omg.uml.foundation.core.Dependency clientDependency);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param client Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getClient(org.omg.uml.foundation.core.Dependency clientDependency);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param clientDependency Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getClientDependency(org.omg.uml.foundation.core.ModelElement client);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param client Value of the first association end.
     * @param clientDependency Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.ModelElement client, org.omg.uml.foundation.core.Dependency clientDependency);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param client Value of the first association end.
     * @param clientDependency Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.ModelElement client, org.omg.uml.foundation.core.Dependency clientDependency);
}
