package org.omg.uml.behavioralelements.usecases;

/**
 * A_extensionPoint_extend association proxy interface.
 */
public interface AExtensionPointExtend extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param extensionPoint Value of the first association end.
     * @param extend Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.usecases.ExtensionPoint extensionPoint, org.omg.uml.behavioralelements.usecases.Extend extend);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param extensionPoint Required value of the first association end.
     * @return List of related objects.
     */
    public java.util.List getExtensionPoint(org.omg.uml.behavioralelements.usecases.Extend extend);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param extend Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getExtend(org.omg.uml.behavioralelements.usecases.ExtensionPoint extensionPoint);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param extensionPoint Value of the first association end.
     * @param extend Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.usecases.ExtensionPoint extensionPoint, org.omg.uml.behavioralelements.usecases.Extend extend);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param extensionPoint Value of the first association end.
     * @param extend Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.usecases.ExtensionPoint extensionPoint, org.omg.uml.behavioralelements.usecases.Extend extend);
}
