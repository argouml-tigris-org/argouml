// $Id$
// Copyright (c) 2006-2007 The Regents of the University of California. All
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
import org.argouml.uml.StereotypeUtility;
import org.argouml.uml.diagram.deployment.DeploymentDiagramGraphModel;
import org.argouml.uml.diagram.ui.SelectionNodeClarifiers2;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;

/**
 * The buttons on selection for a Stereotype. <p>
 * 
 * TODO: Find a way to hide the OperationsCompartment 
 * on the FigClass of the created metaclass.
 * 
 * @author michiel
 */
public class SelectionStereotype extends SelectionNodeClarifiers2 {

    private static Icon inheritIcon =
        ResourceLoaderWrapper.lookupIconResource("Generalization");
    private static Icon dependIcon =
        ResourceLoaderWrapper.lookupIconResource("Dependency");

    private boolean useComposite;
    
    private static Icon icons[] = 
    {inheritIcon,
     dependIcon,
     null,
     null,
     null,
    };

    // TODO: I18N required
    private static String instructions[] = 
    {"Add a baseClass",
     "Add a subStereotype",
     null,
     null,
     null,
     "Move object(s)",
    };

    /**
     * Construct a new  SelectionStereotype for the given Fig.
     *
     * @param f the given fig
     */
    public SelectionStereotype(Fig f) {
        super(f);
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        super.mouseEntered(me);
        useComposite = me.isShiftDown();
    }

    @Override
    protected Object getNewNode(int index) {
        if (index == 0) {
            index = getButton();
        }
        Object ns = Model.getFacade().getNamespace(getContent().getOwner());
        switch (index) {
        case 10:
            Object clazz = Model.getCoreFactory().buildClass(ns);
            StereotypeUtility.dealWithStereotypes(clazz, "metaclass", false);
            return clazz;
        case 11:
            Object st = 
                Model.getExtensionMechanismsFactory().createStereotype();
            Model.getCoreHelper().setNamespace(st, ns);
            return st;
        }
        return null;
    }

    @Override
    protected Object getNewNodeType(int index) {
        switch (index) {
        case 10:
            return Model.getMetaTypes().getClass();
        case 11:
            return Model.getMetaTypes().getStereotype();
        }
        return null;
    }

    @Override
    protected Object createEdgeAbove(MutableGraphModel mgm, Object newNode) {
        Object dep = super.createEdgeAbove(mgm, newNode);
        StereotypeUtility.dealWithStereotypes(dep, "stereotype", false);
        return dep;
    }

    @Override
    protected Icon[] getIcons() {
        // In the DeploymentDiagram there are no Generalizations
        if (!(Globals.curEditor().getGraphModel() 
                instanceof DeploymentDiagramGraphModel)) {
            return null;
        }
        return icons;
    }

    @Override
    protected String getInstructions(int index) {
        return instructions[index - BASE];
    }

    @Override
    protected Object getNewEdgeType(int index) {
        if (index == TOP) {
            return Model.getMetaTypes().getDependency();
        } else if (index == BOTTOM) {
            return Model.getMetaTypes().getGeneralization();
        }
        return null;
    }

    @Override
    protected boolean isReverseEdge(int index) {
        if (index == BOTTOM) {
            return true;
        }
        return false;
    }

    @Override
    protected boolean isEdgePostProcessRequested() {
        return useComposite;
    }

}
