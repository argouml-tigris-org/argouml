// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
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
import java.beans.*;

import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.table.*;
import com.sun.java.swing.plaf.metal.MetalLookAndFeel;

import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;

public class TabTaggedValues extends TabSpawnable
implements TabModelTarget, ActionListener, ListSelectionListener {
  ////////////////////////////////////////////////////////////////
  // constants
  public final static String DEFAULT_NAME = "tag";
  public final static String DEFAULT_VALUE = "value";
  
  ////////////////////////////////////////////////////////////////
  // instance variables
  Object _target;
  TableModelTaggedValues _tableModel = new TableModelTaggedValues();
  boolean _shouldBeEnabled = false;
  JTable _table = new JTable(10, 2);
  JButton _addButton = new JButton("Add");
  JButton _removeButton = new JButton("Remove");
  JButton _duplicateButton = new JButton("Duplicate");

  
  ////////////////////////////////////////////////////////////////
  // constructor
  public TabTaggedValues() {
    super("TaggedValues");
    
    JPanel tableButtons = new JPanel();
    tableButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
    tableButtons.add(_addButton);
    tableButtons.add(_removeButton);
    tableButtons.add(_duplicateButton);

    _addButton.addActionListener(this);
    _addButton.setMargin(new Insets(0, 0, 0, 0));
    _removeButton.addActionListener(this);
    _removeButton.setMargin(new Insets(0, 0, 0, 0));
    _duplicateButton.addActionListener(this);
    _duplicateButton.setMargin(new Insets(0, 0, 0, 0));

    _table.setModel(_tableModel);
    TableColumn keyCol = _table.getColumnModel().getColumn(0);
    TableColumn valCol = _table.getColumnModel().getColumn(1);
    keyCol.setMinWidth(50);
    keyCol.setWidth(150);
    valCol.setMinWidth(250);
    valCol.setWidth(550);

    _table.setRowSelectionAllowed(false);
    _table.getSelectionModel().addListSelectionListener(this);
    JScrollPane sp = JTable.createScrollPaneForTable(_table);
    Font labelFont = MetalLookAndFeel.getSubTextFont();
    _table.setFont(labelFont);
    
    setLayout(new BorderLayout());
    add(tableButtons, BorderLayout.SOUTH);
    add(sp, BorderLayout.CENTER);
    //setFont(new Font("Dialog", Font.PLAIN, 10));

    //_list.addListSelectionListener(this);
    //_expr.getDocument().addDocumentListener(this);
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

    _removeButton.setEnabled(false);
    _duplicateButton.setEnabled(false);
    
    ModelElement me = (ModelElement) _target;
    Vector tvs = me.getTaggedValue();
    if (tvs != null && tvs.size() > 0)
      System.out.println("found a tagged value");

    _tableModel.setTarget(me);
    
    validate();
  }
  public Object getTarget() { return _target; }

  public boolean shouldBeEnabled() { return _shouldBeEnabled; }


  ////////////////////////////////////////////////////////////////
  // actions

  public void addRow() {
    System.out.println("add button");
    if (_target instanceof ElementImpl) {
      int nameConflicts = 1;
      Vector tvs = ((ElementImpl)_target).getTaggedValue();
      String name = DEFAULT_NAME;
      boolean conflict = true;
      while (conflict && tvs != null) {
	conflict = false;
	if (nameConflicts > 1) name = DEFAULT_NAME + nameConflicts;
	java.util.Enumeration enum = tvs.elements();
	while (enum.hasMoreElements()) {
	  TaggedValue tv = (TaggedValue) enum.nextElement();
	  if (tv.getTag().getBody().equals(name)) conflict = true;
	}
	nameConflicts++;
      }
      
      TaggedValue newTV = new TaggedValue(name, DEFAULT_VALUE);
      try {
	((ElementImpl)_target).addTaggedValue(newTV);
      }
      catch (PropertyVetoException pve) {
	System.out.println("could not add tagged value");
      }
    }
  }

  public void removeRow() {
    System.out.println("remove button");
    TaggedValue tv = getSelectedTaggedValue();
    if (tv == null) return;
    try {
      ((ElementImpl)_target).removeTaggedValue(tv);
    }
    catch (PropertyVetoException pve) {
      System.out.println("could not remove row");
    }    
  }

  public void duplicateRow() {
    System.out.println("duplicate button");
    TaggedValue tv = getSelectedTaggedValue();
    if (tv == null) return;
    try {
      TaggedValue newTV = new TaggedValue(tv.getTag().getBody(),
					  tv.getValue().getBody());
      ((ElementImpl)_target).addTaggedValue(newTV);
    }
    catch (PropertyVetoException pve) {
      System.out.println("could not duplicate row");
    }    
  }

  protected TaggedValue getSelectedTaggedValue() {
    int selectedRow = _table.getSelectedRow();
    Vector tvs = ((ElementImpl)_target).getTaggedValue();
    if (tvs == null || tvs.size() <= selectedRow || selectedRow < 0)
      return null;
    TaggedValue tv = (TaggedValue) tvs.elementAt(selectedRow);
    return tv;
  }
  
  ////////////////////////////////////////////////////////////////
  // event handling

//   public void insertUpdate(DocumentEvent e) {
//     System.out.println(getClass().getName() + " insert");
//     if (e.getDocument() == _expr.getDocument()) {
//       //setTargetName();
//       System.out.println("changed constraint expression text");
//     }
//   }

//   public void removeUpdate(DocumentEvent e) { insertUpdate(e); }

//   public void changedUpdate(DocumentEvent e) {
//     System.out.println(getClass().getName() + " changed");
//     // Apparently, this method is never called.
//   }


  // enable buttons when selection made

  public void actionPerformed(ActionEvent ae) {
    Object src = ae.getSource();
    if (src == _addButton) addRow();
    else if (src == _removeButton) removeRow();
    else if (src == _duplicateButton) duplicateRow();
  }

  public void valueChanged(ListSelectionEvent e) {
    //System.out.println("valueChanged");
    Object src = e.getSource();
    if (src == _table.getSelectionModel()) {
      int selectedRow = e.getFirstIndex();
      //System.out.println("selection changed: " + selectedRow);
      if (selectedRow >= 0) {
	_removeButton.setEnabled(true);
	_duplicateButton.setEnabled(true);
      }
    }
  }

} /* end class TabTaggedValues */




