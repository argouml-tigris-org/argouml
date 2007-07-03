// $Id: SelectionClass.java 12670 2007-05-26 21:06:27Z tfmorris $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import java.awt.event.MouseEvent;

import javax.swing.Icon;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.uml.diagram.deployment.DeploymentDiagramGraphModel;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.tigris.gef.base.Globals;
import org.tigris.gef.presentation.Fig;

/**
 * @author jrobbins@ics.uci.edu
 */
public class SelectionClass extends SelectionNodeClarifiers2 {

    private static Icon inherit =
        ResourceLoaderWrapper.lookupIconResource("Generalization");

    private static Icon assoc =
        ResourceLoaderWrapper.lookupIconResource("Association");

    private static Icon compos =
        ResourceLoaderWrapper.lookupIconResource("CompositeAggregation");

    private static Icon selfassoc =
        ResourceLoaderWrapper.lookupIconResource("SelfAssociation");

    private boolean useComposite;

    private static Icon icons[] = 
    {inherit,
     inherit,
     assoc,
     assoc,
     selfassoc,
    };
    
    // TODO: I18N required
    private static String instructions[] = 
    {"Add a superclass",
     "Add a subclass",
     "Add an associated class",
     "Add an associated class",
     "Add a self association",
     "Move object(s)",
    };

    private static Object edgeType[] = 
    {Model.getMetaTypes().getGeneralization(),
     Model.getMetaTypes().getGeneralization(),
     Model.getMetaTypes().getAssociation(),
     Model.getMetaTypes().getAssociation(),
     Model.getMetaTypes().getAssociation(),
    };

    /**
     * Construct a new SelectionClass for the given Fig.
     *
     * @param f The given Fig.
     */
    public SelectionClass(Fig f) { 
        super(f);
    }

    @Override
    protected Icon[] getIcons() {
        Icon workingIcons[] = new Icon[icons.length];
        System.arraycopy(icons, 0, workingIcons, 0, icons.length);

        // No Generalizations on Deployment Diagram
        if (Globals.curEditor().getGraphModel() 
                instanceof DeploymentDiagramGraphModel) {
            workingIcons[TOP - BASE] = null;
            workingIcons[BOTTOM - BASE] = null;
        }
        if (useComposite) {
            workingIcons[RIGHT - BASE] = compos;
            workingIcons[RIGHT - BASE] = compos;
        } 
        return workingIcons;
    }
    
    @Override
    protected String getInstructions(int index) {
        return instructions[index - BASE];
    }

    @Override
    protected Object getNewNodeType(int i) {
        return Model.getMetaTypes().getUMLClass();
    }

    @Override
    protected Object getNewEdgeType(int i) {
        if (i == 0) {
            i = getButton();
        }
        return edgeType[i - 10];
    }

    @Override
    protected boolean isReverseEdge(int i) {
        if (i == BOTTOM || i == LEFT) {
            return true;
        } 
        return false;
    }
    
    @Override
    protected boolean isDraggableHandle(int index) {
        // Self-association isn't draggable
        if (index == LOWER_LEFT) {
            return false;
        }
        return true;
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

    @Override
    protected Object getNewNode(int index) {
        return Model.getCoreFactory().buildClass();
    }

}
