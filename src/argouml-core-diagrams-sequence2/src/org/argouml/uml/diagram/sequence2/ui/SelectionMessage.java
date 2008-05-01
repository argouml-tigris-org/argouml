// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.uml.diagram.sequence2.ui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import org.argouml.model.Model;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.SelectionRerouteEdge;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.Handle;

/**
 * A custom select object to handle the special requirements of
 * rerouting, reshaping or dragging a message.
 * @author penyaskito
 */
public class SelectionMessage extends SelectionRerouteEdge {

    
    /**
     * The constructor
     * @param feme the fig.
     */
    public SelectionMessage(FigEdgeModelElement feme) {
        super(feme);
    }
    
    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_LEFT 
                || ke.getKeyCode() == KeyEvent.VK_RIGHT) {
            // we don't let the user move the messages horizontally.
            ke.consume();
        } else {
            handleMovement();
        }
    }       
    
    @Override
    public void mousePressed(MouseEvent me) {
	FigMessage message = (FigMessage) getContent(); 
	if (!message.isSelfMessage()) {
	    super.mousePressed(me);
	}
    }

    @Override
    public void mouseDragged(MouseEvent me) {
	FigMessage message = (FigMessage) getContent(); 
	if (message.isSelfMessage()) {
	    message.translate(0, me.getY() - message.getY());
	}
	else {
	    super.mouseDragged(me);
	}
	handleMovement();
    }

    @Override
    public void dragHandle(int x, int y, int w, int h, Handle handle) {
        FigMessage message = (FigMessage) getContent(); 
        if (!message.isSelfMessage()) {
            super.dragHandle(x, y, w, h, handle);
        }
    }
    
    private void handleMovement() {
        FigMessage message = (FigMessage) getContent();
        
        // if it is a create action, relocate its dest node.
        if (Model.getFacade().isACreateAction(message.getAction())) {
            ((FigClassifierRole) message.getDestFigNode()).relocate();
        }
        
        // we recalculate all the activations
        FigNode source = message.getSourceFigNode();
        if (source != null && source instanceof FigClassifierRole) {
            ((FigClassifierRole) source).createActivations();
        }
        FigNode dest = message.getDestFigNode();
        if (dest != null && !message.isSelfMessage() 
        	&& dest instanceof FigClassifierRole) {
            ((FigClassifierRole) dest).createActivations();
        }
    }
    
}
