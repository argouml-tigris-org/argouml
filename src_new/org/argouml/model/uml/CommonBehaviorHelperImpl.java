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
import java.util.Vector;

import org.argouml.model.CommonBehaviorHelper;

import ru.novosoft.uml.MExtension;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.behavior.common_behavior.MArgument;
import ru.novosoft.uml.behavior.common_behavior.MAttributeLink;
import ru.novosoft.uml.behavior.common_behavior.MCallAction;
import ru.novosoft.uml.behavior.common_behavior.MComponentInstance;
import ru.novosoft.uml.behavior.common_behavior.MInstance;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MLinkEnd;
import ru.novosoft.uml.behavior.common_behavior.MNodeInstance;
import ru.novosoft.uml.behavior.common_behavior.MReception;
import ru.novosoft.uml.behavior.common_behavior.MSendAction;
import ru.novosoft.uml.behavior.common_behavior.MSignal;
import ru.novosoft.uml.behavior.common_behavior.MStimulus;
import ru.novosoft.uml.behavior.state_machines.MCallEvent;
import ru.novosoft.uml.behavior.state_machines.MGuard;
import ru.novosoft.uml.behavior.state_machines.MSignalEvent;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.data_types.MActionExpression;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MIterationExpression;
import ru.novosoft.uml.foundation.data_types.MObjectSetExpression;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;

