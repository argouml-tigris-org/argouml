/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel van der Wulp
 *    Bob Tarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.uml.diagram.use_case.ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.model.UseCaseDiagram;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.diagram.DiagramElement;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.static_structure.ui.FigPackage;
import org.argouml.uml.diagram.ui.ActionAddExtensionPoint;
import org.argouml.uml.diagram.ui.ActionSetAddAssociationMode;
import org.argouml.uml.diagram.ui.ActionSetMode;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.RadioAction;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.diagram.use_case.UseCaseDiagramGraphModel;
import org.argouml.util.ToolBarUtility;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.LayerPerspectiveMutable;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.presentation.FigNode;

/**
 * The base class of the use case diagram.<p>
 *
 * Defines the toolbar, provides for its initialization and provides
 * constructors for a top level diagram and one within a defined
 * namespace.<p>
 *
 * A use case diagram has as owner either a package or a classifier.
 */
public class UMLUseCaseDiagram extends UMLDiagram implements UseCaseDiagram {

    private static final Logger LOG =
        Logger.getLogger(UMLUseCaseDiagram.class.getName());

    // Actions specific to the use case diagram toolbar

    /**
     * Tool to add an actor node.<p>
     */
    private Action actionActor;

    /**
     * Tool to add a use case node.<p>
     */
    private Action actionUseCase;

    /**
     * Tool to create an association between UML artifacts using a
     * polyedge.<p>
     */
    private Action actionAssociation;
    private Action actionAggregation;
    private Action actionComposition;
    private Action actionUniAssociation;
    private Action actionUniAggregation;
    private Action actionUniComposition;

    /**
     * Tool to create a generalization between UML artifacts using a
     * polyedge.<p>
     */
    private Action actionGeneralize;

    /**
     * Tool to create an extend relationship between UML use cases
     * using a polyedge.<p>
     */
    private Action actionExtend;

    /**
     * Tool to create an include relationship between UML use cases
     * using a polyedge.<p>
     */
    private Action actionInclude;

    /**
     * Tool to create a dependency between UML artifacts using a
     * polyedge.<p>
     */
    private Action actionDependency;

    private Action actionExtensionPoint;

    /**
     * Construct a new use case diagram with no defined namespace.<p>
     *
     * Note we must never call this directly, since defining the
     * namespace is what makes everything work. However GEF will call
     * it directly when loading a new diagram, so it must remain
     * public.<p>
     *
     * A unique name is constructed by using the serial index.
     * We allow for the possibility
     * that setting this may fail, in which case no name is set.<p>
     * @deprecated ony for use by PGML parser.
     */
    @Deprecated
    public UMLUseCaseDiagram() {
        super(new UseCaseDiagramGraphModel());
        try {
            setName(getNewDiagramName());
        } catch (PropertyVetoException pve) { }
    }

    /**
     * Construct a new use case diagram with in a defined namespace.<p>
     *
     * Invokes the generic constructor {@link #UMLUseCaseDiagram()},
     * then intialises the namespace (which initializes all the
     * graphics).<p>
     *
     * This is the constructor which should always be used.<p>
     *
     * @param m  the desired namespace for this diagram.
     */
    public UMLUseCaseDiagram(Object m) {
        this();
        if (!Model.getFacade().isANamespace(m)) {
            throw new IllegalArgumentException();
        }
        setNamespace(m);
    }

    /**
     * Constructor.
     *
     * @param name the name for the diagram
     * @param namespace the namespace for the diagram
     */
    public UMLUseCaseDiagram(String name, Object namespace) {
        this(namespace);
        if (!Model.getFacade().isANamespace(namespace)) {
            throw new IllegalArgumentException();
        }
        try {
            setName(name);
        } catch (PropertyVetoException v) { }
    }

