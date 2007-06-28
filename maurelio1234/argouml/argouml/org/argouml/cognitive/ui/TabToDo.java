// $Id$
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

package org.argouml.cognitive.ui;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Wizard;
import org.argouml.configuration.Configuration;
import org.argouml.ui.AbstractArgoJPanel;
import org.argouml.ui.TabToDoTarget;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.undo.UndoableAction;
import org.tigris.swidgets.BorderSplitPane;
import org.tigris.swidgets.Horizontal;
import org.tigris.swidgets.Vertical;
import org.tigris.toolbar.ToolBarFactory;

/**
 * The ToDo Tab.
 *
 */
public class TabToDo extends AbstractArgoJPanel 
    implements TabToDoTarget, ComponentListener {
    
    ////////////////////////////////////////////////////////////////
    // static variables
    private static int numHushes;

    private static UndoableAction actionNewToDoItem = new ActionNewToDoItem();
    private static ToDoItemAction actionResolve = new ActionResolve();
    private static ToDoItemAction actionSnooze = new ActionSnooze();

    ////////////////////////////////////////////////////////////////
    // instance variables

    private WizDescription description = new WizDescription();
    private JPanel lastPanel;
    private BorderSplitPane splitPane;
    private Object target;

    /**
     * increments the numHushes.
     */
    public static void incrementNumHushes() {
        numHushes++;
    }

    /**
     * The constructor.
     * Is only called thanks to its listing in the org/argouml/argo.ini file.
     */
    public TabToDo() {
        super("tab.todo-item");
        String position =
	    Configuration.getString(Configuration.makeKey("layout",
							  "tabtodo"));
        setOrientation(
            ((position.equals("West") || position.equals("East"))
             ? Vertical.getInstance() : Horizontal.getInstance()));

        setLayout(new BorderLayout());

        Object[] actions = {actionNewToDoItem, actionResolve, actionSnooze };
        ToolBarFactory factory = new ToolBarFactory(actions);
        factory.setRollover(true);
        factory.setFloatable(false);
        factory.setOrientation(SwingConstants.VERTICAL);
        JToolBar toolBar = factory.createToolBar();

        add(toolBar, BorderLayout.WEST);

        splitPane = new BorderSplitPane();
        add(splitPane, BorderLayout.CENTER);
        setTarget(null);
        
        addComponentListener(this);
    }

    /**
     * Show the description of a todo item.
     */
    public void showDescription() {
        if (lastPanel != null) {
            splitPane.remove(lastPanel);
        }
        splitPane.add(description, BorderSplitPane.CENTER);
        lastPanel = description;
        validate();
        repaint();
    }

    /**
     * @param tdp the todo pane
     */
    public void setTree(ToDoPane tdp) {
        if (getOrientation().equals(Horizontal.getInstance())) {
            splitPane.add(tdp, BorderSplitPane.WEST);
        } else {
            splitPane.add(tdp, BorderSplitPane.NORTH);
        }
    }

    /**
     * @param ws the panel to be shown
     */
    public void showStep(JPanel ws) {
        if (lastPanel != null) {
            splitPane.remove(lastPanel);
	}
        if (ws != null) {
            splitPane.add(ws, BorderSplitPane.CENTER);
            lastPanel = ws;
        } else {
            splitPane.add(description, BorderSplitPane.CENTER);
            lastPanel = description;
        }
        validate();
        repaint();
    }

    /**
     * Sets the target of the TabToDo.
     *
     * @param item the new target
     */
    public void setTarget(Object item) {
        target = item;
        if (isVisible()) {
            setTargetInternal(item);
        }
    }

    private void setTargetInternal(Object item) {
        // the target of description will always be set directly by tabtodo
        description.setTarget(item);
        Wizard w = null;
        if (item instanceof ToDoItem) {
            w = ((ToDoItem) item).getWizard();
        }
        if (w != null) {
            showStep(w.getCurrentPanel());
        } else {
            showDescription();
        }
        updateActionsEnabled(item);
    }

   /**
    * Returns the target of the TabToDo.
    *
    * @return The current target of the TabToDo
    */
    public Object getTarget() {
        return target;
    }


    /**
     * Set the target again to what it was before.
     */
    public void refresh() {
        setTarget(TargetManager.getInstance().getTarget());
    }

    /**
     * Update the "enabled" state of the resolve and snooze actions.
     * 
     * @param item  the target of the TabToDo class
     */
    protected static void updateActionsEnabled(Object item) {
        actionResolve.setEnabled(actionResolve.isEnabled());
        actionResolve.updateEnabled(item);
        actionSnooze.setEnabled(actionSnooze.isEnabled());
        actionSnooze.updateEnabled(item);
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(
     *          TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
	setTarget(e.getNewTarget());
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(
     *          TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
	// how to handle empty target lists?
	// probably the wizstep should only show an empty pane in that case
	setTarget(e.getNewTarget());
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(TargetEvent)
     */
    public void targetSet(TargetEvent e) {
	setTarget(e.getNewTarget());
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 4819730646847978729L;

    /*
     * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
     */
    public void componentShown(ComponentEvent e) {
        // Update our model with our saved target
        setTargetInternal(target);
    }
    
    /*
     * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
     */
    public void componentHidden(ComponentEvent e) {
        // Stop updating model when we're not visible
        setTargetInternal(null);
    }

    public void componentMoved(ComponentEvent e) {
        // ignored
    }

    public void componentResized(ComponentEvent e) {
        // ignored
    }

    
} /* end class TabToDo */
