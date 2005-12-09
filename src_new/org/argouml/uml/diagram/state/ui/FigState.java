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

package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.Iterator;

import org.argouml.model.Model;
import org.argouml.notation.NotationProvider4;
import org.argouml.notation.NotationProviderFactory2;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigRRect;
import org.tigris.gef.presentation.FigText;

/**
 * The fig hierarchy should comply as much as possible to the hierarchy of the
 * UML metamodel. Reason for this is to make sure that events from the model are
 * not missed by the figs. The hierarchy of the states was not compliant to
 * this. This resulted in a number of issues (issue 1430 for example). Therefore
 * introduced an abstract FigState and made FigCompositeState and FigSimpleState
 * subclasses of this state.
 *
 * @author jaap.branderhorst@xs4all.nl
 * @since Dec 30, 2002
 */
public abstract class FigState extends FigStateVertex {

    protected static final int SPACE_TOP = 0;
    protected static final int SPACE_MIDDLE = 0;
    protected static final int DIVIDER_Y = 0;
    protected static final int SPACE_BOTTOM = 6;

    protected static final int MARGIN = 2;

    protected NotationProvider4 notationProviderBody;

    /**
     * The text inside the state.
     */
    private FigText internal;

    /**
     * Constructor for FigState.
     */
    public FigState() {
        super();
        setBigPort(new FigRRect(getInitialX() + 1, getInitialY() + 1,
                getInitialWidth() - 2, getInitialHeight() - 2,
                Color.cyan, Color.cyan));
        getNameFig().setLineWidth(0);
        getNameFig().setBounds(getInitialX() + 2, getInitialY() + 2,
                       getInitialWidth() - 4,
                       getNameFig().getBounds().height);
        getNameFig().setFilled(false);
        
        internal =
            new FigText(getInitialX() + 2,
                    getInitialY() + 2 + 21 + 4,
                    getInitialWidth() - 4,
                    getInitialHeight() - (getInitialY() + 2 + 21 + 4));
        internal.setFont(getLabelFont());
        internal.setTextColor(Color.black);
        internal.setLineWidth(0);
        internal.setFilled(false);
        internal.setExpandOnly(true);
        internal.setReturnAction(FigText.INSERT);
        internal.setJustification(FigText.JUSTIFY_LEFT);
    }

    /**
     * Constructor for FigState, used when an UML elm already exists.
     *
     * @param gm ignored
     * @param node the UML element
     */
    public FigState(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object newOwner) {
        super.setOwner(newOwner);
        updateInternal();
    }

    /**
     * @see org.argouml.uml.diagram.state.ui.FigStateVertex#initNotationProviders(java.lang.Object)
     */
    protected void initNotationProviders(Object own) {
        super.initNotationProviders(own);
        if (Model.getFacade().isAState(own)) {
            notationProviderBody =
                NotationProviderFactory2.getInstance().getNotationProvider(
                        NotationProviderFactory2.TYPE_STATEBODY, this, own);
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (mee.getSource().equals(getOwner())) {
            // the events concerning the MState
            if (mee.getPropertyName().equals("classifierInState")
                    || mee.getPropertyName().equals("deferrableEvent")
                    || mee.getPropertyName().equals("internalTransition")
                    || mee.getPropertyName().equals("doActivity")
                    || mee.getPropertyName().equals("entry")
                    || mee.getPropertyName().equals("exit")) {
                updateInternal();
                // register this fig as a listener if the event is
                // about adding modelelements to the state
                updateListeners(getOwner());
                damage();
            }
            // we don't have to act on incoming and outgoing
            // transitions since that doesn't change the fig.
        } else if (getOwner() != null)
            if (Model.getFacade().getInternalTransitions(getOwner())
                .contains(mee.getSource())
                || // the internal transitions
                (mee.getSource() == Model.getFacade().getEntry(getOwner()))
                || // the entry
                (mee.getSource() == Model.getFacade().getExit(getOwner()))
                || // the exit
                (mee.getSource() == Model.getFacade().getDoActivity(getOwner()))
                || // the doacitivity
                Model.getFacade().getDeferrableEvents(getOwner()).contains(
                        mee.getSource())) {
            // the defered events
            updateInternal();
            updateListeners(getOwner());
            damage();
        }

    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object)
     */
    protected void updateListeners(Object newOwner) {
        super.updateListeners(newOwner);
        Object oldOwner = getOwner();
        if (oldOwner != null) {
            // lets remove all registrations since this is called
            // BEFORE the owner is changed (I hope nobody is going to
            // change that...) the owner is the oldOwner
            Object state = getOwner();
            if (state != null) {
                Iterator it =
                    Model.getFacade().getInternalTransitions(state).iterator();
                while (it.hasNext()) {
                    Model.getPump().removeModelEventListener(this,
                            it.next());
                }
                if (Model.getFacade().getDoActivity(state) != null) {
                    Model.getPump().removeModelEventListener(this,
                            Model.getFacade().getDoActivity(state));
                }
                if (Model.getFacade().getEntry(state) != null) {
                    Model.getPump().removeModelEventListener(this,
                            Model.getFacade().getEntry(state));
                }
                if (Model.getFacade().getExit(state) != null) {
                    Model.getPump().removeModelEventListener(this,
                            Model.getFacade().getExit(state));
                }
            }
        }
        if (newOwner != null) {
            // register for events from all internal transitions
            Object state = newOwner;
            Iterator it =
                Model.getFacade().getInternalTransitions(state).iterator();
            while (it.hasNext()) {
                Model.getPump().addModelEventListener(this,
                        it.next());
            }
            // register for the doactivity etc.
            if (Model.getFacade().getDoActivity(state) != null) {
                Model.getPump().addModelEventListener(this,
                        Model.getFacade().getDoActivity(state));
            }
            if (Model.getFacade().getEntry(state) != null) {
                Model.getPump().addModelEventListener(this,
                        Model.getFacade().getEntry(state));
            }
            if (Model.getFacade().getExit(state) != null) {
                Model.getPump().addModelEventListener(this,
                        Model.getFacade().getExit(state));
            }
        }
    }

    /**
     * Updates the text inside the state.
     */
    protected void updateInternal() {
        Object state = getOwner();
        if (state == null) {
            return;
        }
        if(notationProviderBody != null)
            internal.setText(notationProviderBody.toString());
        calcBounds();
        setBounds(getBounds());
    }

    /**
     * @return the initial X
     */
    protected abstract int getInitialX();

    /**
     * @return the initial Y
     */
    protected abstract int getInitialY();

    /**
     * @return the initial width
     */
    protected abstract int getInitialWidth();

    /**
     * @return the initial height
     */
    protected abstract int getInitialHeight();

    /**
     * @param theInternal The internal to set.
     */
    protected void setInternal(FigText theInternal) {
        this.internal = theInternal;
    }

    /**
     * @return Returns the internal.
     */
    protected FigText getInternal() {
        return internal;
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    protected void textEditStarted(FigText ft) {
        super.textEditStarted(ft);
        if (ft == internal) {
            showHelp(notationProviderBody.getParsingHelp());
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    public void textEdited(FigText ft) throws PropertyVetoException {
        super.textEdited(ft);
        if (ft == getInternal()) {
            Object st = getOwner();
            if (st == null) {
                return;
            }
            ft.setText(notationProviderBody.parse(ft.getText()));
        }
    }

}
