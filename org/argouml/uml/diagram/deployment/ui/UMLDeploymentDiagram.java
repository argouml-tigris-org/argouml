// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

// File: UMLDeploymentDiagram.java
// Classes: UmlDeploymentDiagram
// Author: Clemens Eichler (5eichler@informatik.uni-hamburg.de)

package org.argouml.uml.diagram.deployment.ui;

import java.beans.PropertyVetoException;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.deployment.DeploymentDiagramGraphModel;
import org.argouml.uml.diagram.ui.ActionAddAssociation;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.ui.ActionAddNote;

import org.tigris.gef.base.CmdSetMode;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;

import ru.novosoft.uml.behavior.common_behavior.MComponentInstance;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MNodeInstance;
import ru.novosoft.uml.behavior.common_behavior.MObject;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MComponent;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MNode;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;

public class UMLDeploymentDiagram extends UMLDiagram {
    protected static Logger cat =
        Logger.getLogger(UMLDeploymentDiagram.class);

    ////////////////
    // actions for toolbar

    protected static Action _actionMNode =
        new CmdCreateNode(MNode.class, "Node");

    protected static Action _actionMNodeInstance =
        new CmdCreateNode(MNodeInstance.class, "NodeInstance");

    protected static Action _actionMComponent =
        new CmdCreateNode(MComponent.class, "Component");

    protected static Action _actionMComponentInstance =
        new CmdCreateNode(MComponentInstance.class, "ComponentInstance");

    protected static Action _actionMClass =
        new CmdCreateNode(MClass.class, "Class");

    protected static Action _actionMInterface =
        new CmdCreateNode(MInterface.class, "Interface");

    protected static Action _actionMObject =
        new CmdCreateNode(MObject.class, "Object");

    protected static Action _actionMDependency =
        new CmdSetMode(
		       ModeCreatePolyEdge.class,
		       "edgeClass",
		       MDependency.class,
		       "Dependency");

    protected static Action _actionMAssociation =
        new CmdSetMode(
		       ModeCreatePolyEdge.class,
		       "edgeClass",
		       MAssociation.class,
		       "Association");

    protected static Action _actionMLink =
        new CmdSetMode(
		       ModeCreatePolyEdge.class,
		       "edgeClass",
		       MLink.class,
		       "Link");

    protected static Action _actionAssociation =
        new ActionAddAssociation(MAggregationKind.NONE, false, "Association");
    protected static Action _actionAggregation =
        new ActionAddAssociation(
				 MAggregationKind.AGGREGATE,
				 false,
				 "Aggregation");
    protected static Action _actionComposition =
        new ActionAddAssociation(
				 MAggregationKind.COMPOSITE,
				 false,
				 "Composition");
    protected static Action _actionUniAssociation =
        new ActionAddAssociation(MAggregationKind.NONE, true, "UniAssociation");
    protected static Action _actionUniAggregation =
        new ActionAddAssociation(
				 MAggregationKind.AGGREGATE,
				 true,
				 "UniAggregation");
    protected static Action _actionUniComposition =
        new ActionAddAssociation(
				 MAggregationKind.COMPOSITE,
				 true,
				 "UniComposition");

    ////////////////////////////////////////////////////////////////
    // contructors
    protected static int _DeploymentDiagramSerial = 1;

    public UMLDeploymentDiagram() {

        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) {
        }
    }

    public UMLDeploymentDiagram(MNamespace m) {
        this();
        setNamespace(m);
    }

    /** method to perform a number of important initializations of a
     * <I>Deployment Diagram</I>.
     * 
     * each diagram type has a similar <I>UMLxxxDiagram</I> class.
     *
     * @param m  MNamespace from the model in NSUML...
     * @modified changed <I>lay</I> from <I>LayerPerspective</I> to
     * <I>LayerPerspectiveMutable</I>.  This class is a child of
     * <I>LayerPerspective</I> and was implemented to correct some
     * difficulties in changing the model. <I>lay</I> is used mainly
     * in <I>LayerManager</I>(GEF) to control the adding, changing and
     * deleting layers on the diagram...
     *
     * @author psager@tigris.org Jan. 24, 2oo2
     */
    public void setNamespace(Object handle) {
        if (!ModelFacade.isANamespace(handle)) {
            cat.error("Illegal argument. Object " + handle
		      + " is not a namespace");
            throw new IllegalArgumentException("Illegal argument. Object "
					       + handle
					       + " is not a namespace");
        }
        MNamespace m = (MNamespace) handle;
        super.setNamespace(m);
        DeploymentDiagramGraphModel gm = new DeploymentDiagramGraphModel();
        gm.setNamespace(m);
        setGraphModel(gm);
        LayerPerspective lay = new LayerPerspectiveMutable(m.getName(), gm);
        setLayer(lay);
        DeploymentDiagramRenderer rend = new DeploymentDiagramRenderer();
        // singleton
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
    }

    /**
     * Get the actions from which to create a toolbar or equivilent
     * graphic triggers
     */
    protected Object[] getUmlActions() {
        Object actions[] = {
            _actionMNode,
            _actionMNodeInstance,
            _actionMComponent,
            _actionMComponentInstance,
            _actionMDependency,
            getAssociationActions(),
            _actionMObject,
            _actionMLink,
            null,
            ActionAddNote.SINGLETON
        };
        return actions;
    }

    private Object[] getAssociationActions() {
        Object actions[][] = {
        {_actionAssociation, _actionUniAssociation},
	{_actionAggregation, _actionUniAggregation},
	{_actionComposition, _actionUniComposition}
        };

        return actions;
    }
    

    static final long serialVersionUID = -375918274062198744L;

    /**
     * Creates a new diagramname.
     * @return String
     */
    protected static String getNewDiagramName() {
        String name = null;
        name = "Deployment Diagram " + _DeploymentDiagramSerial;
        _DeploymentDiagramSerial++;
        if (!ProjectManager.getManager().getCurrentProject()
                .isValidDiagramName(name)) {
            name = getNewDiagramName();
        }
        return name;
    }
} /* end class UMLDeploymentDiagram */
