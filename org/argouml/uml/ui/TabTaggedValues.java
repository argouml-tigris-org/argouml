// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.DelayedChangeNotify;
import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.model.Model;
import org.argouml.model.ModelFacade;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.TabSpawnable;
import org.argouml.ui.targetmanager.TargetEvent;
import org.tigris.gef.presentation.Fig;
import org.tigris.toolbar.ToolBar;

/**
 * Table view of a Model Element's Tagged Values.
 */
public class TabTaggedValues extends TabSpawnable
    implements TabModelTarget {
    ////////////////////////////////////////////////////////////////
    // constants
    private static final String DEFAULT_NAME = "tag";
    private static final String DEFAULT_VALUE = "value";

    ////////////////////////////////////////////////////////////////
    // instance variables
    private Object target;
    private TableModelTaggedValues tableModel = null;
    private boolean shouldBeEnabled = false;
    private JTable table = new JTable(10, 2);
    private JLabel titleLabel;
    private JToolBar buttonPanel;

    /**
     * The constructor.
     */
    public TabTaggedValues() {
        super("tab.tagged-values");
        buttonPanel = new ToolBar();
        buttonPanel.putClientProperty("JToolBar.isRollover",  Boolean.TRUE);
        buttonPanel.setFloatable(false);

        JButton b = new JButton();
        buttonPanel.add(b);
        b.setToolTipText(Translator.localize("button.delete"));
        b.setAction(new ActionRemoveTaggedValue(this));

        tableModel = new TableModelTaggedValues(this);
        table.setModel(tableModel);

        table.setRowSelectionAllowed(false);
        JScrollPane sp = new JScrollPane(table);
        Font labelFont = LookAndFeelMgr.getInstance().getSmallFont();
        table.setFont(labelFont);

        titleLabel = new JLabel("none");
        resizeColumns();
        setLayout(new BorderLayout());
        titleLabel.setLabelFor(buttonPanel);

        JPanel topPane = new JPanel(new BorderLayout());
        topPane.add(titleLabel, BorderLayout.WEST);
        topPane.add(buttonPanel, BorderLayout.CENTER);

        add(topPane, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
    }

    /**
     * Resize the columns.
     */
    public void resizeColumns() {
        TableColumn keyCol = table.getColumnModel().getColumn(0);
        TableColumn valCol = table.getColumnModel().getColumn(1);
        keyCol.setMinWidth(50);
        keyCol.setWidth(150);
        keyCol.setPreferredWidth(150);
        valCol.setMinWidth(250);
        valCol.setWidth(550);
        valCol.setPreferredWidth(550);
        //_table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.sizeColumnsToFit(-1);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @see org.argouml.ui.TabTarget#setTarget(java.lang.Object)
     */
    public void setTarget(Object theTarget) {
        if (table.isEditing()) {
            TableCellEditor ce = table.getCellEditor();
            if (ce != null && !ce.stopCellEditing()) {
                ce.cancelCellEditing();
            }
        }

        Object t = (theTarget instanceof Fig)
                    ? ((Fig) theTarget).getOwner() : theTarget;
        if (!(org.argouml.model.ModelFacade.isAModelElement(t))) {
            target = null;
            shouldBeEnabled = false;
            return;
        }
        target = t;
        shouldBeEnabled = true;

        //TableColumn keyCol = _table.getColumnModel().getColumn(0);
        //TableColumn valCol = _table.getColumnModel().getColumn(1);
        //keyCol.setMinWidth(50);
        //keyCol.setWidth(150);
        //keyCol.setPreferredWidth(150);
        //valCol.setMinWidth(250);
        //valCol.setWidth(550);
        //valCol.setPreferredWidth(550);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.sizeColumnsToFit(0);

        Vector tvs = new Vector(ModelFacade.getTaggedValuesCollection(target));
        tableModel.setTarget(target);
        if (target != null) {
            titleLabel.setText("Target: "
				+ ModelFacade.getUMLClassName(target)
				+ " (" + ModelFacade.getName(target) + ")");
        } else {
            titleLabel.setText("none");
        }
        validate();
    }

    /**
     * @see org.argouml.ui.TabTarget#getTarget()
     */
    public Object getTarget() { return target; }

    /**
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() { setTarget(target); }

    /**
     * @see org.argouml.ui.TabTarget#shouldBeEnabled(java.lang.Object)
     */
    public boolean shouldBeEnabled(Object theTarget) {
        Object t = (theTarget instanceof Fig)
            ? ((Fig) theTarget).getOwner() : theTarget;
        if (!(org.argouml.model.ModelFacade.isAModelElement(t))) {
            shouldBeEnabled = false;
            return shouldBeEnabled;
        } else {
            shouldBeEnabled = true;
            return true;
        }
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(
     *         org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(
     *         org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        setTarget(e.getNewTarget());

    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(
     *         org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTarget());

    }

    /**
     * @return Returns the tableModel.
     */
    protected TableModelTaggedValues getTableModel() {
        return tableModel;
    }
    /**
     * @return Returns the table.
     */
    protected JTable getTable() {
        return table;
    }
} /* end class TabTaggedValues */




/**
 * The model for the table with the tagged values.
 *
 */
class TableModelTaggedValues extends AbstractTableModel
    implements VetoableChangeListener,
	       DelayedVChangeListener,
	       PropertyChangeListener {

    ////////////////
    // instance varables
    private Object target;		// ModelElement
    private TabTaggedValues tab = null;

    ////////////////
    // constructor
    /**
     * The constructor.
     *
     * @param t the tab
     */
    public TableModelTaggedValues(TabTaggedValues t) {
        tab = t;
    }

    ////////////////
    // accessors
    /**
     * @param t the target modelelement
     */
    public void setTarget(Object t) {

        if (!ModelFacade.isAModelElement(t)) {
            throw new IllegalArgumentException();
        }

	if (target != null) {
	    Model.getPump().removeModelEventListener(this, target);
	}
	target = t;
	Model.getPump().addModelEventListener(this, t);
	fireTableDataChanged();
	tab.resizeColumns();
    }

    ////////////////
    // TableModel implemetation
    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() { return 2; }

    /**
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String  getColumnName(int c) {
	if (c == 0) {
	    return Translator.localize("label.taggedvaluespane.tag");
	}
	if (c == 1) {
	    return Translator.localize("label.taggedvaluespane.value");
	}
	return "XXX";
    }

    /**
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class getColumnClass(int c) {
	return String.class;
    }

    /**
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int row, int col) {
	return true;
    }

    /**
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
	if (target == null) {
	    return 0;
	}
	Collection tvs = ModelFacade.getTaggedValuesCollection(target);
	//if (tvs == null) return 1;
	return tvs.size() + 1;
    }

    /**
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
	Vector tvs = new Vector(ModelFacade.getTaggedValuesCollection(target));
	//if (tvs == null) return "";
	if (row == tvs.size()) {
	    return ""; //blank line allows addition
	}
	Object tv = tvs.elementAt(row);
	if (col == 0) {
	    String n = ModelFacade.getTagOfTag(tv);
	    if (n == null) {
	        return "";
	    }
	    return n;
	}
	if (col == 1) {
	    String be = ModelFacade.getValueOfTag(tv);
	    if (be == null) {
	        return "";
	    }
	    return be;
	}
	return "TV-" + row * 2 + col; // for debugging
    }

    /**
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	TableModelEvent mEvent = null;

	if (columnIndex != 0 && columnIndex != 1) {
	    return;
	}
	if (!(aValue instanceof String)) {
	    return;
	}
        if (aValue == null) {
            aValue = "";
        }

	Vector tvs = new Vector(ModelFacade.getTaggedValuesCollection(target));
	if (tvs.size() <= rowIndex) {
	    Object tv =
		Model.getExtensionMechanismsFactory().createTaggedValue();
	    if (columnIndex == 0) {
	        Model.getExtensionMechanismsHelper().setTag(tv, aValue);
	    }
	    if (columnIndex == 1) {
		Model.getExtensionMechanismsHelper().setTag(tv, "");
		Model.getCommonBehaviorHelper().setValue(tv, aValue);
	    }
	    tvs.addElement(tv);

	    mEvent =
		new TableModelEvent(this, tvs.size(), tvs.size(),
				    TableModelEvent.ALL_COLUMNS,
				    TableModelEvent.INSERT);
	} else if ("".equals(aValue) && columnIndex == 0) {
	    tvs.removeElementAt(rowIndex);
	    mEvent =
		new TableModelEvent(this, rowIndex, rowIndex,
				    TableModelEvent.ALL_COLUMNS,
				    TableModelEvent.DELETE);
	} else {
	    Object tv = tvs.elementAt(rowIndex);
	    if (columnIndex == 0) {
	        Model.getExtensionMechanismsHelper().setTag(tv, aValue);
	    }
	    if (columnIndex == 1)  {
                Model.getCommonBehaviorHelper().setValue(tv, aValue);
            }
	    mEvent = new TableModelEvent(this, rowIndex);
	}
	Model.getCoreHelper().setTaggedValues(target, tvs);
	if (mEvent != null) {
	    fireTableChanged(mEvent);
	}
	tab.resizeColumns();
    }

    ////////////////
    // event handlers

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
//      TODO: How to implement this ("mee" was a MElementEvent)?
        //if ("taggedValue".equals(mee.getName()))
        fireTableChanged(new TableModelEvent(this));
    }

    /**
     * @see java.beans.VetoableChangeListener#vetoableChange(java.beans.PropertyChangeEvent)
     */
    public void vetoableChange(PropertyChangeEvent pce) {
	DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
	SwingUtilities.invokeLater(delayedNotify);
    }

    /**
     * @see org.argouml.kernel.DelayedVChangeListener#delayedVetoableChange(java.beans.PropertyChangeEvent)
     */
    public void delayedVetoableChange(PropertyChangeEvent pce) {
	fireTableDataChanged();
	tab.resizeColumns();
    }

} /* end class TableModelTaggedValues */


class ActionRemoveTaggedValue extends AbstractAction {

    private TabTaggedValues tab;

    /**
     * The constructor.
     */
    public ActionRemoveTaggedValue(TabTaggedValues tabtv) {
        super("", ResourceLoaderWrapper.lookupIconResource("Delete"));
        tab = tabtv;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        TableModelTaggedValues model = tab.getTableModel();
        JTable table = tab.getTable();
        int row = table.getSelectedRow();
        List c = new ArrayList(
                ModelFacade.getTaggedValuesCollection(tab.getTarget()));
        if ((row != -1) && (c.size() > row)) {
            c.remove(row);
            Model.getCoreHelper().setTaggedValues(tab.getTarget(), c);
            model.fireTableChanged(new TableModelEvent(model));
        }
    }
}
