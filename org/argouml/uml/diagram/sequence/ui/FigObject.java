// Copyright (c) 1996-2003 The Regents of the University of California. All
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
package org.argouml.uml.diagram.sequence.ui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

/**
 * @author jaap.branderhorst@xs4all.nl
 * Aug 11, 2003
 */
public class FigObject extends FigNodeModelElement implements MouseListener {
    
    /**
     * The defaultwidth of the object rectangle
     */
    public final static int DEFAULT_WIDTH = 50;
    
    /**
     * The distance between two rows in the object rectangle.
     */
    public final static int ROWDISTANCE = 2;
    
    /**
     * The defaultheight of the object rectangle. That's 3 times the rowheight + 
     * 3 times a distance of 2 between the rows + the stereoheight. 
     */
    public final static int DEFAULT_HEIGHT = 3 * ROWHEIGHT + 3 * ROWDISTANCE + STEREOHEIGHT;
        
    
    
    /**
     * The fig whose owner is the Object. Used in selections.
     */
    private Fig _bigPort;
    
    /**
     * The fig that's the upper rectangle (the object rectangle) itself. It contains
     * the stereotype textfield and the name textfield.
     */
    private FigRect _objectFig;
       

    /**
     * Default constructor. Constructs the object rectangle, the name box and the stereotype box.
     */
    public FigObject() {
        super();
        _bigPort = new FigRect(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        _objectFig = new FigRect(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, Color.black, Color.white);
        _stereo = new FigText(DEFAULT_WIDTH / 2, ROWHEIGHT + ROWDISTANCE, 0, 0, Color.black, "Dialog", 12, false);
        _stereo.setAllowsTab(false);
        _stereo.setEditable(false);
        _stereo.setText("");        
        _name = new FigText(DEFAULT_WIDTH / 2, 2 * ROWDISTANCE + STEREOHEIGHT + ROWHEIGHT, 0, 0, Color.black, "Dialog", 12, false);
        _name.setEditable(false);
        _name.setText("");
        _name.setAllowsTab(false);
        addFig(_bigPort);
        addFig(_objectFig);
        addFig(_stereo);
        addFig(_name);                
    }

    /**
     * @param gm
     * @param node
     */
    public FigObject(Object node) {
        this();
        setOwner(node);                
    }
              

    /**
     * When the mouse button is released, this fig will be moved into position
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent me) {        
        super.mouseReleased(me);
        Layer lay = Globals.curEditor().getLayerManager().getActiveLayer();
        if (lay instanceof SequenceDiagramLayout) {
        	((SequenceDiagramLayout)lay).putInPosition(this);
        }
    }

}
