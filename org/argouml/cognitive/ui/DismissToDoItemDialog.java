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

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import org.argouml.ui.*;
import org.argouml.cognitive.*;

public class DismissToDoItemDialog extends JDialog implements ActionListener {

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
    super(ProjectBrowser.TheInstance, "Dismiss Feedback Item");
    JLabel instrLabel = new JLabel("This item should be removed because");

    setLocation(300, 200);
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

    _explaination.setText("<Enter Rationale Here>\n" +
                          "WARNING:\n" +
                          "AT THE MOMENT DISMISSED TODO ITEMS\n" +
                          "ARE NOT SAVED IN ARGOUML AND ARE LOST\n" +
                          "AFTER SAVING THE PROJECT.");
    //_explaination.requestFocus();
  }

  public void setTarget(Object t) { _target = (ToDoItem) t; }

  /** Prepare for typing in rationale field when window is opened. */
  public void setVisible(boolean b) {
    super.setVisible(b);
    if (b) {
      _explaination.requestFocus();
      _explaination.selectAll();
    }
  }

  
  ////////////////////////////////////////////////////////////////
  // event handlers
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == _badGoalButton) {
      //System.out.println("bad goal");
      GoalsDialog d = new GoalsDialog(ProjectBrowser.TheInstance);
      d.setVisible(true);
      setVisible(false);
      dispose();
      return;
    }
    if (e.getSource() == _badDecButton) {
      //System.out.println("bad decision");
      DesignIssuesDialog d = new DesignIssuesDialog(ProjectBrowser.TheInstance);
      d.setVisible(true);
      setVisible(false);
      dispose();
      return;
    }
    if (e.getSource() == _explainButton) {
      //System.out.println("I can explain!");
      //needs-more-work: make a new history item
      ToDoList list = Designer.TheDesigner.getToDoList();
      list.explicitlyResolve(_target, _explaination.getText());
      setVisible(false);
      dispose();
      return;
    }
    System.out.println("unknown src in DismissToDoItemDialog: " + e.getSource());
  }

} /* end class DismissToDoItemDialog */
