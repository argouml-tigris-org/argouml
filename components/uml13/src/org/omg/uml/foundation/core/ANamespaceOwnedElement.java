package org.omg.uml.foundation.core;

/**
 * A_namespace_ownedElement association proxy interface.
 */
public interface ANamespaceOwnedElement extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param namespace Value of the first association end.
     * @param ownedElement Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.Namespace namespace, org.omg.uml.foundation.core.ModelElement ownedElement);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param namespace Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.Namespace getNamespace(org.omg.uml.foundation.core.ModelElement ownedElement);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param ownedElement Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getOwnedElement(org.omg.uml.foundation.core.Namespace namespace);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param namespace Value of the first association end.
     * @param ownedElement Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.Namespace namespace, org.omg.uml.foundation.core.ModelElement ownedElement);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param namespace Value of the first association end.
     * @param ownedElement Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.Namespace namespace, org.omg.uml.foundation.core.ModelElement ownedElement);
}
