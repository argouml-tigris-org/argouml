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

//File: FigObjectFlowState.java
//Classes: FigObjectFlowState
//Original Author: MVW

package org.argouml.uml.diagram.activity.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.text.ParseException;
import java.util.Iterator;

import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;
import org.argouml.application.api.Notation;
import org.argouml.model.ModelFacade;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.generator.ParserDisplay;


/** Class to display graphics for a UML ObjectFlowState in a diagram. 
 * 
 * The NameFig of this class may either contain the Class name, or
 * it contains the name of the ClassifierInState AND its state.
 * In the examples in the UML standard, this is written like 
 * PurchaseOrder
 * [approved]
 * i.e. in 2 lines. The first line is underlined, 
 * to indicate that it is an instance (object).
 */
public class FigObjectFlowState extends FigNodeModelElement {
   
    private static final int PADDING = 8;
    private static final int OFFSET = 10;
    private static final int WIDTH = 90;
    private static final int HEIGHT = 50;
    
    private FigRect cover;
    private FigText state;

    ////////////////////////////////////////////////////////////////
    // constructors
    
    /**
     * Main Constructor FigObjectFlowState (called from file loading)
     */
    public FigObjectFlowState() {
        setBigPort(new FigRect(OFFSET, OFFSET, WIDTH, HEIGHT, 
                Color.cyan, Color.cyan));
        cover = new FigRect(OFFSET, OFFSET, WIDTH, HEIGHT, 
                Color.black, Color.white);
        getNameFig().setLineWidth(0);
        getNameFig().setFilled(false);
        getNameFig().setUnderline(true);
        getNameFig().setMultiLine(false);
        
        state = new FigText(OFFSET, OFFSET, WIDTH, 21); // values don't care
        state.setFont(getLabelFont());
        state.setTextColor(Color.black);
        state.setMultiLine(false);
        state.setAllowsTab(false);
        state.setLineWidth(0);
        state.setFilled(false);

        // add Figs to the FigNode in back-to-front order
        addFig(getBigPort());
        addFig(cover);
        addFig(getNameFig());
        addFig(state);
        
        Rectangle r = getBounds();
        setBounds(r.x, r.y, r.width, r.height);
    }
    
    /**
     * Constructor FigObjectFlowState that hooks the Fig into 
     * an existing UML model element
     * @param gm ignored!
     * @param node owner, i.e. the UML element
     */
    public FigObjectFlowState(GraphModel gm, Object node) {
        this();
        setOwner(node);
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
        Iterator it = figClone.getFigs(null).iterator();
        figClone.setBigPort((FigRect) it.next());
        figClone.cover = (FigRect) it.next();
        figClone.setNameFig((FigText) it.next());
        return figClone;
    }

    /**
     * Get the minimum size.
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

    /**
     * Override setBounds to keep shapes looking right.
     * The nameFig is nicely centered vertically, 
     * and stretched out over the full width, 
     * to allow easy selection with the mouse.
     * The Fig can only be shrinked to half its original size - so that 
     * it is not reduceable to a few pixels only.
     * 
     * @see org.tigris.gef.presentation.Fig#setBounds(int, int, int, int)
     */
    public void setBounds(int x, int y, int w, int h) {
        if (getNameFig() == null) return;
        Rectangle oldBounds = getBounds();
        
        Dimension nameDim = getNameFig().getMinimumSize();
        Dimension stateDim = state.getMinimumSize();
        /* the height of the blank space above and below the text figs: */
        int blank = (h - PADDING - nameDim.height - stateDim.height) / 2;
        getNameFig().setBounds(x + PADDING, 
                y + blank, 
                w - PADDING * 2, 
                nameDim.height);
        state.setBounds(x + PADDING,
                y + blank + nameDim.height + PADDING,
                w - PADDING * 2, 
                stateDim.height);
        
        getBigPort().setBounds(x, y, w, h);
        cover.setBounds(x, y, w, h);

        calcBounds(); 
        updateEdges();
        firePropChange("bounds", oldBounds, getBounds());
    }


    
    ////////////////////////////////////////////////////////////////
    // Fig accessors

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
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#textEdited(org.tigris.gef.presentation.FigText)
     */
    protected void textEdited(FigText ft) throws PropertyVetoException {
        try {
            if (ft == getNameFig() && this.getOwner() != null) { 
                ParserDisplay.SINGLETON.parseObjectFlowState1(ft.getText(), 
                    this.getOwner());
            } else if (ft == state && this.getOwner() != null) {  
                ParserDisplay.SINGLETON.parseObjectFlowState2(state.getText(), 
                        this.getOwner());
            }
            ProjectBrowser.getInstance().getStatusBar().showStatus("");
            //updateNameText();
        } catch (ParseException pe) {
            ProjectBrowser.getInstance().getStatusBar()
                .showStatus("Error: " + pe + " at " + pe.getErrorOffset());
        }
        //super.textEdited(ft);
    } 

} /* end class FigObjectFlowState */
