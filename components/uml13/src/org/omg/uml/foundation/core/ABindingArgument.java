package org.omg.uml.foundation.core;

/**
 * A_binding_argument association proxy interface.
 */
public interface ABindingArgument extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param binding Value of the first association end.
     * @param argument Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.Binding binding, org.omg.uml.foundation.core.ModelElement argument);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param binding Required value of the first association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.foundation.core.Binding getBinding(org.omg.uml.foundation.core.ModelElement argument);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param argument Required value of the second association end.
     * @return List of related objects.
     */
    public java.util.List getArgument(org.omg.uml.foundation.core.Binding binding);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param binding Value of the first association end.
     * @param argument Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.Binding binding, org.omg.uml.foundation.core.ModelElement argument);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param binding Value of the first association end.
     * @param argument Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.Binding binding, org.omg.uml.foundation.core.ModelElement argument);
}
