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

// File: FigComment.java
// Classes: FigComment
// Original Author: a_rueckert@gmx.net
// $Id$

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import ru.novosoft.uml.foundation.core.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.behavior.state_machines.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;

import org.apache.commons.logging.Log;
import org.argouml.kernel.*;  
import org.argouml.ui.*;
import org.argouml.uml.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.state.*;
import org.argouml.uml.diagram.state.ui.*;
import org.argouml.model.uml.UmlFactory;


/** 
 * Class to display a UML note in a diagram 
 * Since we don't need stereotypes for the note and an
 * empty stereotype textfield causes problems with the
 * note layout, I subclass FigNode instead of FigNodeModelElement.
 */
public class FigComment extends FigNodeModelElement implements VetoableChangeListener, DelayedVChangeListener, MouseListener, KeyListener, PropertyChangeListener {
    protected static Log logger = org.apache.commons.logging.LogFactory.getLog(FigComment.class);


    ////////////////////////////////////////////////////////////////
    // constants

    // public final int MARGIN = 2;
    public int x = 0;
    public int y = 0;
    public int width = 80;
    public int height = 60;
    public int gapY = 10;

    protected boolean _readyToEdit = true;     

    public static Font LABEL_FONT;
    public static Font ITALIC_LABEL_FONT;
    public final int MARGIN = 2;

    static {
	LABEL_FONT = MetalLookAndFeel.getSubTextFont();
	ITALIC_LABEL_FONT = new Font(LABEL_FONT.getFamily(),
				     Font.ITALIC, LABEL_FONT.getSize());
    }                                                                     

    ////////////////////////////////////////////////////////////////
    // instance variables

    // The model element the note is attached to.
    private MModelElement _noteOwner = null;

    // The figure that holds the text of the note.
    FigText _text;

    /** UML does not really use ports, so just define one big one so
     *  that users can drag edges to or from any point in the icon. */
    FigRect _bigPort;

    FigPoly _body;
    FigPoly _urCorner;

    ////////////////////////////////////////////////////////////////
    // constructors

    public FigComment() {
	_body = new FigPoly( Color.black, Color.white);
	_body.addPoint(x, y);
	_body.addPoint(x + width - 1 - gapY, y);
	_body.addPoint(x + width - 1, y + gapY);
	_body.addPoint(x + width - 1, y + height - 1);
	_body.addPoint(x, y + height - 1);
	_body.addPoint(x, y);
	_body.setFilled(true);
	_body.setLineWidth(1);
	
	_urCorner = new FigPoly(Color.black, Color.white);
	_urCorner.addPoint(x + width - 1 - gapY, y);
	_urCorner.addPoint(x + width - 1, y + gapY);
	_urCorner.addPoint(x + width - 1 - gapY, y + gapY);
	_urCorner.addPoint(x + width - 1 - gapY, y);
	_urCorner.setFilled(true);
	_urCorner.setLineWidth(1);

	_bigPort = new FigRect(x, y, width, height, null, null);     
	_bigPort.setFilled(false);
	_bigPort.setLineWidth(0);

	_text = new FigText(2, 2, width - 2 - gapY, height - 4, true);
	_text.setFont(LABEL_FONT);
	_text.setTextColor(Color.black);
	_text.setMultiLine(true);
	_text.setAllowsTab(false);
	// _text.setText(placeString());        
	_text.setJustification(FigText.JUSTIFY_LEFT);
	_text.setFilled(false);
	_text.setLineWidth(0);
	//_text.setLineColor(Color.white);

	// add Figs to the FigNode in back-to-front order
	addFig(_bigPort); 
	addFig(_body);
	addFig(_urCorner);
	addFig(_text);

	setBlinkPorts(false); //make port invisble unless mouse enters
	Rectangle r = getBounds();
	setBounds(r.x, r.y, r.width, r.height);
	updateEdges();

	_readyToEdit = false;
    }

    /**
     * Construct a new note
     *
     * @param gm The graphmodel
     * @param node The underlying MComment node
     */
    public FigComment(GraphModel gm, Object node) {
	this();
	setOwner(node);
	modelChanged(); 
    }

