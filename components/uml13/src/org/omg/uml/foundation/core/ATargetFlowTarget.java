package org.omg.uml.foundation.core;

/**
 * A_targetFlow_target association proxy interface.
 */
public interface ATargetFlowTarget extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param targetFlow Value of the first association end.
     * @param target Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.Flow targetFlow, org.omg.uml.foundation.core.ModelElement target);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param targetFlow Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getTargetFlow(org.omg.uml.foundation.core.ModelElement target);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param target Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getTarget(org.omg.uml.foundation.core.Flow targetFlow);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param targetFlow Value of the first association end.
     * @param target Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.Flow targetFlow, org.omg.uml.foundation.core.ModelElement target);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param targetFlow Value of the first association end.
     * @param target Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.Flow targetFlow, org.omg.uml.foundation.core.ModelElement target);
}
