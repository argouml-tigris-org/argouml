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

package org.argouml.uml.diagram.activity.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.text.ParseException;
import java.util.Iterator;

import org.argouml.application.api.Notation;
import org.argouml.application.events.ArgoEvent;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.generator.ParserDisplay;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;


/**
 * Class to display graphics for a UML ObjectFlowState in a diagram.<p>
 *
 * The Fig of this modelelement may either contain the Classifier name, or
 * it contains the name of the ClassifierInState AND the name of its state.
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
    private static final int HEIGHT = 40;

    private FigRect cover;
    private FigText classifier; // the classifier(instate) name
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

        classifier = new FigText(OFFSET, HEIGHT - OFFSET, WIDTH, 21);
        classifier.setFont(getLabelFont());
        classifier.setTextColor(Color.black);
        classifier.setReturnAction(FigText.END_EDITING);
        classifier.setTabAction(FigText.END_EDITING);
        classifier.setLineWidth(0);
        classifier.setFilled(false);
        classifier.setUnderline(true);

        state = new FigText(OFFSET, OFFSET, WIDTH, 21);
        state.setFont(getLabelFont());
        state.setTextColor(Color.black);
        state.setReturnAction(FigText.END_EDITING);
        state.setReturnAction(FigText.END_EDITING);
        state.setLineWidth(0);
        state.setFilled(false);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(cover);
        addFig(classifier);
        addFig(state);

        enableSizeChecking(false);
        setReadyToEdit(false);
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);

        setNameFig(null); // DEBUG only!

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

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if ((mee.getSource() == getOwner())
            || (mee.getSource() == Model.getFacade().getType(getOwner()))) {
            renderingChanged();
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() {
        return "new ObjectFlowState";
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigObjectFlowState figClone = (FigObjectFlowState) super.clone();
        Iterator it = figClone.getFigs().iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.cover = (FigRect) it.next();
        figClone.classifier = (FigText) it.next();
        figClone.state = (FigText) it.next();
        return figClone;
    }

    /**
     * Get the minimum size.
     * The space between the 2 text figs is: PADDING.
     * @see org.tigris.gef.presentation.Fig#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        Dimension tempDim = classifier.getMinimumSize();
        int w = tempDim.width + PADDING * 2;
        int h = tempDim.height + PADDING;
        tempDim = state.getMinimumSize();
        w = Math.max(w, tempDim.width + PADDING * 2);
        h = h + PADDING + tempDim.height + PADDING;

        return new Dimension(Math.max(w, WIDTH / 2), Math.max(h, HEIGHT / 2));
    }

    /**
     * Override setBounds to keep shapes looking right.
     * The classifier and state Figs are nicely centered vertically,
     * and stretched out over the full width,
     * to allow easy selection with the mouse.
     * The Fig can only be shrinked to half its original size - so that
     * it is not reduceable to a few pixels only.
     *
     * @see org.tigris.gef.presentation.Fig#setBoundsImpl(int, int, int, int)
     */
    protected void setBoundsImpl(int x, int y, int w, int h) {
        //if (getNameFig() == null) return;
        Rectangle oldBounds = getBounds();

        Dimension classDim = classifier.getMinimumSize();
        Dimension stateDim = state.getMinimumSize();
        /* the height of the blank space above and below the text figs: */
        int blank = (h - PADDING - classDim.height - stateDim.height) / 2;
        classifier.setBounds(x + PADDING,
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

    /**
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
            if (getOwner() == null) {
                return;
            }
            String theNewText =
                Notation.generate(this, getOwner()); // the ObjectFlowState
            classifier.setText(theNewText);
        }
    }

    /**
     * Updates the text of the state FigText.
     */
    private void updateStateText() {
        if (isReadyToEdit()) {
            if (getOwner() == null) {
                return;
            }
            String theNewText = "";
            Object cis = Model.getFacade().getType(getOwner());
            if (Model.getFacade().isAClassifierInState(cis)) {
                theNewText = "[" + Notation.generate(this, cis) + "]";
            }
            state.setText(theNewText);
        }
    }

    ////////////////////////////////////////////////////////////////
    // Fig accessors

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#getNameFig()
     */
    public FigText getNameFig() {
        return null;
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#getName()
     */
    public String getName() {
        return null;
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#setName(java.lang.String)
     */
    public void setName(String n) {

    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineColor(java.awt.Color)
     */
    public void setLineColor(Color col) { cover.setLineColor(col); }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineColor()
     */
    public Color getLineColor() { return cover.getLineColor(); }

    /**
     * @see org.tigris.gef.presentation.Fig#setFillColor(java.awt.Color)
     */
    public void setFillColor(Color col) { cover.setFillColor(col); }

    /**
     * @see org.tigris.gef.presentation.Fig#getFillColor()
     */
    public Color getFillColor() { return cover.getFillColor(); }

    /**
     * @see org.tigris.gef.presentation.Fig#setFilled(boolean)
     */
    public void setFilled(boolean f) {
        cover.setFilled(f);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getFilled()
     */
    public boolean getFilled() {
        return cover.getFilled();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
     */
    public void setLineWidth(int w) {
        cover.setLineWidth(w);
    }

    /**
     * @see org.tigris.gef.presentation.Fig#getLineWidth()
     */
    public int getLineWidth() {
        return cover.getLineWidth();
    }

    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent ke) {
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
        classifier.keyPressed(ke);
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) throws PropertyVetoException {
        try {
            if (ft == classifier && this.getOwner() != null) {
                ParserDisplay.SINGLETON.parseObjectFlowState1(ft.getText(),
                    this.getOwner());
            } else if (ft == state && this.getOwner() != null) {
                ParserDisplay.SINGLETON.parseObjectFlowState2(ft.getText(),
                        this.getOwner());
            }
            ProjectBrowser.getInstance().getStatusBar().showStatus("");
        } catch (ParseException pe) {
            String msg = "statusmsg.bar.error.parsing.objectflowstate";
            Object[] args = {
                pe.getLocalizedMessage(),
                new Integer(pe.getErrorOffset()),
            };
            ProjectBrowser.getInstance().getStatusBar().showStatus(
                    Translator.messageFormat(msg, args));
            updateClassifierText();
            updateStateText();
        }
    }

} /* end class FigObjectFlowState */
