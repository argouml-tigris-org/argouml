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

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import uci.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
//import com.sun.java.swing.border.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Use_Cases.*;
import uci.uml.Model_Management.*;

public class TabConstraints extends TabSpawnable
implements TabModelTarget, ListSelectionListener, DocumentListener, ActionListener
{
  ////////////////////////////////////////////////////////////////
  // instance variables
  Object _target;
  boolean _shouldBeEnabled = false;
  JList _list = new JList();
  JTextArea _expr = new JTextArea();
  JButton _addButton = new JButton("Add");
  JButton _removeButton = new JButton("Remove");
  JButton _duplicateButton = new JButton("Duplicate");

  // maybe add some expression constructor buttons SOUTH
  JButton _ltButton = new JButton("<");
  JButton _leButton = new JButton("<=");
  JButton _gtButton = new JButton(">");
  JButton _geButton = new JButton(">=");
  JButton _eqButton = new JButton("=");
  JButton _sizeButton = new JButton("->size");
  JButton _asSetButton = new JButton("->asSet");
  JButton _forAllButton = new JButton("->forAll");
  JButton _existsButton = new JButton("->exists");
  // more...  alow user to select terms from lists
  

  
  ////////////////////////////////////////////////////////////////
  // constructor
  public TabConstraints() {
    super("Constraints");
    
    JPanel listButtons = new JPanel();
    listButtons.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    listButtons.add(_addButton);
    _addButton.setMargin(new Insets(0, 0, 0, 0));
    listButtons.add(_removeButton);
    _removeButton.setMargin(new Insets(0, 0, 0, 0));
    listButtons.add(_duplicateButton);
    _duplicateButton.setMargin(new Insets(0, 0, 0, 0));

    //_addButton.set
    
    JPanel listPane = new JPanel();
    listPane.setLayout(new BorderLayout());
    listPane.add(_list, BorderLayout.CENTER);
    listPane.add(listButtons, BorderLayout.SOUTH);

    JPanel exprButtons = new JPanel();
    exprButtons.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
    exprButtons.add(_gtButton);
    _gtButton.setMargin(new Insets(0, 0, 0, 0));
    exprButtons.add(_geButton);
    _geButton.setMargin(new Insets(0, 0, 0, 0));
    exprButtons.add(_ltButton);
    _ltButton.setMargin(new Insets(0, 0, 0, 0));
    exprButtons.add(_leButton);
    _leButton.setMargin(new Insets(0, 0, 0, 0));
    exprButtons.add(_eqButton);
    _eqButton.setMargin(new Insets(0, 0, 0, 0));
    exprButtons.add(_sizeButton);
    _sizeButton.setMargin(new Insets(0, 0, 0, 0));
    exprButtons.add(_asSetButton);
    _asSetButton.setMargin(new Insets(0, 0, 0, 0));
    exprButtons.add(_forAllButton);
    _forAllButton.setMargin(new Insets(0, 0, 0, 0));
    exprButtons.add(_existsButton);
    _existsButton.setMargin(new Insets(0, 0, 0, 0));

    JPanel exprPane = new JPanel();
    exprPane.setLayout(new BorderLayout());
    exprPane.add(_expr, BorderLayout.CENTER);
    exprPane.add(exprButtons, BorderLayout.SOUTH);

    setLayout(new BorderLayout());
    JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					 listPane, exprPane);
    //add(listPane, BorderLayout.WEST);
    //add(exprPane, BorderLayout.CENTER);
    splitter.setDividerSize(2);
    splitter.setDividerLocation(200);
    add(splitter, BorderLayout.CENTER);
    setFont(new Font("Dialog", Font.PLAIN, 10));
    _list.setFont(new Font("Dialog", Font.PLAIN, 10));

    _list.addListSelectionListener(this);
    _expr.getDocument().addDocumentListener(this);

    _addButton.addActionListener(this);
    _removeButton.addActionListener(this);
    _duplicateButton.addActionListener(this);

    _gtButton.addActionListener(this);
    _geButton.addActionListener(this);
    _ltButton.addActionListener(this);
    _leButton.addActionListener(this);
    _eqButton.addActionListener(this);
    _sizeButton.addActionListener(this);
    _forAllButton.addActionListener(this);
    _existsButton.addActionListener(this);

    updateEnabled(null);
  }

  ////////////////////////////////////////////////////////////////
  // accessors
  public void setTarget(Object t) {
    if (!(t instanceof ModelElement)) {
      _target = null;
      _shouldBeEnabled = false;
      return;
    }
    _target = t;
    _shouldBeEnabled = true;

    ModelElement me = (ModelElement) _target;
    Vector constraints = me.getConstraint();
    if (constraints != null && constraints.size() > 0) {
      System.out.println("found a constraint");
      _list.setListData(constraints);
    }
    else _list.setListData(new Vector());
    _list.clearSelection();
    //updateEnabled(null);
    validate();
  }
  public Object getTarget() { return _target; }

  public boolean shouldBeEnabled() { return _shouldBeEnabled; }

  ////////////////////////////////////////////////////////////////
  // utility methods

  /** Enable/disable buttons based on the current selection */
  protected void updateEnabled(Constraint selectedConstraint) {
    _addButton.setEnabled(true);
    _removeButton.setEnabled(selectedConstraint != null);
    _duplicateButton.setEnabled(selectedConstraint != null);

    _expr.setEnabled(selectedConstraint != null);

    _gtButton.setEnabled(selectedConstraint != null);
    _geButton.setEnabled(selectedConstraint != null);
    _ltButton.setEnabled(selectedConstraint != null);
    _leButton.setEnabled(selectedConstraint != null);
    _eqButton.setEnabled(selectedConstraint != null);
    _sizeButton.setEnabled(selectedConstraint != null);
    _asSetButton.setEnabled(selectedConstraint != null);
    _forAllButton.setEnabled(selectedConstraint != null);
    _existsButton.setEnabled(selectedConstraint != null);
  }

  ////////////////////////////////////////////////////////////////
  // actions

  public void doAddButton() { }

  public void doRemoveButton() { }

  public void doDuplicateButton() { }

  ////////////////////////////////////////////////////////////////
  // event handling

  public void insertUpdate(DocumentEvent e) {
    System.out.println(getClass().getName() + " insert");
    if (e.getDocument() == _expr.getDocument()) {
      //setTargetName();
      System.out.println("changed constraint expression text");
    }
  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    System.out.println(getClass().getName() + " changed");
    // Apparently, this method is never called.
  }

  /** Called when a button is pressed */
  public void actionPerformed(ActionEvent ae) {
    Object src = ae.getSource();
    if (src == _addButton) doAddButton();
    else if (src == _removeButton) doRemoveButton();
    else if (src == _duplicateButton) doDuplicateButton();
    else if (src instanceof JButton) {
      String text = ((JButton)src).getText();
      boolean anyLetters = false;
      if (text == null || text.length() == 0) return;
      for (int i = 0; i < text.length(); i++) 
	if (Character.isLetter(text.charAt(i)))
	  anyLetters = true;
      if (!anyLetters) text = " " + text + " ";
      _expr.append(text);
      _expr.requestFocus();
    }
  }
  
  /** Called whenever the constraint selection changes. */
  public void valueChanged(ListSelectionEvent e) {
    //if (e.isAdjusting()) return;
    if (e.getSource() == _list) {
      Constraint c = (Constraint) _list.getSelectedValue();
      System.out.println("user selected " + c);
      String bodyText = "";
      if (c != null && c.getBody() != null)
	bodyText = c.getBody().getBody().getBody();
      _expr.setText(bodyText);
      updateEnabled(c);      
    }
  }
  

  

} /* end class TabConstraints */
