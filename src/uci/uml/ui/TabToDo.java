// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


package uci.uml.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;

import uci.ui.ToolBar;
import uci.util.*;
import uci.argo.kernel.*;


public class TabToDo extends TabSpawnable implements TabToDoTarget {
  ////////////////////////////////////////////////////////////////
  // static variables

  public static UMLAction _actionNewToDoItem = Actions.NewToDoItem;
  public static UMLAction _actionResolve = Actions.Resolve;
  public static UMLAction _actionEmailExpert = Actions.EmailExpert;
  public static UMLAction _actionMoreInfo = Actions.MoreInfo;
  public static UMLAction _actionHush = Actions.Hush;
  public static UMLAction _actionRecordFix = Actions.RecordFix;
  public static UMLAction _actionReplayFix = Actions.ReplayFix;
//   public static UMLAction _actionFixItNext = Actions.FixItNext;
//   public static UMLAction _actionFixItBack = Actions.FixItBack;
//   public static UMLAction _actionFixItFinish = Actions.FixItFinish;
  
  ////////////////////////////////////////////////////////////////
  // instance variables
  Object _target;  //not ToDoItem
//   JButton _newButton = new JButton("New");
//   JButton _resolveButton = new JButton("Resolve");
//   JButton _fixItButton = new JButton("FixIt");  //html
//   JButton _moreInfoButton = new JButton("More Info"); //html
//   JButton _emailExpertButton = new JButton("Email Expert"); //html
//   JButton _hushButton = new JButton("Hush");
  JTextArea _description = new JTextArea();
  

  ////////////////////////////////////////////////////////////////
  // constructor
  public TabToDo() {
    super("ToDoItem");
    System.out.println("making TabToDo");
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
//     buttonPane.add(_hushButton);
//     _hushButton.setFont(new Font("Dialog", Font.PLAIN, 9));
//     add(buttonPane, BorderLayout.NORTH);

    ToolBar toolBar = new ToolBar();
    toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.Y_AXIS));
    toolBar.add(_actionNewToDoItem);
    toolBar.add(_actionResolve);
    toolBar.add(_actionEmailExpert);
    toolBar.add(_actionMoreInfo);
    toolBar.add(_actionHush);
    toolBar.addSeparator();
    
    toolBar.add(_actionRecordFix);
    toolBar.add(_actionReplayFix);

    //     toolBar.add(_actionFixItNext);
    //     toolBar.add(_actionFixItBack);
    //     toolBar.add(_actionFixItFinish);

    //     addTool(toolBar, "New");
    //     addTool(toolBar, "FixIt");
    //     addTool(toolBar, "Resolve");
    //     addTool(toolBar, "EmailExpert");
    //     addTool(toolBar, "MoreInfo");
    //     addTool(toolBar, "Hush");
    //     //_description.setFont(new Font("Dialog", Font.PLAIN, 9));
    add(toolBar, BorderLayout.WEST);
    _description.setLineWrap(true);
    add(new JScrollPane(_description), BorderLayout.CENTER);
    setTarget(null);
  }


  // needs-more-work: change this to use Actions
//   protected void addTool(JToolBar tb, String name) {
//     ImageIcon icon = loadImageIcon("images/" + name + ".gif",name);
//     JButton b = (JButton) tb.add(new JButton(icon));
//     b.setToolTipText(name);
//     b.setMargin(new Insets(0,0,0,0));
//     b.getAccessibleContext().setAccessibleName(name);
//   }

//   protected ImageIcon loadImageIcon(String filename, String desc) {
//     return new ImageIcon(filename, desc);
//   }
  
  ////////////////////////////////////////////////////////////////
  // accessors
  public void setTarget(Object item) {  //ToDoItem
    _target = item;
    updateActionsEnabled();
    if (_target == null) {
      //_description.setEnabled(false);
      _description.setText("No ToDoItem selected");
    }
    else if (_target instanceof ToDoItem) {
      ToDoItem tdi = (ToDoItem) _target;
      _description.setEnabled(true);
      _description.setText(tdi.getDescription()); // getDescription()
    }
    else {
      _description.setText("needs-more-work");
      return;
    }

  }
  public Object getTarget() { return _target; }

  protected void updateActionsEnabled() {
    _actionResolve.updateEnabled(_target);
    _actionEmailExpert.updateEnabled(_target);
    _actionMoreInfo.updateEnabled(_target);
    _actionHush.updateEnabled(_target);
    _actionRecordFix.updateEnabled(_target);
    _actionReplayFix.updateEnabled(_target);
    //     _actionFixItNext.updateEnabled(_target);
    //     _actionFixItBack.updateEnabled(_target);
    //     _actionFixItFinish.updateEnabled(_target);
  }

  
} /* end class TabToDo */






