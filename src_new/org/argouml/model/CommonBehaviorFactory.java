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

import ru.novosoft.uml.behavior.common_behavior.MArgument;
import ru.novosoft.uml.behavior.common_behavior.MAttributeLink;
import ru.novosoft.uml.behavior.common_behavior.MCallAction;
import ru.novosoft.uml.behavior.common_behavior.MComponentInstance;
import ru.novosoft.uml.behavior.common_behavior.MCreateAction;
import ru.novosoft.uml.behavior.common_behavior.MDataValue;
import ru.novosoft.uml.behavior.common_behavior.MDestroyAction;
import ru.novosoft.uml.behavior.common_behavior.MException;
import ru.novosoft.uml.behavior.common_behavior.MInstance;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MLinkEnd;
import ru.novosoft.uml.behavior.common_behavior.MLinkObject;
import ru.novosoft.uml.behavior.common_behavior.MNodeInstance;
import ru.novosoft.uml.behavior.common_behavior.MObject;
import ru.novosoft.uml.behavior.common_behavior.MReception;
import ru.novosoft.uml.behavior.common_behavior.MReturnAction;
import ru.novosoft.uml.behavior.common_behavior.MSendAction;
import ru.novosoft.uml.behavior.common_behavior.MSignal;
import ru.novosoft.uml.behavior.common_behavior.MStimulus;
import ru.novosoft.uml.behavior.common_behavior.MTerminateAction;
import ru.novosoft.uml.behavior.common_behavior.MUninterpretedAction;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
import ru.novosoft.uml.foundation.core.MOperation;

/**
 * The interface for the factory of the CommonBehavior.<p>
 *
 * Created from the old CommonBehaviorFactory.
 */
public interface CommonBehaviorFactory {
    /**
     * Create an empty but initialized instance of a UML Action.
     *
     * @return an initialized UML Action instance.
     */
    Object createAction();

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
    MArgument createArgument();

    /**
     * Create an empty but initialized instance of a UML AttributeLink.
     *
     * @return an initialized UML AttributeLink instance.
     */
    MAttributeLink createAttributeLink();

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
    MComponentInstance createComponentInstance();

    /**
     * Create an empty but initialized instance of a UML CreateAction.
     *
     * @return an initialized UML CreateAction instance.
     */
    MCreateAction createCreateAction();

    /**
     * Create an empty but initialized instance of a UML DataValue.
     *
     * @return an initialized UML DataValue instance.
     */
    MDataValue createDataValue();

    /**
     * Create an empty but initialized instance of a UML DestroyAction.
     *
     * @return an initialized UML DestroyAction instance.
     */
    MDestroyAction createDestroyAction();

    /**
     * Create an empty but initialized instance of a UML Exception.
     *
     * @return an initialized UML Exception instance.
     */
    MException createException();

    /**
     * Create an empty but initialized instance of a UML Instance.
     *
     * @return an initialized UML Instance instance.
     */
    MInstance createInstance();

    /**
     * Create an empty but initialized instance of a UML Link.
     *
     * @return an initialized UML Link instance.
     */
    MLink createLink();

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
    MLinkObject createLinkObject();

    /**
     * Create an empty but initialized instance of a UML NodeInstance.
     *
     * @return an initialized UML NodeInstance instance.
     */
    MNodeInstance createNodeInstance();

    /**
     * Create an empty but initialized instance of a UML Object.
     *
     * @return an initialized UML Object instance.
     */
    MObject createObject();

    /**
     * Create an empty but initialized instance of a UML Reception.
     *
     * @return an initialized UML Reception instance.
     */
    MReception createReception();

    /**
     * Create an empty but initialized instance of a UML ReturnAction.
     *
     * @return an initialized UML ReturnAction instance.
     */
    MReturnAction createReturnAction();

    /**
     * Create an empty but initialized instance of a UML SendAction.
     *
     * @return an initialized UML SendAction instance.
     */
    MSendAction createSendAction();

    /**
     * Create an empty but initialized instance of a UML Signal.
     *
     * @return an initialized UML Signal instance.
     */
    MSignal createSignal();

    /**
     * Create an empty but initialized instance of a UML Stimulus.
     *
     * @return an initialized UML Stimulus instance.
     */
    MStimulus createStimulus();

    /**
     * Create an empty but initialized instance of a UML TerminateAction.
     *
     * @return an initialized UML TerminateAction instance.
     */
    MTerminateAction createTerminateAction();

    /**
     * Create an empty but initialized instance of a UML UninterpretedAction.
     *
     * @return an initialized UML UninterpretedAction instance.
     */
    MUninterpretedAction createUninterpretedAction();

    /**
     * Builds a CallAction belonging to operation oper with a given name.
     * Ownership of this modelelement is not set! It is unwise to build a
     * callaction without an operation since the multiplicity according to the
     * UML spec 1.3 is 1..1. Therefore precondition is that there is an
     * operation.
     *
     * @param oper the given operation
     * @param name the name for the CallAction
     * @return MCallAction
     */
    Object buildCallAction(MOperation oper, String name);

    /**
     * Builds a new uninterpreted action. If the argument is an action state,
     * the new action is set as the entry action.
     *
     * @param actionState the given action state or null
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
     * Builds a signal belonging to some behavioralfeature.
     *
     * @param feature the given behaviouralfeature
     * @return the newly build Signal
     */
    MSignal buildSignal(MBehavioralFeature feature);

    /**
     * Builds a stimulus based on a given link. The link must have two
     * linkends that are connected to an instance. These instances are
     * used as sender and receiver of the stimulus. The source will
     * become the sender, the destination the receiver.
     *
     * @param link the link
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

    /**
     * @param elem the Action to be deleted
     */
    void deleteAction(Object elem);

    /**
     * @param elem the ActionSequence to be deleted
     */
    void deleteActionSequence(Object elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteArgument(MArgument elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteAttributeLink(MAttributeLink elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteCallAction(MCallAction elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteComponentInstance(MComponentInstance elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteCreateAction(MCreateAction elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteDataValue(MDataValue elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteDestroyAction(MDestroyAction elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteException(MException elem);

    /**
     * when an instance is deleted,
     * delete its linkend's.
     * similar to deleting a classifier in the CoreFactory.
     *
     * @param elem the element to be deleted
     */
    void deleteInstance(MInstance elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteLink(MLink elem);

    /**
     * When a linkend is deleted,
     * delete its Links.
     *
     * @param elem the element to be deleted
     */
    void deleteLinkEnd(MLinkEnd elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteLinkObject(MLinkObject elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteNodeInstance(MNodeInstance elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteObject(MObject elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteReception(MReception elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteReturnAction(MReturnAction elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteSendAction(MSendAction elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteSignal(MSignal elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteStimulus(MStimulus elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteTerminateAction(MTerminateAction elem);

    /**
     * @param elem the element to be deleted
     */
    void deleteUninterpretedAction(MUninterpretedAction elem);
}
