package org.omg.uml.foundation.core;

/**
 * A_sourceFlow_source association proxy interface.
 */
public interface ASourceFlowSource extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param sourceFlow Value of the first association end.
     * @param source Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.Flow sourceFlow, org.omg.uml.foundation.core.ModelElement source);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param sourceFlow Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getSourceFlow(org.omg.uml.foundation.core.ModelElement source);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param source Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getSource(org.omg.uml.foundation.core.Flow sourceFlow);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param sourceFlow Value of the first association end.
     * @param source Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.Flow sourceFlow, org.omg.uml.foundation.core.ModelElement source);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param sourceFlow Value of the first association end.
     * @param source Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.Flow sourceFlow, org.omg.uml.foundation.core.ModelElement source);
}
