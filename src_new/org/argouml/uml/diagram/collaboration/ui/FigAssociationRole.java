// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.uml.diagram.collaboration.ui;

import java.util.Iterator;
import java.util.Vector;

import org.argouml.application.api.Notation;
import org.argouml.uml.diagram.ui.FigAssociation;
import org.argouml.uml.diagram.ui.FigMessage;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.PathConvPercent;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;

import ru.novosoft.uml.MElementEvent;
public class FigAssociationRole extends FigAssociation {
    ////////////////////////////////////////////////////////////////
    // constructors
  
    protected FigMessageGroup _messages = new FigMessageGroup();

    public FigAssociationRole() {
	super(); // this really is questionable
	addPathItem(_messages, new PathConvPercent(this, 50, 10));
    }

    /**
     * Constructor for FigAssociationRole.
     * @param edge
     * @param lay
     */
    public FigAssociationRole(Object edge, Layer lay) {
	this();
	setLayer(lay);
    	setOwner(edge);
    }

    ////////////////////////////////////////////////////////////////
    // event handlers
    /**
     * calls the method on the "super" (FigAssociation)
     * and then changes the name to take care of the
     * "/ name : base association name" form.
     **/    
    protected void modelChanged(MElementEvent e) {
        super.modelChanged(e);
        //change the name
        Object ar = /*(MAssociationRole)*/ getOwner();
        if (ar == null) return;
        // String asNameStr = ((ar.getName() == null) && (ar.getBase()
        // == null)) ? "" : Notation.generate(this, ar);
        String asNameStr = Notation.generate(this, ar);
        _name.setText(asNameStr);
    }
    
    public void addMessage(FigMessage message) {
    	_messages.addFig(message);
    	// damage();
    	updatePathItemLocations();
    	_messages.damage();
    }

    /**
     * @see org.tigris.gef.presentation.Fig#delete()
     */
    public void delete() {
        super.delete();
        _messages.delete();
    }

} /* end class FigAssociationRole */

class FigMessageGroup extends FigGroup {
	
    /**
     * Constructor for FigMessageGroup.
     */
    public FigMessageGroup() {
	super();
    }

    /**
     * Constructor for FigMessageGroup.
     * @param figs
     */
    public FigMessageGroup(Vector figs) {
	super(figs);
    }

	
    protected void updateFigPositions() {
    	Vector figs = getFigs();
    	if (!figs.isEmpty()) {
	    FigMessage first = (FigMessage) figs.get(0);
	    for (int i = 0; i < figs.size(); i++) {
		FigMessage fig = (FigMessage) figs.get(i);
		fig.startTrans();
		fig.setX(getX());
		if (i != 0) {
		    fig.setY(((FigMessage) figs.get(i - 1)).getY()
			     + ((FigMessage) figs.get(i - 1)).getHeight()
			     + 5);
		} else {
		    fig.setY(getY());
		}
		fig.endTrans();
	    }
    	}
    }
    
    

    /**
     * @see org.tigris.gef.presentation.Fig#calcBounds()
     */
    public void calcBounds() {
	super.calcBounds();
	Vector figs = getFigs();
	if (!figs.isEmpty()) {
	    Fig last = (Fig) figs.lastElement();
	    Fig first = (Fig) figs.firstElement();
	    // _x = first.getX();
	    // _y = first.getY();
	    _h = last.getY() + last.getHeight() - first.getY();
	    _w = 0;
	    for (int i = 0; i < figs.size(); i++) {
		Fig fig = (Fig) figs.get(i);
		if (fig.getWidth() > _w) { 
		    _w = fig.getWidth();
		}
	    }
	} else {
	    _w = 0;
	    _h = 0;
	}
		
    }
	
	

    /**
     * @see org.tigris.gef.presentation.FigGroup#addFig(Fig)
     */
    public void addFig(Fig f) {
	super.addFig(f);
	updateFigPositions();
	calcBounds();
    }


    /**
     * @see org.tigris.gef.presentation.Fig#delete()
     */ 
    public void delete() { 
        Vector figs = getFigs();
        if (figs != null) {
            Iterator it = figs.iterator();
            while (it.hasNext()) {
                Fig fig = (Fig) it.next();
                fig.delete();
            }
        } 
        removeAll();
        super.delete();
    }


    /**
     * @see org.tigris.gef.presentation.Fig#dispose()
     */
    public void dispose() {
    	Vector figs = getFigs();
        if (figs != null) {
	    Iterator it = figs.iterator();
	    while (it.hasNext()) {
		Fig fig = (Fig) it.next();
		fig.dispose();
	    }
        }
        removeAll();
        super.dispose();
    }

}

