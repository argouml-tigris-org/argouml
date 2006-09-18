// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.tigris.gef.presentation.Fig;

public class Utilities {
    
    private Utilities() {
        
    }
    
    /**
     * Generate a description of a Fig that would be most meaningful to a
     * developer and the user.
     * This is used by the repair routines to describe the Fig that was repaired
     * <ul>
     * <li>FigComment - the text within body compartment of the Fig
     * <li>FigNodeModelElement -
     *        the text within the name compartment of the FigNode
     * <li>FigEdgeModelElement -
     *        the text within name compartment of the FigEdge and the
     *        descriptions of the adjoining FigNodes
     * </ul>
     * @param f the Fig to describe
     * @return The description as a String.
     */
    public static String debugDescription(Fig f) {
        String description = "\n" + f.getClass().getName();
        if (f instanceof FigComment) {
            description += " \"" + ((FigComment) f).getBody() + "\"";
        } else if (f instanceof FigNodeModelElement) {
            description += " \"" + ((FigNodeModelElement) f).getName() + "\"";
        } else if (f instanceof FigEdgeModelElement) {
            FigEdgeModelElement fe = (FigEdgeModelElement) f;
            description += " \"" + fe.getName() + "\"";
            String source;
            if (fe.getSourceFigNode() == null) {
                source = "(null)";
            } else {
                source =
                    ((FigNodeModelElement) fe.getSourceFigNode()).getName();
            }
            String dest;
            if (fe.getDestFigNode() == null) {
                dest = "(null)";
            } else {
                dest = ((FigNodeModelElement) fe.getDestFigNode()).getName();
            }
            description += " [" + source + "=>" + dest + "]";
        }
        return description + "\n";
    }


}
