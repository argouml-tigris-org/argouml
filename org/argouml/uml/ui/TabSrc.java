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

package org.argouml.uml.ui;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import org.apache.log4j.Logger;
import org.argouml.application.api.Notation;
import org.argouml.application.api.NotationContext;
import org.argouml.application.api.NotationName;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.events.ArgoNotationEventListener;
import org.argouml.language.ui.NotationComboBox;
import org.argouml.model.Model;
import org.argouml.ui.TabText;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

/**
 * Details panel tabbed panel for displaying a source code representation of
 * a UML model element in a particular Notation.
 */
public class TabSrc
    extends TabText
    implements ArgoNotationEventListener, NotationContext, ItemListener {
    
    private static final Logger LOG = Logger.getLogger(TabSrc.class);
    private boolean stop;
    private NotationName notationName = null;

    ////////////////////////////////////////////////////////////////
    // constructor
    
    /** 
     * Create a tab that contains a toolbar.
     * Then add a notation selector onto it.
     */
    public TabSrc() {
        super("tab.source", true);
        notationName = null;
        getToolbar().add(NotationComboBox.getInstance());
        NotationComboBox.getInstance().addItemListener(this);
        getToolbar().addSeparator();
        ArgoEventPump.addListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
    }

    /**
     * @see java.lang.Object#finalize()
     */
    public void finalize() {
        ArgoEventPump.removeListener(ArgoEventTypes.ANY_NOTATION_EVENT, this);
        NotationComboBox.getInstance().removeItemListener(this);
    }
    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @see org.argouml.ui.TabText#genText(java.lang.Object)
     */
    protected String genText(Object modelObject) {
        modelObject = (modelObject instanceof Fig)
            ? ((Fig) modelObject).getOwner() : modelObject;
        if (!Model.getFacade().isAElement(modelObject))
            return null;

        LOG.debug("TabSrc getting src for " + modelObject);
        return Notation.generate(this, modelObject, true);
    }

    /**
     * @see org.argouml.ui.TabText#parseText(java.lang.String)
     */
    protected void parseText(String s) {
        LOG.debug("TabSrc   setting src for " + getTarget());
        Object modelObject = getTarget();
        if (getTarget() instanceof FigNode)
            modelObject = ((FigNode) getTarget()).getOwner();
        if (getTarget() instanceof FigEdge)
            modelObject = ((FigEdge) getTarget()).getOwner();
        if (modelObject == null)
            return;
        LOG.debug("TabSrc   setting src for " + modelObject);
        /* TODO: Implement this! */
        //Parser.ParseAndUpdate(modelObject, s);
    }

    /**
     * Sets the target of this tab.
     *
     * @see org.argouml.ui.TabTarget#setTarget(java.lang.Object)
     */
    public void setTarget(Object t) {
        Object modelTarget = (t instanceof Fig) ? ((Fig) t).getOwner() : t;
        notationName = null;
        setShouldBeEnabled(Model.getFacade().isAModelElement(modelTarget));
        // If the target is a notation context, use its notation.
        if (t instanceof NotationContext) {
            notationName = ((NotationContext) t).getContextNotation();
        } else {
            notationName = Notation.getDefaultNotation();
        }
        stop = true;
        NotationComboBox.getInstance().setSelectedItem(notationName);
        stop = false;
        super.setTarget(t);
    }

    /**
     * Determines if the current tab should be enabled with the given target.
     * Returns true if the given target is either
     * a modelelement or is a fig with as owner a modelelement.
     *
     * @see org.argouml.ui.TabTarget#shouldBeEnabled(java.lang.Object)
     */
    public boolean shouldBeEnabled(Object target) {
        target = (target instanceof Fig) ? ((Fig) target).getOwner() : target;

        setShouldBeEnabled(false);
        if (Model.getFacade().isAModelElement(target)) {
            setShouldBeEnabled(true);
        }

        return shouldBeEnabled();
    }

    /**
     * Invoked when any aspect of the notation has been changed.
     *
     * @see org.argouml.application.events.ArgoNotationEventListener#notationChanged(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationChanged(ArgoNotationEvent e) {
        refresh();
    }

    /**
     * Ignored.
     *
     * @see org.argouml.application.events.ArgoNotationEventListener#notationAdded(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationAdded(ArgoNotationEvent e) {
    }

    /**
     * Ignored.
     *
     * @see org.argouml.application.events.ArgoNotationEventListener#notationRemoved(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationRemoved(ArgoNotationEvent e) {
    }

    /**
     * Ignored.
     *
     * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderAdded(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationProviderAdded(ArgoNotationEvent e) {
    }

    /**
     * Ignored.
     *
     * @see org.argouml.application.events.ArgoNotationEventListener#notationProviderRemoved(org.argouml.application.events.ArgoNotationEvent)
     */
    public void notationProviderRemoved(ArgoNotationEvent e) {
    }

    /**
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent event) {
        if (!stop && event.getStateChange() == ItemEvent.SELECTED) {
            notationName = (NotationName) NotationComboBox.getInstance()
                .getSelectedItem();
            if (getTarget() instanceof NotationContext) {
                ((NotationContext) getTarget())
                    .setContextNotation(notationName);
            }
            refresh();
        }
    }

    /**
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {
        setTarget(getTarget());
    }

    /**
     * @see org.argouml.application.api.NotationContext#getContextNotation()
     */
    public NotationName getContextNotation() {
        return notationName;
    }

    /**
     * @see org.argouml.application.api.NotationContext#setContextNotation(org.argouml.application.api.NotationName)
     */
    public void setContextNotation(NotationName nn) {
        notationName = nn;
    }

} /* end class TabSrc */
