// Copyright (c) 1996-01 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import org.argouml.ui.*;
import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import java.awt.event.*;
import java.util.*;


public class ActionZoom extends UMLAction {

    ////////////////////////////////////////////////////////////////
    // instance variables

    protected double _scale;

    ////////////////////////////////////////////////////////////////
    // constructors

    public ActionZoom(int scale_percent) {
	super("" + scale_percent + " %", NO_ICON);
	_scale = (double)scale_percent / 100.0;
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    /** 
     * We can always zoom.
     */
    public boolean shouldBeEnabled() {
	return true;
    }

    /** 
     * This action changes the scale factor of the editor and
     * triggers a redraw of the editor pane.
     */
    public void actionPerformed(ActionEvent ae) {

	Editor ce = Globals.curEditor();
	SelectionManager sm = ce.getSelectionManager();         

        // Get all the figures from the diagram.
        Vector nodes = ((Diagram)ProjectBrowser.TheInstance.getActiveDiagram()).getLayer().getContents();
        for(int i=0; i < nodes.size(); i++) {
	    sm.select((Fig)(nodes.elementAt(i)));  // Select all the figures in the diagram.
        }      

	sm.startTrans();    // Notify the selection manager that selected figures will be moved now.
	ce.setScale(_scale);
	sm.endTrans();      // Finish the transition.
	sm.deselectAll();   // Deselect all figures.
    }
} /* end class ActionLayout */
