// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

import javax.swing.Action;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.ui.CmdCreateNode;
import org.argouml.ui.CmdSetMode;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.argouml.uml.diagram.ui.ActionAddAssociation;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.diagram.ui.ActionAddAttribute;
import org.argouml.uml.diagram.ui.ActionAddOperation;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;

/**
 * @author jrobbins@ics.uci.edy
 */
public class UMLClassDiagram extends UMLDiagram {

    ////////////////
    // actions for toolbar
    // TODO: These should not be static.

    protected static Action _actionClass = new RadioAction(
	new CmdCreateNode(ModelFacade.CLASS, "Class"));

    protected static Action _actionObject = new RadioAction(
	new CmdCreateNode(ModelFacade.INSTANCE, "Instance"));

    protected static Action _actionInterface = new RadioAction(
	new CmdCreateNode(ModelFacade.INTERFACE, "Interface"));

    protected static Action _actionDepend = new RadioAction(
	new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass",
                    ModelFacade.DEPENDENCY, "Dependency"));

    protected static Action _actionPermission = new RadioAction(
	new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass", 
                    ModelFacade.PERMISSION, "Permission"));

    protected static Action _actionUsage = new RadioAction(
	new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass",
                    ModelFacade.USAGE, "Usage"));

    protected static Action _actionLink = new RadioAction(
	new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass",
                    ModelFacade.LINK, "Link"));

    protected static Action _actionGeneralize = new RadioAction(
	new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass",
                    ModelFacade.GENERALIZATION, "Generalization"));

    protected static Action _actionRealize = new RadioAction(
	new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass",
                    ModelFacade.ABSTRACTION, "Realization"));

    protected static Action _actionPackage = new RadioAction(
	new CmdCreateNode(ModelFacade.PACKAGE, "Package"));

    protected static Action _actionModel = new RadioAction(
	new CmdCreateNode(ModelFacade.MODEL, "Model"));

    protected static Action _actionSubsystem = new RadioAction(
	new CmdCreateNode(ModelFacade.SUBSYSTEM, "Subsystem"));

    protected static Action _actionAssociation = new RadioAction(
	new ActionAddAssociation(ModelFacade.NONE_AGGREGATIONKIND,
                    false, "Association"));
    protected static Action _actionAggregation = new RadioAction(
	new ActionAddAssociation(ModelFacade.AGGREGATE_AGGREGATIONKIND,
                    false, "Aggregation"));
    protected static Action _actionComposition = new RadioAction(
	new ActionAddAssociation(ModelFacade.COMPOSITE_AGGREGATIONKIND, 
                    false, "Composition"));
    protected static Action _actionUniAssociation = new RadioAction(
	new ActionAddAssociation(ModelFacade.NONE_AGGREGATIONKIND, 
                    true, "UniAssociation"));
    protected static Action _actionUniAggregation = new RadioAction(
	new ActionAddAssociation(ModelFacade.AGGREGATE_AGGREGATIONKIND, 
                    true, "UniAggregation"));
    protected static Action _actionUniComposition = new RadioAction(
	new ActionAddAssociation(ModelFacade.COMPOSITE_AGGREGATIONKIND, 
                    true, "UniComposition"));

    ////////////////////////////////////////////////////////////////
    // contructors
    protected static int _ClassDiagramSerial = 1;

    public UMLClassDiagram() {
        super();
    }

    public UMLClassDiagram(String name, Object m) {
        super(name, /*(MNamespace)*/ m);
    }

    public UMLClassDiagram(Object m) {
        this(getNewDiagramName(), m);
    }

    public void setNamespace(Object handle) {
        if (!ModelFacade.isANamespace(handle)) {
	    cat.error("Illegal argument. "
		      + "Object " + handle + " is not a namespace");
	    throw new IllegalArgumentException("Illegal argument. "
					       + "Object " + handle
					       + " is not a namespace");
        }
        Object m = /*(MNamespace)*/ handle;
        super.setNamespace(m);
        ClassDiagramGraphModel gm = new ClassDiagramGraphModel();
        gm.setNamespace(m);
	LayerPerspective lay =
	    new LayerPerspectiveMutable(ModelFacade.getName(m), gm);
	ClassDiagramRenderer rend = new ClassDiagramRenderer(); // singleton
	lay.setGraphNodeRenderer(rend);
	lay.setGraphEdgeRenderer(rend);
	setLayer(lay);
    }

    /**
     * Get the actions from which to create a toolbar or equivilent
     * graphic trigger.
     */
    protected Object[] getUmlActions() {
        Object actions[] = {
            _actionPackage,
            _actionClass,
            getAssociationActions(),
            _actionDepend,
            _actionPermission,
            _actionUsage,
            _actionGeneralize, null,
            _actionInterface,
            _actionRealize, null,
            ActionAddAttribute.SINGLETON,
            ActionAddOperation.SINGLETON, null,
            _actionComment,
            _actionCommentLink
        };

        return actions;
    }
    
    // remove the comment marks to enable models and subsystems.
    // also replace _actionPackage with getPackageActions() in getUmlActions().
    private Object[] getPackageActions() {
        /* subsystem, model enabled 
	   Object actions[] = 
	   { _actionPackage , 
	   _actionModel,
	   _actionSubsystem };
        */
        /* subsystem. model disabled */
	Object actions[] = {
	    _actionPackage 
	};
       
        return actions;
    }

    /**
     * Return an array of association actions in the
     * pattern of which to build a popup toolbutton
     */
    private Object[] getAssociationActions() {
        Object actions[][] = {
            {_actionAssociation, _actionUniAssociation},
            {_actionAggregation, _actionUniAggregation},
            {_actionComposition, _actionUniComposition}
        };

        return actions;
    }
    
    
    /**
     * Creates a new diagramname.
     * @return String
     */
    protected static String getNewDiagramName() {
        String name = null;
        name = "Class Diagram " + _ClassDiagramSerial;
        _ClassDiagramSerial++;
        if (!ProjectManager.getManager().getCurrentProject()
	        .isValidDiagramName(name)) {
            name = getNewDiagramName();
        }
        return name;
    }
} /* end class UMLClassDiagram */
