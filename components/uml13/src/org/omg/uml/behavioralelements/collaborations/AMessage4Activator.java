package org.omg.uml.behavioralelements.collaborations;

/**
 * A_message4_activator association proxy interface.
 */
public interface AMessage4Activator extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param message4 Value of the first association end.
     * @param activator Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.collaborations.Message message4, org.omg.uml.behavioralelements.collaborations.Message activator);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param message4 Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getMessage4(org.omg.uml.behavioralelements.collaborations.Message activator);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param activator Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.collaborations.Message getActivator(org.omg.uml.behavioralelements.collaborations.Message message4);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param message4 Value of the first association end.
     * @param activator Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.collaborations.Message message4, org.omg.uml.behavioralelements.collaborations.Message activator);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param message4 Value of the first association end.
     * @param activator Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.collaborations.Message message4, org.omg.uml.behavioralelements.collaborations.Message activator);
}
