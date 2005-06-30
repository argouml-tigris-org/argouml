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

package org.argouml.uml.diagram.state.ui;

import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.SelectionMoveClarifiers;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.Iterator;

/**
 * Class to display graphics for a UML MStubState in a diagram.
 *
 * @author pepargouml@yahoo.es
 */

public class FigStubState extends FigStateVertex {

    ////////////////////////////////////////////////////////////////
    // constants

    private static final int MARGIN = 2;
    private int x = 0;
    private int y = 0;
    private int width = 45;
    private int height = 20;

    ////////////////////////////////////////////////////////////////
    // instance variables
    private FigText referenceFig;
    private FigLine stubline;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     */
    public FigStubState() {
        super();

        setBigPort(new FigRect(x, y, width, height));
        getBigPort().setLineWidth(0);
        getBigPort().setFilled(false);
        stubline = new FigLine(x,
                y,
                width,
                y,
                Color.black);

        referenceFig = new FigText(0, 0, width, height, true);
        referenceFig.setFont(getLabelFont());
        referenceFig.setTextColor(Color.black);
        referenceFig.setMultiLine(false);
        referenceFig.setAllowsTab(false);
        referenceFig.setJustification(FigText.JUSTIFY_CENTER);
        referenceFig.setLineWidth(0);
        referenceFig.setBounds(x, y,
                width, referenceFig.getBounds().height);
        referenceFig.setFilled(false);
        referenceFig.setEditable(false);


        addFig(getBigPort());
        addFig(referenceFig);
        addFig(stubline);

        setShadowSize(0);
        setBlinkPorts(false); //make port invisble unless mouse enters
        Rectangle r = getBounds();
    }

