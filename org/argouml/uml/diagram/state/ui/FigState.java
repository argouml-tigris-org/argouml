// $Id$
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

// $Id$
package org.argouml.uml.diagram.state.ui;

import java.awt.Color;
import java.util.Iterator;

import org.argouml.application.api.Notation;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlModelEventPump;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.behavior.state_machines.MState;

/**
 * The fig hierarchy should comply as much as possible to the hierarchy of the
 * UML metamodel. Reason for this is to make sure that events from the model are
 * not missed by the figs. 
 * The hierarchy of the states was not compliant to this. This resulted in a
 * number of issues (issue 1430 for example). Therefore introduced a FigState
 * and made FigCompositeState and FigSimpleState subclasses of this state.
 * @author jaap.branderhorst@xs4all.nl	
 * @since Dec 30, 2002
 */
public abstract class FigState extends FigStateVertex {

    protected FigText _internal;

    /**
     * Constructor for FigState.
     */
    public FigState() {
        super();
        _internal =
	    new FigText(getInitialX() + 2, getInitialY() + 2 + 21 + 4,
			getInitialWidth() - 4,
			getInitialHeight() - (getInitialY() + 2 + 21 + 4));
        _internal.setFont(LABEL_FONT);
        _internal.setTextColor(Color.black);
        _internal.setLineWidth(0);
        _internal.setFilled(false);
        _internal.setExpandOnly(true);
        _internal.setMultiLine(true);
        _internal.setJustification(FigText.JUSTIFY_LEFT);
    }

    /**
     * Constructor for FigState.
     * @param gm
     * @param node
     */
    public FigState(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    public void setOwner(Object node) {
        super.setOwner(node);
        updateInternal();
    }

    /**
     * @see
     * org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(ru.novosoft.uml.MElementEvent)
     */
    protected void modelChanged(MElementEvent mee) {
        super.modelChanged(mee);
        if (mee.getSource().equals(getOwner())) {
	    // the events concerning the MState
            if (mee.getName().equals("classifierInState")
                || mee.getName().equals("deferrableEvent")
                || mee.getName().equals("internalTransition")
                || mee.getName().equals("doActivity")
                || mee.getName().equals("entry")
                || mee.getName().equals("exit")) {
                updateInternal();
                // register this fig as a listener if the event is
                // about adding modelelements to the state
                updateListeners(getOwner());
                damage();
            }
            // we don't have to act on incoming and outgoing
            // transitions since that doesn't change the fig.
        } else if (ModelFacade.getInternalTransitions(getOwner()).contains(mee.getSource())
		   || // the internal transitions
		   (mee.getSource() == ModelFacade.getEntry(getOwner()))
		   || // the entry
		   (mee.getSource() == ModelFacade.getExit(getOwner()))
		   || // the exit
		   (mee.getSource() == ModelFacade.getDoActivity(getOwner()))
		   || // the doacitivity
		   ModelFacade.getDeferrableEvents(getOwner()).contains(mee.getSource()))
	{
	    // the defered events
            updateInternal();
            updateListeners(getOwner());
            damage();
        }

    }

    /**
     * @see
     * org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object)
     */
    protected void updateListeners(Object newOwner) {
        super.updateListeners(newOwner);
        if (newOwner != null) {
            // register for events from all internal transitions
            MState state = (MState) newOwner;
            Iterator it = state.getInternalTransitions().iterator();
            while (it.hasNext()) {
                UmlModelEventPump.getPump()
		    .addModelEventListener(this, (MBase) it.next());
            }
            // register for the doactivity etc.
            if (state.getDoActivity() != null) {
                UmlModelEventPump.getPump()
		    .addModelEventListener(this, (MBase) state.getDoActivity());
            }
            if (state.getEntry() != null) {
                UmlModelEventPump.getPump()
		    .addModelEventListener(this, (MBase) state.getEntry());
            }
            if (state.getExit() != null) {
                UmlModelEventPump.getPump()
		    .addModelEventListener(this, (MBase) state.getExit());
            }
        } else {
            // lets remove all registrations since this is called
            // BEFORE the owner is changed (I hope nobody is going to
            // change that...)  the owner is the oldOwner
            MState state = (MState) getOwner();
            if (state != null) {
                Iterator it = state.getInternalTransitions().iterator();
                while (it.hasNext()) {
                    UmlModelEventPump.getPump()
			.removeModelEventListener(this, (MBase) it.next());
                }
                if (state.getDoActivity() != null) {
                    UmlModelEventPump.getPump()
			.removeModelEventListener(this,
						  (MBase)
						  state.getDoActivity());
                }
                if (state.getEntry() != null) {
                    UmlModelEventPump.getPump()
			.removeModelEventListener(this,
						  (MBase) state.getEntry());
                }
                if (state.getExit() != null) {
                    UmlModelEventPump.getPump()
			.removeModelEventListener(this,
						  (MBase) state.getExit());
                }
            }

        }
    }

    /**
     * Updates the text inside the state
     */
    protected void updateInternal() {
        MState s = (MState) getOwner();
        if (s == null)
            return;
        String newText = Notation.generateStateBody(this, s);
        _internal.setText(newText);

        calcBounds();
        setBounds(getBounds());
    }
    
    protected abstract int getInitialX();
    protected abstract int getInitialY();
    protected abstract int getInitialWidth();
    protected abstract int getInitialHeight();
    
    public Selection makeSelection() {
	return new SelectionState(this);
    }

}