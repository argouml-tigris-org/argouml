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

package org.argouml.uml.ui;

import org.argouml.uml.diagram.static_structure.ui.*;
import org.argouml.ui.*;
import org.tigris.gef.base.*;
import org.tigris.gef.graph.*;
import org.tigris.gef.presentation.*;
import java.awt.event.*;
import java.util.*;

/** An action that makes all edges on the selected node visible/not visible
 *  on the diagram.
 *
 * <p>$Id$
 *
 * @author David Manura
 * @since 0.13.5
 */

public class ActionEdgesDisplay extends UMLAction {

    ////////////////////////////////////////////////////////////////
    // static variables

    // compartments
    public static UMLAction ShowEdges
        = new ActionEdgesDisplay(true, "Show All Edges");
    public static UMLAction HideEdges
        = new ActionEdgesDisplay(false, "Hide All Edges");

    private boolean _show;

    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionEdgesDisplay(boolean show, String desc) {
        super(desc, NO_ICON);
        _show = show;
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent ae) {
        ProjectBrowser pb = ProjectBrowser.getInstance();
        ArgoDiagram d = pb.getActiveDiagram();
        Editor ce = Globals.curEditor();
        MutableGraphModel mgm = (MutableGraphModel) ce.getGraphModel();

        Enumeration e = ce.getSelectionManager().selections().elements();
        // note: multiple selection not currently supported (2002-04-05)
        while (e.hasMoreElements()) {
            Selection sel = (Selection) e.nextElement();
            Object owner = sel.getContent().getOwner();

            if (_show) { // add
                mgm.addNodeRelatedEdges(owner);
            }
            else { // remove
                Vector edges = mgm.getInEdges(owner);
                edges.addAll(mgm.getOutEdges(owner));
                Enumeration e2 = edges.elements();
                while (e2.hasMoreElements()) {
                    Object edge = e2.nextElement();
                    Fig fig = d.presentationFor(edge);
                    if (fig != null)
                        fig.delete();
                }
            }
        }
    }

    public boolean shouldBeEnabled() { 
        return true; 
    }

} /* end class ActionEdgesDisplay */



