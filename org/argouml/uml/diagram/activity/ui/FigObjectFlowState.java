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

package org.argouml.uml.diagram.activity.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.application.events.ArgoEvent;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.model.Model;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.FigSingleLineText;
import org.argouml.uml.notation.NotationProvider;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;


/**
 * Class to display graphics for a UML ObjectFlowState in a diagram.<p>
 *
 * The Fig of this modelelement may either represent the following UMLelements:
 * <p>
 * (1) an ObjectFlowState with a Classifier as type, or <p>
 * (2) an ObjectFlowState with a ClassifierInState as type. <p>
 *
 * In both cases (1) and (2), the Fig shows
 * the underlined name of the Classifier,
 * and in the latter case (2), it shows also the names of the states
 * of the ClassifierInState. <p>
 *
 * In the examples in the UML standard, this is written like<pre>
 *      PurchaseOrder
 *       [approved]
 * </pre>
 * i.e. in 2 lines. The first line is underlined,
 * to indicate that it is an instance (object).<p>
 *
 * The fact that the first line is underlined, and the 2nd not, is the
 * reason to implement them in 2 seperate Figs.<p>
 *
 * TODO: Allow stereotypes to be shown.
 *
 * @author mvw
 */
public class FigObjectFlowState extends FigNodeModelElement {

    private static final int PADDING = 8;
    private static final int OFFSET = 10;
    private static final int WIDTH = 70;
    private static final int HEIGHT = 50;

    private NotationProvider notationProviderType;
    private NotationProvider notationProviderState;
    
    private FigRect cover;
    private FigText state;      // the state name

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Main Constructor FigObjectFlowState (called from file loading).
     */
    public FigObjectFlowState() {
        setBigPort(new FigRect(OFFSET, OFFSET, WIDTH, HEIGHT,
                Color.cyan, Color.cyan));
        cover =
            new FigRect(OFFSET, OFFSET, WIDTH, HEIGHT,
                    Color.black, Color.white);

        getNameFig().setUnderline(true);
        getNameFig().setLineWidth(0);

        state = new FigSingleLineText(OFFSET, OFFSET, WIDTH, 21, true);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(cover);
        addFig(getNameFig());
        addFig(state);

        enableSizeChecking(false);
        setReadyToEdit(false);
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);