class TableModelTaggedValues extends AbstractTableModel
implements VetoableChangeListener, DelayedVetoableChangeListener {
  ////////////////
  // instance varables
  ModelElement _target;

  ////////////////
  // constructor
  public TableModelTaggedValues() { }

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
    if (tvs == null) return 0;
    return tvs.size();
  }
  
  public Object getValueAt(int row, int col) {
    Vector tvs = _target.getTaggedValue();
    if (tvs == null) return "null tvs";
    TaggedValue tv = (TaggedValue) tvs.elementAt(row);
    if (col == 0) return tv.getTag().getBody();
    if (col == 1) return tv.getValue().getBody();
    return "tv" + row*2+col;
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    if (columnIndex != 0 && columnIndex != 1) return;
    if (!(aValue instanceof String)) return;
    Vector tvs = _target.getTaggedValue();
    if (tvs == null || tvs.size() <= rowIndex) return;
    TaggedValue tv = (TaggedValue) tvs.elementAt(rowIndex);
    if (columnIndex == 0) tv.setTag(new Name((String) aValue));
    if (columnIndex == 1) tv.setValue(new Uninterpreted((String) aValue));
  }

  ////////////////
  // event handlers

  public void vetoableChange(PropertyChangeEvent pce) {
    DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
    SwingUtilities.invokeLater(delayedNotify);
  }

  public void delayedVetoableChange(PropertyChangeEvent pce) {
    fireTableStructureChanged();
  }

  
} /* end class TableModelTaggedValues */

