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



// File: PropPanelUseCase.java
// Classes: PropPanelUseCase
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
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.Use_Cases.*;
import uci.uml.ui.*;

/** User interface panel shown at the bottom of the screen that allows
 *  the user to edit the properties of the selected UML model element.
 *  Needs-More-Work: cut and paste base class code from
 *  PropPanelClass. */

public class PropPanelUseCase extends PropPanel {

  ////////////////////////////////////////////////////////////////
  // constants
  // needs-more-work 

  ////////////////////////////////////////////////////////////////
  // instance vars
  JLabel _extPtsLabel = new JLabel("Extension Points");
  TableModelExtensions _tableModel = null;
  JTable _extPts = new JTable(4, 1);
  // declare and initialize all widgets

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelUseCase() {
    super("UseCase Properties");
    GridBagLayout gb = (GridBagLayout) getLayout();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.0;
    c.ipadx = 0; c.ipady = 0;

    _tableModel = new TableModelExtensions(this);
    _extPts.setModel(_tableModel);

    Font labelFont = MetalLookAndFeel.getSubTextFont();
    _extPts.setFont(labelFont);

    _extPts.setIntercellSpacing(new Dimension(0, 1));
    _extPts.setShowVerticalLines(false);
    //_extPts.getSelectionModel().addListSelectionListener(this);
    _extPts.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

    //TableColumn descCol = _extPts.getColumnModel().getColumn(0);
    //descCol.setMinWidth(50);


//     SpacerPanel spacer1 = new SpacerPanel();
//     c.gridx = 0;
//     c.gridy = 11;
//     c.weighty = 1.0;
//     gb.setConstraints(spacer1, c);
//     add(spacer1);


//     SpacerPanel spacer2 = new SpacerPanel();
//     c.weightx = 0.0;
//     c.gridx = 2;
//     c.gridy = 0;
//     gb.setConstraints(spacer2, c);
//     add(spacer2);

    // add all widgets and labels
    JPanel rightPanel = new JPanel();
    rightPanel.setLayout(new BorderLayout());
    rightPanel.add(_extPtsLabel, BorderLayout.NORTH);

    c.gridx = 0;
    c.gridwidth = 2;
//     c.gridy = 0;
//     c.weighty = 0.0;
//     gb.setConstraints(_extPtsLabel, c);
//     add(_extPtsLabel);

    //c.gridy = 1;
    c.gridy = 11;
    c.gridheight = GridBagConstraints.REMAINDER;
    c.weightx = 0.0;
    c.weighty = 10.0;
    JScrollPane scroll = new JScrollPane(_extPts,
					 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//     gb.setConstraints(scroll, c);
//     add(scroll);
    rightPanel.add(scroll, BorderLayout.CENTER);
    gb.setConstraints(rightPanel, c);
    add(rightPanel);
    _extPts.setTableHeader(null);

    // register interest in change events from all widgets
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  /** Set the values to be shown in all widgets based on model */
  protected void setTargetInternal(Object t) {
    super.setTargetInternal(t);
    UseCase uc = (UseCase) t;
    // set the values to be shown in all widgets based on model

    _tableModel.setTarget(uc);
//     TableColumn descCol = _extPts.getColumnModel().getColumn(0);
//     descCol.setMinWidth(50);
    resizeColumns();
    validate();
  }

  public void resizeColumns() {
    _extPts.sizeColumnsToFit(0);
  }

  ////////////////////////////////////////////////////////////////
  // event handlers


  /** The user typed some text */
  public void insertUpdate(DocumentEvent e) {
    //System.out.println(getClass().getName() + " insert");
    // check if it was one of my text fields
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


} /* end class PropPanelUseCase */


class TableModelExtensions extends AbstractTableModel
implements VetoableChangeListener, DelayedVChangeListener {
  ////////////////
  // instance varables
  UseCase _target;
  PropPanelUseCase _panel;

  ////////////////
  // constructor
  public TableModelExtensions(PropPanelUseCase panel) {
    _panel = panel;
  }

  ////////////////
  // accessors
  public void setTarget(UseCase uc) {
    if (_target instanceof ElementImpl)
      ((ModelElementImpl)_target).removeVetoableChangeListener(this);
    _target = uc;
    if (_target instanceof ElementImpl)
      ((ModelElementImpl)_target).addVetoableChangeListener(this);
    fireTableStructureChanged();
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
    Vector extPts = _target.getExtensionPoint();
    if (extPts == null) return 0;
    return extPts.size() + 1;
  }

  public Object getValueAt(int row, int col) {
    Vector extPts = _target.getExtensionPoint();
    if (extPts == null) return "no extension points";
    if (row == extPts.size()) return ""; // blank line allows adding
    String ext = (String) extPts.elementAt(row);
    if (col == 0) return ext;
    else return "UC-" + row*2+col; // for debugging
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex)  {
    //System.out.println("setting table value " + rowIndex + ", " + columnIndex);
    if (columnIndex != 0) return;
    if (!(aValue instanceof String)) return;
    String val = (String) aValue;
    Vector extPts = _target.getExtensionPoint();
    if (rowIndex >= extPts.size()) {
      extPts.addElement(val);
      fireTableStructureChanged();
      _panel.resizeColumns();
    }
    else if (val.equals("")) {
      extPts.removeElementAt(rowIndex);
      fireTableStructureChanged();
      _panel.resizeColumns();
    }
    else extPts.setElementAt(val, rowIndex);
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


} /* end class TableModelExtensions */

