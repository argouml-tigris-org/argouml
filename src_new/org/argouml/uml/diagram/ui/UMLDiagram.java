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

package org.argouml.uml.diagram.ui;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.Enumeration;

import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.ConfigurationKey;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.CmdCreateNode;
import org.argouml.ui.CmdSetMode;
import org.argouml.ui.explorer.Relocatable;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.UUIDHelper;
import org.tigris.gef.base.ModeBroom;
import org.tigris.gef.base.ModeCreateFigCircle;
import org.tigris.gef.base.ModeCreateFigInk;
import org.tigris.gef.base.ModeCreateFigLine;
import org.tigris.gef.base.ModeCreateFigPoly;
import org.tigris.gef.base.ModeCreateFigRRect;
import org.tigris.gef.base.ModeCreateFigRect;
import org.tigris.gef.base.ModeCreateFigSpline;
import org.tigris.gef.base.ModeCreateFigText;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.base.ModeSelect;
import org.tigris.gef.presentation.Fig;
import org.tigris.toolbar.ToolBarFactory;
import org.tigris.toolbar.ToolBarManager;
import org.tigris.toolbar.toolbutton.ToolButton;

/**
 * This class provides support for writing a UML diagram for argo using
 * the GEF framework. <p>
 *
 * It adds common buttons, a namespace, capability
 * to delete itself when its namespace is deleted, some help
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
 * For a statechart diagram is that the statemachine.
 * For a activitydiagram is that the activitygraph.
 * Override the getOwner method to return the owner. <p>
 *
 * TODO: MVW: I am not sure of the following:<p>
 * The "namespace" of the diagram is e.g. used when creating new elements
 * that are shown on the diagram; they will have their namespace set
 * according this. It is NOT necessarily equal to the "owner". <p>
 *
 * MVW: I am sure about the following: <p>
 * The "namespace" of the diagram is e.g. used to register a listener
 * to the UML model, to be notified if this element is removed;
 * which will imply that this diagram has to be deleted, too. <p>
 * 
 * Hence the namespace of e.g. a collaboration diagram should be the
 * represented classifier or, in case of a represented operation, the
 * classifier that owns this operation.
 * And the namespace of the statechart diagram should be 
 * the namespace of its statemachine.
 */
