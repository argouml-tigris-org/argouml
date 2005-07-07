// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.awt.Point;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import org.argouml.i18n.Translator;
import org.tigris.gef.base.Command;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ModePlace;
import org.tigris.gef.graph.GraphFactory;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;

/**
* ActionAddExistingNode enables pasting of an existing node into a Diagram.
*
* @author Eugenio Alvarez
* Data Access Technologies.
*/
public class AddExistingNodeCommand implements Command, GraphFactory {

    ////////////////////////////////////////////////////////////////
    // instance variables
    
    /**
     * The UML object to be added to the diagram.
     */
    private Object object;
    
    private DropTargetDropEvent dropEvent;
    
    public AddExistingNodeCommand(Object o) {
        object = o;
    }

    public AddExistingNodeCommand(Object o, DropTargetDropEvent event) {
        object = o;
        dropEvent = event;
    }

    ////////////////////////////////////////////////////////////////
    // constructor

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void execute() {
        Editor ce = Globals.curEditor();
        GraphModel gm = ce.getGraphModel();
        if (!(gm instanceof MutableGraphModel)) return;

        String instructions = null;
        if (object != null) {
            instructions =
                Translator.localize("misc.message.click-on-diagram-to-add")
                    + object.toString();
            Globals.showStatus(instructions);
        }
        ModePlace placeMode = new ModePlace(this, instructions);
        placeMode.setAddRelatedEdges(true);

        if (dropEvent == null) {
            Globals.mode(placeMode, false);
        } else {
            MouseEvent me = new MouseEvent(
                    ce.getJComponent(),
                    0,
                    0,
                    0,
                    dropEvent.getLocation().x,
                    dropEvent.getLocation().y,
                    0,
                    false);
            placeMode.mousePressed(me);
            me = new MouseEvent(
                    ce.getJComponent(),
                    0,
                    0,
                    0,
                    dropEvent.getLocation().x,
                    dropEvent.getLocation().y,
                    0,
                    false);
            placeMode.mouseReleased(me);
        }
    }

    ////////////////////////////////////////////////////////////////
    // GraphFactory implementation

    /**
     * @see org.tigris.gef.graph.GraphFactory#makeGraphModel()
     */
    public GraphModel makeGraphModel() { return null; }

    /**
     * @see org.tigris.gef.graph.GraphFactory#makeEdge()
     */
    public Object makeEdge() { return null; }

    /**
     * @see org.tigris.gef.graph.GraphFactory#makeNode()
     */
    public Object makeNode() {
        return object;
    }

} /* end class ActionAddExistingNode */
