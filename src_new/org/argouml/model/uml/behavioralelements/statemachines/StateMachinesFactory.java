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

package org.argouml.model.uml.behavioralelements.statemachines;

import org.argouml.model.uml.AbstractModelFactory;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.state_machines.MCallEvent;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MGuard;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.behavior.state_machines.MTransition;

/**
 * Factory to create UML classes for the UML
 * BehaviorialElements::StateMachines package.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
public class StateMachinesFactory extends AbstractModelFactory {

    /** Singleton instance.
     */
    private static StateMachinesFactory SINGLETON =
                   new StateMachinesFactory();

    /** Singleton instance access method.
     */
    public static StateMachinesFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private StateMachinesFactory() {
    }

    /** Create an empty but initialized instance of a UML CallEvent.
     *  
     *  @return an initialized UML CallEvent instance.
     */
    public MCallEvent createCallEvent() {
        MCallEvent modelElement = MFactory.getDefaultFactory().createCallEvent();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML CompositeState.
     *  
     *  @return an initialized UML CompositeState instance.
     */
    public MCompositeState createCompositeState() {
        MCompositeState modelElement = MFactory.getDefaultFactory().createCompositeState();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Guard.
     *  
     *  @return an initialized UML Guard instance.
     */
    public MGuard createGuard() {
        MGuard modelElement = MFactory.getDefaultFactory().createGuard();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML State.
     *  
     *  @return an initialized UML State instance.
     */
    public MState createState() {
        MState modelElement = MFactory.getDefaultFactory().createState();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML StateMachine.
     *  
     *  @return an initialized UML StateMachine instance.
     */
    public MStateMachine createStateMachine() {
        MStateMachine modelElement = MFactory.getDefaultFactory().createStateMachine();
	super.initialize(modelElement);
	return modelElement;
    }

    /** Create an empty but initialized instance of a UML Transition.
     *  
     *  @return an initialized UML Transition instance.
     */
    public MTransition createTransition() {
        MTransition modelElement = MFactory.getDefaultFactory().createTransition();
	super.initialize(modelElement);
	return modelElement;
    }

}

