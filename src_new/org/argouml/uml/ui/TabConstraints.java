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

package org.argouml.uml.ui;

import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.model_management.*;

import tudresden.ocl.*;
import tudresden.ocl.parser.OclParserException;

import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.ocl.ArgoFacade;
import org.argouml.ocl.ui.*;

public class TabConstraints extends TabSpawnable
	implements TabModelTarget, DocumentListener, ActionListener,
	ListSelectionListener
{
	////////////////////////////////////////////////////////////////
	// instance variables
	private MModelElement _target;
	private boolean _shouldBeEnabled = false;
	private boolean _updating = false;
	private TableModelConstraints _tableModel = new TableModelConstraints();
	private DefaultListSelectionModel _selectionModel = new DefaultListSelectionModel();
	private JTable _table = new JTable(4, 2);
	private JTextField _context = new JTextField();
	private JTextArea _expr = new JTextArea();
	private JSplitPane _splitter;

	private JButton _addButton = new JButton("Add");
	private JButton _editButton = new JButton("Edit");
	private JButton _removeButton = new JButton("Del");

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

		_selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_table.setModel(_tableModel);
		_table.setSelectionModel(_selectionModel);
		Font labelFont = MetalLookAndFeel.getSubTextFont();
		_table.setFont(labelFont);

		_table.setIntercellSpacing(new Dimension(0, 1));
		_table.setShowVerticalLines(false);
		//_table.getSelectionModel().addListSelectionListener(this);
		_table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

		_context.setEditable(false);

		//TableColumn descCol = _table.getColumnModel().getColumn(0);
		//descCol.setMinWidth(100);
		//descCol.setWidth(190);
		//_table.setTableHeader(null);

		JPanel listButtons = new JPanel();
		listButtons.setLayout(new GridLayout(1, 3));
		//listButtons.setLayout(new FlowLayout());
		listButtons.add(_addButton);
		listButtons.add(_editButton);
		listButtons.add(_removeButton);

		JPanel listPane = new JPanel();
		listPane.setLayout(new BorderLayout());
		listPane.add(new JScrollPane(_table), BorderLayout.CENTER);
		listPane.add(listButtons, BorderLayout.SOUTH);
		listPane.setMinimumSize(new Dimension(200, 100));
		listPane.setPreferredSize(new Dimension(400, 100));

		JPanel exprPane = new JPanel();
		exprPane.setLayout(new BorderLayout());
		exprPane.add(_context, BorderLayout.NORTH);
		exprPane.add(_expr, BorderLayout.CENTER);
		_expr.setLineWrap(true);
		_expr.setWrapStyleWord(true);
		_expr.setEditable(true);
		_expr.setEnabled(true);
		_expr.addFocusListener(
			new FocusListener() {
				public void focusLost(FocusEvent fe) {
					updateConstraintName();
					_table.repaint();
				}
				public void focusGained(FocusEvent fe) {
				}
			}
		);

		setLayout(new BorderLayout());
		_splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
								   listPane, exprPane);
		_splitter.setDividerSize(2);
		_splitter.setDividerLocation(300);
		add(_splitter, BorderLayout.CENTER);
		setFont(new Font("Dialog", Font.PLAIN, 10));

		_selectionModel.addListSelectionListener(this);
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
		_editButton.addActionListener(this);
		_removeButton.addActionListener(this);

		updateEnabled(null);
	}

	////////////////////////////////////////////////////////////////
	// accessors
	public void setTarget(Object t) {
		if (!(t instanceof MModelElementImpl)) {
			_target = null;
			_shouldBeEnabled = false;
			return;
		}
		_target = (MModelElementImpl) t;
		_shouldBeEnabled = true;

		Vector constraints = new Vector(_target.getConstraints());
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
	protected void updateEnabled(MConstraint selectedConstraint) {
		_addButton.setEnabled(_target != null);
		_editButton.setEnabled(selectedConstraint != null);
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
			Vector cs = new Vector(_target.getConstraints());
			//int row = _table.getSelectionModel().getMinSelectionIndex();
			int row = _selectionModel.getMinSelectionIndex();
			if (row != -1 && row < cs.size()) {
				//System.out.println("setting constraint body: " + row);
				MConstraint c = (MConstraint) cs.elementAt(row);
				c.setBody(new MBooleanExpression(
					"OCL",
					_expr.getText())
				);
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
			MConstraint c = new MConstraintImpl();
			c.setBody(new MBooleanExpression("OCL", ""));
			_target.addConstraint(c);
			_target.getNamespace().addOwnedElement(c);
			_table.tableChanged(null);

			_table.sizeColumnsToFit(1);
			Vector cs = new Vector(_target.getConstraints());
			String contextDeclaration = "context "+_target.getName();
			int index = cs.indexOf(c);
			_tableModel.update();
			_selectionModel.setSelectionInterval(index, index); //cs.indexOf(c), cs.indexOf(c));
			_context.setText(contextDeclaration);
			_expr.setText("");
			_expr.setEnabled(true);
			_expr.setEditable(true);
			_expr.requestFocus();
			return;
		}
		if (src == _editButton) {
		    Vector cs = new Vector(_target.getConstraints());
	            MConstraint c;
		    int row = _selectionModel.getMinSelectionIndex();
		    if (row != -1 && row < cs.size()) {
				c = (MConstraint) cs.elementAt(row);
				System.out.println("user selected " + row + " = " + c);
				_updating = true;
				try {
					DialogConstraint dialog = new DialogConstraint(_target, c, ProjectBrowser.TheInstance);
					dialog.setVisible(true);
					String result = dialog.getResultingExpression();
					if (result != null) {
						c.setBody(new MBooleanExpression("OCL", result));

						if (dialog.getConstraintName()==null) {
							c.setName("malformed constraint");
						}
						else {
							c.setName(dialog.getConstraintName());
						}

						_table.tableChanged(null);
						_table.sizeColumnsToFit(1);

						_expr.setText(result);
						_expr.setCaretPosition(0);

					}
				}
				finally { _updating = false; }
				updateEnabled(c);
			}
			else {
				c = null;
			}
		    return;
		}
		if (src == _removeButton) {
			int row = _selectionModel.getMinSelectionIndex();
			Vector cs = new Vector(_target.getConstraints());
			if (row > -1 && row < cs.size()) {
				MConstraint c = (MConstraint)cs.elementAt(row);
				_target.removeConstraint(c);
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
		_context.setText("context "+_target.getName());
		if (lse.getValueIsAdjusting()) return;
		if (lse.getSource() == _selectionModel) {
			Vector cs = new Vector(_target.getConstraints());
			MConstraint c;
			//int row = lse.getFirstIndex();
			int row = _selectionModel.getMinSelectionIndex();
			if (row != -1 && row < cs.size()) c = (MConstraint) cs.elementAt(row);
			else c = null;
			//System.out.println("user selected " + row + " = " + c);
			String bodyText = " ";
			if (c != null && c.getBody() != null)
				bodyText = c.getBody().getBody();
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

	private void updateConstraintName() {
		_context.setText("context "+_target.getName());
		Vector cs = new Vector(_target.getConstraints());
		int row = _selectionModel.getMinSelectionIndex();
		if (row != -1 && row < cs.size()) {
			MConstraint c = (MConstraint) cs.elementAt(row);
			try {
				String ocl = "context "+_target.getName()+" "+c.getBody().getBody();
				OclTree tree = OclTree.createTree(ocl, new ArgoFacade(_target));
				String name = tree.getConstraintName();
				c.setName(name);
			}
			catch (Exception e) {
				c.setName("malformed constraint");
			}
		}
	}

} /* end class TabConstraints */




class TableModelConstraints extends AbstractTableModel
implements VetoableChangeListener, DelayedVChangeListener, MElementListener {
  ////////////////
  // instance varables
  MModelElement _target;

  ////////////////
  // constructor
  public TableModelConstraints() { }

  ////////////////
  // accessors
  public void setTarget(MModelElement me) {
    if (_target instanceof MElementImpl)
      ((MModelElementImpl)_target).removeMElementListener(this);
    _target = me;
    if (_target instanceof MElementImpl)
      ((MModelElementImpl)_target).addMElementListener(this);
    fireTableStructureChanged(); //?
  }

  ////////////////
  // TableModel implemetation
  public int getColumnCount() { return 2; }

  public String  getColumnName(int c) {
    if (c == 0) {
		return "welformed";
	}
	else if (c == 1) {
		return "Constraint name";
	}
 	else
		return "XXX";
  }

  public Class getColumnClass(int c) {
	if (c==0) {
		return Boolean.class;
	}
	else {
	    return String.class;
	}
  }

  public boolean isCellEditable(int row, int col) {
    return false;
  }

  public int getRowCount() {
    if (_target == null) return 0;
    Collection cs = _target.getConstraints();
    if (cs == null) return 0;
    return cs.size();
  }

  public Object getValueAt(int row, int col) {
    Vector cs = new Vector(_target.getConstraints());
    if (cs == null) return "null constraints";
    //if (row == cs.size()) return ""; // allows adding new constraint
    MConstraint c = (MConstraint) cs.elementAt(row);

	OclTree tree = null;
	boolean isWelformed = false;
	try {
		String ocl = "context "+_target.getName()+" "+c.getBody().getBody();
		tree = OclTree.createTree(
			ocl,
			new ArgoFacade(_target)
		);
		tree.assureTypes();
		isWelformed = true;
	}
	catch (Exception e) {
	}
    if (col == 0) {
		return new Boolean( isWelformed );
	}
	else if (col == 1) {
		return c.getName();
	}
    else return "C-" + row+","+col; // for debugging
  }

  public void setValueAt(Object aValue, int rowIndex, int columnIndex)  {
    //System.out.println("setting table value " + rowIndex + ", " + columnIndex);
    if (columnIndex != 0) return;
    if (!(aValue instanceof String)) return;
    String val = (String) aValue;
    Vector cs = new Vector(_target.getConstraints());
    //     if (rowIndex >= cs.size()) {
    //       cs.addElement(new MConstraint(val, "expr"));
    //       //fireTableStructureChanged();//?
    //     } else
    if (val.equals("")) {
      cs.removeElementAt(rowIndex);
      //fireTableStructureChanged();//?
    }
    else {
      MConstraint c = (MConstraint) cs.elementAt(rowIndex);
      c.setName(val);
      fireTableRowsUpdated(rowIndex, rowIndex);
    }
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
	}
	public void roleRemoved(MElementEvent mee) {
	}


  public void vetoableChange(PropertyChangeEvent pce) {
    DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
    SwingUtilities.invokeLater(delayedNotify);
  }

  public void delayedVetoableChange(PropertyChangeEvent pce) {
    fireTableStructureChanged();
  }

	public void update() {
		fireTableChanged( new TableModelEvent(this) );
    }


} /* end class TableModelConstraints */