    /**
     * Create a note for a given model element.
     *
     * @param element The annotated model element.
     */
    public FigComment(MModelElement element) {
	this();                                     // Construct the figure.
	MComment node = UmlFactory.getFactory().getCore().createComment();         // Create a new Comment node.
	setOwner(node);                             // Set it as the owner of the figure.
	element.addComment(node);                   // Tell the annotated element, that it has a comment now.

	// Notes in state diagrams need a special treatment, cause
	// the nodes in them don't necessary have a namespace, where
	// we could add the note. So I added this hack... :-(
	// Andreas Rueckert <a_rueckert@gmx.net>
	if(element instanceof MStateVertex) { 

	    ProjectBrowser pb = ProjectBrowser.TheInstance;    // If the current target is a state diagram, we have to
	    if (pb.getTarget() instanceof UMLStateDiagram) {   // check, if we are editing the diagram.
		StateDiagramGraphModel gm = (StateDiagramGraphModel)(((UMLStateDiagram)pb.getTarget()).getGraphModel());
		node.setNamespace(gm.getNamespace());  // We are editing, so we set the Namespace directly.
	    }
	} else {
	    node.setNamespace(element.getNamespace());  // Add the comment to the same namespace as the annotated element.
	}

	storeNote( placeString());                  // Set the default text for this figure type.
    }

    /**
     * Get the default text for this figure. 
     *   
     * @return The default text for this figure.
     */
    public String placeString() { 
        String placeString = retrieveNote();
        if (placeString == null) placeString = "new note"; 
        return placeString;
    }

