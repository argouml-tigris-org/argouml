package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import uci.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
//import com.sun.java.swing.border.*;


public class TabToDo extends TabSpawnable implements TabToDoTarget {
  ////////////////////////////////////////////////////////////////
  // instance variables
  Object _target;  //ToDoItem
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

    JToolBar toolBar = new JToolBar();
    toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.Y_AXIS));
    addTool(toolBar, "New");
    addTool(toolBar, "FixIt");
    addTool(toolBar, "Resolve");
    addTool(toolBar, "EmailExpert");
    addTool(toolBar, "MoreInfo");
    addTool(toolBar, "Hush");
    add(toolBar, BorderLayout.WEST);
    //_description.setFont(new Font("Dialog", Font.PLAIN, 9));
    add(new JScrollPane(_description), BorderLayout.CENTER);
    setTarget(null);
  }


  // needs-more-work: change this to use Actions
  protected void addTool(JToolBar tb, String name) {
    ImageIcon icon = loadImageIcon("images/" + name + ".gif",name);
    JButton b = (JButton) tb.add(new JButton(icon));
    b.setToolTipText(name);
    b.setMargin(new Insets(0,0,0,0));
    b.getAccessibleContext().setAccessibleName(name);
  }

  protected ImageIcon loadImageIcon(String filename, String desc) {
    return new ImageIcon(filename, desc);
  }
  
  ////////////////////////////////////////////////////////////////
  // accessors
  public void setTarget(Object item) {  //ToDoItem
    _target = item;
    if (_target == null) {
      //_description.setEnabled(false);
      _description.setText("No ToDoItem selected");
    }
    else {
      _description.setEnabled(true);
      _description.setText(_target.toString()); // getDescription()
    }
  }
  public Object getTarget() { return _target; }
  
}






