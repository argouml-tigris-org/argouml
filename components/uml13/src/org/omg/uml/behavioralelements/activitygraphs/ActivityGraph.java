package org.omg.uml.behavioralelements.activitygraphs;

/**
 * ActivityGraph object instance interface.
 */
public interface ActivityGraph extends org.omg.uml.behavioralelements.statemachines.StateMachine {
    /**
     * Returns the value of reference partition.
     * @return Value of reference partition.
     */
    public java.util.Collection getPartition();
}