    /**
     * The constructor.
     * 
     * @param gm (ignored)
     * @param node the UML owner element
     */
    public FigStubState(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object node) {
        super.setOwner(node);
        updateReference();
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigStubState figClone = (FigStubState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.referenceFig = (FigText) it.next();
        figClone.stubline = (FigLine) it.next();
        return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    /**
     * Synch states are fixed size.
     *
     * @see org.tigris.gef.presentation.Fig#isResizable()
     */
    public boolean isResizable() {
        return false;
    }

    /**
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionMoveClarifiers(this);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) {
        stubline.setLineColor(col);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
        return stubline.getLineColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
        referenceFig.setFillColor(col);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() {
        return referenceFig.getFillColor();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean f) {
        referenceFig.setFilled(f);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return referenceFig.getFilled();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        stubline.setLineWidth(w);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return stubline.getLineWidth();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     */
    protected void setBoundsImpl(int theX, int theY, int theW, int theH) {
        Rectangle oldBounds = getBounds();
        theW = 60;

        referenceFig.setBounds(theX, theY, theW, 
                referenceFig.getBounds().height);
        stubline.setShape(theX, theY,
                theX + theW, theY);

        getBigPort().setBounds(theX, theY, theW, theH);

        calcBounds(); //_x = x; _y = y; _w = w; _h = h;
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    ////////////////////////////////////////////////////////////////
    // event processing

    /**
     * Update the text labels
     *
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (getOwner() == null)
            return;
        Object top = null;
        Object oldRef = null;
        Object container = Model.getFacade().getContainer(getOwner());

        //The event source is the owner stub state
        if ((mee.getSource().equals(getOwner()))) {
            if (mee.getPropertyName().equals("referenceState")) {
                updateReference();
                if (container != null
                        && Model.getFacade().isASubmachineState(container)
                        && Model.getFacade().getSubmachine(container) != null) {
                    top = Model.getFacade()
                            .getTop(Model.getFacade().getSubmachine(container));
                    oldRef = Model.getStateMachinesHelper()
                            .getStatebyName((String) mee.getOldValue(), top);
                }
                if (oldRef != null)
                    updateListeners(getOwner(), oldRef);
                else
                    updateListeners(getOwner());
            } else if ((mee.getPropertyName().equals("container")
                    && Model.getFacade().isASubmachineState(container))) {
                updateListeners(null);
                Object o = mee.getOldValue();
                if (o != null
                        && Model.getFacade().isASubmachineState(o)) {
                    Model.getPump().removeModelEventListener(this, o);

                }
                Model.getStateMachinesHelper()
                        .setReferenceState(getOwner(), null);
                updateListeners(getOwner());
                updateReference();
            }
        } else {
            /*The event source is the submachine state*/
            if (container != null
                    && mee.getSource().equals(container)
                    && Model.getFacade().isASubmachineState(container)
                    && Model.getFacade().getSubmachine(container) != null) {
                /* The submachine has got a new name*/
                if (mee.getPropertyName().equals("submachine")) {
                    if (mee.getOldValue() != null) {
                        top = Model.getFacade().getTop(mee.getOldValue());
                        oldRef = Model.getStateMachinesHelper()
                                .getStatebyName(Model.getFacade()
                                .getReferenceState(getOwner()), top);
                    }
                    Model.getStateMachinesHelper()
                            .setReferenceState(getOwner(), null);
                    updateListeners(getOwner(), oldRef);
                    updateReference();
                }

            }
            /*The event source is the stub state's referenced state or one of
            the referenced state's path*/
            else {
                if (Model.getFacade().getSubmachine(container) != null) {
                    top = Model
                            .getFacade()
                            .getTop(Model.getFacade()
                            .getSubmachine(container));
                }
                String path = (String) Model.getFacade()
                        .getReferenceState(getOwner());
                Object refObject = Model.getStateMachinesHelper()
                        .getStatebyName(path, top);
                String ref;
                if (refObject == null)
                //The source was the referenced state that has got a new name.
                    ref = Model.getStateMachinesHelper()
                            .getPath(mee.getSource());
                else
                //The source was one of the referenced state's path which
                // has got a new name.
                    ref = Model.getStateMachinesHelper()
                            .getPath(refObject);
                // The Referenced State or one of his path's states has got
                // a new name
                Model.getStateMachinesHelper()
                        .setReferenceState(getOwner(), ref);
                updateReference();
            }
        }
    }

    /**
     * Update the reference text.
     */
    protected void updateReference() {
        Object text = null;
        try {
            text = Model.getFacade().getReferenceState(getOwner());
        } catch (Exception e) {
        }
        if (text != null)
            referenceFig.setText((String) text);
        else
            referenceFig.setText("");

        calcBounds();
        setBounds(getBounds());
        damage();
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object)
     */
    protected void updateListeners(Object newOwner) {
        super.updateListeners(newOwner);
        Object container = null;
        Object top = null;
        Object reference = null;

        if (newOwner != null) {
            container = Model.getFacade().getContainer(newOwner);
            //The new submachine container is added as listener
            if (container != null
                    && Model.getFacade().isASubmachineState(container)) {
                Model.getPump().addModelEventListener(this, container);
            }

            //All states in the new reference state's path are added
            // as listeners
            if (container != null
                    && Model.getFacade().isASubmachineState(container)
                    && Model.getFacade().getSubmachine(container) != null) {
                top = Model.getFacade().
                        getTop(Model.getFacade()
                        .getSubmachine(container));
                reference = Model.getStateMachinesHelper()
                        .getStatebyName(Model.getFacade()
                        .getReferenceState(newOwner), top);
                if (reference != null) {
                    Model.getPump()
                            .addModelEventListener(this, reference);
                    container = Model.getFacade().getContainer(reference);
                    while (container != null
                            && !Model.getFacade().isTop(container)) {

                        Model.getPump()
                                .addModelEventListener(this, container);
                        container = Model.getFacade().getContainer(container);
                    }
                }
            }
        } else {
            Object oldOwner = getOwner();
            if (oldOwner != null) {
                container = Model.getFacade().getContainer(oldOwner);
                //The old submachine container is deleted as listener
                if (container != null
                        && Model.getFacade().isASubmachineState(container)) {
                    Model.getPump().removeModelEventListener(this, container);
                }
                //All states in the old reference state's path are deleted
                // as listeners
                if (container != null
                        && Model.getFacade().isASubmachineState(container)
                        && Model.getFacade().getSubmachine(container) != null) {

                    top = Model.getFacade().
                            getTop(Model.getFacade().
                            getSubmachine(container));
                    reference = Model.getStateMachinesHelper()
                            .getStatebyName(Model.getFacade()
                            .getReferenceState(oldOwner), top);
                    if (reference != null) {
                        Model.getPump()
                                .removeModelEventListener(this, reference);

                        container =
                                Model.getFacade().getContainer(reference);
                        while (container != null
                                && !Model.getFacade().isTop(container)) {

                            Model.getPump()
                                    .removeModelEventListener(this, container);
                            container =
                                    Model.getFacade().getContainer(container);
                        }
                    }
                }
            }
        }
    }

    /**
     * @param newOwner the new owner UML object
     * @param oldV the old owner UML object
     */
    protected void updateListeners(Object newOwner, Object oldV) {
        Object container = null;
        if (oldV != null) {
            Model.getPump()
                    .removeModelEventListener(this, oldV);
            container = Model.getFacade().getContainer(oldV);
            while (container != null
                    && !Model.getFacade().isTop(container)) {
                Model.getPump()
                        .removeModelEventListener(this, container);
                container = Model.getFacade().getContainer(container);
            }
        }
        updateListeners(newOwner);
    }
} /* end class FigStubState */