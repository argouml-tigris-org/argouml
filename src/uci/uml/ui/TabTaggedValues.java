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
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;

public class TabTaggedValues extends TabSpawnable
implements TabModelTarget {
  ////////////////////////////////////////////////////////////////
  // constants
  public final static String DEFAULT_NAME = "tag";
  public final static String DEFAULT_VALUE = "value";

  ////////////////////////////////////////////////////////////////
  // instance variables
  Object _target;
  TableModelTaggedValues _tableModel = null;
  boolean _shouldBeEnabled = false;
  JTable _table = new JTable(10, 2);

  ////////////////////////////////////////////////////////////////
  // constructor
  public TabTaggedValues() {
    super("TaggedValues");
    _tableModel = new TableModelTaggedValues(this);
    _table.setModel(_tableModel);
    //     TableColumn keyCol = _table.getColumnModel().getColumn(0);
    //     TableColumn valCol = _table.getColumnModel().getColumn(1);
    //     keyCol.setMinWidth(50);
    //     keyCol.setWidth(150);
    //     keyCol.setPreferredWidth(150);
    //     valCol.setMinWidth(250);
    //     valCol.setWidth(550);
    //     valCol.setPreferredWidth(550);
    //     //_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    //     _table.sizeColumnsToFit(-1);

    _table.setRowSelectionAllowed(false);
    // _table.getSelectionModel().addListSelectionListener(this);
    JScrollPane sp = new JScrollPane(_table);
    Font labelFont = MetalLookAndFeel.getSubTextFont();
    _table.setFont(labelFont);
    resizeColumns();
    setLayout(new BorderLayout());
    add(sp, BorderLayout.CENTER);
  }

  public void resizeColumns() {
    TableColumn keyCol = _table.getColumnModel().getColumn(0);
    TableColumn valCol = _table.getColumnModel().getColumn(1);
    keyCol.setMinWidth(50);
    keyCol.setWidth(150);
    keyCol.setPreferredWidth(150);
    valCol.setMinWidth(250);
    valCol.setWidth(550);
    valCol.setPreferredWidth(550);
    //_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    _table.sizeColumnsToFit(-1);
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

    //TableColumn keyCol = _table.getColumnModel().getColumn(0);
    //TableColumn valCol = _table.getColumnModel().getColumn(1);
    //keyCol.setMinWidth(50);
    //keyCol.setWidth(150);
    //keyCol.setPreferredWidth(150);
    //valCol.setMinWidth(250);
    //valCol.setWidth(550);
    //valCol.setPreferredWidth(550);
    _table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    _table.sizeColumnsToFit(0);

    ModelElement me = (ModelElement) _target;
    Vector tvs = me.getTaggedValue();
    _tableModel.setTarget(me);
    validate();
  }
  public Object getTarget() { return _target; }

  public void refresh() { setTarget(_target); }

  public boolean shouldBeEnabled() { return _shouldBeEnabled; }

} /* end class TabTaggedValues */




class TableModelTaggedValues extends AbstractTableModel
implements VetoableChangeListener, DelayedVChangeListener {
  ////////////////
  // instance varables
  ModelElement _target;
  TabTaggedValues _tab = null;

  ////////////////
  // constructor
  public TableModelTaggedValues(TabTaggedValues t) { _tab = t; }

  ////////////////
  // accessors
  public void setTarget(ModelElement t) {
    if (_target instanceof ElementImpl)
      ((ModelElementImpl)_target).removeVetoableChangeListener(this);
    _target = t;
    if (_target instanceof ElementImpl)
      ((ModelElementImpl)_target).addVetoableChangeListener(this);
    fireTableStructureChanged();
    _tab.resizeColumns();
  }

  ////////////////
  // TableModel implemetation
  public int getColumnCount() { return 2; }

  public String  getColumnName(int c) {
    if (c == 0) return "Tag";
    if (c == 1) return "Value";
    return "XXX";
  }

  public Class getColumnClass(int c) {
    return String.class;
  }

  public boolean isCellEditable(int row, int col) {
    return true;
  }

  public int getRowCount() {
    if (_target == null) return 0;
    Vector tvs = _target.getTaggedValue();
    //if (tvs == null) return 1;
    return tvs.size() + 1;
  }

  public Object getValueAt(int row, int col) {
    Vector tvs = _target.getTaggedValue();
    //if (tvs == null) return "";
    if (row == tvs.size()) return ""; //blank line allows addition
    TaggedValue tv = (TaggedValue) tvs.elementAt(row);
    if (col == 0) {
      Name n = tv.getTag();
      if (n == null || n.getBody() == null) return "";
      return n.getBody();
    }
    if (col == 1) {
      Uninterpreted be = tv.getValue();
      if (be == null || be.getBody() == null) return "";
      return be.getBody();
    }
    return "TV-" + row*2+col; // for debugging
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    if (columnIndex != 0 && columnIndex != 1) return;
    if (!(aValue instanceof String)) return;
    Vector tvs = _target.getTaggedValue();
    if (tvs.size() == rowIndex) {
      TaggedValue tv = new TaggedValue();
      if (columnIndex == 0) tv.setTag(new Name((String) aValue));
      if (columnIndex == 1) tv.setValue(new Uninterpreted((String) aValue));
      tvs.addElement(tv);
      fireTableStructureChanged(); //?
      _tab.resizeColumns();
    }
    else if ("".equals(aValue)) {
      tvs.removeElementAt(rowIndex);
      fireTableStructureChanged(); //?
      _tab.resizeColumns();
    }
    else {
      TaggedValue tv = (TaggedValue) tvs.elementAt(rowIndex);
      if (columnIndex == 0) tv.setTag(new Name((String) aValue));
      if (columnIndex == 1) tv.setValue(new Uninterpreted((String) aValue));
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
    _tab.resizeColumns();
  }

} /* end class TableModelTaggedValues */

