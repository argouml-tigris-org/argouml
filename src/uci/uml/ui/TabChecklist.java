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




package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.plaf.metal.MetalLookAndFeel;


import uci.util.*;
import uci.argo.checklist.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;

public class TabChecklist extends TabSpawnable
implements TabModelTarget, ActionListener, ListSelectionListener {

  ////////////////////////////////////////////////////////////////
  // instance variables
  protected uci.argo.checklist.CheckManager SINGLTON =
  new uci.argo.checklist.CheckManager();

  Object _target;
  TableModelChecklist _tableModel = null;
  boolean _shouldBeEnabled = false;
  JTable _table = new JTable(10, 2);


  ////////////////////////////////////////////////////////////////
  // constructor
  public TabChecklist() {
    super("Checklist");

    _tableModel = new TableModelChecklist(this);
    _table.setModel(_tableModel);

    Font labelFont = MetalLookAndFeel.getSubTextFont();
    _table.setFont(labelFont);

    //_table.setRowSelectionAllowed(false);
    _table.setIntercellSpacing(new Dimension(0, 1));
    _table.setShowVerticalLines(false);
    _table.getSelectionModel().addListSelectionListener(this);
    _table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

    TableColumn checkCol = _table.getColumnModel().getColumn(0);
    TableColumn descCol = _table.getColumnModel().getColumn(1);
    checkCol.setMinWidth(20);
    checkCol.setMaxWidth(30);
    checkCol.setWidth(30);
    descCol.setPreferredWidth(900);
    //descCol.setWidth(900);
    _table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    _table.sizeColumnsToFit(-1);

    //JScrollPane sp = JTable.createScrollPaneForTable(_table);
    JScrollPane sp = new JScrollPane(_table);

    setLayout(new BorderLayout());
    add(sp, BorderLayout.CENTER);
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
    Checklist cl = CheckManager.getChecklistFor(me);
    if (cl == null) {
      _target = null;
      _shouldBeEnabled = false;
      return;
    }

    _tableModel.setTarget(me);

    TableColumn checkCol = _table.getColumnModel().getColumn(0);
    TableColumn descCol = _table.getColumnModel().getColumn(1);
    checkCol.setMinWidth(20);
    checkCol.setMaxWidth(30);
    checkCol.setWidth(30);
    //descCol.setWidth(900);
    descCol.setPreferredWidth(900);
    _table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    resizeColumns();
    validate();
  }
  public Object getTarget() { return _target; }

  public void refresh() { setTarget(_target); }

  public boolean shouldBeEnabled() { return _shouldBeEnabled; }

  public void resizeColumns() {
    _table.sizeColumnsToFit(0);
  }

  ////////////////////////////////////////////////////////////////
  // event handling


  // enable buttons when selection made

  public void actionPerformed(ActionEvent ae) {
    Object src = ae.getSource();
    //System.out.println("got actionPerformed from " + src);
  }

  public void valueChanged(ListSelectionEvent lse) {
    Object src = lse.getSource();
    //System.out.println("got valueChanged from " + src);
  }

} /* end class TabChecklist */




class TableModelChecklist extends AbstractTableModel
implements VetoableChangeListener, DelayedVChangeListener {
  ////////////////
  // instance varables
  ModelElement _target;
  TabChecklist _panel;

  ////////////////
  // constructor
  public TableModelChecklist(TabChecklist tc) { _panel = tc; }

  ////////////////
  // accessors
  public void setTarget(ModelElement t) {
    if (_target instanceof ElementImpl)
      ((ModelElementImpl)_target).removeVetoableChangeListener(this);
    _target = t;
    if (_target instanceof ElementImpl)
      ((ModelElementImpl)_target).addVetoableChangeListener(this);
    fireTableStructureChanged();
  }

  ////////////////
  // TableModel implemetation
  public int getColumnCount() { return 2; }

  public String  getColumnName(int c) {
    if (c == 0) return "X";
    if (c == 1) return "Description";
    return "XXX";
  }

  public Class getColumnClass(int c) {
    if (c == 0) return Boolean.class;
    if (c == 1) return String.class;
    return String.class;
  }

  public boolean isCellEditable(int row, int col) {
    return col == 0;
  }

  public int getRowCount() {
    if (_target == null) return 0;
    Checklist cl = CheckManager.getChecklistFor(_target);
    if (cl == null) return 0;
    return cl.size();
  }

  public Object getValueAt(int row, int col) {
    Checklist cl = CheckManager.getChecklistFor(_target);
    if (cl == null) return "no checklist";
    CheckItem ci = (CheckItem) cl.elementAt(row);
    if (col == 0) {
      ChecklistStatus stat = CheckManager.getStatusFor(_target);
      return (stat.contains(ci)) ? Boolean.TRUE : Boolean.FALSE;
    }
    else if (col == 1) {
      return ci.getDescription(_target);
    }
    else
      return "CL-" + row*2+col;
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex)  {
    //System.out.println("setting table value " + rowIndex + ", " + columnIndex);
    if (columnIndex != 0) return;
    if (!(aValue instanceof Boolean)) return;
    boolean val = ((Boolean)aValue).booleanValue();
    Checklist cl = CheckManager.getChecklistFor(_target);
    if (cl == null) return;
    CheckItem ci = (CheckItem) cl.elementAt(rowIndex);
    if (columnIndex == 0) {
      ChecklistStatus stat = CheckManager.getStatusFor(_target);
      if (val) stat.addItem(ci);
      else stat.removeItem(ci);
    }
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

} /* end class TableModelChecklist */

