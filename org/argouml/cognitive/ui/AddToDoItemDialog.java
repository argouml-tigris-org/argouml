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

import org.tigris.gef.util.*;
import org.argouml.application.api.*;

import org.argouml.cognitive.*;
import org.argouml.ui.ProjectBrowser;

public class AddToDoItemDialog extends JDialog implements ActionListener {

  ////////////////////////////////////////////////////////////////
  // constants
  private static final String BUNDLE = "Cognitive";

  static final String high = Argo.localize(BUNDLE, "level.high");
  static final String medium = Argo.localize(BUNDLE, "level.medium");
  static final String low = Argo.localize(BUNDLE, "level.low");

  public static final String PRIORITIES[] = { high, medium, low };

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected JTextField _headline = new JTextField();
  protected JComboBox  _priority = new JComboBox(PRIORITIES);
  protected JTextField _moreinfo = new JTextField();
  protected JTextArea  _description = new JTextArea();
  protected JButton _addButton = new JButton("Add");
  protected JButton _cancelButton = new JButton("Cancel");

  ////////////////////////////////////////////////////////////////
  // constructors

  public AddToDoItemDialog() {
    super(ProjectBrowser.TheInstance, "Add a ToDoItem");
    JLabel headlineLabel = new JLabel("Headline:");
    JLabel priorityLabel = new JLabel("Priority:");
    JLabel moreInfoLabel = new JLabel("MoreInfoURL:");

    _priority.setSelectedItem(PRIORITIES[0]);

    setSize(new Dimension(400, 350));
    getContentPane().setLayout(new BorderLayout());
    JPanel top = new JPanel();
    GridBagLayout gb = new GridBagLayout();
    top.setLayout(gb);
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.ipadx = 3; c.ipady = 3;

    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 0;
    gb.setConstraints(headlineLabel, c);
    top.add(headlineLabel);
    c.gridy = 1;
    gb.setConstraints(priorityLabel, c);
    top.add(priorityLabel);
    c.gridy = 2;
    gb.setConstraints(moreInfoLabel, c);
    top.add(moreInfoLabel);

    c.weightx = 1.0;
    c.gridx = 1;
    c.gridwidth = GridBagConstraints.REMAINDER;
    c.gridy = 0;
    gb.setConstraints(_headline, c);
    top.add(_headline);
    c.gridy = 1;
    gb.setConstraints(_priority, c);
    top.add(_priority);
    c.gridy = 2;
    gb.setConstraints(_moreinfo, c);
    top.add(_moreinfo);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    JPanel buttonInner = new JPanel(new GridLayout(1, 2));
    buttonInner.add(_addButton);
    buttonInner.add(_cancelButton);
    buttonPanel.add(buttonInner);

    _description.setText("<Enter TODO Item here>\n" +
                         "WARNING:\n"+
                         "TODO ITEMS DO NOT GET SAVED\n" +
                         "IN THE CURRENT VERSION OF ARGOUML\n");

    getContentPane().add(top, BorderLayout.NORTH);
    getContentPane().add(new JScrollPane(_description), BorderLayout.CENTER);
    getContentPane().add(buttonPanel, BorderLayout.SOUTH);

    getRootPane().setDefaultButton(_addButton);
    _addButton.addActionListener(this);
    _cancelButton.addActionListener(this);
  }


  ////////////////////////////////////////////////////////////////
  // event handlers
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == _addButton) {
      Designer dsgr = Designer.TheDesigner;
      String head = _headline.getText();
      int pri = ToDoItem.HIGH_PRIORITY;
      switch (_priority.getSelectedIndex()) {
      case 0: pri = ToDoItem.HIGH_PRIORITY; break;
      case 1: pri = ToDoItem.MED_PRIORITY; break;
      case 2: pri = ToDoItem.LOW_PRIORITY; break;
      }
      String desc = _description.getText();
      String more = _moreinfo.getText();
      VectorSet offs = new VectorSet();  //? null
      ToDoItem item = new ToDoItem(dsgr, head, pri, desc, more, offs);
      dsgr.getToDoList().addElement(item); //? inform()
      //System.out.println("add an item");
      setVisible(false);
      dispose();
    }
    if (e.getSource() == _cancelButton) {
      //System.out.println("cancel");
      hide();
      dispose();
    }
  }

} /* end class AddToDoItemDialog */
