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

import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import uci.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Use_Cases.*;
import uci.uml.Model_Management.*;

public class TabConstraints extends TabSpawnable
implements TabModelTarget, DocumentListener, ActionListener,
  ListSelectionListener
{
  ////////////////////////////////////////////////////////////////
  // instance variables
  private ModelElementImpl _target;
  private boolean _shouldBeEnabled = false;
  private boolean _updating = false;
  private TableModelConstraints _tableModel = new TableModelConstraints();
  private JTable _table = new JTable(4, 1);
  private JTextArea _expr = new JTextArea();
  private JSplitPane _splitter;

  private JButton _addButton = new JButton("Add");
  private JButton _removeButton = new JButton("Remove");

//   private JButton _ltButton = new JButton("<");
//   private JButton _leButton = new JButton("<=");
//   private JButton _gtButton = new JButton(">");
//   private JButton _geButton = new JButton(">=");
//   private JButton _eqButton = new JButton("=");
//   private JButton _sizeButton = new JButton("->size");
//   private JButton _asSetButton = new JButton("->asSet");
//   private JButton _forAllButton = new JButton("->forAll");
//   private JButton _existsButton = new JButton("->exists");
  // more...  allow user to select terms from lists

  ////////////////////////////////////////////////////////////////
  // constructor
  public TabConstraints() {
    super("Constraints");

    _table.setModel(_tableModel);
    Font labelFont = MetalLookAndFeel.getSubTextFont();
    _table.setFont(labelFont);

    _table.setIntercellSpacing(new Dimension(0, 1));
    _table.setShowVerticalLines(false);
    _table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    //_table.getSelectionModel().addListSelectionListener(this);
    _table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

    //TableColumn descCol = _table.getColumnModel().getColumn(0);
    //descCol.setMinWidth(100);
    //descCol.setWidth(190);
    //_table.setTableHeader(null);

    JPanel listButtons = new JPanel();
    listButtons.setLayout(new GridLayout(1, 2));
    listButtons.add(_addButton);
    listButtons.add(_removeButton);

    JPanel listPane = new JPanel();
    listPane.setLayout(new BorderLayout());
    listPane.add(new JScrollPane(_table), BorderLayout.CENTER);
    listPane.add(listButtons, BorderLayout.SOUTH);
    listPane.setMinimumSize(new Dimension(100, 100));
    listPane.setPreferredSize(new Dimension(200, 100));

    JPanel exprButtons = new JPanel();
    exprButtons.setBorder(new EtchedBorder());
    exprButtons.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
//     exprButtons.add(new JLabel("  Insert: "));
//     exprButtons.add(_gtButton);
//     _gtButton.setMargin(new Insets(0, 0, 0, 0));
//     exprButtons.add(_geButton);
//     _geButton.setMargin(new Insets(0, 0, 0, 0));
//     exprButtons.add(_ltButton);
//     _ltButton.setMargin(new Insets(0, 0, 0, 0));
//     exprButtons.add(_leButton);
//     _leButton.setMargin(new Insets(0, 0, 0, 0));
//     exprButtons.add(_eqButton);
//     _eqButton.setMargin(new Insets(0, 0, 0, 0));
//     exprButtons.add(new SpacerPanel());
//     exprButtons.add(_sizeButton);
//     _sizeButton.setMargin(new Insets(0, 0, 0, 0));
//     exprButtons.add(_asSetButton);
//     _asSetButton.setMargin(new Insets(0, 0, 0, 0));
//     exprButtons.add(_forAllButton);
//     _forAllButton.setMargin(new Insets(0, 0, 0, 0));
//     exprButtons.add(_existsButton);
//     _existsButton.setMargin(new Insets(0, 0, 0, 0));

    JPanel exprPane = new JPanel();
    exprPane.setLayout(new BorderLayout());
    exprPane.add(_expr, BorderLayout.CENTER);
    exprPane.add(exprButtons, BorderLayout.SOUTH);
    _expr.setLineWrap(true);
    _expr.setWrapStyleWord(true);

    setLayout(new BorderLayout());
    _splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
					 listPane, exprPane);
    _splitter.setDividerSize(2);
    _splitter.setDividerLocation(200);
    add(_splitter, BorderLayout.CENTER);
    setFont(new Font("Dialog", Font.PLAIN, 10));

    _table.getSelectionModel().addListSelectionListener(this);
    _expr.getDocument().addDocumentListener(this);

//     _gtButton.addActionListener(this);
//     _geButton.addActionListener(this);
//     _ltButton.addActionListener(this);
//     _leButton.addActionListener(this);
//     _eqButton.addActionListener(this);
//     _sizeButton.addActionListener(this);
//     _asSetButton.addActionListener(this);
//     _forAllButton.addActionListener(this);
//     _existsButton.addActionListener(this);

    _addButton.addActionListener(this);
    _removeButton.addActionListener(this);

    updateEnabled(null);
  }

  ////////////////////////////////////////////////////////////////
  // accessors
  public void setTarget(Object t) {
    if (!(t instanceof ModelElementImpl)) {
      _target = null;
      _shouldBeEnabled = false;
      return;
    }
    _target = (ModelElementImpl) t;
    _shouldBeEnabled = true;

    Vector constraints = _target.getConstraint();
    _tableModel.setTarget(_target);
    //TableColumn descCol = _table.getColumnModel().getColumn(0);
    //descCol.setMinWidth(100);
    //descCol.setWidth(190);
    _table.sizeColumnsToFit(0);
    _splitter.setDividerLocation(200);
    updateEnabled(null);
    validate();
  }
  public Object getTarget() { return _target; }

  public void refresh() { setTarget(_target); }

  public boolean shouldBeEnabled() { return _shouldBeEnabled; }

  ////////////////////////////////////////////////////////////////
  // utility methods

  /** Enable/disable buttons based on the current selection */
  protected void updateEnabled(Constraint selectedConstraint) {
    _addButton.setEnabled(_target != null);
    _removeButton.setEnabled(selectedConstraint != null);

    _expr.setEnabled(selectedConstraint != null);

//     _gtButton.setEnabled(selectedConstraint != null);
//     _geButton.setEnabled(selectedConstraint != null);
//     _ltButton.setEnabled(selectedConstraint != null);
//     _leButton.setEnabled(selectedConstraint != null);
//     _eqButton.setEnabled(selectedConstraint != null);
//     _sizeButton.setEnabled(selectedConstraint != null);
//     _asSetButton.setEnabled(selectedConstraint != null);
//     _forAllButton.setEnabled(selectedConstraint != null);
//     _existsButton.setEnabled(selectedConstraint != null);
  }

  ////////////////////////////////////////////////////////////////
  // event handling

  public void insertUpdate(DocumentEvent e) {
    if (_updating) return;
    //System.out.println(getClass().getName() + " insert");
    if (e.getDocument() == _expr.getDocument()) {
      Vector cs = _target.getConstraint();
      //int row = _table.getSelectionModel().getMinSelectionIndex();
      int row = _table.getSelectedRow();
      if (row != -1 && row < cs.size()) {
	//System.out.println("setting constraint body: " + row);
	Constraint c = (Constraint) cs.elementAt(row);
	c.getBody().getBody().setBody(_expr.getText());
	//System.out.println("text=" + _expr.getText());
      }
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
    if (src == _addButton) {
      Vector cs = _target.getConstraint();
      System.out.println("needs-more-work: add constraint dialog box");
      cs.addElement(new Constraint("EnterName",
				   "EnterExpression"));
      _table.tableChanged(null);
      _table.sizeColumnsToFit(0);
      return;
    }
    if (src == _removeButton) {
      int row = _table.getSelectedRow();
      Vector cs = _target.getConstraint();
      if (row > -1 && row < cs.size()) {
	cs.removeElementAt(row);
	_table.tableChanged(null);
	_table.sizeColumnsToFit(0);
      }
      else System.out.println("invalid row to remove");
      return;
    }
    if (src instanceof JButton) {
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
  public void valueChanged(ListSelectionEvent lse) {
    if (lse.getValueIsAdjusting()) return;
    if (lse.getSource() == _table.getSelectionModel()) {
      Vector cs = _target.getConstraint();
      Constraint c;
      //int row = lse.getFirstIndex();
      int row = _table.getSelectedRow();
      if (row != -1 && row < cs.size()) c = (Constraint) cs.elementAt(row);
      else c = null;
      //System.out.println("user selected " + row + " = " + c);
      String bodyText = " ";
      if (c != null && c.getBody() != null)
	bodyText = c.getBody().getBody().getBody();
      //System.out.println("bodytext=" + bodyText);
      _updating = true;
      try {
	_expr.setText(bodyText);
	_expr.setCaretPosition(0);
      }
      finally { _updating = false; }
      updateEnabled(c);
    }
  }

} /* end class TabConstraints */




class TableModelConstraints extends AbstractTableModel
implements VetoableChangeListener, DelayedVChangeListener {
  ////////////////
  // instance varables
  ModelElement _target;

  ////////////////
  // constructor
  public TableModelConstraints() { }

  ////////////////
  // accessors
  public void setTarget(ModelElement me) {
    if (_target instanceof ElementImpl)
      ((ModelElementImpl)_target).removeVetoableChangeListener(this);
    _target = me;
    if (_target instanceof ElementImpl)
      ((ModelElementImpl)_target).addVetoableChangeListener(this);
    fireTableStructureChanged(); //?
  }

  ////////////////
  // TableModel implemetation
  public int getColumnCount() { return 1; }

  public String  getColumnName(int c) {
    if (c == 0) return "Constraint names";
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
    Vector cs = _target.getConstraint();
    if (cs == null) return 0;
    return cs.size();
  }

  public Object getValueAt(int row, int col) {
    Vector cs = _target.getConstraint();
    if (cs == null) return "null constraints";
    //if (row == cs.size()) return ""; // allows adding new constraint
    Constraint c = (Constraint) cs.elementAt(row);
    if (col == 0) return c.getName().getBody();
    else return "C-" + row+","+col; // for debugging
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex)  {
    //System.out.println("setting table value " + rowIndex + ", " + columnIndex);
    if (columnIndex != 0) return;
    if (!(aValue instanceof String)) return;
    String val = (String) aValue;
    Vector cs = _target.getConstraint();
    //     if (rowIndex >= cs.size()) {
    //       cs.addElement(new Constraint(val, "expr"));
    //       //fireTableStructureChanged();//?
    //     } else
    if (val.equals("")) {
      cs.removeElementAt(rowIndex);
      //fireTableStructureChanged();//?
    }
    else {
      Constraint c = (Constraint) cs.elementAt(rowIndex);
      try { c.setName(new Name(val)); }
      catch (PropertyVetoException pve) { }
      fireTableRowsUpdated(rowIndex, rowIndex);
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
  }


} /* end class TableModelConstraints */

