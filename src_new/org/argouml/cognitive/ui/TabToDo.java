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

package org.argouml.cognitive.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.argouml.application.api.Configuration;

import org.argouml.cognitive.ToDoItem;
import org.argouml.kernel.Wizard;
import org.argouml.swingext.BorderSplitPane;
import org.argouml.swingext.Horizontal;
import org.argouml.swingext.Vertical;
import org.argouml.ui.Actions;
import org.argouml.ui.TabSpawnable;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLAction;
import org.tigris.toolbar.ToolBar;

/**
 * The toDo Tab.
 *
 */
public class TabToDo extends TabSpawnable implements TabToDoTarget {
    ////////////////////////////////////////////////////////////////
    // static variables
    private static int numHushes = 0;

    private static UMLAction actionNewToDoItem = Actions.NewToDoItem;
    private static UMLAction actionResolve = Actions.Resolve;
    private static UMLAction actionEmailExpert = Actions.EmailExpert;
    //public static UMLAction _actionMoreInfo = Actions.MoreInfo;
    private static UMLAction actionSnooze = Actions.Snooze;
    //public static UMLAction _actionRecordFix = Actions.RecordFix;
    //public static UMLAction _actionReplayFix = Actions.ReplayFix;
    //public static UMLAction _actionFixItNext = Actions.FixItNext;
    //public static UMLAction _actionFixItBack = Actions.FixItBack;
    //public static UMLAction _actionFixItFinish = Actions.FixItFinish;

    ////////////////////////////////////////////////////////////////
    // instance variables
    //JButton _newButton = new JButton("New");
    //JButton _resolveButton = new JButton("Resolve");
    //JButton _fixItButton = new JButton("FixIt");  //html
    //JButton _moreInfoButton = new JButton("More Info"); //html
    //JButton _emailExpertButton = new JButton("Email Expert"); //html
    //JButton _snoozeButton = new JButton("Snooze");
    //JTextArea _description = new JTextArea();
    private WizDescription description = new WizDescription();
    private JPanel lastPanel = null;
    private BorderSplitPane splitPane;
    private Object target;

    /**
     * increments the _numHushes.
     */
    public static void incrementNumHushes() {
        numHushes++;
    }

    /**
     * The constructor.
     * 
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

        JToolBar toolBar = new ToolBar(JToolBar.VERTICAL);
        toolBar.putClientProperty("JToolBar.isRollover",  Boolean.TRUE);
        toolBar.add(actionNewToDoItem);
        toolBar.add(actionResolve);
        toolBar.add(actionEmailExpert);
        toolBar.add(actionSnooze);
        toolBar.setFloatable(false);
        
        add(toolBar, BorderLayout.WEST);

        splitPane = new BorderSplitPane();
        add(splitPane, BorderLayout.CENTER);
        setTarget(null);
    }

    /**
     * 
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
     * Sets the target of the TabToDo
     * @deprecated As of ArgoUml version 0.13.5,
     *             the visibility of this method will change in the future,
     *             replaced by 
     *             {@link org.argouml.ui.targetmanager.TargetManager}.
     * @param item the new target
     */
    public void setTarget(Object item) {
        Object t = item;
        target = t;
        // the target of description will allways be set directly by tabtodo
        description.setTarget(t);
        Wizard w = null;
        if (t instanceof ToDoItem) {
            w = ((ToDoItem) t).getWizard();
	}
        if (w != null) {
            showStep(w.getCurrentPanel());
        } else {
            showDescription();
        }
        updateActionsEnabled(item);
    }

   /**
    * Returns the target of the TabToDo
    * @return The current target of the TabToDo
    */
    public Object getTarget() {
        return target;
    }

    /**
     * @see org.argouml.cognitive.ui.TabToDoTarget#refresh()
     */
    public void refresh() {
        setTarget(TargetManager.getInstance().getTarget());
    }

    /**
     * @param target ignored
     */
    protected static void updateActionsEnabled(Object target) {      
        actionResolve.updateEnabled(target);
        actionEmailExpert.updateEnabled(target);
        actionSnooze.updateEnabled(target);
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(
     *          TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
	setTarget(e.getNewTarget());
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(
     *          TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
	// how to handle empty target lists?
	// probably the wizstep should only show an empty pane in that case
	setTarget(e.getNewTarget());
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(TargetEvent)
     */
    public void targetSet(TargetEvent e) {
	setTarget(e.getNewTarget());
    }

} /* end class TabToDo */
