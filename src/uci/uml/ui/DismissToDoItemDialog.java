package uci.uml.ui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;

import uci.util.*;
import uci.argo.kernel.*;

public class DismissToDoItemDialog extends JFrame implements ActionListener {

  ////////////////////////////////////////////////////////////////
  // constants
  String BAD_GOAL_LABEL = "It is not relevant to my goals";
  String BAD_DECISION_LABEL = "It is not of concern at the momemt";
  String EXPLAIN_LABEL = "Reason given below";

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected JButton _badGoalButton = new JButton(BAD_GOAL_LABEL);
  protected JButton _badDecButton = new JButton(BAD_DECISION_LABEL);
  protected JButton _explainButton = new JButton(EXPLAIN_LABEL);
  protected JTextArea _explaination = new JTextArea();
  protected ToDoItem _target;

  ////////////////////////////////////////////////////////////////
  // constructors
  
  public DismissToDoItemDialog() {
    super("Dismiss Feedback Item");
    JLabel instrLabel = new JLabel("This item should be removed because");

    setSize(new Dimension(300, 250));
    Container content = getContentPane();
    GridBagLayout gb = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1.0;
    c.ipadx = 3; c.ipady = 3;

    content.setLayout(gb);

    JScrollPane explain = new JScrollPane(_explaination);
    //set size?

    c.gridx = 0;
    c.gridy = 0;
    gb.setConstraints(instrLabel, c);
    content.add(instrLabel);
    c.gridy = 1;
    gb.setConstraints(_badGoalButton, c);
    content.add(_badGoalButton);
    c.gridy = 2;
    gb.setConstraints(_badDecButton, c);
    content.add(_badDecButton);
    c.gridy = 3;
    gb.setConstraints(_explainButton, c);
    content.add(_explainButton);
    c.gridy = 4;
    c.weighty = 1.0;
    gb.setConstraints(explain, c);
    content.add(explain);

    _badGoalButton.addActionListener(this);
    _badDecButton.addActionListener(this);
    _explainButton.addActionListener(this);
  }

  public void setTarget(Object t) { _target = (ToDoItem) t; }

  
  ////////////////////////////////////////////////////////////////
  // event handlers
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == _badGoalButton) {
      System.out.println("bad goal");
      hide();
      dispose();
    }
    if (e.getSource() == _badDecButton) {
      System.out.println("bad decision");
      hide();
      dispose();
    }
    if (e.getSource() == _explainButton) {
      System.out.println("I can explain!");
      hide();
      dispose();
    }
  }

} /* end class DismissToDoItemDialog */
