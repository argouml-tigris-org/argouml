// Copyright (c) 1996-2002 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
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

package org.argouml.model.uml.behavioralelements.activitygraphs;

import org.argouml.model.uml.AbstractUmlModelFactory;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.activity_graphs.MActionState;
import ru.novosoft.uml.behavior.activity_graphs.MActivityGraph;
import ru.novosoft.uml.behavior.activity_graphs.MCallState;
import ru.novosoft.uml.behavior.activity_graphs.MClassifierInState;
import ru.novosoft.uml.behavior.activity_graphs.MObjectFlowState;
import ru.novosoft.uml.behavior.activity_graphs.MPartition;
import ru.novosoft.uml.behavior.activity_graphs.MSubactivityState;

/**
 * Factory to create UML classes for the UML
 * BehaviorialElements::ActivityGraphs package.
 * 
 * This class contains all create, remove and build methods for ActivityGraphs 
 * modelelements.
 * Create methods create an empty modelelement. It is registred with the 
 * eventpump however. Remove methods remove a modelelement including the listener.
 * Build methods create a modelelement but also instantiate the modelelement, 
 * for example with defaults.
 * 
 * Helper methods for ActivityGraphs should not be placed here. Helper methods are methods
 * like getReturnParameters. These should be placed in ActivityGraphsHelper 
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @author jaap.branderhorst@xs4all.nl
 * 
 * @see org.argouml.model.uml.behavioralelements.activitygraphs.ActivityGraphsHelper
 * @see org.argouml.model.uml.UmlFactory
 */
public class ActivityGraphsFactory extends AbstractUmlModelFactory {

    /** Singleton instance.
     */
    private static ActivityGraphsFactory SINGLETON =
                   new ActivityGraphsFactory();

    /** Singleton instance access method.
     */
    public static ActivityGraphsFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private ActivityGraphsFactory() {
    }

    /** Create an empty but initialized instance of a UML ActionState.
     *  
     *  @return an initialized UML ActionState instance.
     */
    public MActionState createActionState() {
        MActionState modelElement = MFactory.getDefaultFactory().createActionState();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML ActivityGraph.
     *  
     *  @return an initialized UML ActivityGraph instance.
     */
    public MActivityGraph createActivityGraph() {
        MActivityGraph modelElement = MFactory.getDefaultFactory().createActivityGraph();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML CallState.
     *  
     *  @return an initialized UML CallState instance.
     */
    public MCallState createCallState() {
        MCallState modelElement = MFactory.getDefaultFactory().createCallState();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML ClassifierInState.
     *  
     *  @return an initialized UML ClassifierInState instance.
     */
    public MClassifierInState createClassifierInState() {
        MClassifierInState modelElement = MFactory.getDefaultFactory().createClassifierInState();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML ObjectFlowState.
     *  
     *  @return an initialized UML ObjectFlowState instance.
     */
    public MObjectFlowState createObjectFlowState() {
        MObjectFlowState modelElement = MFactory.getDefaultFactory().createObjectFlowState();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Partition.
     *  
     *  @return an initialized UML Partition instance.
     */
    public MPartition createPartition() {
        MPartition modelElement = MFactory.getDefaultFactory().createPartition();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML SubactivityState.
     *  
     *  @return an initialized UML SubactivityState instance.
     */
    public MSubactivityState createSubactivityState() {
        MSubactivityState modelElement = MFactory.getDefaultFactory().createSubactivityState();
	super.initialize(modelElement);
	return modelElement;
    }
    
    /** Remove an instance of a UML ActionState
     */
    public void  removeActionState(MActionState modelelement) {
    	modelelement.remove();
    }
    
    /** Remove an instance of a UML ActivityGraph
     */
    public void  removeActivityGraph(MActivityGraph modelelement) {
    	modelelement.remove();
    }
    
    /** Remove an instance of a UML CallState
     */
    public void  removeCallState(MCallState modelelement) {
    	modelelement.remove();
    }
    
    /** Remove an instance of a UML ClassifierInState
     */
    public void  removeClassifierInState(MClassifierInState modelelement) {
    	modelelement.remove();
    }
    
    /** Remove an instance of a UML ObjectFlowState
     */
    public void  removeObjectFlowState(MObjectFlowState modelelement) {
    	modelelement.remove();
    }
    
    /** Remove an instance of a UML Partition
     */
    public void  removePartition(MPartition modelelement) {
    	modelelement.remove();
    }
    
    /** Remove an instance of a UML SubactivityState
     */
    public void  removeSubactivityState(MSubactivityState modelelement) {
    	modelelement.remove();
    }

}

