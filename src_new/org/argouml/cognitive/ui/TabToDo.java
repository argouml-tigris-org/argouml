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

package org.argouml.cognitive.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JToolBar;
import org.argouml.application.api.Configuration;

import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ui.ToDoPane;
import org.argouml.kernel.Wizard;
import org.argouml.swingext.BorderSplitPane;
import org.argouml.swingext.Horizontal;
import org.argouml.swingext.Toolbar;
import org.argouml.swingext.Vertical;
import org.argouml.ui.Actions;
import org.argouml.ui.TabSpawnable;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLAction;

public class TabToDo extends TabSpawnable implements TabToDoTarget {
    ////////////////////////////////////////////////////////////////
    // static variables
    public static int _numHushes = 0;

    public static UMLAction _actionNewToDoItem = Actions.NewToDoItem;
    public static UMLAction _actionResolve = Actions.Resolve;
    public static UMLAction _actionEmailExpert = Actions.EmailExpert;
    //public static UMLAction _actionMoreInfo = Actions.MoreInfo;
    public static UMLAction _actionSnooze = Actions.Snooze;
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
    WizDescription _description = new WizDescription();
    JPanel _lastPanel = null;
    private JPanel _itemPanel;
    private JPanel _treePanel;
    private BorderSplitPane _splitPane;
    private Object _target;

    ////////////////////////////////////////////////////////////////
    // constructor
    public TabToDo() {
        super("tab.todo-item");
        String position = Configuration.getString(Configuration.makeKey("layout", "tabtodo"));
        orientation = 
            ((position.equals("West") || position.equals("East"))
             ? Vertical.getInstance() : Horizontal.getInstance());
        
        setLayout(new BorderLayout());
        //     JPanel buttonPane = new JPanel();
        //     buttonPane.setFont(new Font("Dialog", Font.PLAIN, 9));
        //     buttonPane.setLayout(new FlowLayout());
        //     buttonPane.add(_newButton);
        //     _newButton.setFont(new Font("Dialog", Font.PLAIN, 9));
        //     buttonPane.add(_resolveButton);
        //     _resolveButton.setFont(new Font("Dialog", Font.PLAIN, 9));
        //     buttonPane.add(_fixItButton);
        //     _fixItButton.setFont(new Font("Dialog", Font.PLAIN, 9));
        //     buttonPane.add(_moreInfoButton);
        //     _moreInfoButton.setFont(new Font("Dialog", Font.PLAIN, 9));
        //     buttonPane.add(_emailExpertButton);
        //     _emailExpertButton.setFont(new Font("Dialog", Font.PLAIN, 9));
        //     buttonPane.add(_snoozeButton);
        //     _snoozeButton.setFont(new Font("Dialog", Font.PLAIN, 9));
        //     add(buttonPane, BorderLayout.NORTH);

        Toolbar toolBar = new Toolbar(JToolBar.VERTICAL);
        //toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.Y_AXIS));
        toolBar.add(_actionNewToDoItem);
        toolBar.add(_actionResolve);
        toolBar.add(_actionEmailExpert);
        //toolBar.add(_actionMoreInfo);
        toolBar.add(_actionSnooze);
        toolBar.addSeparator();

        //     toolBar.add(_actionRecordFix);
        //     toolBar.add(_actionReplayFix);

        //     toolBar.add(_actionFixItNext);
        //     toolBar.add(_actionFixItBack);
        //     toolBar.add(_actionFixItFinish);

        //     addTool(toolBar, "New");
        //     addTool(toolBar, "FixIt");
        //     addTool(toolBar, "Resolve");
        //     addTool(toolBar, "EmailExpert");
        //     addTool(toolBar, "MoreInfo");
        //     addTool(toolBar, "Snooze");
        //     //_description.setFont(new Font("Dialog", Font.PLAIN, 9));
        add(toolBar, BorderLayout.WEST);
        //     _description.setLineWrap(true);
        //     _description.setWrapStyleWord(true);

        //Font userFont = MetalLookAndFeel.getUserTextFont();
        //_description.setFont(userFont);
        //add(new JScrollPane(_description), BorderLayout.CENTER);
        //@ add(_description, BorderLayout.CENTER);
        _splitPane = new BorderSplitPane();
        add(_splitPane, BorderLayout.CENTER);
        setTarget(null);
    }

    public void showDescription() {
        if (_lastPanel != null) {
            _splitPane.remove(_lastPanel);
        }
        _splitPane.add(_description, BorderSplitPane.CENTER);
        _lastPanel = _description;
        validate();
        repaint();
    }
    
    public void setTree(ToDoPane tdp) {
        if (orientation.equals(Horizontal.getInstance())) {
            _splitPane.add(tdp, BorderSplitPane.WEST);
        } else {
            _splitPane.add(tdp, BorderSplitPane.NORTH);
        }
    }

    public void showStep(JPanel ws) {
        if (_lastPanel != null)
            _splitPane.remove(_lastPanel);
        if (ws != null) {
            _splitPane.add(ws, BorderSplitPane.CENTER);
            _lastPanel = ws;
        } else {
            _splitPane.add(_description, BorderSplitPane.CENTER);
            _lastPanel = _description;
        }
        validate();
        repaint();
    }

    /**
     * Sets the target of the TabToDo
     * @deprecated As of ArgoUml version 0.13.5,
     *             the visibility of this method will change in the future,
     *             replaced by {@link org.argouml.ui.targetmanager.TargetManager}.
     * @param Object the new target
     */
    public void setTarget(Object item) {
        Object target = item;
        _target = target;
        // the target of description will allways be set directly by tabtodo
        _description.setTarget(target);
        Wizard w = null;
        if (target instanceof ToDoItem)
            w = ((ToDoItem) target).getWizard();
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
        return _target;
    }

    public void refresh() {
        setTarget(TargetManager.getInstance().getTarget());
    }

    protected void updateActionsEnabled(Object target) {      
        _actionResolve.updateEnabled(target);
        _actionEmailExpert.updateEnabled(target);
        _actionSnooze.updateEnabled(target);
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        // we can neglect this, the todopane allways selects the first target
        // in a set of targets. The first target can only be 
        // changed in a targetRemoved or a TargetSet event
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        // how to handle empty target lists?
        // probably the TabTodo should only show an empty pane in that case
        setTarget(e.getNewTargets()[0]);
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTargets()[0]);
        // updateActionsEnabled(e.getNewTargets()[0]);
    }

} /* end class TabToDo */