        ArgoEventPump.addListener(ArgoEvent.ANY_NOTATION_EVENT, this);
    }

    /**
     * Constructor FigObjectFlowState that hooks the Fig into
     * an existing UML model element.
     *
     * @param gm ignored!
     * @param node owner, i.e. the UML element
     */
    public FigObjectFlowState(GraphModel gm, Object node) {
        this();
        setOwner(node);
        enableSizeChecking(true);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#initNotationProviders(java.lang.Object)
     */
    protected void initNotationProviders(Object own) {
        super.initNotationProviders(own);
        if (Model.getFacade().isAModelElement(own)) {
            notationProviderType =
                NotationProviderFactory2.getInstance().getNotationProvider(
                        NotationProviderFactory2.TYPE_OBJECTFLOWSTATE_TYPE,
                        own);
            notationProviderState =
                NotationProviderFactory2.getInstance().getNotationProvider(
                        NotationProviderFactory2.TYPE_OBJECTFLOWSTATE_STATE,
                        own);
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        renderingChanged();
        updateListeners(getOwner(), getOwner());
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object, java.lang.Object)
     */
    protected void updateListeners(Object oldOwner, Object newOwner) {
        if (oldOwner != null) {
            removeAllElementListeners();
        }
        if (newOwner != null) {
            /* Let's NOT do this: addElementListener(newOwner);
             * We only need to listen to its "type", and "remove". */
            addElementListener(newOwner, new String[] {"type", "remove"});
            // register for events from the type
            Object type = Model.getFacade().getType(newOwner);
            if (Model.getFacade().isAClassifier(type)) {
                if (Model.getFacade().isAClassifierInState(type)) {
                    Object classifier = Model.getFacade().getType(type);
                    addElementListener(classifier, "name");
                    addElementListener(type, "inState");
                    Collection states = Model.getFacade().getInStates(type);
                    Iterator i = states.iterator();
                    while (i.hasNext()) {
                        addElementListener(i.next(),
                                "name");
                    }
                } else {
                    addElementListener(type, "name");
                }
            }
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() {
        return "new ObjectFlowState";
    }

    /*
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigObjectFlowState figClone = (FigObjectFlowState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.cover = (FigRect) it.next();
        figClone.setNameFig((FigText) it.next());
        figClone.state = (FigText) it.next();
        return figClone;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#setEnclosingFig(org.tigris.gef.presentation.Fig)
     */
    public void setEnclosingFig(Fig encloser) {
        LayerPerspective layer = (LayerPerspective) getLayer();
        // If the layer is null, then most likely we are being deleted.
        if (layer == null) return;

        PartitionUtility.handleEnclosing(getEncloser(), encloser, getOwner());
        super.setEnclosingFig(encloser);
    }

    /*
     * The space between the 2 text figs is: PADDING.
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        Dimension tempDim = getNameFig().getMinimumSize();
        int w = tempDim.width + PADDING * 2;
        int h = tempDim.height + PADDING;
        tempDim = state.getMinimumSize();
        w = Math.max(w, tempDim.width + PADDING * 2);
        h = h + PADDING + tempDim.height + PADDING;

        return new Dimension(Math.max(w, WIDTH / 2), Math.max(h, HEIGHT / 2));
    }

    /*
     * Override setBounds to keep shapes looking right.
     * The classifier and state Figs are nicely centered vertically,
     * and stretched out over the full width,
     * to allow easy selection with the mouse.
     * The Fig can only be shrunk to half its original size - so that
     * it is not reduceable to a few pixels only.
     *
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    protected void setBoundsImpl(int x, int y, int w, int h) {
        //if (getNameFig() == null) return;
        Rectangle oldBounds = getBounds();

        Dimension classDim = getNameFig().getMinimumSize();
        Dimension stateDim = state.getMinimumSize();
        /* the height of the blank space above and below the text figs: */
        int blank = (h - PADDING - classDim.height - stateDim.height) / 2;
        getNameFig().setBounds(x + PADDING,
                y + blank,
                w - PADDING * 2,
                classDim.height);
        state.setBounds(x + PADDING,
                y + blank + classDim.height + PADDING,
                w - PADDING * 2,
                stateDim.height);

        getBigPort().setBounds(x, y, w, h);
        cover.setBounds(x, y, w, h);

        calcBounds();
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#renderingChanged()
     */
    public void renderingChanged() {
        updateClassifierText();
        updateStateText();
        updateBounds();
        damage();
    }

    /**
     * Updates the text of the classifier FigText.
     */
    private void updateClassifierText() {
        if (isReadyToEdit()) {
            if (notationProviderType != null) {
                getNameFig().setText(
                        notationProviderType.toString(getOwner(), null));
            }
        }
    }

    /**
     * Updates the text of the state FigText.
     */
    private void updateStateText() {
        if (isReadyToEdit()) {
            state.setText(notationProviderState.toString(getOwner(), null));
        }
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    /*
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) { cover.setLineColor(col); }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() { return cover.getLineColor(); }

    /*
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) { cover.setFillColor(col); }

    /*
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() { return cover.getFillColor(); }

    /*
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean f) {
        cover.setFilled(f);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return cover.getFilled();
    }

    /*
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        cover.setLineWidth(w);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return cover.getLineWidth();
    }

    /*
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent ke) {
        if (!isReadyToEdit()) {
            if (Model.getFacade().isAModelElement(getOwner())) {
                updateClassifierText();
                updateStateText();
                setReadyToEdit(true);
            } else {
                //LOG.debug("not ready to edit name");
                return;
            }
        }
        if (ke.isConsumed() || getOwner() == null) {
            return;
        }
        getNameFig().keyTyped(ke);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) throws PropertyVetoException {
        if (ft == getNameFig()) {
            notationProviderType.parse(getOwner(), ft.getText());
            ft.setText(notationProviderType.toString(getOwner(), null));
        } else if (ft == state) {
            notationProviderState.parse(getOwner(), ft.getText());
            ft.setText(notationProviderState.toString(getOwner(), null));
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEditStarted(org.tigris.gef.presentation.FigText)
     */
    protected void textEditStarted(FigText ft) {
        if (ft == getNameFig()) {
            showHelp(notationProviderType.getParsingHelp());
        }
        if (ft == state) {
            showHelp(notationProviderState.getParsingHelp());
        }
    }

    /*
     * @see org.tigris.gef.presentation.Fig#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionActionState(this);
    }

} /* end class FigObjectFlowState */
