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

  public UMLAction _actionNewToDoItem = Actions.NewToDoItem;
  public UMLAction _actionResolve = Actions.Resolve;
  public UMLAction _actionEmailExpert = Actions.EmailExpert;
  public UMLAction _actionMoreInfo = Actions.MoreInfo;
  public UMLAction _actionHush = Actions.Hush;
  public UMLAction _actionFixItNext = Actions.FixItNext;
  public UMLAction _actionFixItBack = Actions.FixItBack;
  public UMLAction _actionFixItFinish = Actions.FixItFinish;
  
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
    
    toolBar.add(_actionFixItNext);
    toolBar.add(_actionFixItBack);
    toolBar.add(_actionFixItFinish);

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
    _actionFixItNext.updateEnabled(_target);
    _actionFixItBack.updateEnabled(_target);
    _actionFixItFinish.updateEnabled(_target);
  }

  
} /* end class TabToDo */