    /**
     * Clone this figure.
     *
     * @return The cloned figure.
     */
    public Object clone() {
	FigComment figClone = (FigComment)super.clone();
	Vector v = figClone.getFigs();
	figClone._bigPort = (FigRect)v.elementAt(0);    
	figClone._body = (FigPoly)v.elementAt(1);
	figClone._urCorner = (FigPoly)v.elementAt(2);
	figClone._text = (FigText) v.elementAt(3);
	return figClone;
    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    /**
     * See FigNodeModelElement.java for more info on these methods.
     */

    /** If the user double clicks on any part of this FigNode, pass it
     *  down to one of the internal Figs.  This allows the user to
     *  initiate direct text editing. */
    public void mouseClicked(MouseEvent me) {
	if (!_readyToEdit) {
	    if (getOwner() instanceof MModelElement) {
		MModelElement own = (MModelElement)getOwner();
		storeNote("");
		_readyToEdit = true;
	    } else {
		logger.debug("not ready to edit note");
		return;
	    }
	}
	if (me.isConsumed()) return;
	if (me.getClickCount() >= 2 &&
	    !(me.isPopupTrigger() || me.getModifiers() == InputEvent.BUTTON3_MASK)) {
	    if (getOwner() == null) return;
	    Fig f = hitFig(new Rectangle(me.getX() - 2, me.getY() - 2, 4, 4));
	    if (f instanceof MouseListener) ((MouseListener)f).mouseClicked(me);
	}       
	me.consume();
    }

    public void vetoableChange(PropertyChangeEvent pce) {
	Object src = pce.getSource();
	if (src == getOwner()) {
	    DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
	    SwingUtilities.invokeLater(delayedNotify);
	}
	else logger.debug("FigNodeModelElement got vetoableChange"+
				" from non-owner:" + src);
    }     

    public void delayedVetoableChange(PropertyChangeEvent pce) {
	Object src = pce.getSource();
	startTrans();
	// update any text, colors, fonts, etc.
	modelChanged();
	// update the relative sizes and positions of internel Figs
	updateBounds();
	endTrans();
    }

    public void propertyChange(PropertyChangeEvent pve) {
	Object src = pve.getSource();
	String pName = pve.getPropertyName();
	if (pName.equals("editing") && Boolean.FALSE.equals(pve.getNewValue())) {
	    try {
		startTrans();
		//parse the text that was edited
		textEdited((FigText)src);
		// resize the FigNode to accomodate the new text
		Rectangle bbox = getBounds();
		Dimension minSize = getMinimumSize();
		bbox.width = Math.max(bbox.width, minSize.width);
		bbox.height = Math.max(bbox.height, minSize.height);
		setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
		endTrans();
	    }
	    catch (PropertyVetoException ex) {
                logger.error("could not parse and use the text entered in figcomment", ex);
	    }
	}
	else super.propertyChange(pve);
    }  

    public void keyPressed(KeyEvent ke) {
	if (!_readyToEdit) {
	    if (getOwner() instanceof MModelElement) {
		storeNote("");
		_readyToEdit = true;
	    } else {
		logger.debug("not ready to edit note");
		return;
	    }
	}
	if (ke.isConsumed()) return;
	if (getOwner() == null) return;
	_text.keyPressed(ke);
    }

    /** not used, do nothing. */
    public void keyReleased(KeyEvent ke) { }

    public void keyTyped(KeyEvent ke) {
    }                         
        
    ////////////////////////////////////////////////////////////////
    // Fig accessors

    public void setLineColor(Color col) {
	// The _text element has no border, so the line color doesn't matter.
	_body.setLineColor(col);
	_urCorner.setLineColor(col);
    }

    public Color getLineColor() { return _body.getLineColor(); }

    public void setFillColor(Color col) {
	_body.setFillColor(col);
	_urCorner.setFillColor(col);
    }

    public Color getFillColor() { return _body.getFillColor(); }
    
    public void setFilled(boolean f) {
	_text.setFilled(false);  // The text is always opaque.
	_body.setFilled(f);
	_urCorner.setFilled(f);
    }
    
    public boolean getFilled() { return _body.getFilled(); }

    public void setLineWidth(int w) {
	_text.setLineWidth(0);  // Make a seamless integration of the text
	                        // in the note figure.
	_body.setLineWidth(w);
	_urCorner.setLineWidth(w);
    }
    
    public int getLineWidth() { return _body.getLineWidth(); }

    ////////////////////////////////////////////////////////////////
    // user interaction methods

    protected void textEdited(FigText ft) throws PropertyVetoException {
	if(ft == _text)
	    storeNote( ft.getText());
    }     
    
    public void setEnclosingFig(Fig encloser) {
	super.setEnclosingFig(encloser);
    }

    ////////////////////////////////////////////////////////////////
    // accessor methods

    /**
     * Store a note in the associated model element.
     *
     * @param note The note to store.
     */
    public final void storeNote(String note) {
	if(getOwner() != null)
	    ((MModelElement)getOwner()).setName( note);
    }

    /**
     * Retrieve the note from the associated model element.
     *
     * @return The note from the associated model element.
     */
    public final String retrieveNote() {
	return (getOwner() != null) ? ((MModelElement)getOwner()).getName() : null;
    }

    public boolean getUseTrapRect() { return true; }

    /**
     * Get the minimum size for the note figure.
     *
     * @return The minimum size for the note figure.
     */
    public Dimension getMinimumSize() {

	// Get the size of the text field.
	Dimension textMinimumSize = _text.getMinimumSize();

	// And add the gaps around the textfield to get the minimum
	// size of the note.
	return new Dimension(textMinimumSize.width + 4 + gapY, textMinimumSize.height + 4);
    }

    public void setBounds(int x, int y, int w, int h) {
	if (_text == null) return;

	Rectangle oldBounds = getBounds();

	// Resize the text figure
	_text.setBounds(x + 2, y  + 2 , w - 4 - gapY , h - 4);

	// Resize the big port around the figure
	_bigPort.setBounds(x, y, w, h);

	// Since this is a complex polygon, there's no easy way to resize it.
	Polygon newPoly = new Polygon();
	newPoly.addPoint(x, y);
	newPoly.addPoint(x + w - 1 - gapY, y);
	newPoly.addPoint(x + w - 1, y + gapY);
	newPoly.addPoint(x + w - 1, y + h - 1);
	newPoly.addPoint(x, y + h - 1);
	newPoly.addPoint(x, y);
	_body.setPolygon(newPoly);

	// Just move the corner to it's new position.
	_urCorner.setBounds(x + w - 1 - gapY, y, gapY, gapY);

	calcBounds(); //_x = x; _y = y; _w = w; _h = h;
	firePropChange("bounds", oldBounds, getBounds());
    }

    protected void updateBounds() {
	Rectangle bbox = getBounds();
	Dimension minSize = getMinimumSize();
	bbox.width = Math.max(bbox.width, minSize.width);
	bbox.height = Math.max(bbox.height, minSize.height);
	setBounds(bbox.x, bbox.y, bbox.width, bbox.height);
    }    

    ///////////////////////////////////////////////////////////////////
    // Internal methods

    

    /** 
     * This is called aftern any part of the UML MModelElement has
     * changed. This method automatically updates the note FigText. 
     */
    protected final void modelChanged() {
        super.modelChanged();
	
	    String noteStr = retrieveNote();
	    if(noteStr != null)
		_text.setText(noteStr);
	
    }
    

} /* end class FigComment */
