// $Id$
// Copyright (c) 2003-2004 The Regents of the University of California. All
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
import org.argouml.ui.CmdSetMode;
import org.argouml.uml.diagram.deployment.DeploymentDiagramGraphModel;
import org.argouml.uml.diagram.ui.ActionAddAssociation;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;

/**
 * The base class of the deployment diagram.<p>
 *
 * Defines the toolbar, provides for its initialization and provides
 * constructors for a top level diagram and one within a defined
 * namespace.<p>
 *
 */
public class UMLDeploymentDiagram extends UMLDiagram {
    
    private static final Logger LOG = 
        Logger.getLogger(UMLDeploymentDiagram.class);

    ////////////////
    // actions for toolbar

    private static Action actionMNode = new RadioAction(
        new CmdCreateNode(ModelFacade.NODE, "Node"));

    private static Action actionMNodeInstance = new RadioAction(
        new CmdCreateNode(ModelFacade.NODE_INSTANCE, "NodeInstance"));

    private static Action actionMComponent = new RadioAction(
        new CmdCreateNode(ModelFacade.COMPONENT, "Component"));

    private static Action actionMComponentInstance = new RadioAction(
        new CmdCreateNode(ModelFacade.COMPONENT_INSTANCE, "ComponentInstance"));

    private static Action actionMClass = new RadioAction(
        new CmdCreateNode(ModelFacade.CLASS, "Class"));

    private static Action actionMInterface = new RadioAction(
        new CmdCreateNode(ModelFacade.INTERFACE, "Interface"));

    private static Action actionMObject = new RadioAction(
        new CmdCreateNode(ModelFacade.OBJECT, "Object"));

    private static Action actionMDependency = new RadioAction(
        new CmdSetMode(
            ModeCreatePolyEdge.class,
            "edgeClass",
            ModelFacade.DEPENDENCY,
            "Dependency"));

    private static Action actionMAssociation = new RadioAction(
        new CmdSetMode(
            ModeCreatePolyEdge.class,
            "edgeClass",
            ModelFacade.ASSOCIATION,
            "Association"));

    private static Action actionMLink = new RadioAction(
        new CmdSetMode(
            ModeCreatePolyEdge.class,
            "edgeClass",
            ModelFacade.LINK,
            "Link"));

    private static Action actionAssociation = new RadioAction(
        new ActionAddAssociation(
            ModelFacade.NONE_AGGREGATIONKIND,
            false,
            "Association"));
    private static Action actionAggregation = new RadioAction(
        new ActionAddAssociation(
            ModelFacade.AGGREGATE_AGGREGATIONKIND,
            false,
            "Aggregation"));
    private static Action actionComposition = new RadioAction(
        new ActionAddAssociation(
            ModelFacade.COMPOSITE_AGGREGATIONKIND,
            false,
            "Composition"));
    private static Action actionUniAssociation = new RadioAction(
        new ActionAddAssociation(
            ModelFacade.NONE_AGGREGATIONKIND,
            true,
            "UniAssociation"));
    private static Action actionUniAggregation = new RadioAction(
        new ActionAddAssociation(
            ModelFacade.AGGREGATE_AGGREGATIONKIND,
            true,
            "UniAggregation"));
    private static Action actionUniComposition = new RadioAction(
        new ActionAddAssociation(
            ModelFacade.COMPOSITE_AGGREGATIONKIND,
            true,
            "UniComposition"));

    ////////////////////////////////////////////////////////////////
    // contructors
    private static int deploymentDiagramSerial = 1;

    /**
     * Constructor
     */
    public UMLDeploymentDiagram() {

        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) { }
    }

    /**
     * @param namespace the namespace for the new diagram
     */
    public UMLDeploymentDiagram(Object namespace) {
        this();
        setNamespace(namespace);
    }

    /** 
     * Method to perform a number of important initializations of a
     * <I>Deployment Diagram</I>.<p>
     * 
     * Each diagram type has a similar <I>UMLxxxDiagram</I> class.<p>
     *
     * Changed <I>lay</I> from <I>LayerPerspective</I> to
     * <I>LayerPerspectiveMutable</I>.  This class is a child of
     * <I>LayerPerspective</I> and was implemented to correct some
     * difficulties in changing the model. <I>lay</I> is used mainly
     * in <I>LayerManager</I>(GEF) to control the adding, changing and
     * deleting layers on the diagram...<p>
     *
     * @param handle MNamespace from the model in NSUML...
     * @author psager@tigris.org Jan. 24, 2002
     */
    public void setNamespace(Object handle) {
        if (!ModelFacade.isANamespace(handle)) {
            LOG.error(
                "Illegal argument. Object " + handle + " is not a namespace");
            throw new IllegalArgumentException(
                "Illegal argument. Object " + handle + " is not a namespace");
        }
        Object m = /*(MNamespace)*/ handle;
        super.setNamespace(m);
        DeploymentDiagramGraphModel gm = new DeploymentDiagramGraphModel();
        gm.setNamespace(m);
        LayerPerspective lay =
            new LayerPerspectiveMutable(ModelFacade.getName(m), gm);
        DeploymentDiagramRenderer rend = new DeploymentDiagramRenderer();
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
        setLayer(lay);

        // singleton

    }

    /**
     * Get the actions from which to create a toolbar or equivilent
     * graphic triggers
     *
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getUmlActions()
     */
    protected Object[] getUmlActions() {
        Object actions[] = {
	    actionMNode,
	    actionMNodeInstance,
	    actionMComponent,
	    actionMComponentInstance,
	    actionMDependency,
	    getAssociationActions(),
	    actionMObject,
	    actionMLink,
	    null,
	    actionComment,
            actionCommentLink};
        return actions;
    }

    private Object[] getAssociationActions() {
        Object actions[][] = {
	    {actionAssociation, actionUniAssociation },
	    {actionAggregation, actionUniAggregation },
	    {actionComposition, actionUniComposition }
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
        name = "Deployment Diagram " + deploymentDiagramSerial;
        deploymentDiagramSerial++;
        if (!ProjectManager.getManager().getCurrentProject()
	        .isValidDiagramName(name)) {
            name = getNewDiagramName();
        }
        return name;
    }
} /* end class UMLDeploymentDiagram */
