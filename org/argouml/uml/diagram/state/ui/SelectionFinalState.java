//Copyright (c) 1996-2002 The Regents of the University of California. All
//Rights Reserved. Permission to use, copy, modify, and distribute this
//software and its documentation without fee, and without a written
//agreement is hereby granted, provided that the above copyright notice
//and this paragraph appear in all copies.  This software program and
//documentation are copyrighted by The Regents of the University of
//California. The software program and documentation are supplied "AS
//IS", without any accompanying services from The Regents. The Regents
//does not warrant that the operation of the program will be
//uninterrupted or error-free. The end-user understands that the program
//was developed for research purposes and is advised not to rely
//exclusively on the program for any reason.  IN NO EVENT SHALL THE
//UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
//SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
//ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
//THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
//SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
//WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
//MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
//PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
//CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
//UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

//$Header $

package org.argouml.uml.diagram.state.ui;

import java.awt.Graphics;

import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ModeManager;
import org.tigris.gef.base.ModeModify;
import org.tigris.gef.base.SelectionManager;
import org.tigris.gef.presentation.Fig;

/**
 * Selection for a final state. Needed to be subclassed to prevent the selection
 * box to be drawn illegaly.
 * @author jaap.branderhorst@xs4all.nl
 */
public class SelectionFinalState extends SelectionState {

	/**
	 * Constructor for SelectionFinalState.
	 * @param f
	 */
	public SelectionFinalState(Fig f) {
		super(f);
	}

	/**
	 * @see org.tigris.gef.base.Selection#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		if (_paintButtons) {
			Editor ce = Globals.curEditor();
			SelectionManager sm = ce.getSelectionManager();
			if (sm.size() != 1)
				return;
			ModeManager mm = ce.getModeManager();
			if (mm.includes(ModeModify.class) && _pressedButton == -1)
				return;
			paintButtons(g);
		}
		((FigNodeModelElement) _content).paintClarifiers(g);
	}

}
