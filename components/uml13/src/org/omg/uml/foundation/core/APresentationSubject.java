package org.omg.uml.foundation.core;

/**
 * A_presentation_subject association proxy interface.
 */
public interface APresentationSubject extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param presentation Value of the first association end.
     * @param subject Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.foundation.core.PresentationElement presentation, org.omg.uml.foundation.core.ModelElement subject);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param presentation Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getPresentation(org.omg.uml.foundation.core.ModelElement subject);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param subject Required value of the second association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getSubject(org.omg.uml.foundation.core.PresentationElement presentation);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param presentation Value of the first association end.
     * @param subject Value of the second association end.
     */
    public boolean add(org.omg.uml.foundation.core.PresentationElement presentation, org.omg.uml.foundation.core.ModelElement subject);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param presentation Value of the first association end.
     * @param subject Value of the second association end.
     */
    public boolean remove(org.omg.uml.foundation.core.PresentationElement presentation, org.omg.uml.foundation.core.ModelElement subject);
}
