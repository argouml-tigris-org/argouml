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



// File: PropPanelState.java
// Classes: PropPanelState
// Original Author: your email address here
// $Id$

package uci.uml.ui.props;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.table.*;
import javax.swing.plaf.metal.*;
import javax.swing.border.*;

import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.generate.*;
import uci.uml.ui.*;

/** User interface panel shown at the bottom of the screen that allows
 *  the user to edit the properties of the selected UML model
 *  element. */

public class PropPanelState extends PropPanel
implements DocumentListener, ItemListener {

  ////////////////////////////////////////////////////////////////
  // constants
  // needs-more-work 

  ////////////////////////////////////////////////////////////////
  // instance vars
  JLabel _entryLabel = new JLabel("Entry: ");
  JTextField _entryField = new JTextField();
  JLabel _exitLabel = new JLabel("Exit: ");
  JTextField _exitField = new JTextField();

  JLabel _internalLabel = new JLabel("Internal Transitions");
  TableModelInternalTrans _tableModel = null;
  JTable _internalTable = new JTable(4, 1);

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelState() {
    super("State Properties");
    _tableModel = new TableModelInternalTrans(this);
    GridBagLayout gb = (GridBagLayout) getLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weighty = 0.0;
    c.weightx = 0.0;
    c.ipadx = 0; c.ipady = 0;

    c.gridx = 0;
    c.gridwidth = 1;
    c.gridy = 1;
    gb.setConstraints(_entryLabel, c);
    add(_entryLabel);

    c.gridy = 2;
    gb.setConstraints(_exitLabel, c);
    add(_exitLabel);

    _entryField.setMinimumSize(new Dimension(120, 20));
    c.weightx = 1.0;
    c.gridx = 1;
    c.gridy = 1;
    gb.setConstraints(_entryField, c);
    add(_entryField);

    c.gridy = 2;
    gb.setConstraints(_exitField, c);
    add(_exitField);

    _internalTable.setModel(_tableModel);

    Font labelFont = MetalLookAndFeel.getSubTextFont();
    _internalTable.setFont(labelFont);

    _internalTable.setIntercellSpacing(new Dimension(0, 1));
    _internalTable.setShowVerticalLines(false);
    //_internalTable.getSelectionModel().addListSelectionListener(this);
    _internalTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

//     TableColumn descCol = _internalTable.getColumnModel().getColumn(0);
//     descCol.setMinWidth(50);

    SpacerPanel spacer1 = new SpacerPanel();
    c.gridx = 0;
    c.gridy = 11;
    c.weighty = 1.0;
    gb.setConstraints(spacer1, c);
    add(spacer1);

    SpacerPanel spacer2 = new SpacerPanel();
    c.weightx = 0.0;
    c.gridx = 2;
    c.gridy = 0;
    gb.setConstraints(spacer2, c);
    add(spacer2);

    c.gridx = 3;
    c.gridwidth = 1;
    c.gridy = 0;
    c.weightx = 1.0;
    c.weighty = 0.0;
    gb.setConstraints(_internalLabel, c);
    add(_internalLabel);

    c.gridy = 1;
    c.gridheight = 12; //GridBagConstraints.REMAINDER;
    c.weightx = 1.0;
    c.weighty = 1.0;
    JScrollPane scroll =
      new JScrollPane(_internalTable,
		      JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		      JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scroll.setPreferredSize(new Dimension(300, 200));
    gb.setConstraints(scroll, c);
    add(scroll);
    _internalTable.setTableHeader(null);

    _entryField.getDocument().addDocumentListener(this);
    _entryField.setFont(_stereoField.getFont());
    _exitField.getDocument().addDocumentListener(this);
    _exitField.setFont(_stereoField.getFont());

    resizeColumns();
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Set the values to be shown in all widgets based on model */
  protected void setTargetInternal(Object t) {
    super.setTargetInternal(t);
    State st = (State) t;

    _entryField.setText(GeneratorDisplay.Generate(st.getEntry()));
    _exitField.setText(GeneratorDisplay.Generate(st.getExit()));

    _tableModel.setTarget(st);
    _internalTable.sizeColumnsToFit(-1);
//     TableColumn descCol = _internalTable.getColumnModel().getColumn(0);
//     descCol.setMinWidth(50);
    resizeColumns();
    validate();
  }

  public void resizeColumns() {
    _internalTable.sizeColumnsToFit(0);
  }


  public void setTargetEntry() {
    if (_inChange) return;
    State s = (State) _target;
    String newText = _entryField.getText();
    System.out.println("setTargetEntry: " + newText);
    try {
      s.setEntry(new ActionSequence(new Name(newText)));
    }
    catch (PropertyVetoException pve) { }
  }

  public void setTargetExit() {
    if (_inChange) return;
    State s = (State) _target;
    String newText = _exitField.getText();
    System.out.println("setTargetExit: " + newText);
    try {
      s.setExit(new ActionSequence(new Name(newText)));
    }
    catch (PropertyVetoException pve) { }
  }


  ////////////////////////////////////////////////////////////////
  // event handlers

  /** The user typed some text */
  public void insertUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " insert");
    // check if it was one of my text fields
    if (e.getDocument() == _entryField.getDocument()) setTargetEntry();
    if (e.getDocument() == _exitField.getDocument()) setTargetExit();
    super.insertUpdate(e);
  }

  public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

  public void changedUpdate(DocumentEvent e) {
    System.out.println(getClass().getName() + " changed");
    // Apparently, this method is never called.
  }

  /** The user modified one of the widgets */
  public void itemStateChanged(ItemEvent e) {
    Object src = e.getSource();
    // check for each widget, and update the model with new value
  }


} /* end class PropPanelState */


