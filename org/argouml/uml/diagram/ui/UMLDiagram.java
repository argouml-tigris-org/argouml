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

package org.argouml.uml.diagram.ui;

import java.awt.Component;
import java.beans.PropertyVetoException;
import java.util.Enumeration;

import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.JToolBar;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.CmdCreateNode;
import org.argouml.ui.CmdSetMode;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.static_structure.ui.CommentEdge;
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
import org.tigris.toolbar.toolbutton.ToolButton;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

/**
 * This class provides support for writing a UML diagram for argo using
 * the GEF framework.
 * <p>It adds common buttons, a namespace, capability
 * to delete itself when its namespace is deleted, some help
 * with creating a valid diagram name.
 *
 */
public abstract class UMLDiagram
    extends ArgoDiagram
    implements MElementListener {

    private static final Logger LOG = Logger.getLogger(UMLDiagram.class);

    ////////////////////////////////////////////////////////////////
    // actions for toolbar
    
    /**
     * Tool to add a comment node.
     */
    protected static Action _actionComment =
	new RadioAction(new CmdCreateNode(ModelFacade.COMMENT, "Note"));

    /**
     * Tool to create an relationship between a comment node and some other node
     * using a polyedge.<p>
     */
    protected static Action _actionCommentLink = new RadioAction(
        new CmdSetMode(
            ModeCreatePolyEdge.class,  
            "edgeClass",
            CommentEdge.class,
            "CommentLink"));


    protected static Action _actionSelect =
        new CmdSetMode(ModeSelect.class, "Select");

    protected static Action _actionBroom =
        new CmdSetMode(ModeBroom.class, "Broom");

    protected static Action _actionRectangle =
        new RadioAction(new CmdSetMode(ModeCreateFigRect.class, "Rectangle",
        Translator.localize("misc.primitive.rectangle")));

    protected static Action _actionRRectangle =
        new RadioAction(new CmdSetMode(ModeCreateFigRRect.class, "RRect",
        Translator.localize("misc.primitive.rounded-rectangle")));

    protected static Action _actionCircle =
        new RadioAction(new CmdSetMode(ModeCreateFigCircle.class, "Circle",
        Translator.localize("misc.primitive.circle")));

    protected static Action _actionLine =
        new RadioAction(new CmdSetMode(ModeCreateFigLine.class, "Line",
        Translator.localize("misc.primitive.line")));

    protected static Action _actionText =
        new RadioAction(new CmdSetMode(ModeCreateFigText.class, "Text",
        Translator.localize("misc.primitive.text")));

    protected static Action _actionPoly =
        new RadioAction(new CmdSetMode(ModeCreateFigPoly.class, "Polygon",
        Translator.localize("misc.primitive.polygon")));

    protected static Action _actionSpline =
        new RadioAction(new CmdSetMode(ModeCreateFigSpline.class, "Spline",
        Translator.localize("misc.primitive.spline")));

    protected static Action _actionInk =
        new RadioAction(new CmdSetMode(ModeCreateFigInk.class, "Ink",
        Translator.localize("misc.primitive.ink")));

    ////////////////////////////////////////////////////////////////
    // instance variables
    private Object namespace;
    private DiagramInfo diagramName = new DiagramInfo(this);

    private JToolBar toolBar;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * constructor
     */
    public UMLDiagram() {
        super();
    }

    /**
     * @param ns the UML namespace
     */
    public UMLDiagram(Object ns) {
        this();

        if (!ModelFacade.isANamespace(ns))
            throw new IllegalArgumentException();

        setNamespace(ns);
    }

    /**
     * @param name the name of the diagram
     * @param ns the UML namespace
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
        if (org.argouml.model.ModelFacade.isANamespace(owner))
            setNamespace(owner);
        else
            LOG.debug("unknown object in UMLDiagram initialize:" + owner);
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
     * sets the namespace of the Diagram, and
     * adds the diagram as a listener of its namspace in the UML model.
     * (so that it can delete itself when the model element is deleted).
     *
     * @param ns the namespace
     */
    public void setNamespace(Object ns) {
        if (!ModelFacade.isANamespace(ns)) {
            LOG.error("Not a namespace");
            LOG.error(ns);
            throw new IllegalArgumentException("Given object not a namespace");
        }
        namespace = ns;
        // add the diagram as a listener to the namspace so
        // that when the namespace is remove()d the diagram is deleted also.
        UmlModelEventPump.getPump().addModelEventListener(
            this,
            namespace,
            UmlModelEventPump.REMOVE);
    }

    /**
     * @see org.tigris.gef.base.Diagram#getClassAndModelID()
     */
    public String getClassAndModelID() {
        String s = super.getClassAndModelID();
        if (getOwner() == null)
            return s;
        String id = ModelFacade.getUUID(getOwner());
        return s + "|" + id;
    }

    // TODO: should be overwritten by each subclass of UMLDiagram

    /**
     * @return the namespace
     */
    public Object getOwner() {
        return getNamespace();
    }

    /**
     * @see org.tigris.gef.base.Diagram#setName(java.lang.String)
     */
    public void setName(String n) throws PropertyVetoException {
        super.setName(n);
        diagramName.updateName();
    }

    static final long serialVersionUID = -401219134410459387L;

    /**
     * Get the toolbar for the diagram
     * @return the diagram toolbar
     */
    public JToolBar getJToolBar() {
        if (toolBar == null) {
            initToolBar();
        }
        return toolBar;
    }

    /**
     * Create the toolbar based on actions for the spcific diagram
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
        Object manipulateActions[] = getManipulateActions();
        Object umlActions[] = getUmlActions();
        Object shapeActions[] = getShapeActions();

        Object actions[] =
            new Object[manipulateActions.length
                + umlActions.length
                + shapeActions.length];

        int posn = 0;
        System.arraycopy(
            manipulateActions,
            0,
            actions,
            posn,
            manipulateActions.length);
        posn += manipulateActions.length;
        System.arraycopy(umlActions, 0, actions, posn, umlActions.length);
        posn += umlActions.length;
        System.arraycopy(shapeActions, 0, actions, posn, shapeActions.length);

        return actions;
    }

    /**
     * Implement on the ancestor to get actions to populate toolbar.
     *
     * @return the actions for the toolbar
     */
    protected abstract Object[] getUmlActions();

    private Object[] getManipulateActions() {
        Object actions[] =
        {
	    new RadioAction(_actionSelect),
	    new RadioAction(_actionBroom),
	    null
	};
        return actions;
    }

    private Object[] getShapeActions() {
        Object actions[] = {
	    null, getShapePopupActions(),
	};
        return actions;
    }

    private Object[] getShapePopupActions() {
        Object actions[][] = {
	    {
		_actionRectangle, _actionRRectangle
	    },
	    {
                _actionCircle, _actionLine }, {
                _actionText, _actionPoly }, {
                _actionSpline, _actionInk }
        };

        return actions;
    }

    /**
     * This diagram listens to events from is namespace ModelElement;
     * When the modelelement is removed, we also want to delete this
     * diagram too.  <p>
     *
     * There is also a risk that if this diagram was the one shown in
     * the diagram panel, then it will remain after it has been
     * deleted. so we need to deselect this diagram.
     *
     * @see ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent e) {
        Object newTarget =
            ProjectManager.getManager().getCurrentProject().getDiagrams().get(
                0);
        TargetManager.getInstance().setTarget(newTarget);
        UmlModelEventPump.getPump().removeModelEventListener(
            this,
            namespace,
            UmlModelEventPump.REMOVE);
        ProjectManager.getManager().getCurrentProject().moveToTrash(this);

    }

    /**
     * not used the UMLDiagram is only interested in the removed() event.
     *
     * @see ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent e) {
    }

    /**
     * not used the UMLDiagram is only interested in the removed() event.
     *
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
    }

    /**
     * not used the UMLDiagram is only interested in the removed() event.
     *
     * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
    }

    /**
     * not used the UMLDiagram is only interested in the removed() event.
     *
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent e) {
    }

    /**
     * not used the UMLDiagram is only interested in the removed() event.
     *
     * @see ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent e) {
    }

    /**
     * Removes the UMLDiagram and all the figs on it as listener to 
     * UmlModelEventPump. Is called by setTarget in TabDiagram to improve 
     * performance. 
     *
     */
    public void removeAsTarget() {
        Enumeration elems = elements();
        UmlModelEventPump pump = UmlModelEventPump.getPump();
        while (elems.hasMoreElements()) {
            Object o = elems.nextElement();
            if (ModelFacade.isAElementListener(o)) {
                MElementListener listener = (MElementListener) o;
                Fig fig = (Fig) o;
                pump.removeModelEventListener(listener, fig.getOwner());
            }
        }
        pump.removeModelEventListener(this, getNamespace());

    }

    /**
     * Adds the UMLDiagram and all the figs on it as listener to
     * UmlModelEventPump.  Together with removeAsModelListener this is
     * a performance improvement.
     *
     */
    public void setAsTarget() {
        Enumeration elems = elements();
        while (elems.hasMoreElements()) {
            Fig fig = (Fig) elems.nextElement();
            if (org.argouml.model.ModelFacade.isAElementListener(fig)) {
                Object owner = fig.getOwner();
                // pump.addModelEventListener((MElementListener)fig, owner);
                // this will make sure all the correct event listeners are set. 
                fig.setOwner(null);
                fig.setOwner(owner);
            }
        }
    }

    /**
     * Set all toolbar buttons to unselected other then the toolbar button
     * with the supplied action.
     *
     * @param otherThanAction the action of the button 
     *                        that is NOT to be deselected
     */
    public void deselectOtherTools(Action otherThanAction) {
        //cat.debug("Looking for action " + otherThanAction);
        int toolCount = toolBar.getComponentCount();
        for (int i = 0; i < toolCount; ++i) {
            Component c = toolBar.getComponent(i);
            if (c instanceof ToolButton) {
                ToolButton tb = (ToolButton) c;
                Action action = tb.getRealAction();
                if (action instanceof RadioAction) {
                    action = ((RadioAction) action).getAction();
                }
                Action otherAction = otherThanAction;
                if (otherThanAction instanceof RadioAction) {
                    otherAction = ((RadioAction) otherThanAction).getAction();
                }
                if (!action.equals(otherAction)) {
                    //cat.debug("Unselecting " + tb);
                    tb.setSelected(false);
                    ButtonModel bm = tb.getModel();
                    bm.setRollover(false);
                    bm.setSelected(false);
                    bm.setArmed(false);
                    bm.setPressed(false);
                    tb.setBorderPainted(false);
                } else {
                    //cat.debug("Selecting " + tb);
                    tb.setSelected(true);
                    ButtonModel bm = tb.getModel();
                    bm.setRollover(true);
                    tb.setBorderPainted(true);
                }
            }
        }
    }

    /**
     * Set all toolbar buttons to unselected other then the toolbar button
     * with the supplied action.
     */
    public void deselectAllTools() {
        int toolCount = toolBar.getComponentCount();
        for (int i = 0; i < toolCount; ++i) {
            Component c = toolBar.getComponent(i);
            if (c instanceof ToolButton) {
                ToolButton tb = (ToolButton) c;
                Action action = tb.getRealAction();
                if (action instanceof RadioAction) {
                    action = ((RadioAction) action).getAction();
                }
                tb.setSelected(false);
                ButtonModel bm = tb.getModel();
                bm.setRollover(false);
                bm.setSelected(false);
                bm.setArmed(false);
                bm.setPressed(false);
                tb.setBorderPainted(false);
            }
        }
    }
} /* end class UMLDiagram */

