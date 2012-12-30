/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.beans.PropertyChangeEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.model.ModelEventPump;
import org.argouml.ui.targetmanager.TargetEvent;
import org.tigris.gef.presentation.Fig;

/**
 * Model for a text property on a model element. It listens to
 * property change events for the given property name so that
 * changes made to the underlying UML model are reflected here.
 * <p>
 * NOTE: If you override the insertString() or remove() methods
 * be sure to preserve the flushEvents() calls to keep things
 * synchronized.  Events caused by updates are delivered
 * asynchronously to the actual update calls.
 * <p>
 * @since Oct 6, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class UMLPlainTextDocument
    extends PlainDocument
    implements UMLDocument {

    private static final Logger LOG =
        Logger.getLogger(UMLPlainTextDocument.class.getName());

    /**
     * True if an event should be fired when the text of the document is changed
     */
    private boolean firing = true;

    /**
     * True if an user edits the document directly (by typing in text)
     */
    @Deprecated
    private boolean editing = false;

    /**
     * The target of the propertypanel that's behind this property.
     */
    private Object panelTarget = null;

    /**
     * The name of the property set event that will change the
     * property this document shows.
     */
    private String eventName = null;

    /**
     * Constructor for UMLPlainTextDocument. This takes a panel to set the
     * thirdpartyeventlistener to the given list of events to listen to.
     *
     * @param name the event
     */
    public UMLPlainTextDocument(String name) {
        super();
        setEventName(name);
    }

    /*
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        // NOTE: This may be called from a different thread, so we need to be
        // careful of the threading restrictions imposed by AbstractDocument
        // for mutators to be sure we don't deadlock.
        if (evt instanceof AttributeChangeEvent
                && eventName.equals(evt.getPropertyName())) {
            updateText((String) evt.getNewValue());
        }
    }

    /**
     * Returns the target.
     * @return Object
     */
    public final Object getTarget() {
        return panelTarget;
    }

    /**
     * Sets the target.
     * @param target The target to set
     */
    public final void setTarget(Object target) {
        target = target instanceof Fig ? ((Fig) target).getOwner() : target;
        if (Model.getFacade().isAUMLElement(target)) {
            if (target != panelTarget) {
                ModelEventPump eventPump = Model.getPump();
                if (panelTarget != null) {
                    eventPump.removeModelEventListener(this, panelTarget,
                            getEventName());
                }
                panelTarget = target;
                eventPump.addModelEventListener(this, panelTarget,
                        getEventName());
            }
            updateText(getProperty());
        }
    }

    /*
     * @see javax.swing.text.Document#insertString(
     *         int, java.lang.String, javax.swing.text.AttributeSet)
     */
    public void insertString(int offset, String str, AttributeSet a)
        throws BadLocationException {

        // Mutators hold write lock & will deadlock if use is not thread safe
        super.insertString(offset, str, a);

        setPropertyInternal(getText(0, getLength()));
    }


    /*
     * @see javax.swing.text.Document#remove(int, int)
     */
    public void remove(int offs, int len) throws BadLocationException {

        // Mutators hold write lock & will deadlock if use is not thread safe
        super.remove(offs, len);

        setPropertyInternal(getText(0, getLength()));
    }

    /**
     * Wrapped version of setProperty which attempts to keep us from hearing
     * our own echo on the event listener when we change something.  Also
     * skips updates equal the current value.
     */
    private void setPropertyInternal(String newValue) {
        // TODO: This is updating model on a per character basis as
        // well as unregistering/reregistering event listeners every
        // character - very wasteful - tfm
        if (isFiring() && !newValue.equals(getProperty())) {
            setFiring(false);
            setProperty(newValue);
            Model.getPump().flushModelEvents();
            setFiring(true);
        }
    }

    /**
     * @param text the value of the property
     */
    protected abstract void setProperty(String text);

    /**
     * @return the value of the property
     */
    protected abstract String getProperty();

    /**
     * Enable/disable firing of updates.  As a side effect, it unregisters
     * model event listeners during disable and registers them again during
     * enable.
     *
     * @param f new firing state.  Pass false to disable updates.
     */
    private final synchronized void setFiring(boolean f) {
        ModelEventPump eventPump = Model.getPump();
        if (f && panelTarget != null) {
            eventPump.addModelEventListener(this, panelTarget, eventName);
        } else {
            eventPump.removeModelEventListener(this, panelTarget, eventName);
        }
        firing = f;
    }

    /**
     * Return the state of the firing flag.  Method is synchronized so it will
     * return the correct value from any thread.
     *
     * @return true firing of updates is allowed currently. Returns false if we
     *         are in the process of doing an update so that we know to ignore
     *         any resulting events.
     */
    private final synchronized boolean isFiring() {
        return firing;
    }

    private final void updateText(String textValue) {
        try {
            if (textValue == null) {
                textValue = "";
            }
            String currentValue = getText(0, getLength());
            if (isFiring() && !textValue.equals(currentValue)) {
                setFiring(false);

                // Mutators hold write lock & will deadlock
                // if use is not thread-safe
                super.remove(0, getLength());
                super.insertString(0, textValue, null);
            }
        } catch (BadLocationException b) {
            LOG.log(Level.SEVERE,
		      "A BadLocationException happened\n"
		      + "The string to set was: "
		      + getProperty(),
		      b);
        } finally {
            setFiring(true);
        }
    }


    /**
     * Returns the eventName.
     * @return String
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Sets the eventName.
     * @param en The eventName to set
     */
    protected void setEventName(String en) {
        eventName = en;
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

}
