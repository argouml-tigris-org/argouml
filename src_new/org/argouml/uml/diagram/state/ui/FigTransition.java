// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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
import java.awt.Graphics;

import org.argouml.application.api.Notation;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.model.uml.behavioralelements.statemachines.StateMachinesHelper;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.generator.ParserDisplay;

import org.tigris.gef.base.Layer;
import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.MElementEvent;

public class FigTransition extends FigEdgeModelElement {
    private ArrowHeadGreater endArrow = new ArrowHeadGreater();

    ////////////////////////////////////////////////////////////////
    // constructors
    public FigTransition() {
        super();
        addPathItem(getNameFig(), new PathConvPercent(this, 50, 10));
        _fig.setLineColor(Color.black);
        setDestArrowHead(endArrow);
    }

    public FigTransition(Object edge, Layer lay) {
        this();
        if (org.argouml.model.ModelFacade.isATransition(edge)) {
            Object tr = /* (MTransition) */edge;
            Object sourceSV = ModelFacade.getSource(tr);
            Object destSV = ModelFacade.getTarget(tr);
            FigNode sourceFN = (FigNode) lay.presentationFor(sourceSV);
            FigNode destFN = (FigNode) lay.presentationFor(destSV);
            setSourcePortFig(sourceFN);
            setSourceFigNode(sourceFN);
            setDestPortFig(destFN);
            setDestFigNode(destFN);
        }
        setLayer(lay);
        setOwner(edge);

    }

    public FigTransition(Object edge) {
        this(edge, ProjectManager.getManager().getCurrentProject()
                .getActiveDiagram().getLayer());
    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    /**
     * This method is called after the user finishes editing a text field that
     * is in the FigEdgeModelElement. Determine which field and update the
     * model. This class handles the name, subclasses should override to handle
     * other text elements.
     */
    protected void textEdited(FigText ft) {
        Object t = /* (MTransition) */getOwner();
        if (t == null)
            return;
        String s = ft.getText();
        ParserDisplay.SINGLETON.parseTransition(/* (MTransition) */t, s);
    }

    /**
     * This is called after any part of the UML MModelElement has changed. This
     * method automatically updates the name FigText. Subclasses should override
     * and update other parts.
     */
    protected void modelChanged(MElementEvent e) {
        super.modelChanged(e);
        if (e == null) {
            return;
        }

        // register the guard condition
        if (ModelFacade.isATransition(e.getSource())
                && (e.getSource() == getOwner() 
                        && e.getName().equals("guard"))) {
            UmlModelEventPump.getPump().addModelEventListener(this,
                    e.getNewValue(), "expression");
            updateNameText();
            damage();
        } else if (ModelFacade.isATransition(e.getSource())
                && e.getSource() == getOwner()
                && e.getName().equals("trigger")) {
            // register the event (or trigger)
            UmlModelEventPump.getPump().addModelEventListener(this,
                    e.getNewValue(), new String[] {
                    	"parameter", "name",
            	    });
            updateNameText();
            damage();
        } else if (ModelFacade.isATransition(e.getSource())
                && e.getSource() == getOwner() 
                && e.getName().equals("effect")) {
            // register the action
            UmlModelEventPump.getPump().addModelEventListener(this,
                    e.getNewValue(), "script");
            updateNameText();
            damage();
        } else if (ModelFacade.isAEvent(e.getSource())
                && ModelFacade.getTransitions(e.getSource()).contains(
                        getOwner())) {
            // handle events send by the event
            if (e.getName().equals("parameter")) {
                if (e.getAddedValue() != null) {
                    UmlModelEventPump.getPump().addModelEventListener(
                            this, 
                            e.getAddedValue());
                } else if (e.getRemovedValue() != null) {
                    UmlModelEventPump.getPump().removeModelEventListener(
                            this, 
                            e.getRemovedValue());
                }
            }
            updateNameText();
            damage();
        } else if (ModelFacade.isAGuard(e.getSource())) {
            // handle events send by the guard
            updateNameText();
            damage();
        } else if (ModelFacade.isAAction(e.getSource())) {
            // handle events send by the action-effect
            updateNameText();
            damage();
        } else if (ModelFacade.isAParameter(e.getSource())) {
            // handle events send by the parameters of the event
            updateNameText();
            damage();
        }
    }

    protected int[] flip(int[] Ps) {
        int[] r = new int[Ps.length];
        for (int i = Ps.length; i == 0; i--) {
            r[Ps.length - i] = Ps[i];
        }
        return r;
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getDestination()
     */
    protected Object getDestination() {
        if (getOwner() != null) {
            return StateMachinesHelper.getHelper().getDestination(
            /* (MTransition) */getOwner());
        }
        return null;
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getSource()
     */
    protected Object getSource() {
        if (getOwner() != null) {
            return StateMachinesHelper.getHelper().getSource(
            /* (MTransition) */getOwner());
        }
        return null;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        endArrow.setLineColor(getLineColor());
        super.paint(g);
    }

    /**
     * Updates the name text box. In case of a transition the name text box
     * contains:
     * <ul>
     * <li>The event-signature</li>
     * <li>The guard condition between []</li>
     * <li>The action expression</li>
     * </ul>
     * <p>
     * The contents of the text box is generated by the Generator
     * 
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateNameText()
     */
    protected void updateNameText() {
        getNameFig().setText(Notation.generate(this, getOwner()));
    }

} /* end class FigTransition */
