// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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
import javax.swing.JToolBar;

import org.apache.log4j.Category;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.swingext.PopupToolBoxButton;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.base.CmdSetMode;
import org.tigris.gef.base.ModeBroom;
import org.tigris.gef.base.ModeCreateFigCircle;
import org.tigris.gef.base.ModeCreateFigInk;
import org.tigris.gef.base.ModeCreateFigLine;
import org.tigris.gef.base.ModeCreateFigPoly;
import org.tigris.gef.base.ModeCreateFigRRect;
import org.tigris.gef.base.ModeCreateFigRect;
import org.tigris.gef.base.ModeCreateFigSpline;
import org.tigris.gef.base.ModeCreateFigText;
import org.tigris.gef.base.ModeSelect;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.ui.ToolBar;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;
import ru.novosoft.uml.foundation.core.MNamespace;

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
    

    protected static Category cat = Category.getInstance(UMLDiagram.class);
  
    ////////////////////////////////////////////////////////////////
    // actions for toolbar

    protected static Action _actionSelect =
        new CmdSetMode(ModeSelect.class, "Select");

    protected static Action _actionBroom =
        new CmdSetMode(ModeBroom.class, "Broom");

    protected static Action _actionRectangle =
        new CmdSetMode(ModeCreateFigRect.class, "Rectangle");

    protected static Action _actionRRectangle =
        new CmdSetMode(ModeCreateFigRRect.class, "RRect");

    protected static Action _actionCircle =
        new CmdSetMode(ModeCreateFigCircle.class, "Circle");

    protected static Action _actionLine =
        new CmdSetMode(ModeCreateFigLine.class, "Line");

    protected static Action _actionText =
        new CmdSetMode(ModeCreateFigText.class, "Text"); 

    protected static Action _actionPoly =
        new CmdSetMode(ModeCreateFigPoly.class, "Polygon");

    protected static Action _actionSpline =
        new CmdSetMode(ModeCreateFigSpline.class, "Spline");

    protected static Action _actionInk =
        new CmdSetMode(ModeCreateFigInk.class, "Ink");

    ////////////////////////////////////////////////////////////////
    // instance variables
    protected MNamespace  _namespace;
    protected DiagramInfo _diagramName = new DiagramInfo(this);

    ////////////////////////////////////////////////////////////////
    // constructors

    public UMLDiagram() { 
  	super();
    }
  
    public UMLDiagram(MNamespace ns) {
        this();
        setNamespace(ns);
    }
  
    public UMLDiagram(String diagramName, MNamespace ns) {
        this(ns);
        try { setName(diagramName); }
        catch (PropertyVetoException pve) { 
            cat.fatal("Name not allowed in construction of diagram");
        }
    }

    public void initialize(Object owner) {
	super.initialize(owner);
	if (owner instanceof MNamespace) setNamespace((MNamespace) owner);
	else cat.debug("unknown object in UMLDiagram initialize:"
		       + owner);
    }


    ////////////////////////////////////////////////////////////////
    // accessors

    public MNamespace getNamespace() { return _namespace; }
  
    /**
     * sets the namespace of the Diagram, and
     * adds the diagram as a listener of its namspace in the UML model.
     * (so that it can delete itself when the model element is deleted).
     */
    public void setNamespace(Object ns) {
        if (!ModelFacade.isANamespace(ns)) {
            cat.error("Not a namespace");
            cat.error(ns);
            throw new IllegalArgumentException("Given object not a namespace");
        }
        _namespace = (MNamespace) ns;      
        // add the diagram as a listener to the namspace so
        // that when the namespace is remove()d the diagram is deleted also.
        UmlModelEventPump.getPump().addModelEventListener(this,
							  _namespace,
							  UmlModelEventPump.REMOVE);
    }
  
    public String getClassAndModelID() {
        String s = super.getClassAndModelID();
        if (getOwner() == null) return s;
        String id = ModelFacade.getUUID(getOwner());
        return s + "|" + id;
    }

    // TODO: should be overwritten by each subclass of UMLDiagram
    public Object getOwner() {
        return getNamespace();
    }
    
    public void setName(String n) throws PropertyVetoException {
        super.setName(n);
        _diagramName.updateName();
    }
  
    static final long serialVersionUID = -401219134410459387L;

    /**
     * Get the toolbar for the diagram
     * @return the diagram toolbar
     */
    public ToolBar getToolBar() {
        if (_toolBar == null) {
            initToolBar();
        }
        return _toolBar;
    }
  
    /**
     * This is a template method. It sets up the standard toolbar buttons
     * required for all diagram toolbars calling the abstract method
     * initToolBar(JToolBar) which should be implemented on the ancestor
     * to populate the toolbar with diagram specific buttons.
     * @see org.tigris.gef.base.Diagram#initToolBar()
     */
    public void initToolBar() {
        _toolBar = new ToolBar();
        _toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        addActionsToToolbar(_toolBar, getActions());
    }

    /**
     * Return actions available for building toolbar or similar.
     * @return an array of available actions.
     */
    public Object[] getActions() {
        Object manipulateActions[] = getManipulateActions();
        Object umlActions[] = getUmlActions();
        Object shapeActions[] = getShapeActions();

        Object actions[] = new Object[manipulateActions.length
				      + umlActions.length
				      + shapeActions.length];

        int posn = 0;
        System.arraycopy(manipulateActions,
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
     * <p>Initialize the toolbar with buttons required for a specific
     * diagram</p>
     * @param toolBar The toolbar to which to add the buttons.
     */
    private void addActionsToToolbar(JToolBar toolBar, Object actions[]) {
        
        for (int i = 0; i < actions.length; ++i) {
            Object o = actions[i];
            if (o == null) {
                toolBar.addSeparator();
            } else if (o instanceof Action) {
                toolBar.add((Action) o);
            } else if (o instanceof Object[]) {
                Object[] subActions = (Object[]) o;
                toolBar.add(buildPopupToolBoxButton(subActions));
            } else if (o instanceof Component) {
                toolBar.add((Component) o);
            }
        }
    }

    private PopupToolBoxButton buildPopupToolBoxButton(Object[] actions) {
        PopupToolBoxButton toolBox = null;
        for (int i = 0; i < actions.length; ++i) {
            if (actions[i] instanceof Action) {
                Action a = (Action) actions[i];
                if (toolBox == null) {
                    toolBox = new PopupToolBoxButton(a, 0, 1);
                }
                toolBox.add(a);
            } else if (actions[i] instanceof Object[]) {
                Object[] actionRow = (Object[]) actions[i];
                for (int j = 0; j < actionRow.length; ++j) {
                    Action a = (Action) actionRow[j];
                    if (toolBox == null) {
                        int cols = actionRow.length;
                        toolBox = new PopupToolBoxButton(a, 0, cols);
                    }
                    toolBox.add(a);
                }
            }
        }
        return toolBox;
    }
    
    
    /**
     * Implement on the ancestor to get actions to populate toolbar.
     */
    protected abstract Object[] getUmlActions();

    private Object[] getManipulateActions() {
        Object actions[] = {
            _actionSelect,
            _actionBroom,
            null
        };
        return actions;
    }
    
    private Object[] getShapeActions() {
        Object actions[] = {
            null,
            getShapePopupActions(), null,
            _diagramName.getJComponent()
        };
        return actions;
    }
    
    private Object[] getShapePopupActions() {
        Object actions[][] = {
        {_actionRectangle, _actionRRectangle},
	{_actionCircle,    _actionLine},
	{_actionText,      _actionPoly},
	{_actionSpline,    _actionInk}
        };

        return actions;
    }
    
    private PopupToolBoxButton buildShapePopup() {
        PopupToolBoxButton toolBox = new PopupToolBoxButton(_actionRectangle,
							    0,
							    2);
        toolBox.add(_actionRectangle);
        toolBox.add(_actionRRectangle);
        toolBox.add(_actionCircle);
        toolBox.add(_actionLine);
        toolBox.add(_actionText);
        toolBox.add(_actionPoly);
        toolBox.add(_actionSpline);
        toolBox.add(_actionInk);
        return toolBox;
    }
    
    
    /**
     * This diagram listens to events from is namespace ModelElement;
     * When the modelelement is removed, we also want to delete this
     * diagram too.  <p>There is also a risk that if this diagram was
     * the one shown in the diagram panel, then it will remain after
     * it has been deleted. so we need to deselect this diagram.
     */
    public void removed(MElementEvent e) {
	Object newTarget =
	    ProjectManager.getManager().getCurrentProject().getDiagrams().get(0);
	TargetManager.getInstance().setTarget(newTarget);
	UmlModelEventPump.getPump().removeModelEventListener(this,
							     _namespace,
							     UmlModelEventPump.REMOVE);
	ProjectManager.getManager().getCurrentProject().moveToTrash(this);      
      
      
    }
  
    /**
     * not used the UMLDiagram is only interested in the removed() event.
     */
    public void propertySet(MElementEvent e) {
      
    }
  
    /**
     * not used the UMLDiagram is only interested in the removed() event.
     */
    public void roleAdded(MElementEvent e) {
      
    }
  
    /**
     * not used the UMLDiagram is only interested in the removed() event.
     */
    public void roleRemoved(MElementEvent e) {
      
    }
  
    /**
     * not used the UMLDiagram is only interested in the removed() event.
     */
    public void listRoleItemSet(MElementEvent e) {
      
    }
  
    /**
     * not used the UMLDiagram is only interested in the removed() event.
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
	Enumeration enum = elements();
	UmlModelEventPump pump = UmlModelEventPump.getPump();
	while (enum.hasMoreElements()) {
	    Object o = enum.nextElement();
	    if (o instanceof MElementListener) {
		MElementListener listener = (MElementListener) o;
		Fig fig = (Fig) o;
		pump.removeModelEventListener(listener,
					      (MBase) fig.getOwner()); 
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
	Enumeration enum = elements();
	UmlModelEventPump pump = UmlModelEventPump.getPump();
	while (enum.hasMoreElements()) {
	    Fig fig = (Fig) enum.nextElement();
	    if (fig instanceof MElementListener) {
		Object owner = fig.getOwner();
		// pump.addModelEventListener((MElementListener)fig, owner);
		// this will make sure all the correct event listeners are set. 
		fig.setOwner(null); 
		fig.setOwner(owner);
	    }
	}    
      
    }
    
  
  
} /* end class UMLDiagram */
