package org.omg.uml.behavioralelements.collaborations;

/**
 * A_classifierRole_availableContents association proxy interface.
 */
public interface AClassifierRoleAvailableContents extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param classifierRole Value of the first association end.
     * @param availableContents Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.collaborations.ClassifierRole classifierRole, org.omg.uml.foundation.core.ModelElement availableContents);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param classifierRole Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getClassifierRole(org.omg.uml.foundation.core.ModelElement availableContents);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param availableContents Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getAvailableContents(org.omg.uml.behavioralelements.collaborations.ClassifierRole classifierRole);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param classifierRole Value of the first association end.
     * @param availableContents Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.collaborations.ClassifierRole classifierRole, org.omg.uml.foundation.core.ModelElement availableContents);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param classifierRole Value of the first association end.
     * @param availableContents Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.collaborations.ClassifierRole classifierRole, org.omg.uml.foundation.core.ModelElement availableContents);
}
