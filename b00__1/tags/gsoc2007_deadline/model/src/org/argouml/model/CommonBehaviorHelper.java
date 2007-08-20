// $Id:CommonBehaviorHelper.java 11622 2006-12-20 21:25:42Z mvw $
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

import java.util.Collection;
import java.util.List;
import java.util.Vector;


/**
 * The interface to the helper of the CommonBehavior.<p>
 *
 * Created from the old CommonBehaviorHelper.
 */
public interface CommonBehaviorHelper {

    /**
     * Returns the instance which the create action creates.
     *
     * @param createaction the create action
     * @return the instantiation of the create action
     */
    Object getInstantiation(Object createaction);

    /**
     * Set the instantiation for a create action.
     * @param createaction the action
     * @param instantiation the classifier
     */
    void setInstantiation(Object createaction, Object instantiation);

    /**
     * Returns the source of a link. The source of a binary link is defined as
     * the instance where the first linkend is pointing to via the association
     * instance.
     *
     * @param link the given link
     * @return MInstance the source of the given link
     */
    Object getSource(Object link);

    /**
     * Returns the destination of a link. The destination of a binary link is
     * defined as the instance where the second linkend is pointing to via the
     * association instance.
     * @param link the given link
     * @return MInstance the destination of the given link
     */
    Object getDestination(Object link);

    /**
     * Removes the actual Argument from an Action.
     *
     * @param handle Action
     * @param argument Argument
     */
    void removeActualArgument(Object handle, Object argument);

    /**
     * This method replaces all arguments of the given action
     * by the given list of arguments.
     *
     * @param action the given action
     * @param arguments the new arguments
     */
    void setActualArguments(Object action, List arguments);

    /**
     * This method classifier from an instance.
     *
     * @param handle is the instance
     * @param classifier is the classifier
     */
    void removeClassifier(Object handle, Object classifier);

    /**
     * Remove the given context (BehavioralFeature) from a Signal.
     *
     * @param handle Signal
     * @param context BehavioralFeature
     */
    void removeContext(Object handle, Object context);

    /**
     * Remove a given Reception from a given Signal.
     *
     * @param handle the Signal
     * @param reception the Reception
     */
    void removeReception(Object handle, Object reception);

    /**
     * Adds an actual argument to an action.
     *
     * @param handle the action
     * @param argument the argument
     */
    void addActualArgument(Object handle, Object argument);

    /**
     * Adds an actual argument to an action.
     *
     * @param handle the action
     * @param position the 0-based position at which 
     *          to insert the actualArgument
     * @param argument the argument
     */
    void addActualArgument(Object handle, int position, Object argument);

    /**
     * Adds a Classifier to an Instance.
     *
     * @param handle Instance
     * @param classifier Classifier
     */
    void addClassifier(Object handle, Object classifier);

    /**
     * Adds a stimulus to a action or link.
     *
     * @param handle the action or link
     * @param stimulus is the stimulus
     */
    void addStimulus(Object handle, Object stimulus);

    /**
     * Sets the asynchronous property of an action.
     *
     * @param handle the action
     * @param value the value to alter the asynchronous property to
     */
    void setAsynchronous(Object handle, boolean value);

    /**
     * Set the Operation of a CallAction or CallEvent.
     *
     * @param handle CallAction or CallEvent
     * @param operation Operation
     */
    void setOperation(Object handle, Object operation);

    /**
     * Sets the classifiers of some instance.
     *
     * @param handle is the instance
     * @param v is the classifier vector
     */
    void setClassifiers(Object handle, Vector v);

    /**
     * Sets the communicationLink between a link c and a stimulus handle.
     *
     * @param handle the stimulus
     * @param c the link
     */
    void setCommunicationLink(Object handle, Object c);

    /**
     * @param handle Instance
     * @param c ComponentInstance or null
     */
    void setComponentInstance(Object handle, Object c);

    /**
     * Sets the contexts for a Signal.
     *
     * @param handle the Signal
     * @param c the collection of contexts
     */
    void setContexts(Object handle, Collection c);

    /**
     * Sets the dispatch action for some stimulus.
     *
     * @param handle the stimulus
     * @param value the action. Can be <code>null</code>.
     */
    void setDispatchAction(Object handle, Object value);

    /**
     * Sets the given Instance to the given LinkEnd or AttributeLink.
     *
     * @param handle LinkEnd or AttributeLink
     * @param inst null or Instance
     */
    void setInstance(Object handle, Object inst);

    /**
     * Set the NodeInstance of a ComponentInstance.
     *
     * @param handle ComponentInstance
     * @param nodeInstance NodeInstance
     */
    void setNodeInstance(Object handle, Object nodeInstance);

    /**
     * Sets the receiver of some model element.
     *
     * @param handle model element
     * @param receiver the receiver
     */
    void setReceiver(Object handle, Object receiver);
    
    /**
     * Sets the collection of receptions for a Sinal.
     * 
     * @param handle the signal
     * @param receptions a collection with receptions
     */
    void setReception(Object handle, Collection receptions);

    /**
     * Set the recurrence of an Action.
     *
     * @param handle Action
     * @param expr IterationExpression
     */
    void setRecurrence(Object handle, Object expr);

    /**
     * Set the Expression (script) for an Action.
     *
     * @param handle Action
     * @param expr the script (ActionExpression)
     */
    void setScript(Object handle, Object expr);

    /**
     * Sets the sender of some model element.<p>
     *
     * @param handle model element
     * @param sender the sender
     */
    void setSender(Object handle, Object sender);

    /**
     * Set the Signal.
     *
     * @param handle SendAction or Reception or SignalEvent
     * @param signal Signal or null
     */
    void setSignal(Object handle, Object signal);

    /**
     * @param handle a reception
     * @param specification the specification
     */
    void setSpecification(Object handle, String specification);

    /**
     * Sets the target of some action or transition.
     *
     * @param handle the model element
     * @param element the target of the model elemnet
     */
    void setTarget(Object handle, Object element);

    /**
     * Set the Transition of a guard or effect (Action).
     *
     * @param handle the Guard or Action
     * @param trans the Transition
     */
    void setTransition(Object handle, Object trans);

    /**
     * Set the value of a given object.
     *
     * @param handle the Object of which the value will be set
     * @param value Object
     */
    void setValue(Object handle, Object value);

    /**
     * Return the owner of an action.
     *
     * @param handle the action
     * @return owning element of this action
     */
    Object getActionOwner(Object handle);

    /**
     * Add an action to action sequence.
     *
     * @param handle the action sequence
     * @param action the action
     */
    void addAction(Object handle, Object action);

    /**
     * Inserts an action at the specified position in an action sequence.
     *
     * @param handle the action sequence
     * @param position the 0-based position at which to insert the action
     * @param action the action
     */
    void addAction(Object handle, int position, Object action);

    /**
     * Remove an Action from an ActionSequence.
     *
     * @param handle ActionSequence
     * @param action Action
     */
    void removeAction(Object handle, Object action);

}
