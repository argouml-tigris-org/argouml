package org.omg.uml.foundation.core;

/**
 * A_behavioralFeature_parameter association proxy interface.
 */
public interface ABehavioralFeatureParameter extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param behavioralFeature Value of the first association end.
     * @param parameter Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.BehavioralFeature behavioralFeature, org.omg.uml.foundation.core.Parameter parameter);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param behavioralFeature Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.BehavioralFeature getBehavioralFeature(org.omg.uml.foundation.core.Parameter parameter);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param parameter Required value of the second association end.
     * @return List of related objects.
     */
    public java.util.List getParameter(org.omg.uml.foundation.core.BehavioralFeature behavioralFeature);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param behavioralFeature Value of the first association end.
     * @param parameter Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.BehavioralFeature behavioralFeature, org.omg.uml.foundation.core.Parameter parameter);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param behavioralFeature Value of the first association end.
     * @param parameter Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.BehavioralFeature behavioralFeature, org.omg.uml.foundation.core.Parameter parameter);
}
