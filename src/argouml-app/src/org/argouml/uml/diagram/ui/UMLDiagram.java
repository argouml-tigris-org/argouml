/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.JToolBar;

import org.argouml.gefext.ArgoModeCreateFigCircle;
import org.argouml.gefext.ArgoModeCreateFigInk;
import org.argouml.gefext.ArgoModeCreateFigLine;
import org.argouml.gefext.ArgoModeCreateFigPoly;
import org.argouml.gefext.ArgoModeCreateFigRRect;
import org.argouml.gefext.ArgoModeCreateFigRect;
import org.argouml.gefext.ArgoModeCreateFigSpline;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.argouml.ui.CmdCreateNode;
import org.argouml.uml.UUIDHelper;
import org.argouml.uml.diagram.ArgoDiagramImpl;
import org.argouml.uml.diagram.DiagramElement;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.Relocatable;
import org.argouml.uml.diagram.UMLMutableGraphSupport;
import org.argouml.util.ToolBarUtility;
import org.tigris.gef.base.LayerPerspective;
import org.tigris.gef.base.ModeBroom;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.base.ModePlace;
import org.tigris.gef.base.ModeSelect;
import org.tigris.gef.graph.GraphFactory;
import org.tigris.gef.graph.GraphModel;
import org.tigris.toolbar.ToolBarFactory;
import org.tigris.toolbar.ToolBarManager;
import org.tigris.toolbar.toolbutton.ToolButton;

/**
 * This class provides support for writing a UML diagram for argo using
 * the GEF framework. <p>
 *
 * It adds common buttons, and some help
 * with creating a valid diagram name. <p>
 *
 * There are various methods for returning 'structures' of Actions
 * which are used to build toolbars and dropdown buttons within toolbars.
 * These structures are arrays of Objects.
 * An array element is actually either an Action, null or another array.
 * When building a toolbar an Action is used to create a button and null
 * is used to create a spacer in the toolbar.
 * An element containing an array results in a dropdown toolbar button
 * being created which contains all the items in that array. <p>
 *
 * The "owner" of the UMLDiagram needs to be set to the
 * UML modelelement of which the diagram depends.
 * For a class diagram is that its namespace.
 * For a collaboration diagram is that the Collaboration UML object.
 * For a sequence diagram is that the collaboration.
 * For a deployment diagram is that the namespace.
 * For a statechart diagram is that the statemachine.
 * For a activitydiagram is that the activitygraph.
 * Override the getOwner method to return the owner. <p>
 *
 * The "owner" is shown in the diagram's properties
 * panel as the "home model". <p>
 */
