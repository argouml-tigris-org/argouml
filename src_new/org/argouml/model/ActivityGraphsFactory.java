// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.model;


/**
 * The interface for the factory for ActivityGraphs.<p>
 *
 * Created from the old ActivityGraphsFactory.
 */
public interface ActivityGraphsFactory {
    /**
     * Create an empty but initialized instance of a UML ActionState.
     *
     * @return an initialized UML ActionState instance.
     */
    Object createActionState();

    /**
     * Create an empty but initialized instance of a UML ActivityGraph.
     *
     * @return an initialized UML ActivityGraph instance.
     */
    Object createActivityGraph();

    /**
     * Create an empty but initialized instance of a UML CallState.
     *
     * @return an initialized UML CallState instance.
     */
    Object createCallState();

    /**
     * Create an empty but initialized instance of a UML ClassifierInState.
     *
     * @return an initialized UML ClassifierInState instance.
     */
    Object createClassifierInState();

    /**
     * Create an empty but initialized instance of a UML ObjectFlowState.
     *
     * @return an initialized UML ObjectFlowState instance.
     */
    Object createObjectFlowState();

    /**
     * Create an empty but initialized instance of a UML Partition.
     *
     * @return an initialized UML Partition instance.
     */
    Object createPartition();

    /**
     * Create an empty but initialized instance of a UML SubactivityState.
     *
     * @return an initialized UML SubactivityState instance.
     */
    Object createSubactivityState();

    /**
     * Builds an activity graph owned by the given context.<p>
     *
     * @param theContext is a ModelElement that will own the graph.
     * @return the new MActivityGraph as Object
     */
    Object buildActivityGraph(Object theContext);

    /**
     * Builds an objectflowstate. The objectflowstate will be a subvertix of
     * the given compositestate. The parameter compositeState is of
     * type Object to decouple the factory and NSUML as much as
     * possible from the rest of ArgoUML.
     *
     * @author MVW
     * @param compositeState the given compositestate
     * @return Object the newly build objectflow state.
     */
    Object buildObjectFlowState(Object compositeState);

    /**
     * Builds a ClassifierInState. Links it to the 2 required objects:
     * the classifier that forms the type of this classifierInState,
     * and the state.
     *
     * @param classifier the classifier (type)
     * @param state the state (inState)
     * @return the newly build classifierInState
     */
    Object buildClassifierInState(Object classifier, Object state);

    /**
     * @param elem the ActionState to be deleted
     */
    void deleteActionState(Object elem);

    /**
     * @param elem the ActivityGraph to be deleted
     */
    void deleteActivityGraph(Object elem);

    /**
     * @param elem the CallState to be deleted
     */
    void deleteCallState(Object elem);

    /**
     * @param elem the ClassifierInState to be deleted
     */
    void deleteClassifierInState(Object elem);

    /**
     * @param elem ObjectFlowState
     */
    void deleteObjectFlowState(Object elem);

    /**
     * @param elem Partition
     */
    void deletePartition(Object elem);

    /**
     * @param elem SubactivityState
     */
    void deleteSubactivityState(Object elem);
}
