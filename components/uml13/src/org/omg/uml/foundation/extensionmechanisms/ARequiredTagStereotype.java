package org.omg.uml.foundation.extensionmechanisms;

/**
 * A_requiredTag_stereotype association proxy interface.
 */
public interface ARequiredTagStereotype extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param requiredTag Value of the first association end.
     * @param stereotype Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.extensionmechanisms.TaggedValue requiredTag, org.omg.uml.foundation.extensionmechanisms.Stereotype stereotype);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param requiredTag Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getRequiredTag(org.omg.uml.foundation.extensionmechanisms.Stereotype stereotype);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param stereotype Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.extensionmechanisms.Stereotype getStereotype(org.omg.uml.foundation.extensionmechanisms.TaggedValue requiredTag);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param requiredTag Value of the first association end.
     * @param stereotype Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.extensionmechanisms.TaggedValue requiredTag, org.omg.uml.foundation.extensionmechanisms.Stereotype stereotype);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param requiredTag Value of the first association end.
     * @param stereotype Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.extensionmechanisms.TaggedValue requiredTag, org.omg.uml.foundation.extensionmechanisms.Stereotype stereotype);
}