public abstract class UMLDiagram
    extends ArgoDiagramImpl
    implements Relocatable {

    private static final Logger LOG =
        Logger.getLogger(UMLDiagram.class.getName());

    /**
     * Tool to add a comment node.
     */
    private static Action actionComment =
        new RadioAction(new ActionAddNote());

    /**
     * Tool to create an relationship between a comment node and some other node
     * using a polyedge.<p>
     */
    private static Action actionCommentLink =
        new RadioAction(new ActionSetAddCommentLinkMode());


    private static Action actionSelect =
        new ActionSetMode(ModeSelect.class, "button.select");

    private static Action actionBroom =
        new ActionSetMode(ModeBroom.class, "button.broom");

    private static Action actionRectangle =
        new RadioAction(new ActionSetMode(ArgoModeCreateFigRect.class,
                "Rectangle", "misc.primitive.rectangle"));

    private static Action actionRRectangle =
        new RadioAction(new ActionSetMode(ArgoModeCreateFigRRect.class,
                "RRect", "misc.primitive.rounded-rectangle"));

    private static Action actionCircle =
        new RadioAction(new ActionSetMode(ArgoModeCreateFigCircle.class,
                "Circle", "misc.primitive.circle"));

    private static Action actionLine =
        new RadioAction(new ActionSetMode(ArgoModeCreateFigLine.class,
                "Line", "misc.primitive.line"));

    private static Action actionText =
        new RadioAction(new ActionSetMode(ArgoModeCreateFigText.class,
                "Text", "misc.primitive.text"));

    private static Action actionPoly =
        new RadioAction(new ActionSetMode(ArgoModeCreateFigPoly.class,
                "Polygon", "misc.primitive.polygon"));

    private static Action actionSpline =
        new RadioAction(new ActionSetMode(ArgoModeCreateFigSpline.class,
                "Spline", "misc.primitive.spline"));

    private static Action actionInk =
        new RadioAction(new ActionSetMode(ArgoModeCreateFigInk.class,
                "Ink", "misc.primitive.ink"));

    private JToolBar toolBar;

    private Action selectedAction;

    /**
     * Default constructor will become protected. All subclasses should have
     * their constructors invoke the 3-arg version of the constructor.
     *
     * @deprecated for 0.27.2 by tfmorris. Use
     *             {@link #UMLDiagram(String, Object, GraphModel)} or another
     *             explicit constructor.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    public UMLDiagram() {
        super();
    }

    /**
     * @param ns the UML namespace of this diagram
     * @deprecated for 0.27.2 by tfmorris. Use
     *             {@link #UMLDiagram(String, Object, GraphModel)}.
     */
    @Deprecated
    public UMLDiagram(Object ns) {
        this();
        if (!Model.getFacade().isANamespace(ns)) {
            throw new IllegalArgumentException();
        }
        // TODO: Should we require a GraphModel in the constructor since
        // our implementations of setNamespace are going to try and set
        // the namespace on the graphmodel as well?
        setNamespace(ns);
    }

    /**
     * @param name the name of the diagram
     * @param ns the UML namespace of this diagram
     * @deprecated for 0.27.2 by tfmorris. Use
     *             {@link #UMLDiagram(String, Object, GraphModel)}.
     */
    @Deprecated
    public UMLDiagram(String name, Object ns) {
        this(ns);
        try {
            setName(name);
        } catch (PropertyVetoException pve) {
            LOG.log(Level.SEVERE, "Name not allowed in construction of diagram");
        }
    }


    /**
     * Construct a new ArgoUML diagram.  This is the fully specified form
     * of the constructor typically used by subclasses.
     *
     * @param name the name of the new diagram
     * @param graphModel graph model to associate with diagram
     * @param ns the namespace which will "own" the diagram
     */
    public UMLDiagram(String name, Object ns, GraphModel graphModel) {
        super(name, graphModel, new LayerPerspective(name, graphModel));
        setNamespace(ns);
    }

    /**
     * Construct an unnamed diagram using the given GraphModel.
     *
     * @param graphModel graph model to associate with diagram
     */
    public UMLDiagram(GraphModel graphModel) {
        super("", graphModel, new LayerPerspective("", graphModel));
    }

    /**
     * Method called by PGML parser during diagram load to initialize a diagram
     * after it's been constructed. Order of method invocations currently is:
     * <ul>
     * <li>0-arg constructor
     * <li>setDiagramSettings
     * <li>initialize(Object) // UML element representing owner/home model
     * <li>setName(String)
     * <li>setScale(double)
     * <li>setShowSingleMultiplicity(boolean)
     * <ul>
     *
     * @param owner UML model element representing owner/namespace/home model
     * @see org.tigris.gef.base.Diagram#initialize(java.lang.Object)
     */
    @Override
    public void initialize(Object owner) {
        super.initialize(owner);
        /* The following is the default implementation
         * for diagrams of which the owner is a namespace.
         */
        if (Model.getFacade().isANamespace(owner)) {
            setNamespace(owner);
        }
    }

    /*
     * @see org.tigris.gef.base.Diagram#getClassAndModelID()
     */
    public String getClassAndModelID() {
        String s = super.getClassAndModelID();
        if (getOwner() == null) {
            return s;
        }
        String id = UUIDHelper.getUUID(getOwner());
        return s + "|" + id;
    }

    /**
     * Get the toolbar for the diagram.
     * @return the diagram toolbar
     */
    public JToolBar getJToolBar() {
        if (toolBar == null) {
            initToolBar();
            toolBar.setName("misc.toolbar.diagram");
        }
        return toolBar;
    }

    /**
     * Create the toolbar based on actions for the specific diagram
     * subclass.
     * @see org.tigris.gef.base.Diagram#initToolBar()
     */
    public void initToolBar() {
        ToolBarFactory factory = new ToolBarFactory(getActions());
        factory.setRollover(true);
        factory.setFloatable(false);

        toolBar = factory.createToolBar();
        toolBar.putClientProperty("ToolBar.toolTipSelectTool",
                Translator.localize("action.select"));
    }

    /**
     * Return actions available for building toolbar or similar.
     * @return an array of available actions.
     */
    public Object[] getActions() {
        Object[] manipulateActions = getManipulateActions();
        Object[] umlActions = getUmlActions();
        Object[] commentActions = getCommentActions();
        Object[] shapeActions = getShapeActions();

        Object[] actions =
            new Object[manipulateActions.length
                + umlActions.length
                + commentActions.length
                + shapeActions.length];

        int posn = 0;
        System.arraycopy(
            manipulateActions,           // source
            0,                           // source position
            actions,                     // destination
            posn,                        // destination position
            manipulateActions.length);   // number of objects to be copied
        posn += manipulateActions.length;

        System.arraycopy(umlActions, 0, actions, posn, umlActions.length);
        posn += umlActions.length;

        System.arraycopy(commentActions, 0, actions, posn,
                commentActions.length);
        posn += commentActions.length;

        System.arraycopy(shapeActions, 0, actions, posn, shapeActions.length);

        return actions;
    }

    /**
     * Implement in the ancestor to get a 'structure' of actions for
     * appending the UML creation tools to the toolbar.
     * @return the actions structure
     */
    protected abstract Object[] getUmlActions();

    /**
     * Get a 'structure' of actions for appending the manipulation
     * mode tools to the toolbar.
     * @return the actions structure
     */
    private Object[] getManipulateActions() {
        Object[] actions =
        {
	    new RadioAction(actionSelect),
	    new RadioAction(actionBroom),
	    null,
	};
        return actions;
    }

    /**
     * Get a 'structure' of actions for appending the comment
     * tools to the toolbar.
     * @return the actions structure
     */
    private Object[] getCommentActions() {
        Object[] actions =
        {
            null,
            actionComment,
            actionCommentLink,
        };
        return actions;
    }

    /**
     * Get a 'structure' of actions for appending primitive drawing
     * tools to the toolbar.
     * @return the actions structure
     */
    private Object[] getShapeActions() {
        Object[] actions = {
	    null,
            getShapePopupActions(),
	};
        return actions;
    }

    /**
     * Get a 'structure' of actions for showing in the shape
     * primitives popup tool button.
     * @return the actions structure
     */
    private Object[] getShapePopupActions() {
        Object[][] actions = {
	    {actionRectangle, actionRRectangle },
	    {actionCircle,    actionLine },
            {actionText,      actionPoly },
            {actionSpline,    actionInk },
        };

        ToolBarUtility.manageDefault(actions, "diagram.shape");
        return actions;
    }

    /**
     * Set the given action as the selected action (ie pressed down on the
     * diagram toolbar). All other actions become unselected.
     *
     * @param theAction the action to become selected
     */
    public void setSelectedAction(Action theAction) {
        selectedAction = theAction;
        int toolCount = toolBar.getComponentCount();
        for (int i = 0; i < toolCount; ++i) {
            Component c = toolBar.getComponent(i);
            if (c instanceof ToolButton) {
                ToolButton tb = (ToolButton) c;
                Action action = tb.getRealAction();
                if (action instanceof RadioAction) {
                    action = ((RadioAction) action).getAction();
                }
                Action otherAction = theAction;
                if (theAction instanceof RadioAction) {
                    otherAction = ((RadioAction) theAction).getAction();
                }
                if (action != null && !action.equals(otherAction)) {
                    tb.setSelected(false);
                    ButtonModel bm = tb.getModel();
                    bm.setRollover(false);
                    bm.setSelected(false);
                    bm.setArmed(false);
                    bm.setPressed(false);
                    if (!ToolBarManager.alwaysUseStandardRollover()) {
                        tb.setBorderPainted(false);
                    }
                } else {
                    tb.setSelected(true);
                    ButtonModel bm = tb.getModel();
                    bm.setRollover(true);
                    if (!ToolBarManager.alwaysUseStandardRollover()) {
                        tb.setBorderPainted(true);
                    }
                }
            }
        }
    }

    /**
     * Get the selected action.
     *
     * @return the selected action
     */
    public Action getSelectedAction() {
        return selectedAction;
    }

    /**
     * Unselect all the toolbar buttons.
     */
    public void deselectAllTools() {
        setSelectedAction(actionSelect);
        actionSelect.actionPerformed(null);
    }

    /**
     * Factory method to build an Action for creating a node in the
     * diagram.
     *
     * @param modelElement identifies the model element type to make
     * @param descr the description to give this action.
     * @return The action to create a new node.
     */
    protected Action makeCreateNodeAction(Object modelElement, String descr) {
        return new RadioAction(new CmdCreateNode(modelElement, descr));
    }

    /**
     * Factory method to build an Action for creating an edge in the
     * diagram.
     *
     * @param modelElement identifies the model element type to make
     * @param descr the description to give this action.
     * @return The action to create a new node.
     */
    protected Action makeCreateEdgeAction(Object modelElement, String descr) {
        return new RadioAction(
            new ActionSetMode(ModeCreatePolyEdge.class, "edgeClass",
                    modelElement, descr));
    }

    /**
     * Factory method to build an Action for creating an edge in the
     * diagram.
     *
     * @param modeClass the mode class to instantiate drawing
     * @param metaType identifies the model element type to make
     * @param descr the description to give this action.
     * @return The action to create a new node.
     */
    protected Action makeCreateDependencyAction(
	    Class modeClass,
	    Object metaType,
	    String descr) {
        return new RadioAction(
            new ActionSetMode(modeClass, "edgeClass", metaType, descr));
    }

    /**
     * Factory method to build an Action for creating an edge in the
     * diagram.
     * @return The action to create a new generalization mode.
     */
    protected Action makeCreateGeneralizationAction() {
        return new RadioAction(
            new ActionSetMode(
        	    ModeCreateGeneralization.class,
        	    "edgeClass",
        	    Model.getMetaTypes().getGeneralization(),
        	    "button.new-generalization"));
    }

    /**
     * Factory method to build an Action for creating an association edge in
     * the diagram.
     *
     * @param aggregationKind the type of aggregation for this association
     * @param unidirectional true if this is a one way association.
     * @param descr the description to give this action.
     * @return The action to create a new association.
     */
    protected Action makeCreateAssociationAction(
            Object aggregationKind,
            boolean unidirectional,
            String descr) {

        return new RadioAction(
            new ActionSetAddAssociationMode(aggregationKind,
                unidirectional, descr));
    }

    /**
     * Factory method to build an Action for creating an association end edge
     * in the diagram.
     *
     * @param descr the description to give this action.
     * @return The action to create a new association.
     */
    protected Action makeCreateAssociationEndAction(String descr) {

        return new RadioAction(new ActionSetAddAssociationEndMode(descr));
    }

    /**
     * Factory method to build an Action for creating an edge in the
     * diagram.
     *
     * @param descr the description to give this action.
     * @return The action to create a new node.
     */
    protected Action makeCreateAssociationClassAction(String descr) {
        return new RadioAction(new ActionSetAddAssociationClassMode(descr));
    }

    /**
     * Reset the diagram serial counter to the initial value. This should e.g.
     * be done when the menuitem File->New is activated.
     *
     * @deprecated for 0.27.3 by tfmorris. This is a noop. Diagram name
     *             duplication is checked for and managed at the project level.
     */
    @Deprecated
    public void resetDiagramSerial() {
    }

    /**
     * @return Returns the diagramSerial.
     * @deprecated for 0.27.3 by tfmorris. This is always returns 1. Diagram
     *             naming is managed at the project level.
     */
    @Deprecated
    protected int getNextDiagramSerial() {
        return 1;
    }

    /**
     * @return a string that can be used as a label for this kind of diagram
     */
    public abstract String getLabelName();

    /*
     * @see org.argouml.ui.explorer.Relocatable#isRelocationAllowed(java.lang.Object)
     */
    public abstract boolean isRelocationAllowed(Object base);

    /*
     * @see org.argouml.ui.explorer.Relocatable#relocate(java.lang.Object)
     */
    public abstract boolean relocate(Object base);

    @Override
    public final void setProject(Project p) {
	super.setProject(p);
	UMLMutableGraphSupport gm = (UMLMutableGraphSupport) getGraphModel();
	gm.setProject(p);
    }

    /**
     * Create a new diagram name.
     * @return String
     */
    protected String getNewDiagramName() {
        // TODO: Add "unnamed" or "new" or something? (Localized, of course)
        return /*"unnamed " + */ getLabelName();
    }

    /**
     * Method to test it the diagram can accept a certain object.
     * This should be overridden by any diagram that wants to accept a certain
     * type of object. All other diagrams should not bother since the default
     * answer is false, ie. don't accept the object.
     * @param objectToAccept The object which acceptability will be checked.
     * @return True if it can accept it, false otherwise.
     */
    public boolean doesAccept(
            @SuppressWarnings("unused") Object objectToAccept) {
        return false;
    }

    /**
     * Handles elements dropped over.
     * @param droppedObject The dropped object.
     * @param location The location in the diagram where the object is dropped.
     * @return The object that has been added to the diagram.
     */
    public DiagramElement drop(Object droppedObject, Point location) {
        // If location is non-null, convert to a rectangle that we can use
        Rectangle bounds = null;
        if (location != null) {
            bounds = new Rectangle(location.x, location.y, 0, 0);
        }

        DiagramElement de = createDiagramElement(droppedObject, bounds);
        if (de instanceof FigEdgeModelElement) {
            // Issue 6221 - we need to force the edge to redraw after a drop on
            // the diagram from explorer or Add to Diagram
            FigEdgeModelElement fe = (FigEdgeModelElement) de;
            fe.renderingChanged();
        }
        return de;
    }

    /**
     * Gets the instructions to be displayed on the status bar.
     * @param droppedObject The object for which instructions will be given.
     * @return The instructions.
     */
    public String getInstructions(Object droppedObject) {
        return Translator.localize("misc.message.click-on-diagram-to-add",
                new Object[] {Model.getFacade().toString(droppedObject), });
    }

    /**
     * Creates a diagram specific @see org.tigris.gef.base.ModePlace that
     * allows the diagram to place an accepted type of object
     * [ @see #doesAccept(Object) ] as it should. This is required 1. since a
     * diagram may receive an object that can't be placed as is, but needs some
     * transformation and 2. diagrams in modules should be independent from the
     * main app, and should use their own implementation of ModePlace if it's
     * required.
     * @param gf TODO
     * @param instructions a help string for the user
     * @return The created ModePlace.
     */
    public ModePlace getModePlace(GraphFactory gf, String instructions) {
        return new ModePlace(gf, instructions);
    }

    /**
     * Create a nary association diamond shaped FigNode on this diagram.
     *
     * @param modelElement the model element this FigNode is to represent
     * @param bounds the position and size for the diamond node.
     * @param settings the diagram setting for presentation.
     * @return The FigNode of the diamond representing the model element
     */
    protected FigNodeModelElement createNaryAssociationNode(
            final Object modelElement,
            final Rectangle bounds,
            final DiagramSettings settings) {

        final FigNodeAssociation diamondFig =
            new FigNodeAssociation(modelElement, bounds, settings);
        if (Model.getFacade().isAAssociationClass(modelElement)
                && bounds != null) {
            final FigClassAssociationClass classBoxFig =
                new FigClassAssociationClass(
                        modelElement, bounds, settings);
            final FigEdgeAssociationClass dashEdgeFig =
                new FigEdgeAssociationClass(
                        classBoxFig, diamondFig, settings);
            classBoxFig.renderingChanged();

            // TODO: Why isn't this calculation for location working?
            Point location = bounds.getLocation();
            location.y = (location.y - diamondFig.getHeight()) - 32;
            if (location.y < 16) {
                location.y = 16;
            }
            classBoxFig.setLocation(location);
            this.add(diamondFig);
            this.add(classBoxFig);
            this.add(dashEdgeFig);
        }
        return diamondFig;
    }
}