public abstract class UMLDiagram
    extends ArgoDiagram
    implements PropertyChangeListener, Relocatable {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(UMLDiagram.class);

    /**
     * The serial number for new diagrams.
     * Used to create an unique number for the name of the diagram.
     */
    private int diagramSerial = 1;

    ////////////////////////////////////////////////////////////////
    // actions for toolbar

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
        new CmdSetMode(ModeSelect.class, "button.select");

    private static Action actionBroom =
        new CmdSetMode(ModeBroom.class, "button.broom");

    private static Action actionRectangle =
        new RadioAction(new CmdSetMode(ModeCreateFigRect.class, "Rectangle",
        			       "misc.primitive.rectangle"));

    private static Action actionRRectangle =
        new RadioAction(new CmdSetMode(ModeCreateFigRRect.class, "RRect",
        			       "misc.primitive.rounded-rectangle"));

    private static Action actionCircle =
        new RadioAction(new CmdSetMode(ModeCreateFigCircle.class, "Circle",
        			       "misc.primitive.circle"));

    private static Action actionLine =
        new RadioAction(new CmdSetMode(ModeCreateFigLine.class, "Line",
        			       "misc.primitive.line"));

    private static Action actionText =
        new RadioAction(new CmdSetMode(ModeCreateFigText.class, "Text",
        			       "misc.primitive.text"));

    private static Action actionPoly =
        new RadioAction(new CmdSetMode(ModeCreateFigPoly.class, "Polygon",
        			       "misc.primitive.polygon"));

    private static Action actionSpline =
        new RadioAction(new CmdSetMode(ModeCreateFigSpline.class, "Spline",
        			       "misc.primitive.spline"));

    private static Action actionInk =
        new RadioAction(new CmdSetMode(ModeCreateFigInk.class, "Ink",
        			       "misc.primitive.ink"));

    ////////////////////////////////////////////////////////////////
    // instance variables
    private Object namespace;

    private JToolBar toolBar;

    private Action selectedAction;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     */
    public UMLDiagram() {
        super();
    }

    /**
     * @param ns the UML namespace of this diagram
     */
    public UMLDiagram(Object ns) {
        this();
        if (!Model.getFacade().isANamespace(ns)) {
            throw new IllegalArgumentException();
        }
        setNamespace(ns);
    }

    /**
     * @param name the name of the diagram
     * @param ns the UML namespace of this diagram
     */
    public UMLDiagram(String name, Object ns) {
        this(ns);
        try {
            setName(name);
        } catch (PropertyVetoException pve) {
            LOG.fatal("Name not allowed in construction of diagram");
        }
    }

    /**
     * @see org.tigris.gef.base.Diagram#initialize(java.lang.Object)
     */
    public void initialize(Object owner) {
        super.initialize(owner);
        /* The following is the default implementation
         * for diagrams of which the owner is a namespace.
         */
        if (Model.getFacade().isANamespace(owner)) {
            setNamespace(owner);
        }
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @return the namespace for the diagram
     */
    public Object getNamespace() {
        return namespace;
    }

    /**
     * Sets the namespace of the Diagram, and
     * adds the diagram as a listener of its namespace in the UML model
     * (so that it can delete itself when the model element is deleted).
     *
     * @param ns the namespace for the diagram
     */
    public void setNamespace(Object ns) {
        if (!Model.getFacade().isANamespace(ns)) {
            LOG.error("Not a namespace");
            LOG.error(ns);
            throw new IllegalArgumentException("Given object not a namespace");
        }
        if ((namespace != null) && (namespace != ns)) {
            Model.getPump().removeModelEventListener(this, namespace);
        }
        namespace = ns;
        // Add the diagram as a listener to the namespace so
        // that when the namespace is removed the diagram is deleted also.
        /* Listening only to "remove" events does not work... 
         * TODO: Check if this works now with new event pump - tfm 
         */
        Model.getPump().addModelEventListener(this, namespace);
    }

    /**
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
     * The default implementation for diagrams that
     * have the namespace as their owner.
     *
     * @return the namespace
     */
    public Object getOwner() {
        return getNamespace();
    }

    /**
     * Get the toolbar for the diagram.
     * @return the diagram toolbar
     */
    public JToolBar getJToolBar() {
        if (toolBar == null) {
            initToolBar();
        }
        return toolBar;
    }

    /**
     * Create the toolbar based on actions for the specific diagram
     * subclass.
     * @see org.tigris.gef.base.Diagram#initToolBar()
     */
    public void initToolBar() {
        toolBar =
	    ToolBarFactory.createToolBar(true /*rollover*/,
					 getActions(),
					 false /*floating*/);
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

        manageDefault(actions, "diagram.shape");
        return actions;
    }

    /**
     * Manages the selection of the default tool in a popup tool in the toolbar. <p>
     * 
     * I.e. in the diagram toolbar, you can have tools that can be opened,
     * into a grid of tools. The last used tool is remembered, 
     * and put at the top when the popup is closed, i.e.
     * is the only tool that remains visible. This remembering is
     * persistent, hence stored in the configuration file,
     * under a certain key (i.e. name).
     * 
     * @param actions the array of actions that make up the popup
     * @param key appendix for the key for the configuration file
     */
    protected void manageDefault(Object[] actions, String key) {
        Action defaultAction = null;
        ConfigurationKey k =
            Configuration.makeKey("default", "popupactions", key);
        String defaultName = Configuration.getString(k);
        PopupActionsListener listener = new PopupActionsListener(k);
        for (int i = 0; i < actions.length; ++i) {
            if (actions[i] instanceof Action) {
                Action a = (Action) actions[i];
                if (a.getValue(Action.NAME).equals(defaultName)) {
                    defaultAction = a;
                }
                a.addPropertyChangeListener(listener);
            } else if (actions[i] instanceof Object[]) {
                Object[] actionRow = (Object[]) actions[i];
                for (int j = 0; j < actionRow.length; ++j) {
                    Action a = (Action) actionRow[j];
                    if (a.getValue(Action.NAME).equals(defaultName)) {
                        defaultAction = a;
                    }
                    a.addPropertyChangeListener(listener);
                }
            }
        }

        if (defaultAction != null) {
            defaultAction.putValue("isDefault", Boolean.valueOf(true));
        }
    }

    static class PopupActionsListener implements PropertyChangeListener {
        private boolean blockEvents;
        private ConfigurationKey key;

        /**
         * Constructor.
         *
         * @param k
         */
        public PopupActionsListener(ConfigurationKey k) {
            key = k;
        }

        /**
         * @see java.beans.PropertyChangeListener#propertyChange(
         *         java.beans.PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getSource() instanceof Action) {
                Action a = (Action) evt.getSource();
                if (!blockEvents && evt.getPropertyName().equals("popped")) {
                    blockEvents = true;
                    /* Switch the value back off, so that we will
                     * get notified again next time.
                     */
                    a.putValue("popped", Boolean.valueOf(false));
                    blockEvents = false;
                    Configuration.setString(key,
                            (String) a.getValue(Action.NAME));
                }
            }
        }
    }

    /**
     * This diagram listens to events from its namespace ModelElement;
     * when the modelelement is removed, we also want to delete this
     * diagram.  <p>
     *
     * There is also a risk that if this diagram was the one shown in
     * the diagram panel, then it will remain after it has been
     * deleted. So we need to deselect this diagram.
     *
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ((evt.getSource() == namespace) && ProjectManager.getManager()
                .getCurrentProject().isInTrash(namespace)) {

            Model.getPump().removeModelEventListener(this, namespace);
            ProjectManager.getManager().getCurrentProject().moveToTrash(this);

            Object newTarget =
                ProjectManager.getManager().getCurrentProject()
                	.getDiagrams().get(0);
            TargetManager.getInstance().setTarget(newTarget);
        }
    }

    /**
     * Removes the UMLDiagram and all the figs on it as listener to
     * UML Events. Is called by setTarget in TabDiagram to improve
     * performance.
     */
    public void removeAsTarget() {
        Enumeration elems = elements();
        while (elems.hasMoreElements()) {
            Fig fig = (Fig) elems.nextElement();
            if (fig instanceof FigNodeModelElement) {
                ((FigNodeModelElement) fig).updateListeners(null);
            }
            if (fig instanceof FigEdgeModelElement) {
                ((FigEdgeModelElement) fig).updateListeners(null);
            }
        }
    }

    /**
     * Adds the UMLDiagram and all the figs on it as listener to
     * UML Events.  Together with removeAsModelListener this is
     * a performance improvement.
     */
    public void setAsTarget() {
        Enumeration elems = elements();
        while (elems.hasMoreElements()) {
            Fig fig = (Fig) elems.nextElement();
            Object owner = fig.getOwner();
            /* This will make sure all the correct
             * event listeners are set:
             */
            if (fig instanceof FigNodeModelElement) {
                ((FigNodeModelElement) fig).updateListeners(owner);
                ((FigNodeModelElement) fig).renderingChanged();
            }
            if (fig instanceof FigEdgeModelElement) {
                ((FigEdgeModelElement) fig).updateListeners(owner);
                ((FigEdgeModelElement) fig).renderingChanged();
            }
        }
    }

    /**
     * Set the given action as the selected action (ie pressed down on the
     * diagram toolbar). All other actions become unselected.
     *
     * @param selectedAction the action to become selected
     */
    public void setSelectedAction(Action selectedAction) {
        this.selectedAction = selectedAction;
        int toolCount = toolBar.getComponentCount();
        for (int i = 0; i < toolCount; ++i) {
            Component c = toolBar.getComponent(i);
            if (c instanceof ToolButton) {
                ToolButton tb = (ToolButton) c;
                Action action = tb.getRealAction();
                if (action instanceof RadioAction) {
                    action = ((RadioAction) action).getAction();
                }
                Action otherAction = selectedAction;
                if (selectedAction instanceof RadioAction) {
                    otherAction = ((RadioAction) selectedAction).getAction();
                }
                if (!action.equals(otherAction)) {
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
            new CmdSetMode(ModeCreatePolyEdge.class, "edgeClass",
            modelElement, descr));
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
     * @param modelElement identifies the model element type to make
     * @param descr the description to give this action.
     * @return The action to create a new node.
     */
    protected Action makeCreateAssociationClassAction(String descr) {
        return new RadioAction(new ActionSetAddAssociationClassMode(descr));
    }

    /**
     * Reset the diagram serial counter to the initial value.
     * This should e.g. be done when the menuitem File->New is activated.
     */
    public void resetDiagramSerial() {
        diagramSerial = 1;
    }

    /**
     * @return Returns the diagramSerial.
     */
    protected int getNextDiagramSerial() {
        return diagramSerial++;
    }

    /**
     * @return a string that can be used as a label for this kind of diagram
     */
    public abstract String getLabelName();

    /**
     * This method shall indicate if the diagram needs to be removed
     * because the modelelements on which it depends are removed.
     * The default implementation is applicable to e.g. a ClassDiagram,
     * which only depends on its namespace. <p>
     *
     * Matters get more complicated for e.g. a Statechart Diagram,
     * which also depends on its context (the represented modelelement).
     * Hence such a diagram needs to override this method.
     *
     * @return true if the diagram needs to be removed
     */
    public boolean needsToBeRemoved() {
        return namespace == null
                || Model.getUmlFactory().isRemoved(namespace)
                || getOwner() == null
                || Model.getUmlFactory().isRemoved(getOwner());
    }

    /**
     * This method shall return any UML modelelements
     * that should be deleted when the diagram gets deleted,
     * or null if there are none. The default implementation returns null;
     * e.g. a statechart diagram should return its statemachine.
     *
     * @author mvw@tigris.org
     *
     * @return the dependent element - in the general case there aren't, so null
     */
    public Object getDependentElement() {
        return null;
    }

    /**
     * @see org.argouml.ui.explorer.Relocatable#isRelocationAllowed(java.lang.Object)
     */
    public abstract boolean isRelocationAllowed(Object base);

    /**
     * @see org.argouml.ui.explorer.Relocatable#relocate(java.lang.Object)
     */
    public abstract boolean relocate(Object base);

    /**
     * The UID.
     */
    static final long serialVersionUID = -401219134410459387L;
} /* end class UMLDiagram */
