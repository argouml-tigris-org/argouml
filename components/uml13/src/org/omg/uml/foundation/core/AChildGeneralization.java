package org.omg.uml.foundation.core;

/**
 * A_child_generalization association proxy interface.
 */
public interface AChildGeneralization extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param child Value of the first association end.
     * @param generalization Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.GeneralizableElement child, org.omg.uml.foundation.core.Generalization generalization);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param child Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.GeneralizableElement getChild(org.omg.uml.foundation.core.Generalization generalization);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param generalization Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getGeneralization(org.omg.uml.foundation.core.GeneralizableElement child);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param child Value of the first association end.
     * @param generalization Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.GeneralizableElement child, org.omg.uml.foundation.core.Generalization generalization);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param child Value of the first association end.
     * @param generalization Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.GeneralizableElement child, org.omg.uml.foundation.core.Generalization generalization);
}
