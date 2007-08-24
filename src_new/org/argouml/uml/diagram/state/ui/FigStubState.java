// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.argouml.model.Facade;
import org.argouml.model.Model;
import org.argouml.model.StateMachinesHelper;
import org.argouml.uml.diagram.ui.SelectionMoveClarifiers;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * Class to display graphics for a UML StubState in a diagram.
 *
 * @author pepargouml@yahoo.es
 */

public class FigStubState extends FigStateVertex {
    
    private static final Logger LOG = Logger.getLogger(FigStubState.class);

    ////////////////////////////////////////////////////////////////
    // constants

    private int x = 0;
    private int y = 0;
    private int width = 45;
    private int height = 20;

    ////////////////////////////////////////////////////////////////
    // instance variables
    private FigText referenceFig;
    private FigLine stubline;
    
    private Facade facade;
    private StateMachinesHelper stateMHelper;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     */
    public FigStubState() {
        super();
        
        facade = Model.getFacade();
        stateMHelper = Model.getStateMachinesHelper();

        setBigPort(new FigRect(x, y, width, height));
        getBigPort().setLineWidth(0);
        getBigPort().setFilled(false);
        stubline = new FigLine(x,
                y,
                width,
                y,
                Color.black);

        referenceFig = new FigText(0, 0, width, height, true);
        referenceFig.setTextColor(Color.black);
        referenceFig.setReturnAction(FigText.END_EDITING);
        referenceFig.setTabAction(FigText.END_EDITING);
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
        setBlinkPorts(false); //make port invisible unless mouse enters
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

    /*
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object node) {
        super.setOwner(node);
        renderingChanged();
    }

    /*
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
     * @return false
     */
    public boolean isResizable() {
        return false;
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionMoveClarifiers(this);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) {
        stubline.setLineColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() {
        return stubline.getLineColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) {
        referenceFig.setFillColor(col);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() {
        return referenceFig.getFillColor();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean f) {
        referenceFig.setFilled(f);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return referenceFig.getFilled();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        stubline.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return stubline.getLineWidth();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
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

    /*
     * Update the text labels.
     *
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (getOwner() == null) {
            return;
        }
        Object top = null;
        Object oldRef = null;
        Object container = facade.getContainer(getOwner());

        //The event source is the owner stub state
        if ((mee.getSource().equals(getOwner()))) {
            if (mee.getPropertyName().equals("referenceState")) {
                updateReferenceText();
                if (container != null && facade.isASubmachineState(container)
                        && facade.getSubmachine(container) != null) {
                    top = facade.getTop(facade.getSubmachine(container));
                    oldRef = stateMHelper.getStatebyName(
                            (String) mee.getOldValue(), top);
                }
                updateListenersX(getOwner(), oldRef);
            } else if ((mee.getPropertyName().equals("container")
                    && facade.isASubmachineState(container))) {
                removeListeners();
                Object o = mee.getOldValue();
                if (o != null && facade.isASubmachineState(o)) {
                    removeElementListener(o);
                }
                stateMHelper.setReferenceState(getOwner(), null);
                updateListeners(getOwner(), getOwner());
                updateReferenceText();
            }
        } else {
            /*The event source is the submachine state*/
            if (container != null
                    && mee.getSource().equals(container)
                    && facade.isASubmachineState(container)
                    && facade.getSubmachine(container) != null) {
                /* The submachine has got a new name*/
                // This indicates a change in association, not name - tfm
                if (mee.getPropertyName().equals("submachine")) {
                    if (mee.getOldValue() != null) {
                        top = facade.getTop(mee.getOldValue());
                        oldRef = stateMHelper.getStatebyName(facade
                                .getReferenceState(getOwner()), top);
                    }
                    stateMHelper.setReferenceState(getOwner(), null);
                    updateListenersX(getOwner(), oldRef);
                    updateReferenceText();
                }

            } else {
                // The event source is the stub state's referenced state
                // or one of the referenced state's path.
                if (facade.getSubmachine(container) != null) {
                    top = facade.getTop(facade.getSubmachine(container));
                }
                String path = facade.getReferenceState(getOwner());
                Object refObject = stateMHelper.getStatebyName(path, top);
                String ref;
                if (refObject == null) {
                    // The source was the referenced state that has got
                    // a new name.
                    ref = stateMHelper.getPath(mee.getSource());
                } else {
                    //The source was one of the referenced state's path which
                    // has got a new name.
                    ref = stateMHelper.getPath(refObject);
                }
                // The Referenced State or one of his path's states has got
                // a new name
                stateMHelper.setReferenceState(getOwner(), ref);
                updateReferenceText();
            }
        }
    }

    /**
     * Rerender the whole figure.
     * Call superclass then add reference text
     */
    public void renderingChanged() {
        updateReferenceText();
        super.renderingChanged();
    }
    
    /**
     * Update the reference text.
     */
    public void updateReferenceText() {
        Object text = null;
        try {
            text = facade.getReferenceState(getOwner());
        } catch (Exception e) {
            LOG.error(e);
        }
        if (text != null) {
            referenceFig.setText((String) text);
        } else {
            referenceFig.setText("");
        }
        calcBounds();
        setBounds(getBounds());
        damage();
    }

    /**
     * @param newOwner
     */
    private void addListeners(Object newOwner) {
        Object container;
        Object top;
        Object reference;
        container = facade.getContainer(newOwner);
        //The new submachine container is added as listener
        if (container != null
                && facade.isASubmachineState(container)) {
            addElementListener(container);
        }
        
        //All states in the new reference state's path are added
        // as listeners
        if (container != null
                && facade.isASubmachineState(container)
                && facade.getSubmachine(container) != null) {
            top = facade.getTop(facade.getSubmachine(container));
            reference = stateMHelper.getStatebyName(facade
                    .getReferenceState(newOwner), top);
            String[] properties = {"name", "container"};
            container = reference;
            while (container != null
                    && !container.equals(top)) {
                addElementListener(container);
                container = facade.getContainer(container);
            }
        }
    }

    /**
     * Remove all the existing listeners
     */
    private void removeListeners() {
        Object container;
        Object top;
        Object reference;
        Object owner = getOwner();
        if (owner == null) {
            return;
        }
        container = facade.getContainer(owner);
        //The old submachine container is deleted as listener
        if (container != null
                && facade.isASubmachineState(container)) {
            removeElementListener(container);
        }
        //All states in the old reference state's path are deleted
        // as listeners
        if (container != null
                && facade.isASubmachineState(container)
                && facade.getSubmachine(container) != null) {
            
            top = facade.getTop(facade.getSubmachine(container));
            reference = stateMHelper.getStatebyName(facade
                    .getReferenceState(owner), top);
            if (reference != null) {
                removeElementListener(reference);
                container = facade.getContainer(reference);
                while (container != null && !facade.isTop(container)) {
                    removeElementListener(container);
                    container = facade.getContainer(container);
                }
            }
        }
    }

    /**
     * @param newOwner
     *            the new owner UML object
     * @param oldV
     *            the old owner UML object
     */
    protected void updateListenersX(Object newOwner, Object oldV) {
        Object container = null;
        if (oldV != null) {
            removeElementListener(oldV);
            container = facade.getContainer(oldV);
            while (container != null && !facade.isTop(container)) {
                removeElementListener(container);
                container = facade.getContainer(container);
            }
        }
        updateListeners(getOwner(), newOwner);
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateFont()
     */
    @Override
    protected void updateFont() {
        super.updateFont();
        Font f = getProject().getProjectSettings().getFont(Font.PLAIN);
        referenceFig.setFont(f);
    }

} /* end class FigStubState */
