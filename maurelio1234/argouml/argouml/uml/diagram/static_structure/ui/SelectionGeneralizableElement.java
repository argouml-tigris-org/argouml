// $Id: SelectionGeneralizableElement.java 12612 2007-05-13 18:30:40Z tfmorris $
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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.event.MouseEvent;

import javax.swing.Icon;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.diagram.deployment.DeploymentDiagramGraphModel;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.presentation.Fig;

/**
 * Buttons for a selected GeneralizableElement.
 * 
 * @author Tom Morris
 */
public abstract class SelectionGeneralizableElement extends
        SelectionNodeClarifiers2 {

    private static Icon inherit =
            ResourceLoaderWrapper.lookupIconResource("Generalization");

    private static Icon[] icons = 
    {inherit,
     inherit,
     null,
     null,
     null,
    };

    private static String[] instructions = 
    {"Add a supertype",
     "Add a subtype",
     null,
     null,
     null,
     "Move object(s)",
    };

    
    private boolean useComposite;

    /**
     * Construct a SelectionGeneralizableElement
     * 
     * @param f
     *            Fig for which to construct the selection object
     */
    public SelectionGeneralizableElement(Fig f) {
        super(f);
    }

    @Override
    protected Icon[] getIcons() {
        Editor ce = Globals.curEditor();
        GraphModel gm = ce.getGraphModel();
    
        // No generalizations in Deployment Diagrams
        if (gm instanceof DeploymentDiagramGraphModel) {
            return null;
        } else {
            return icons;
        }
    }

    @Override
    protected String getInstructions(int i) {
        return instructions[ i - BASE];
    }
    
    @Override
    protected Object getNewEdgeType(int i) {
        if (i == TOP || i == BOTTOM) {
            return Model.getMetaTypes().getGeneralization();
        }
        return null;
    }
    
    @Override
    protected boolean isReverseEdge(int i) {
        if (i == BOTTOM) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean isEdgePostProcessRequested() {
        return useComposite;
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        super.mouseEntered(me);
        useComposite = me.isShiftDown();
    }

}