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
import java.util.Iterator;

import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;
import org.argouml.uml.diagram.ui.FigNodeModelElement;


/** Class to display graphics for a UML ObjectFlowState in a diagram. 
 */
public class FigObjectFlowState extends FigNodeModelElement {
   
    private FigRect cover;
    private FigRect bigPort;

    ////////////////////////////////////////////////////////////////
    // constructors
    
    /**
     * Main Constructor FigObjectFlowState (called from file loading)
     */
    public FigObjectFlowState() {
        bigPort = new FigRect(10, 10, 90, 50, Color.cyan, Color.cyan);
        cover = new FigRect(10, 10, 90, 50, Color.black, Color.white);
        getNameFig().setLineWidth(0);
        getNameFig().setFilled(false);
        getNameFig().setUnderline(true);
        Dimension nameMin = getNameFig().getMinimumSize();
        getNameFig().setBounds(10, 10, nameMin.width + 20, nameMin.height);
        
        // add Figs to the FigNode in back-to-front order
        addFig(bigPort);
        addFig(cover);
        addFig(getNameFig());
        
        Rectangle r = getBounds();
        setBounds(r.x, r.y, nameMin.width, nameMin.height);
        
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
        figClone.bigPort = (FigRect) it.next();
        figClone.cover = (FigRect) it.next();
        figClone.setNameFig((FigText) it.next());
        return figClone;
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

    
    
} /* end class FigObjectFlowState */
