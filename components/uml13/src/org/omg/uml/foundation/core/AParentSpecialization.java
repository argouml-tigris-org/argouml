package org.omg.uml.foundation.core;

/**
 * A_parent_specialization association proxy interface.
 */
public interface AParentSpecialization extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param parent Value of the first association end.
     * @param specialization Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.GeneralizableElement parent, org.omg.uml.foundation.core.Generalization specialization);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param parent Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.GeneralizableElement getParent(org.omg.uml.foundation.core.Generalization specialization);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param specialization Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getSpecialization(org.omg.uml.foundation.core.GeneralizableElement parent);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param parent Value of the first association end.
     * @param specialization Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.GeneralizableElement parent, org.omg.uml.foundation.core.Generalization specialization);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param parent Value of the first association end.
     * @param specialization Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.GeneralizableElement parent, org.omg.uml.foundation.core.Generalization specialization);
}
