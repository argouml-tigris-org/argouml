package org.omg.uml.foundation.core;

/**
 * A_owner_feature association proxy interface.
 */
public interface AOwnerFeature extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param owner Value of the first association end.
     * @param feature Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.Classifier owner, org.omg.uml.foundation.core.Feature feature);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param owner Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.Classifier getOwner(org.omg.uml.foundation.core.Feature feature);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param feature Required value of the second association end.
     * @return List of related objects.
     */
    public java.util.List getFeature(org.omg.uml.foundation.core.Classifier owner);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param owner Value of the first association end.
     * @param feature Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.Classifier owner, org.omg.uml.foundation.core.Feature feature);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param owner Value of the first association end.
     * @param feature Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.Classifier owner, org.omg.uml.foundation.core.Feature feature);
}
