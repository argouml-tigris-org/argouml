package org.omg.uml.behavioralelements.activitygraphs;

/**
 * Partition object instance interface.
 */
public interface Partition extends org.omg.uml.foundation.core.ModelElement {
    /**
     * Returns the value of reference contents.
     * @return Value of reference contents.
     */
    public java.util.Collection getContents();
    /**
     * Returns the value of reference activityGraph.
     * @return Value of reference activityGraph.
     */
    public org.omg.uml.behavioralelements.activitygraphs.ActivityGraph getActivityGraph();
    /**
     * Sets the value of reference activityGraph. See {@link #getActivityGraph} 
     * for description on the reference.
     * @param newValue New value to be set.
     */
    public void setActivityGraph(org.omg.uml.behavioralelements.activitygraphs.ActivityGraph newValue);
}
