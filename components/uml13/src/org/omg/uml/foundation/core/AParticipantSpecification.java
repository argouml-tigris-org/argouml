package org.omg.uml.foundation.core;

/**
 * A_participant_specification association proxy interface.
 */
public interface AParticipantSpecification extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param participant Value of the first association end.
     * @param specification Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.AssociationEnd participant, org.omg.uml.foundation.core.Classifier specification);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param participant Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getParticipant(org.omg.uml.foundation.core.Classifier specification);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param specification Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getSpecification(org.omg.uml.foundation.core.AssociationEnd participant);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param participant Value of the first association end.
     * @param specification Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.AssociationEnd participant, org.omg.uml.foundation.core.Classifier specification);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param participant Value of the first association end.
     * @param specification Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.AssociationEnd participant, org.omg.uml.foundation.core.Classifier specification);
}