    /**
     * Perform a number of important initializations of a <em>Use Case
     * Diagram</em>.<p>
     *
     * Creates a new graph model for the diagram, settings its
     * namespace to that supplied.<p>
     *
     * Changed <em>lay</em> from <em>LayerPerspective</em> to
     * <em>LayerPerspectiveMutable</em>. This class is a child of
     * <em>LayerPerspective</em> and was implemented to correct some
     * difficulties in changing the model. <em>lay</em> is used mainly
     * in <em>LayerManager</em>(GEF) to control the adding, changing
     * and deleting of items in a layer of the diagram.<p>
     *
     * Set a renderer suitable for the use case diagram.<p>
     *
     * <em>Note</em>. This is declared as public. Not clear that other
     * classes should be allowed to invoke this method.<p>
     *
     * @param handle Namespace to be used for this diagram.
     *
     * @author   psager@tigris.org  Jan 24, 2002
     */
    @Override
    public void setNamespace(Object handle) {
        if (!Model.getFacade().isANamespace(handle)) {
            LOG.log(Level.SEVERE,
                "Illegal argument. Object " + handle + " is not a namespace");
            throw new IllegalArgumentException(
                "Illegal argument. Object " + handle + " is not a namespace");
        }
        Object m = handle;
        super.setNamespace(m);

        UseCaseDiagramGraphModel gm =
            (UseCaseDiagramGraphModel) getGraphModel();
        gm.setHomeModel(m);
        LayerPerspective lay =
            new LayerPerspectiveMutable(Model.getFacade().getName(m), gm);
        UseCaseDiagramRenderer rend = new UseCaseDiagramRenderer();
        lay.setGraphNodeRenderer(rend);
        lay.setGraphEdgeRenderer(rend);
        setLayer(lay);

        // The renderer should be a singleton

    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getUmlActions()
     */
    protected Object[] getUmlActions() {
        Object[] actions =
        {
            getActionActor(),
            getActionUseCase(),
	    null,
	    getAssociationActions(),
	    getActionDependency(),
	    getActionGeneralize(),
	    getActionExtend(),
	    getActionInclude(),
	    null,
	    getActionExtensionPoint(),
	};
        return actions;
    }

    private Object[] getAssociationActions() {
        Object[][] actions = {
	    {getActionAssociation(), getActionUniAssociation() },
	    {getActionAggregation(), getActionUniAggregation() },
	    {getActionComposition(), getActionUniComposition() },
        };
        ToolBarUtility.manageDefault(actions, "diagram.usecase.association");
        return actions;
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#getLabelName()
     */
    public String getLabelName() {
        return Translator.localize("label.usecase-diagram");
    }

    /**
     * @return Returns the actionActor.
     */
    protected Action getActionActor() {
        if (actionActor == null) {
            actionActor = new RadioAction(new CmdCreateNode(
                    Model.getMetaTypes().getActor(), "button.new-actor"));
        }
        return actionActor;
    }
    /**
     * @return Returns the actionAggregation.
     */
    protected Action getActionAggregation() {
        if (actionAggregation == null) {
            actionAggregation = new RadioAction(
                    new ActionSetAddAssociationMode(
                        Model.getAggregationKind().getAggregate(),
                        false,
                        "button.new-aggregation"));
        }
        return actionAggregation;
    }
    /**
     * @return Returns the actionAssociation.
     */
    protected Action getActionAssociation() {
        if (actionAssociation == null) {
            actionAssociation = new RadioAction(
                    new ActionSetAddAssociationMode(
                        Model.getAggregationKind().getNone(),
                        false,
                        "button.new-association"));
        }
        return actionAssociation;
    }
    /**
     * @return Returns the actionComposition.
     */
    protected Action getActionComposition() {
        if (actionComposition == null) {
            actionComposition = new RadioAction(
                    new ActionSetAddAssociationMode(
                        Model.getAggregationKind().getComposite(),
                        false,
                        "button.new-composition"));
        }
        return actionComposition;
    }
    /**
     * @return Returns the actionDependency.
     */
    protected Action getActionDependency() {
        if (actionDependency == null) {
            actionDependency = new RadioAction(
                    new ActionSetMode(
                        ModeCreatePolyEdge.class,
                        "edgeClass",
                        Model.getMetaTypes().getDependency(),
                        "button.new-dependency"));
        }
        return actionDependency;
    }
    /**
     * @return Returns the actionExtend.
     */
    protected Action getActionExtend() {
        if (actionExtend == null) {
            actionExtend = new RadioAction(
                    new ActionSetMode(
                        ModeCreatePolyEdge.class,
                        "edgeClass",
                        Model.getMetaTypes().getExtend(),
                        "button.new-extend"));
        }
        return actionExtend;
    }
    /**
     * @return Returns the actionGeneralize.
     */
    protected Action getActionGeneralize() {
        if (actionGeneralize == null) {
            actionGeneralize = new RadioAction(
                    new ActionSetMode(
                        ModeCreatePolyEdge.class,
                        "edgeClass",
                        Model.getMetaTypes().getGeneralization(),
                        "button.new-generalization"));
        }
        return actionGeneralize;
    }
    /**
     * @return Returns the actionInclude.
     */
    protected Action getActionInclude() {
        if (actionInclude == null) {
            actionInclude = new RadioAction(
                    new ActionSetMode(
                        ModeCreatePolyEdge.class,
                        "edgeClass",
                        Model.getMetaTypes().getInclude(),
                        "button.new-include"));
        }
        return actionInclude;
    }
    /**
     * @return Returns the actionUniAggregation.
     */
    protected Action getActionUniAggregation() {
        if (actionUniAggregation == null) {
            actionUniAggregation  = new RadioAction(
                    new ActionSetAddAssociationMode(
                            Model.getAggregationKind().getAggregate(),
                            true,
                            "button.new-uniaggregation"));
        }
        return actionUniAggregation;
    }
    /**
     * @return Returns the actionUniAssociation.
     */
    protected Action getActionUniAssociation() {
        if (actionUniAssociation == null) {
            actionUniAssociation  = new RadioAction(
                    new ActionSetAddAssociationMode(
                            Model.getAggregationKind().getNone(),
                            true,
                            "button.new-uniassociation"));
        }
        return actionUniAssociation;
    }
    /**
     * @return Returns the actionUniComposition.
     */
    protected Action getActionUniComposition() {
        if (actionUniComposition == null) {
            actionUniComposition  = new RadioAction(
                    new ActionSetAddAssociationMode(
                            Model.getAggregationKind().getComposite(),
                            true,
                            "button.new-unicomposition"));
        }
        return actionUniComposition;
    }
    /**
     * @return Returns the actionUseCase.
     */
    protected Action getActionUseCase() {
        if (actionUseCase == null) {
            actionUseCase = new RadioAction(new CmdCreateNode(
                    Model.getMetaTypes().getUseCase(), "button.new-usecase"));
        }
        return actionUseCase;
    }

    /**
     * @return the action to create an extension point
     */
    protected Action getActionExtensionPoint() {
        if (actionExtensionPoint == null) {
            actionExtensionPoint = ActionAddExtensionPoint.singleton();
        }
        return actionExtensionPoint;
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#isRelocationAllowed(java.lang.Object)
     */
    public boolean isRelocationAllowed(Object base)  {
    	return Model.getFacade().isAPackage(base)
        	|| Model.getFacade().isAClassifier(base);
    }

    /*
     * @see org.argouml.uml.diagram.ui.UMLDiagram#relocate(java.lang.Object)
     */
    public boolean relocate(Object base) {
        setNamespace(base);
        damage();
        return true;
    }

    /*
     * Allow all Packages and Classifiers..
     */
    @SuppressWarnings("unchecked")
    public Collection getRelocationCandidates(Object root) {
        Collection c = new HashSet();
        c.add(Model.getModelManagementHelper()
                .getAllModelElementsOfKindWithModel(root,
                        Model.getMetaTypes().getPackage()));
        c.add(Model.getModelManagementHelper()
                .getAllModelElementsOfKindWithModel(root,
                        Model.getMetaTypes().getClassifier()));
        return c;
    }

    public void encloserChanged(FigNode enclosed,
            FigNode oldEncloser, FigNode newEncloser) {
        // Do nothing.
    }

    @Override
    public boolean doesAccept(Object objectToAccept) {
        if (Model.getFacade().isAActor(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAUseCase(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAComment(objectToAccept)) {
            return true;
        } else if (Model.getFacade().isAPackage(objectToAccept)) {
            /* We accept a Package, but not a Model or a Subsystem. */
            if (Model.getFacade().isAModel(objectToAccept)) {
                return false;
            } else if (Model.getFacade().isASubsystem(objectToAccept)) {
                return false;
            }
            return true;
        }
        return false;

    }

    @Override
    public DiagramElement drop(Object droppedObject, Point location) {
        DiagramElement figNode = null;

        // If location is non-null, convert to a rectangle that we can use
        Rectangle bounds = null;
        if (location != null) {
            bounds = new Rectangle(location.x, location.y, 0, 0);
        }

        return createDiagramElement(droppedObject, bounds);
    }

    public DiagramElement createDiagramElement(
            final Object modelElement,
            final Rectangle bounds) {

        FigNodeModelElement figNode = null;

        DiagramSettings settings = getDiagramSettings();

        if (Model.getFacade().isAActor(modelElement)) {
            figNode = new FigActor(modelElement, bounds, settings);
        } else if (Model.getFacade().isAUseCase(modelElement)) {
            figNode = new FigUseCase(modelElement, bounds, settings);
        } else if (Model.getFacade().isAComment(modelElement)) {
            figNode = new FigComment(modelElement, bounds, settings);
        } else if (Model.getFacade().isAPackage(modelElement)) {
            if (!Model.getFacade().isAModel(modelElement)
                    && !Model.getFacade().isASubsystem(modelElement)) {
                /* If we do not exclude a Model here, then dropping the
                 * Model on a UseCase diagram causes a package
                 * to be drawn. */
                figNode = new FigPackage(modelElement, bounds, settings);
            }
        }
        if (figNode != null) {
            LOG.log(Level.FINE,
                    "Model element {0} converted to {1}",
                    new Object[]{modelElement, figNode});
        } else {
            LOG.log(Level.FINE, "Dropped object NOT added {0}", figNode);
        }
        return figNode;
    }
}
