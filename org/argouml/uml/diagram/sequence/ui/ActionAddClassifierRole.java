// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.sequence.ui;

import org.argouml.model.Model;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.sequence.SequenceDiagramGraphModel;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.GraphModel;

/**
 * Action to add an object to a sequence diagram.
 *
 * @author jaap.branderhorst@xs4all.nl
 * @since Aug 11, 2003
 */
public class ActionAddClassifierRole extends CmdCreateNode {

    private static final long serialVersionUID = 1824497910678123381L;

    /**
     * The constructor.
     */
    public ActionAddClassifierRole() {
        super(Model.getMetaTypes().getClassifierRole(),
                "button.new-classifierrole");
    }

    /**
     * @see org.tigris.gef.graph.GraphFactory#makeNode()
     */
    public Object makeNode() {
        Object node = null;
        Editor ce = Globals.curEditor();
        GraphModel gm = ce.getGraphModel();
        if (gm instanceof SequenceDiagramGraphModel) {
            Object collaboration =
                ((SequenceDiagramGraphModel) gm).getCollaboration();
            node =
                Model.getCollaborationsFactory().buildClassifierRole(
                        collaboration);
            /*
            Model.getCoreHelper().setNamespace(
            	node,
            	Model.getFacade().getNamespace( collaboration));
            */
        } else {
            throw new IllegalStateException("Graphmodel is not a "
					    + "sequence diagram graph model");
        }
        return node;
    }

}
