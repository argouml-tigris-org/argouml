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

import org.argouml.application.api.*;
import org.argouml.uml.diagram.ui.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;

import ru.novosoft.uml.foundation.core.*;

import java.awt.event.*;
import java.util.*;

/** deletes an modelelement from the diagram, but not from the model.
 *  @stereotype singleton
 */
public class ActionDeleteFromDiagram extends UMLChangeAction {

    ////////////////////////////////////////////////////////////////
    // static variables
    
    public static ActionDeleteFromDiagram SINGLETON = new ActionDeleteFromDiagram(); 


    ////////////////////////////////////////////////////////////////
    // constructors

    public ActionDeleteFromDiagram() { super("Remove From Diagram", NO_ICON); }


    ////////////////////////////////////////////////////////////////
    // main methods

    public boolean shouldBeEnabled() {
	int size = 0;
	try {
	    Editor ce = Globals.curEditor();
	    Vector figs = ce.getSelectionManager().getFigs();
	    size = figs.size();
	}
	catch(Exception e) {
	}
	return size > 0;
    }


    public void actionPerformed(ActionEvent ae) {
        int size = 0;
        try {
            Editor ce = Globals.curEditor();
            Vector figs = ce.getSelectionManager().getFigs();
            size = figs.size();
            for (int i = 0; i < size; i++) {
                Fig f = (Fig) figs.elementAt(i);
                /*
                if ( f instanceof FigNodeModelElement ) {
                    FigNodeModelElement fnme = (FigNodeModelElement) f;
                    Object owner = fnme.getOwner();
                    if (owner instanceof MModelElement) {
                        fnme.remove();
                    }
                }
                else if ( f instanceof FigEdgeModelElement ) {
                    FigEdgeModelElement fnme = (FigEdgeModelElement) f;
                    Object owner = fnme.getOwner();
                    if (owner instanceof MModelElement) {
                        fnme.();
                    }
                }
                else {
                    f.delete();
                }
                */
                f.delete();
            }
        }
        catch(Exception ex) {
            Argo.log.error("ActionDeleteFromDiagram.actionPerformed()Exception = ");
        }
    } 

}
