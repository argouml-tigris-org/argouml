// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

import org.argouml.application.api.*;
import org.argouml.kernel.*;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.ui.*;
import org.argouml.ui.targetmanager.TargetEvent;

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
    super("tab.tagged-values");
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
    if (_table.isEditing()) {
      TableCellEditor ce = _table.getCellEditor();
      if (ce != null && !ce.stopCellEditing())
	ce.cancelCellEditing();
    }

    if (!(t instanceof MModelElement)) {
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

    MModelElement me = (MModelElement) _target;
    Vector tvs = new Vector(me.getTaggedValues());
    _tableModel.setTarget(me);
    validate();
  }
  public Object getTarget() { return _target; }

  public void refresh() { setTarget(_target); }

  public boolean shouldBeEnabled(Object target) {
  
    if (!(target instanceof MModelElement)) {
      _shouldBeEnabled = false;
      return _shouldBeEnabled;
    }
    else{
        _shouldBeEnabled = true;
        return true;
    }
  }

    /* (non-Javadoc)
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        // TODO Auto-generated method stub

    }

} /* end class TabTaggedValues */




class TableModelTaggedValues extends AbstractTableModel
implements VetoableChangeListener, DelayedVChangeListener, MElementListener {
  private static final String BUNDLE = "Cognitive";

  ////////////////
  // instance varables
  MModelElement _target;
  TabTaggedValues _tab = null;

  ////////////////
  // constructor
  public TableModelTaggedValues(TabTaggedValues t) { _tab = t; }

  ////////////////
  // accessors
  public void setTarget(MModelElement t) {
    if (_target != null)
        UmlModelEventPump.getPump().removeModelEventListener(this, _target);
    _target = t;
    UmlModelEventPump.getPump().addModelEventListener(this, t);
    fireTableDataChanged();
    _tab.resizeColumns();
  }

  ////////////////
  // TableModel implemetation
  public int getColumnCount() { return 2; }

  public String  getColumnName(int c) {
    if (c == 0) return Argo.localize(BUNDLE, "taggedvaluespane.label.tag");
    if (c == 1) return Argo.localize(BUNDLE, "taggedvaluespane.label.value");
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
    Collection tvs = _target.getTaggedValues();
    //if (tvs == null) return 1;
    return tvs.size() + 1;
  }

  public Object getValueAt(int row, int col) {
    Vector tvs = new Vector(_target.getTaggedValues());
    //if (tvs == null) return "";
    if (row == tvs.size()) return ""; //blank line allows addition
    MTaggedValue tv = (MTaggedValue) tvs.elementAt(row);
    if (col == 0) {
      String n = tv.getTag();
      if (n == null) return "";
      return n;
    }
    if (col == 1) {
      String be = tv.getValue();
      if (be == null) return "";
      return be;
    }
    return "TV-" + row*2+col; // for debugging
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    TableModelEvent mEvent = null;

    if (columnIndex != 0 && columnIndex != 1) return;
    if (!(aValue instanceof String)) return;
    Vector tvs = new Vector(_target.getTaggedValues());
    if (tvs.size() <= rowIndex) {
      MTaggedValue tv = UmlFactory.getFactory().getExtensionMechanisms().createTaggedValue();
      if (columnIndex == 0) tv.setTag((String)aValue);
      if (columnIndex == 1) {
	  tv.setTag("");
	  tv.setValue((String) aValue);
      }
      tvs.addElement(tv);

      mEvent = new TableModelEvent(this, tvs.size(), tvs.size(),
			TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
    }
    else if ("".equals(aValue) && columnIndex == 0) {
      tvs.removeElementAt(rowIndex);
      mEvent = new TableModelEvent(this, rowIndex, rowIndex,
			TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
    }
    else {
      MTaggedValue tv = (MTaggedValue) tvs.elementAt(rowIndex);
      if (columnIndex == 0) tv.setTag((String) aValue);
      if (columnIndex == 1) tv.setValue((String) aValue);
      mEvent = new TableModelEvent(this, rowIndex);
    }
    _target.setTaggedValues(tvs);
    if (mEvent != null)
      fireTableChanged(mEvent);
    _tab.resizeColumns();
  }

  ////////////////
  // event handlers
	public void propertySet(MElementEvent mee) {
	}
	public void listRoleItemSet(MElementEvent mee) {
	}
	public void recovered(MElementEvent mee) {
	}
	public void removed(MElementEvent mee) {
	}
	public void roleAdded(MElementEvent mee) {
	    if ("taggedValue".equals(mee.getName()))
		fireTableChanged(new TableModelEvent(this));
	}
	public void roleRemoved(MElementEvent mee) {
	}


  public void vetoableChange(PropertyChangeEvent pce) {
    DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
    SwingUtilities.invokeLater(delayedNotify);
  }

  public void delayedVetoableChange(PropertyChangeEvent pce) {
    fireTableDataChanged();
    _tab.resizeColumns();
  }

} /* end class TableModelTaggedValues */