/**
 * Helper class for UML BehavioralElements::CommonBehavior Package.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
class CommonBehaviorHelperImpl implements CommonBehaviorHelper {

    /**
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     *
     * @param implementation To get other helpers and factories.
     */
    CommonBehaviorHelperImpl(NSUMLModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * Returns the source of a link. The source of a binary link is defined as
     * the instance where the first linkend is pointing to via the association
     * instance.
     *
     * @param link the given link
     * @return MInstance the source of the given link
     */
    public Object getSource(Object link) {
        Collection con = nsmodel.getFacade().getConnections(link);
        if (con.isEmpty()) {
            return null;
        }
        MLinkEnd le0 = (MLinkEnd) (con.toArray())[0];
        return le0.getInstance();
    }

    /**
     * Returns the destination of a link. The destination of a binary link is
     * defined as the instance where the second linkend is pointing to via the
     * association instance.
     * @param link the given link
     * @return MInstance the destination of the given link
     */
    public Object getDestination(Object link) {
        Collection con = nsmodel.getFacade().getConnections(link);
        if (con.size() <= 1) {
            return null;
        }
        MLinkEnd le0 = (MLinkEnd) (con.toArray())[1];
        return le0.getInstance();
    }

    /**
     * Removes the actual Argument from an Action.
     *
     * @param handle Action
     * @param argument Argument
     */
    public void removeActualArgument(Object handle, Object argument) {
        if (handle instanceof MAction && argument instanceof MArgument) {
            ((MAction) handle).removeActualArgument((MArgument) argument);
            return;
        }
        throw new IllegalArgumentException("Unrecognized object "
                + handle + " or "
                + argument);
    }

    /**
     * This method classifier from an instance.
     *
     * @param handle is the instance
     * @param classifier is the classifier
     */
    public void removeClassifier(Object handle, Object classifier) {
        if (handle instanceof MInstance && classifier instanceof MClassifier) {
            ((MInstance) handle).removeClassifier((MClassifier) classifier);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or classifier: " + classifier);
    }

    /**
     * Remove the given context (BehavioralFeature) from a Signal.
     *
     * @param handle Signal
     * @param context BehavioralFeature
     */
    public void removeContext(Object handle, Object context) {
        if (handle instanceof MSignal
            && context instanceof MBehavioralFeature) {
            ((MSignal) handle).removeContext((MBehavioralFeature) context);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or context: " + context);
    }

    /**
     * Remove a given Reception from a given Signal.
     *
     * @param handle the Signal
     * @param reception the Reception
     */
    public void removeReception(Object handle, Object reception) {
        if (handle instanceof MSignal && reception instanceof MReception) {
            ((MSignal) handle).removeReception((MReception) reception);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or reception: " + reception);
    }

    /**
     * Adds an actual argument to an action.
     *
     * @param handle the action
     * @param argument the argument
     */
    public void addActualArgument(Object handle, Object argument) {
        if (handle instanceof MAction && argument instanceof MArgument) {
            ((MAction) handle).addActualArgument((MArgument) argument);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or argument: " + argument);
    }

    /**
     * Adds a Classifier to an Instance.
     *
     * @param handle Instance
     * @param classifier Classifier
     */
    public void addClassifier(Object handle, Object classifier) {
        if (handle instanceof MInstance && classifier instanceof MClassifier) {
            ((MInstance) handle).addClassifier((MClassifier) classifier);
        return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or classifier: " + classifier);
    }

    /**
     * Adds a stimulus to a action or link.
     *
     * @param handle the action or link
     * @param stimulus is the stimulus
     */
    public void addStimulus(Object handle, Object stimulus) {
        if (handle != null
            && stimulus != null
            && stimulus instanceof MStimulus) {
            if (handle instanceof MAction) {
                ((MAction) handle).addStimulus((MStimulus) stimulus);
                return;
            }
            if (handle instanceof MLink) {
                ((MLink) handle).addStimulus((MStimulus) stimulus);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or stimulus: " + stimulus);
    }

    /**
     * Sets the asynchronous property of an action.
     *
     * @param handle the action
     * @param value the value to alter the asynchronous property to
     */
    public void setAsynchronous(Object handle, boolean value) {
        if (handle instanceof MAction) {
            ((MAction) handle).setAsynchronous(value);
            return;
        }
    throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Set the Operation of a CallAction or CallEvent.
     *
     * @param handle CallAction or CallEvent
     * @param operation Operation
     */
    public void setOperation(Object handle, Object operation) {
        if (handle instanceof MCallAction) {
            ((MCallAction) handle).setOperation((MOperation) operation);
            return;
        }
        if (handle instanceof MCallEvent) {
            ((MCallEvent) handle).setOperation((MOperation) operation);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or operation: " + operation);
    }

    /**
     * Sets the classifiers of some instance.
     *
     * @param handle is the instance
     * @param v is the classifier vector
     */
    public void setClassifiers(Object handle, Vector v) {
        if (handle instanceof MInstance) {
            ((MInstance) handle).setClassifiers(v);
            return;
        }
    throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Sets the communicationLink between a link c and a stimulus handle.
     *
     * @param handle the stimulus
     * @param c the link
     */
    public void setCommunicationLink(Object handle, Object c) {
        if (handle instanceof MStimulus && c instanceof MLink) {
            ((MStimulus) handle).setCommunicationLink((MLink) c);
            return;
        }
    throw new IllegalArgumentException("handle: " + handle + " or c: " + c);
    }

    /**
     * @param handle Instance
     * @param c ComponentInstance or null
     */
    public void setComponentInstance(Object handle, Object c) {
        if (handle instanceof MInstance
                && (c == null || c instanceof MComponentInstance)) {
            ((MInstance) handle).setComponentInstance((MComponentInstance) c);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or c: " + c);
    }

    /**
     * Sets the contexts for a Signal.
     *
     * @param handle the Signal
     * @param c the collection of contexts
     */
    public void setContexts(Object handle, Collection c) {
        if (handle instanceof MSignal) {
            ((MSignal) handle).setContexts(c);
            return;
        }
    throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Sets the dispatch action for some stimulus.
     *
     * @param handle the stimulus
     * @param value the action. Can be <code>null</code>.
     */
    public void setDispatchAction(Object handle, Object value) {
        if (handle instanceof MStimulus
            && (value == null || value instanceof MAction)) {
            ((MStimulus) handle).setDispatchAction((MAction) value);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or value: " + value);
    }

    /**
     * Sets the given Instance to the given LinkEnd or AttributeLink.
     *
     * @param handle LinkEnd or AttributeLink
     * @param inst null or Instance
     */
    public void setInstance(Object handle, Object inst) {
        if (inst == null || inst instanceof MInstance) {
            if (handle instanceof MLinkEnd) {
                ((MLinkEnd) handle).setInstance((MInstance) inst);
                return;
            }
            if (handle instanceof MAttributeLink) {
                ((MAttributeLink) handle).setInstance((MInstance) inst);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or inst: " + inst);
    }

    /**
     * Set the NodeInstance of a ComponentInstance.
     *
     * @param handle ComponentInstance
     * @param nodeInstance NodeInstance
     */
    public void setNodeInstance(Object handle, Object nodeInstance) {
        if (handle instanceof MComponentInstance
                && nodeInstance instanceof MNodeInstance) {
            ((MComponentInstance) handle).setNodeInstance(
                    (MNodeInstance) nodeInstance);
            return;
        }
        else if (handle instanceof MComponentInstance
                && nodeInstance == null) {
            ((MComponentInstance) handle).setNodeInstance(null);
            return;
        }

        throw new IllegalArgumentException("handle: " + handle
                + " or nodeInstance: " + nodeInstance);
    }

    /**
     * Sets the receiver of some model element.
     *
     * @param handle model element
     * @param receiver the receiver
     */
    public void setReceiver(Object handle, Object receiver) {
        if (handle instanceof MMessage
            && (receiver instanceof MClassifierRole
                    || receiver == null)) {
            ((MMessage) handle).setReceiver((MClassifierRole) receiver);
            return;
        }
        if (handle instanceof MStimulus && receiver instanceof MInstance) {
            ((MStimulus) handle).setReceiver((MInstance) receiver);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or receiver: " + receiver);
    }

    /**
     * Set the recurrence of an Action.
     *
     * @param handle Action
     * @param expr IterationExpression
     */
    public void setRecurrence(Object handle, Object expr) {
        if (handle instanceof MAction
            && expr instanceof MIterationExpression) {
            ((MAction) handle).setRecurrence((MIterationExpression) expr);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or expr: " + expr);
    }

    /**
     * Set the Expression (script) for an Action.
     *
     * @param handle Action
     * @param expr the script (ActionExpression)
     */
    public void setScript(Object handle, Object expr) {
        if (handle instanceof MAction
            && (expr == null || expr instanceof MActionExpression)) {
            ((MAction) handle).setScript((MActionExpression) expr);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or expr: " + expr);
    }

    /**
     * Sets the sender of some model element.<p>
     *
     * @param handle model element
     * @param sender the sender
     */
    public void setSender(Object handle, Object sender) {
        if (handle instanceof MMessage
                && (sender instanceof MClassifierRole
                        || sender == null)) {
            ((MMessage) handle).setSender((MClassifierRole) sender);
            return;
        }
        if (handle instanceof MStimulus && sender instanceof MInstance) {
            ((MStimulus) handle).setSender((MInstance) sender);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or sender: " + sender);
    }

    /**
     * Set the Signal.
     *
     * @param handle SendAction or Reception or SignalEvent
     * @param signal Signal or null
     */
    public void setSignal(Object handle, Object signal) {
        if (signal == null || signal instanceof MSignal) {
            if (handle instanceof MSendAction) {
                ((MSendAction) handle).setSignal((MSignal) signal);
                return;
            }
            if (handle instanceof MReception) {
                ((MReception) handle).setSignal((MSignal) signal);
                return;
            }
            if (handle instanceof MSignalEvent) {
                ((MSignalEvent) handle).setSignal((MSignal) signal);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or signal: " + signal);
    }

    /**
     * @param handle a reception
     * @param specification the specification
     */
    public void setSpecification(Object handle, String specification) {
        if (handle instanceof MReception) {
            ((MReception) handle).setSpecification(specification);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    /**
     * Sets the target of some action or transition.
     *
     * @param handle the model element
     * @param element the target of the model elemnet
     */
    public void setTarget(Object handle, Object element) {
        if (handle instanceof MAction
            && element instanceof MObjectSetExpression) {
            ((MAction) handle).setTarget((MObjectSetExpression) element);
            return;
        }
        if (handle instanceof MTransition
            && element instanceof MStateVertex) {
            ((MTransition) handle).setTarget((MStateVertex) element);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or element: " + element);
    }

    /**
     * Set the Transition of a guard or effect (Action).
     *
     * @param handle the Guard or Action
     * @param trans the Transition
     */
    public void setTransition(Object handle, Object trans) {
        if (trans instanceof MTransition) {
            if (handle instanceof MGuard) {
                ((MGuard) handle).setTransition((MTransition) trans);
                return;
            }
            if (handle instanceof MAction) {
                ((MAction) handle).setTransition((MTransition) trans);
                return;
            }
        }
        throw new IllegalArgumentException("handle: " + handle
                + " or trans: " + trans);
    }

    /**
     * Set the value of a given object.
     *
     * @param handle the Object of which the value will be set
     * @param value Object
     */
    public void setValue(Object handle, Object value) {
        if (handle instanceof MArgument) {
            ((MArgument) handle).setValue((MExpression) value);
            return;
        }
        if (handle instanceof MAttributeLink) {
            ((MAttributeLink) handle).setValue((MInstance) value);
            return;
        }
        if (handle instanceof MExtension) {
            ((MExtension) handle).setValue(value);
            return;
        }
        if (handle instanceof MTaggedValue) {
            ((MTaggedValue) handle).setValue((String) value);
            return;
        }
    throw new IllegalArgumentException("handle: " + handle);
    }

}
