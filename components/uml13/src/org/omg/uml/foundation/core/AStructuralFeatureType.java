package org.omg.uml.foundation.core;

/**
 * A_structuralFeature_type association proxy interface.
 */
public interface AStructuralFeatureType extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param structuralFeature Value of the first association end.
     * @param type Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.StructuralFeature structuralFeature, org.omg.uml.foundation.core.Classifier type);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param type Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.Classifier getType(org.omg.uml.foundation.core.StructuralFeature structuralFeature);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param structuralFeature Value of the first association end.
     * @param type Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.StructuralFeature structuralFeature, org.omg.uml.foundation.core.Classifier type);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param structuralFeature Value of the first association end.
     * @param type Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.StructuralFeature structuralFeature, org.omg.uml.foundation.core.Classifier type);
}
