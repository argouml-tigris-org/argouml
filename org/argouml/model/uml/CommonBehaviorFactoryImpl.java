// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.model.uml;

import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.CommonBehaviorFactory;
import org.argouml.model.ModelFacade;

import ru.novosoft.uml.MFactory;
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
 * Factory to create UML classes for the UML
 * BehaviorialElements::CommonBehavior package.
 *
 * TODO: Change visibility to package after reflection problem solved.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
public class CommonBehaviorFactoryImpl
	extends AbstractUmlModelFactory
	implements CommonBehaviorFactory {

    /**
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     *
     * @param implementation To get other helpers and factories.
     */
    CommonBehaviorFactoryImpl(NSUMLModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * Create an empty but initialized instance of a UML Action.
     *
     * @return an initialized UML Action instance.
     */
    public Object createAction() {
        Object modelElement = MFactory.getDefaultFactory().createAction();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML ActionSequence.
     *
     * @return an initialized UML ActionSequence instance.
     */
    public Object createActionSequence() {
        Object modelElement =
            MFactory.getDefaultFactory().createActionSequence();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Argument.
     *
     * @return an initialized UML Argument instance.
     */
    public Object createArgument() {
        MArgument modelElement = MFactory.getDefaultFactory().createArgument();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML AttributeLink.
     *
     * @return an initialized UML AttributeLink instance.
     */
    public Object createAttributeLink() {
        MAttributeLink modelElement =
            MFactory.getDefaultFactory().createAttributeLink();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML CallAction.
     *
     * @return an initialized UML CallAction instance.
     */
    public Object createCallAction() {
        MCallAction modelElement =
            MFactory.getDefaultFactory().createCallAction();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML ComponentInstance.
     *
     * @return an initialized UML ComponentInstance instance.
     */
    public Object createComponentInstance() {
        MComponentInstance modelElement =
            MFactory.getDefaultFactory().createComponentInstance();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML CreateAction.
     *
     * @return an initialized UML CreateAction instance.
     */
    public Object createCreateAction() {
        MCreateAction modelElement =
            MFactory.getDefaultFactory().createCreateAction();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML DataValue.
     *
     * @return an initialized UML DataValue instance.
     */
    public MDataValue createDataValue() {
        MDataValue modelElement =
            MFactory.getDefaultFactory().createDataValue();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML DestroyAction.
     *
     * @return an initialized UML DestroyAction instance.
     */
    public Object createDestroyAction() {
        MDestroyAction modelElement =
            MFactory.getDefaultFactory().createDestroyAction();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Exception.
     *
     * @return an initialized UML Exception instance.
     */
    public Object createException() {
        MException modelElement =
            MFactory.getDefaultFactory().createException();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Instance.
     *
     * @return an initialized UML Instance instance.
     */
    public Object createInstance() {
        MInstance modelElement = MFactory.getDefaultFactory().createInstance();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Link.
     *
     * @return an initialized UML Link instance.
     */
    public Object createLink() {
        MLink modelElement = MFactory.getDefaultFactory().createLink();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML LinkEnd.
     *
     * @return an initialized UML LinkEnd instance.
     */
    public Object createLinkEnd() {
        MLinkEnd modelElement = MFactory.getDefaultFactory().createLinkEnd();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML LinkObject.
     *
     * @return an initialized UML LinkObject instance.
     */
    public Object createLinkObject() {
        MLinkObject modelElement =
            MFactory.getDefaultFactory().createLinkObject();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML NodeInstance.
     *
     * @return an initialized UML NodeInstance instance.
     */
    public Object createNodeInstance() {
        MNodeInstance modelElement =
            MFactory.getDefaultFactory().createNodeInstance();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Object.
     *
     * @return an initialized UML Object instance.
     */
    public Object createObject() {
        MObject modelElement = MFactory.getDefaultFactory().createObject();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Reception.
     *
     * @return an initialized UML Reception instance.
     */
    public Object createReception() {
        MReception modelElement =
            MFactory.getDefaultFactory().createReception();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML ReturnAction.
     *
     * @return an initialized UML ReturnAction instance.
     */
    public Object createReturnAction() {
        MReturnAction modelElement =
            MFactory.getDefaultFactory().createReturnAction();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML SendAction.
     *
     * @return an initialized UML SendAction instance.
     */
    public Object createSendAction() {
        MSendAction modelElement =
            MFactory.getDefaultFactory().createSendAction();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Signal.
     *
     * @return an initialized UML Signal instance.
     */
    public Object createSignal() {
        MSignal modelElement = MFactory.getDefaultFactory().createSignal();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML Stimulus.
     *
     * @return an initialized UML Stimulus instance.
     */
    public Object createStimulus() {
        MStimulus modelElement = MFactory.getDefaultFactory().createStimulus();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML TerminateAction.
     *
     * @return an initialized UML TerminateAction instance.
     */
    public Object createTerminateAction() {
        MTerminateAction modelElement =
            MFactory.getDefaultFactory().createTerminateAction();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Create an empty but initialized instance of a UML UninterpretedAction.
     *
     * @return an initialized UML UninterpretedAction instance.
     */
    public Object createUninterpretedAction() {
        MUninterpretedAction modelElement =
            MFactory.getDefaultFactory().createUninterpretedAction();
        super.initialize(modelElement);
        return modelElement;
    }

    /**
     * Builds a CallAction belonging to operation oper with a given name.
     * Ownership of this modelelement is not set! It is unwise to build a
     * callaction without an operation since the multiplicity according to the
     * UML spec 1.3 is 1..1. Therefore precondition is that there is an
     * operation.
     * @param oper the given operation
     * @param name the name for the CallAction
     * @return MCallAction
     */
    public Object buildCallAction(Object oper, String name) {
        if (!(oper instanceof MOperation)) {
            throw new IllegalArgumentException(
                "There should be an operation" + " with a callaction.");
        }
        Object action = createCallAction();
        ModelFacade.setName(action, name);
        ModelFacade.setOperation(action, oper);
        return action;
    }

    /**
     * Builds a new uninterpreted action. If the argument is an action state,
     * the new action is set as the entry action.
     *
     * @param actionState the given action state or null
     * @return the newly build UninterpretedAction
     */
    public Object buildUninterpretedAction(Object actionState) {
        Object action = createUninterpretedAction();
        if (ModelFacade.isAActionState(actionState)) {
            ModelFacade.setEntry(actionState, action);
        }
        return action;
    }

    /**
     * Builds a Link between two Instances.
     *
     * @param fromInstance the first given instance
     * @param toInstance   the second given instance
     * @return the newly build link
     */
    public Object buildLink(Object fromInstance, Object toInstance) {
        Object link = nsmodel.getCommonBehaviorFactory().createLink();
        Object /*MLinkEnd*/ le0 =
	    nsmodel.getCommonBehaviorFactory().createLinkEnd();
        ModelFacade.setInstance(le0, fromInstance);
        Object /*MLinkEnd*/ le1 =
	    nsmodel.getCommonBehaviorFactory().createLinkEnd();
        ModelFacade.setInstance(le1, toInstance);
        ModelFacade.addConnection(link, le0);
        ModelFacade.addConnection(link, le1);
        return link;
    }

    /**
     * Builds an action (actually an CallAction) for some message.
     *
     * @param message the given message
     * @return the newly build callAction
     */
    public Object buildAction(Object message) {
        Object action = createCallAction();
        ModelFacade.setName(action, "action");
        ModelFacade.setAction(message, action);
        Object interaction = ModelFacade.getInteraction(message);
        if (interaction != null
            && ModelFacade.getContext(interaction) != null) {
            ModelFacade.setNamespace(
                action,
                ModelFacade.getContext(interaction));
        } else {
            throw new IllegalStateException(
                "In buildaction: message does not "
                    + "have an interaction or the "
                    + "interaction does not have "
                    + "a context");
        }
        return action;
    }

    /**
     * Builds a signal belonging to some behavioralfeature.
     *
     * @param feature the given behaviouralfeature
     * @return the newly build Signal
     */
    public Object buildSignal(Object feature) {
        if (!(feature instanceof MBehavioralFeature)) {
            return null;
        }
        MSignal signal = (MSignal) createSignal();
        signal.addContext((MBehavioralFeature) feature);
        return signal;
    }

    /**
     * Builds a stimulus based on a given link. The link must have two
     * linkends that are connected to an instance. These instances are
     * used as sender and receiver of the stimulus. The source will
     * become the sender, the destination the receiver.
     *
     * @param link the link
     * @return the stimulus
     */
    public Object buildStimulus(Object link) {
        if (ModelFacade.isALink(link)
            && nsmodel.getUmlHelper().getCore().getSource(link) != null
            && nsmodel.getUmlHelper().getCore().getDestination(link) != null) {
            Object stimulus = createStimulus();
            Object sender = nsmodel.getUmlHelper().getCore().getSource(link);
            Object receiver =
                nsmodel.getUmlHelper().getCore().getDestination(link);
            ModelFacade.setReceiver(stimulus, receiver);
            ModelFacade.setSender(stimulus, sender);
            ModelFacade.setCommunicationLink(stimulus, link);
            return stimulus;
        }
        throw new IllegalArgumentException("Argument is not a link or "
					   + "does not have "
					   + "a source or "
					   + "destination instance");

    }

    /**
     * Builds a reception belonging to some classifier.
     *
     * @param aClassifier the given classifier (or null)
     * @return the newly created reception
     */
    public Object buildReception(Object aClassifier) {
        Object reception = createReception();
        if (ModelFacade.isAClassifier(aClassifier)) {
            ModelFacade.setOwner(reception, aClassifier);
        }
        return reception;
    }

    /**
     * @param elem the Action to be deleted
     */
    public void deleteAction(Object elem) {
    }

    /**
     * @param elem the ActionSequence to be deleted
     */
    public void deleteActionSequence(Object elem) {
    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteArgument(Object elem) {
        if (!(elem instanceof MArgument)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteAttributeLink(Object elem) {
        if (!(elem instanceof MAttributeLink)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteCallAction(Object elem) {
        if (!(elem instanceof MCallAction)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteComponentInstance(Object elem) {
        if (!(elem instanceof MComponentInstance)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteCreateAction(Object elem) {
        if (!(elem instanceof MCreateAction)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteDataValue(Object elem) {
        if (!(elem instanceof MDataValue)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteDestroyAction(Object elem) {
        if (!(elem instanceof MDestroyAction)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteException(Object elem) {
        if (!(elem instanceof MException)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * when an instance is deleted,
     * delete its linkend's.
     * similar to deleting a classifier in the CoreFactory.
     *
     * @param elem the element to be deleted
     */
    public void deleteInstance(Object elem) {
        if (!(elem instanceof MInstance)) {
            throw new IllegalArgumentException();
        }


        if (elem != null) {
            Collection col = ((MInstance) elem).getLinkEnds();
            Iterator it = col.iterator();
            while (it.hasNext()) {
                nsmodel.getUmlFactory().delete(it.next());
            }
        }
    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteLink(Object elem) {
        if (!(elem instanceof MLink)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * when a linkend is deleted,
     * delete its Links.
     *
     * @param elem the element to be deleted
     */
    public void deleteLinkEnd(Object elem) {
        if (!(elem instanceof MLinkEnd)) {
            throw new IllegalArgumentException();
        }


        MLink link = ((MLinkEnd) elem).getLink();
        if (link != null
            && link.getConnections() != null
            && link.getConnections().size() == 2) { // binary link
            nsmodel.getUmlFactory().delete(link);
        }
    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteLinkObject(Object elem) {
        if (!(elem instanceof MLinkObject)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteNodeInstance(Object elem) {
        if (!(elem instanceof MNodeInstance)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteObject(Object elem) {
        if (!(elem instanceof MObject)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteReception(Object elem) {
        if (!(elem instanceof MReception)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteReturnAction(Object elem) {
        if (!(elem instanceof MReturnAction)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteSendAction(Object elem) {
        if (!(elem instanceof MSendAction)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteSignal(Object elem) {
        if (!(elem instanceof MSignal)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteStimulus(Object elem) {
        if (!(elem instanceof MStimulus)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteTerminateAction(Object elem) {
        if (!(elem instanceof MTerminateAction)) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * @param elem the element to be deleted
     */
    public void deleteUninterpretedAction(Object elem) {
        if (!(elem instanceof MUninterpretedAction)) {
            throw new IllegalArgumentException();
        }

    }
}
