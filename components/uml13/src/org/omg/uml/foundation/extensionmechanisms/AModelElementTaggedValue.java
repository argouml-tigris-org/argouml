package org.omg.uml.foundation.extensionmechanisms;

/**
 * A_modelElement_taggedValue association proxy interface.
 */
public interface AModelElementTaggedValue extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param modelElement Value of the first association end.
     * @param taggedValue Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.ModelElement modelElement, org.omg.uml.foundation.extensionmechanisms.TaggedValue taggedValue);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param modelElement Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.ModelElement getModelElement(org.omg.uml.foundation.extensionmechanisms.TaggedValue taggedValue);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param taggedValue Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getTaggedValue(org.omg.uml.foundation.core.ModelElement modelElement);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param modelElement Value of the first association end.
     * @param taggedValue Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.ModelElement modelElement, org.omg.uml.foundation.extensionmechanisms.TaggedValue taggedValue);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param modelElement Value of the first association end.
     * @param taggedValue Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.ModelElement modelElement, org.omg.uml.foundation.extensionmechanisms.TaggedValue taggedValue);
}
