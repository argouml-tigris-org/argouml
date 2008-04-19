// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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
 * The interface for the factory of the CommonBehavior.
 */
public interface CommonBehaviorFactory extends Factory {

    /**
     * Create an empty but initialized instance of a UML ActionSequence.
     *
     * @return an initialized UML ActionSequence instance.
     */
    Object createActionSequence();

    /**
     * Create an empty but initialized instance of a UML Argument.
     *
     * @return an initialized UML Argument instance.
     */
    Object createArgument();

    /**
     * Create an empty but initialized instance of a UML AttributeLink.
     *
     * @return an initialized UML AttributeLink instance.
     */
    Object createAttributeLink();

    /**
     * Create an empty but initialized instance of a UML CallAction.
     *
     * @return an initialized UML CallAction instance.
     */
    Object createCallAction();

    /**
     * Create an empty but initialized instance of a UML ComponentInstance.
     *
     * @return an initialized UML ComponentInstance instance.
     */
    Object createComponentInstance();

    /**
     * Create an empty but initialized instance of a UML CreateAction.
     *
     * @return an initialized UML CreateAction instance.
     */
    Object createCreateAction();
    
    /**
     * Create an empty but initialized instance of a UML DataValue.
     * 
     * @since Argo 0.21.1 - UML 1.3 type, but not introduced to Model interface
     *        until after UML 1.4 work was done.
     * @return an initialized UML DataValue instance.
     */
    Object createDataValue();

    /**
     * Create an empty but initialized instance of a UML DestroyAction.
     *
     * @return an initialized UML DestroyAction instance.
     */
    Object createDestroyAction();

    /**
     * Create an empty but initialized instance of a UML Exception.
     *
     * @return an initialized UML Exception instance.
     */
    Object createException();

    /**
     * Create an empty but initialized instance of a UML Link.
     *
     * @return an initialized UML Link instance.
     */
    Object createLink();

    /**
     * Create an empty but initialized instance of a UML LinkEnd.
     *
     * @return an initialized UML LinkEnd instance.
     */
    Object createLinkEnd();

    /**
     * Create an empty but initialized instance of a UML LinkObject.
     *
     * @return an initialized UML LinkObject instance.
     */
    Object createLinkObject();

    /**
     * Create an empty but initialized instance of a UML NodeInstance.
     *
     * @return an initialized UML NodeInstance instance.
     */
    Object createNodeInstance();

    /**
     * Create an empty but initialized instance of a UML Object.
     *
     * @return an initialized UML Object instance.
     */
    Object createObject();

    /**
     * Create an empty but initialized instance of a UML Reception.
     *
     * @return an initialized UML Reception instance.
     */
    Object createReception();

    /**
     * Create an empty but initialized instance of a UML ReturnAction.
     *
     * @return an initialized UML ReturnAction instance.
     */
    Object createReturnAction();

    /**
     * Create an empty but initialized instance of a UML SendAction.
     *
     * @return an initialized UML SendAction instance.
     */
    Object createSendAction();

    /**
     * Create an empty but initialized instance of a UML Signal.
     *
     * @return an initialized UML Signal instance.
     */
    Object createSignal();

    /**
     * Create an empty but initialized instance of a UML Stimulus.
     *
     * @return an initialized UML Stimulus instance.
     */
    Object createStimulus();
    
    /**
     * Create an empty but initialized instance of a SubsystemInstance.
     *
     * @since UML 1.4
     * @return an initialized SubsystemInstance instance.
     */
    Object createSubsystemInstance();

    /**
     * Create an empty but initialized instance of a UML TerminateAction.
     *
     * @return an initialized UML TerminateAction instance.
     */
    Object createTerminateAction();

    /**
     * Create an empty but initialized instance of a UML UninterpretedAction.
     *
     * @return an initialized UML UninterpretedAction instance.
     */
    Object createUninterpretedAction();

    /**
     * Builds a CallAction belonging to operation oper with a given name.<p>
     * 
     * Ownership of this modelelement is not set! It is unwise to build a
     * callaction without an operation since the multiplicity according to the
     * UML 1.3 spec is 1..1. Therefore precondition is that there is an
     * operation.
     *
     * @param oper the given operation
     * @param name the name for the CallAction
     * @return MCallAction
     */
    Object buildCallAction(Object oper, String name);

    /**
     * Builds a new uninterpreted action.<p>
     * 
     * If the argument is an action state, the new action is set as the entry
     * action.
     * 
     * @param actionState
     *            the given action state or null
     * @return the newly build UninterpretedAction
     */
    Object buildUninterpretedAction(Object actionState);

    /**
     * Builds a Link between two Instances.
     *
     * @param fromInstance the first given instance
     * @param toInstance   the second given instance
     * @return the newly build link
     */
    Object buildLink(Object fromInstance, Object toInstance);

    /**
     * Builds an action (actually an CallAction) for some message.
     *
     * @param message the given message
     * @return the newly build callAction
     */
    Object buildAction(Object message);

    /**
     * Builds a signal belonging to a BehavioralFeature,
     * SignalEvent, SendAction, or Reception.
     *
     * @param element target ModelElement of appropriate type
     * @return the newly build Signal
     */
    Object buildSignal(Object element);

    /**
     * Builds a stimulus based on a given link.<p>
     * 
     * The link must have two linkends that are connected to an instance. These
     * instances are used as sender and receiver of the stimulus. The source
     * will become the sender, the destination the receiver.
     * 
     * @param link
     *            the link
     * @return the stimulus
     */
    Object buildStimulus(Object link);

    /**
     * Builds a reception belonging to some classifier.
     *
     * @param aClassifier the given classifier (or null)
     * @return the newly created reception
     */
    Object buildReception(Object aClassifier);
}
