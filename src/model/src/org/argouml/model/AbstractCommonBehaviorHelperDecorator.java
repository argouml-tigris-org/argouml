// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
import java.util.Vector;

/**
 * An abstract Decorator for the {@link CommonBehaviorHelper}.
 *
 * @author Bob Tarling
 */
public abstract class AbstractCommonBehaviorHelperDecorator
	implements CommonBehaviorHelper {

    /**
     * The component.
     */
    private CommonBehaviorHelper impl;

    /**
     * @param component The component to decorate.
     */
    AbstractCommonBehaviorHelperDecorator(CommonBehaviorHelper component) {
        impl = component;
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#getSource(java.lang.Object)
     */
    public Object getSource(Object link) {
        return impl.getSource(link);
    }

    /**
     * The component we are decorating.
     *
     * @return Returns the component.
     */
    protected CommonBehaviorHelper getComponent() {
        return impl;
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#getDestination(
     *         java.lang.Object)
     */
    public Object getDestination(Object link) {
        return impl.getDestination(link);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#removeActualArgument(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void removeActualArgument(Object handle, Object argument) {
        impl.removeActualArgument(handle, argument);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#removeClassifier(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void removeClassifier(Object handle, Object classifier) {
        impl.removeClassifier(handle, classifier);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#removeContext(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void removeContext(Object handle, Object context) {
        impl.removeContext(handle, context);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#removeReception(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void removeReception(Object handle, Object reception) {
        impl.removeReception(handle, reception);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#addActualArgument(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void addActualArgument(Object handle, Object argument) {
        impl.addActualArgument(handle, argument);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#addClassifier(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void addClassifier(Object handle, Object classifier) {
        impl.addClassifier(handle, classifier);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#addStimulus(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void addStimulus(Object handle, Object stimulus) {
        impl.addStimulus(handle, stimulus);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setAsynchronous(
     *         java.lang.Object,
     *         boolean)
     */
    public void setAsynchronous(Object handle, boolean value) {
        impl.setAsynchronous(handle, value);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setOperation(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setOperation(Object handle, Object operation) {
        impl.setOperation(handle, operation);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setClassifiers(
     *         java.lang.Object,
     *         java.util.Vector)
     */
    public void setClassifiers(Object handle, Vector v) {
        impl.setClassifiers(handle, v);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setCommunicationLink(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setCommunicationLink(Object handle, Object c) {
        impl.setCommunicationLink(handle, c);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setComponentInstance(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setComponentInstance(Object handle, Object c) {
        impl.setComponentInstance(handle, c);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setContexts(
     *         java.lang.Object,
     *         java.util.Collection)
     */
    public void setContexts(Object handle, Collection c) {
        impl.setContexts(handle, c);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setDispatchAction(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setDispatchAction(Object handle, Object value) {
        impl.setDispatchAction(handle, value);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setInstance(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setInstance(Object handle, Object inst) {
        impl.setInstance(handle, inst);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setNodeInstance(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setNodeInstance(Object handle, Object nodeInstance) {
        impl.setNodeInstance(handle, nodeInstance);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setReceiver(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setReceiver(Object handle, Object receiver) {
        impl.setReceiver(handle, receiver);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setRecurrence(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setRecurrence(Object handle, Object expr) {
        impl.setRecurrence(handle, expr);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setScript(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setScript(Object handle, Object expr) {
        impl.setScript(handle, expr);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setSender(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setSender(Object handle, Object sender) {
        impl.setSender(handle, sender);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setSignal(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setSignal(Object handle, Object signal) {
        impl.setSignal(handle, signal);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setSpecification(
     *         java.lang.Object,
     *         java.lang.String)
     */
    public void setSpecification(Object handle, String specification) {
        impl.setSpecification(handle, specification);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setTarget(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setTarget(Object handle, Object element) {
        impl.setTarget(handle, element);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setTransition(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setTransition(Object handle, Object trans) {
        impl.setTransition(handle, trans);
    }

    /**
     * @see org.argouml.model.CommonBehaviorHelper#setValue(
     *         java.lang.Object,
     *         java.lang.Object)
     */
    public void setValue(Object handle, Object value) {
        impl.setValue(handle, value);
    }

    /**
     * @see CommonBehaviorHelper#getInstantiation(Object)
     */
    public Object getInstantiation(Object createaction) {
        return impl.getInstantiation(createaction);
    }

    /**
     * @see CommonBehaviorHelper#setInstantiation(Object, Object)
     */
    public void setInstantiation(Object createaction, Object instantiation) {
        impl.setInstantiation(createaction, instantiation);
    }

    /**
     * @see CommonBehaviorHelper#getActionOwner(Object)
     */
    public Object getActionOwner(Object handle) {
        return impl.getActionOwner(handle);
    }

}
