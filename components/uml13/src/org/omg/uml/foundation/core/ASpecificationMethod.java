package org.omg.uml.foundation.core;

/**
 * A_specification_method association proxy interface.
 */
public interface ASpecificationMethod extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param specification Value of the first association end.
     * @param method Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.Operation specification, org.omg.uml.foundation.core.Method method);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param specification Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.Operation getSpecification(org.omg.uml.foundation.core.Method method);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param method Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getMethod(org.omg.uml.foundation.core.Operation specification);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param specification Value of the first association end.
     * @param method Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.Operation specification, org.omg.uml.foundation.core.Method method);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param specification Value of the first association end.
     * @param method Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.Operation specification, org.omg.uml.foundation.core.Method method);
}
