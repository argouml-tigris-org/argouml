package org.omg.uml.behavioralelements.commonbehavior;

/**
 * A_stimulus_communicationLink association proxy interface.
 */
public interface AStimulusCommunicationLink extends javax.jmi.reflect.RefAssociation {
    /**
     * Queries whether a link currently exists between a given pair of instance 
     * objects in the associations link set.
     * @param stimulus Value of the first association end.
     * @param communicationLink Value of the second association end.
     * @return Returns true if the queried link exists.
     */
    public boolean exists(org.omg.uml.behavioralelements.commonbehavior.Stimulus stimulus, org.omg.uml.behavioralelements.commonbehavior.Link communicationLink);
    /**
     * Queries the instance objects that are related to a particular instance 
     * object by a link in the current associations link set.
     * @param stimulus Required value of the first association end.
     * @return Collection of related objects.
     */
    public java.util.Collection getStimulus(org.omg.uml.behavioralelements.commonbehavior.Link communicationLink);
    /**
     * Queries the instance object that is related to a particular instance object 
     * by a link in the current associations link set.
     * @param communicationLink Required value of the second association end.
     * @return Related object or <code>null</code> if none exists.
     */
    public org.omg.uml.behavioralelements.commonbehavior.Link getCommunicationLink(org.omg.uml.behavioralelements.commonbehavior.Stimulus stimulus);
    /**
     * Creates a link between the pair of instance objects in the associations 
     * link set.
     * @param stimulus Value of the first association end.
     * @param communicationLink Value of the second association end.
     */
    public boolean add(org.omg.uml.behavioralelements.commonbehavior.Stimulus stimulus, org.omg.uml.behavioralelements.commonbehavior.Link communicationLink);
    /**
     * Removes a link between a pair of instance objects in the current associations 
     * link set.
     * @param stimulus Value of the first association end.
     * @param communicationLink Value of the second association end.
     */
    public boolean remove(org.omg.uml.behavioralelements.commonbehavior.Stimulus stimulus, org.omg.uml.behavioralelements.commonbehavior.Link communicationLink);
}