class TableModelInternalTrans extends AbstractTableModel
implements VetoableChangeListener, DelayedVChangeListener {
  ////////////////
  // instance varables
  State _target;
  PropPanelState _panel;

  ////////////////
  // constructor
  public TableModelInternalTrans(PropPanelState p) { _panel = p; }

  ////////////////
  // accessors
  public void setTarget(State s) {
    if (_target instanceof ElementImpl)
      ((ModelElementImpl)_target).removeVetoableChangeListener(this);
    _target = s;
    if (_target instanceof ElementImpl)
      ((ModelElementImpl)_target).addVetoableChangeListener(this);
    fireTableStructureChanged();
    _panel.resizeColumns();
  }

  ////////////////
  // TableModel implemetation
  public int getColumnCount() { return 1; }

  public String  getColumnName(int c) {
    if (c == 0) return "Description";
    return "XXX";
  }

  public Class getColumnClass(int c) {
    return String.class;
  }

  public boolean isCellEditable(int row, int col) {
    return col == 0;
  }

  public int getRowCount() {
    if (_target == null) return 0;
    Vector trans = _target.getInternalTransition();
    if (trans == null) return 1;
    return trans.size() + 1;
  }

  public Object getValueAt(int row, int col) {
    Vector trans = _target.getInternalTransition();
    if (trans == null) return "";
    if (row >= trans.size()) return ""; // blank line allows adding
    Transition t = (Transition) trans.elementAt(row);
    String tStr = GeneratorDisplay.Generate(t);
    if (col == 0) return tStr;
    else return "UC-" + row*2+col; // for debugging
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex)  { 
   //System.out.println("setting table value " + rowIndex + ", " + columnIndex);
    if (columnIndex != 0) return;
    if (!(aValue instanceof String)) return;
    String val = (String) aValue;
    val = val.trim();
    Vector trans = ((State)_target).getInternalTransition();
    if (trans == null) trans = new Vector();
    Transition newTrans = ParserDisplay.SINGLETON.parseTransition(val);
    if (newTrans != null) {
      try {
	State st = (State) _target;
	newTrans.setSource(st);
	newTrans.setTarget(st);
	newTrans.setStateMachine(st.getStateMachine());
	//newTrans.setState(st);
      }
      catch (PropertyVetoException pve) {
	System.out.println("PropertyVetoException in PropPanelState");
      }
    }
    else {
      System.out.println("newTrans is null!");
      fireTableStructureChanged();
      _panel.resizeColumns();
      return;
    }

    if (rowIndex == trans.size()) trans.addElement(newTrans);
    else if (val.equals("")) trans.removeElementAt(rowIndex);
    else trans.setElementAt(newTrans, rowIndex);

    try { ((State)_target).setInternalTransition(trans); }
    catch (PropertyVetoException pve) {
      System.out.println("could not set internal transitions");
    }

    fireTableStructureChanged();
    _panel.resizeColumns();
  }

  ////////////////
  // event handlers

  public void vetoableChange(PropertyChangeEvent pce) {
    DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
    SwingUtilities.invokeLater(delayedNotify);
  }

  public void delayedVetoableChange(PropertyChangeEvent pce) {
    fireTableStructureChanged();
    _panel.resizeColumns();
  }


} /* end class TableModelInternalTrans */

