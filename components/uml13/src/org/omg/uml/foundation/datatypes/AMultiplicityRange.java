package org.omg.uml.foundation.datatypes;

/**
 * A_multiplicity_range association proxy interface.
 */
public interface AMultiplicityRange extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param multiplicity Value of the first association end.
     * @param range Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.datatypes.Multiplicity multiplicity, org.omg.uml.foundation.datatypes.MultiplicityRange range);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param multiplicity Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.datatypes.Multiplicity getMultiplicity(org.omg.uml.foundation.datatypes.MultiplicityRange range);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param range Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getRange(org.omg.uml.foundation.datatypes.Multiplicity multiplicity);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param multiplicity Value of the first association end.
     * @param range Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.datatypes.Multiplicity multiplicity, org.omg.uml.foundation.datatypes.MultiplicityRange range);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param multiplicity Value of the first association end.
     * @param range Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.datatypes.Multiplicity multiplicity, org.omg.uml.foundation.datatypes.MultiplicityRange range);
}
