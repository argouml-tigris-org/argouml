package org.omg.uml.behavioralelements.usecases;

/**
 * A_base_extend2 association proxy interface.
 */
public interface ABaseExtend2 extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param base Value of the first association end.
     * @param extend2 Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.usecases.UseCase base, org.omg.uml.behavioralelements.usecases.Extend extend2);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param base Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.usecases.UseCase getBase(org.omg.uml.behavioralelements.usecases.Extend extend2);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param extend2 Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getExtend2(org.omg.uml.behavioralelements.usecases.UseCase base);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param base Value of the first association end.
     * @param extend2 Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.usecases.UseCase base, org.omg.uml.behavioralelements.usecases.Extend extend2);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param base Value of the first association end.
     * @param extend2 Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.usecases.UseCase base, org.omg.uml.behavioralelements.usecases.Extend extend2);
}
