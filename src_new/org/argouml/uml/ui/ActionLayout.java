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

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;

import org.argouml.ui.*;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.diagram.static_structure.ui.*;
import org.argouml.uml.diagram.static_structure.layout.*;

import java.awt.event.*;
import java.util.Vector;

public class ActionLayout extends UMLAction {

    ////////////////////////////////////////////////////////////////
    // instance variables

    protected String _tabName;


    ////////////////////////////////////////////////////////////////
    // constructors

    public ActionLayout(String tabName) {
	super(tabName, NO_ICON);
	_tabName = tabName;
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    /** check whether we deal with a supported diagram type 
     * (currently only UMLClassDiagram).
     * Incremental Layout is not implemented for any diagram type,
     * so it is greyed out.
     * @see org.argouml.ui.ProjectBrowser
     */
    public boolean shouldBeEnabled() {
	return (super.shouldBeEnabled() 
		&& (ProjectBrowser.TheInstance.getActiveDiagram() instanceof UMLClassDiagram)
        && "action.layout-automatic".equals(_tabName));
    }

    /** This action performs the layout and triggers a redraw
     * of the editor pane.
     */
    public void actionPerformed(ActionEvent ae) {
	ClassdiagramLayouter layouter = 
	    new ClassdiagramLayouter((UMLDiagram)ProjectBrowser.
				     TheInstance.getActiveDiagram());

	Editor ce = Globals.curEditor();
	SelectionManager sm = ce.getSelectionManager();         

        // Get all the figures from the diagram.
        Vector nodes = ((UMLClassDiagram)ProjectBrowser.TheInstance.getActiveDiagram()).getLayer().getContents();
        for(int i=0; i < nodes.size(); i++) {
	    sm.select((Fig)(nodes.elementAt(i)));  // Select all the figures in the diagram.
        }      

	sm.startTrans();    // Notify the selection manager that selected figures will be moved now.
	layouter.layout();  // Compute a new layout.
	sm.endTrans();      // Finish the transition.
	sm.deselectAll();   // Deselect all figures.
    }
} /* end class ActionLayout */
