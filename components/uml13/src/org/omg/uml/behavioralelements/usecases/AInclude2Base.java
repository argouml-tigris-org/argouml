package org.omg.uml.behavioralelements.usecases;

/**
 * A_include2_base association proxy interface.
 */
public interface AInclude2Base extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param include2 Value of the first association end.
     * @param base Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.usecases.Include include2, org.omg.uml.behavioralelements.usecases.UseCase base);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param include2 Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getInclude2(org.omg.uml.behavioralelements.usecases.UseCase base);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param base Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.usecases.UseCase getBase(org.omg.uml.behavioralelements.usecases.Include include2);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param include2 Value of the first association end.
     * @param base Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.usecases.Include include2, org.omg.uml.behavioralelements.usecases.UseCase base);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param include2 Value of the first association end.
     * @param base Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.usecases.Include include2, org.omg.uml.behavioralelements.usecases.UseCase base);
}
