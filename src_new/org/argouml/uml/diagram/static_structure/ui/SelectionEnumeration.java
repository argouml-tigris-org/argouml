// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.static_structure.ui;

import org.argouml.model.Model;
import org.tigris.gef.presentation.Fig;

/**
 * The buttons on selection for an Enumeration.
 * 
 * @author Michiel
 */
class SelectionEnumeration extends SelectionDataType {

    /**
     * @param f the given fi
     */
    public SelectionEnumeration(Fig f) {
        super(f);
    }


    /*
     * @see org.argouml.uml.diagram.ui.SelectionNodeClarifiers#getInstructions(int)
     */
    protected String getInstructions(int i) {
        if (i == 10) {
            return "Add a super-enumeration";
        } else if (i == 10) {
            return "Add a sub-enumeration";
        } else if (i == -1) {
            return "Move object(s)";
        }
        return null;
    }

    /*
     * @see org.argouml.uml.diagram.static_structure.ui.SelectionDataType#getNewNode(int)
     */
    protected Object getNewNode(int buttonCode) {
        Object ns = Model.getFacade().getNamespace(getContent().getOwner());
        return Model.getCoreFactory().buildEnumeration("", ns);
    }

    /*
     * @see org.argouml.uml.diagram.static_structure.ui.SelectionDataType#getNewNodeType(int)
     */
    protected Object getNewNodeType(int buttonCode) {
        return Model.getMetaTypes().getEnumeration();
    }


}
