package org.omg.uml.behavioralelements.collaborations;

/**
 * A_predecessor_message3 association proxy interface.
 */
public interface APredecessorMessage3 extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param predecessor Value of the first association end.
     * @param message3 Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.collaborations.Message predecessor, org.omg.uml.behavioralelements.collaborations.Message message3);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param predecessor Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getPredecessor(org.omg.uml.behavioralelements.collaborations.Message message3);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param message3 Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getMessage3(org.omg.uml.behavioralelements.collaborations.Message predecessor);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param predecessor Value of the first association end.
     * @param message3 Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.collaborations.Message predecessor, org.omg.uml.behavioralelements.collaborations.Message message3);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param predecessor Value of the first association end.
     * @param message3 Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.collaborations.Message predecessor, org.omg.uml.behavioralelements.collaborations.Message message3);
}
