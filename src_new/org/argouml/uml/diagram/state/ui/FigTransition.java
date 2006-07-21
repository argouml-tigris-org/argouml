// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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
import java.beans.PropertyChangeEvent;

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.notation.NotationProvider4;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.PathConvPercent2;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.ArrowHeadGreater;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigText;

/**
 * This class represents the graphical representation of a transition
 * on a diagram.
 *
 */
public class FigTransition extends FigEdgeModelElement {

    private ArrowHeadGreater endArrow = new ArrowHeadGreater();

    private NotationProvider4 notationProvider;

    /**
     * If <code>dashed</code> is true, then the transition represents
     * "object flow".
     * If the line is solid, then it represents "control flow".
     */
    private boolean dashed;

    ////////////////////////////////////////////////////////////////
    // constructors
    /**
     * The main constructor.
     */
    public FigTransition() {
        super();
        addPathItem(getNameFig(),
                    new PathConvPercent2(this, getNameFig(), 50, 10));
        getFig().setLineColor(Color.black);
        setDestArrowHead(endArrow);
        allowRemoveFromDiagram(false);
    }

    /**
     * The constructor that hooks the Fig into an existing UML element.
     *
     * It also adapts the line to be dashed if the source or destination
     * is an ObjectFlowState.
     *
     * @param edge the UML element
     * @param lay the layer
     */
    public FigTransition(Object edge, Layer lay) {
        this();
        if (Model.getFacade().isATransition(edge)) {
            Object tr = /* (MTransition) */edge;
            Object sourceSV = Model.getFacade().getSource(tr);
            Object destSV = Model.getFacade().getTarget(tr);
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

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#setOwner(java.lang.Object)
     */
    public void setOwner(Object newOwner) {
        super.setOwner(newOwner);
        if (Model.getFacade().isATransition(newOwner)) {
            dashed =
                Model.getFacade().isAObjectFlowState(
                        Model.getFacade().getSource(newOwner))
                    || Model.getFacade().isAObjectFlowState(
                            Model.getFacade().getTarget(newOwner));
            getFig().setDashed(dashed);
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#initNotationProviders(java.lang.Object)
     */
    protected void initNotationProviders(Object own) {
        super.initNotationProviders(own);
        if (Model.getFacade().isATransition(own)) {
            notationProvider =
                NotationProviderFactory2.getInstance().getNotationProvider(
                        NotationProviderFactory2.TYPE_TRANSITION, own);
        }
    }

    /**
     * @see org.tigris.gef.presentation.FigEdge#setFig(org.tigris.gef.presentation.Fig)
     */
    public void setFig(Fig f) {
        super.setFig(f);
        getFig().setDashed(dashed);
    }

    /**
     * This method is called after the user finishes editing a text field that
     * is in the FigEdgeModelElement. Determine which field and update the
     * model. This class handles the name, subclasses should override to handle
     * other text elements.
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) {
        ft.setText(notationProvider.parse(ft.getText()));
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    protected void textEditStarted(FigText ft) {
        if (ft == getNameFig()) {
            showHelp(notationProvider.getParsingHelp());
        }
    }

    /**
     * This is called after any part of the UML ModelElement has changed. This
     * method automatically updates the attached FigText.
     *
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent e) {
        super.modelChanged(e);
        if (e == null) {
            return;
        }
        // TODO: In case of AttributeChange we probably want to
        // unregister old listener - tfm
        if (e instanceof AddAssociationEvent
                || e instanceof AttributeChangeEvent) {
            // register the guard condition
            if (Model.getFacade().isATransition(e.getSource())
                    && (e.getSource() == getOwner()
                            && e.getPropertyName().equals("guard"))) {
                if (e.getNewValue() != null) {
                    addElementListener(e.getNewValue(), "expression");
                }
                updateNameText();
                damage();
            } else if (Model.getFacade().isATransition(e.getSource())
                    && e.getSource() == getOwner()
                    && e.getPropertyName().equals("trigger")) {
                // register the event (or trigger)
                if (e.getNewValue() != null) {
                    addElementListener(e.getNewValue(),
                            new String[] {"parameter", "name"}
                    // TODO: How to listen to time/change expression?
                    );
                }
                updateNameText();
                damage();
            } else if (Model.getFacade().isATransition(e.getSource())
                    && e.getSource() == getOwner()
                    && e.getPropertyName().equals("effect")) {
                // register the action
                if (Model.getFacade().isAAction(e.getNewValue())) {
                    addElementListener(e.getNewValue(),
                            new String[] {"script", "actualArgument"});
                }
                /* The new value may be null if the effect is removed. */
                updateNameText();
                damage();
            } else if (Model.getFacade().isAEvent(e.getSource())
                    && Model.getFacade().getTransitions(e.getSource()).contains(
                            getOwner())) {
                // handle events send by the event for its parameter
                if (e.getPropertyName().equals("parameter")) {
                    if (e.getOldValue() != null) {
                        removeElementListener(e.getOldValue());
                    } else if (e.getNewValue() != null) {
                        addElementListener(e.getNewValue());
                    }
                }
                updateNameText();
                damage();
            } else if (Model.getFacade().isAGuard(e.getSource())) {
                // handle events send by the guard
                updateNameText();
                damage();
            } else if (Model.getFacade().isAAction(e.getSource())) {
                // handle events send by the action-effect
                // PropertyName is "trigger" or "script"
                if (Model.getFacade().isAArgument(e.getNewValue())) {
                    /* For arguments, listen to changes of the value */
                    addElementListener(e.getNewValue(), "value");
                }
                /* The next lines outside the above if clause for the case
                 * where the "script" changes!
                 */
                updateNameText();
                damage();
            } else if (Model.getFacade().isAArgument(e.getSource())) {
                // handle events from the arguments of the effect action
                updateNameText();
                damage();
            } else if (Model.getFacade().isAParameter(e.getSource())) {
                // handle events send by the parameters of the event
                updateNameText();
                damage();
            } else if ((e.getSource() == getOwner())
                    && (e.getPropertyName().equals("source")
                            || (e.getPropertyName().equals("target")))) {
                dashed =
                    Model.getFacade().isAObjectFlowState(getSource())
                    || Model.getFacade().isAObjectFlowState(getDestination());
                getFig().setDashed(dashed);
            }
        } else if (e instanceof RemoveAssociationEvent) {
            /*
             * If an association has been removed, stop listening to
             * the element that was there (and update our figure just in case)
             */
            removeElementListener(e.getOldValue());
            updateNameText();
            damage();
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getDestination()
     */
    protected Object getDestination() {
        if (getOwner() != null) {
            return Model.getStateMachinesHelper().getDestination(
            /* (MTransition) */getOwner());
        }
        return null;
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#getSource()
     */
    protected Object getSource() {
        if (getOwner() != null) {
            return Model.getStateMachinesHelper().getSource(
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
     * <li>The event-signature
     * <li>The guard condition between []
     * <li>The action expression
     * </ul>
     *
     * The contents of the text box is generated by the notationProvider
     *
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateNameText()
     */
    protected void updateNameText() {
        if (notationProvider != null) {
            getNameFig().setText(notationProvider.toString());
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#paintClarifiers(java.awt.Graphics)
     */
    public void paintClarifiers(Graphics g) {
        indicateBounds(getNameFig(), g);
        super.paintClarifiers(g);
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 2938247797781036110L;

} /* end class FigTransition */
