package org.omg.uml.behavioralelements.usecases;

/**
 * A_extensionPoint_useCase association proxy interface.
 */
public interface AExtensionPointUseCase extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param extensionPoint Value of the first association end.
     * @param useCase Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.usecases.ExtensionPoint extensionPoint, org.omg.uml.behavioralelements.usecases.UseCase useCase);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param extensionPoint Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getExtensionPoint(org.omg.uml.behavioralelements.usecases.UseCase useCase);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param useCase Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.usecases.UseCase getUseCase(org.omg.uml.behavioralelements.usecases.ExtensionPoint extensionPoint);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param extensionPoint Value of the first association end.
     * @param useCase Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.usecases.ExtensionPoint extensionPoint, org.omg.uml.behavioralelements.usecases.UseCase useCase);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param extensionPoint Value of the first association end.
     * @param useCase Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.usecases.ExtensionPoint extensionPoint, org.omg.uml.behavioralelements.usecases.UseCase useCase);
}
