// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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
import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import org.argouml.i18n.Translator;
import org.argouml.kernel.DelayedChangeNotify;
import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.model.ModelFacade;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.TabSpawnable;
import org.argouml.ui.targetmanager.TargetEvent;
import org.tigris.gef.presentation.Fig;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

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

    /**
     * The constructor.
     * 
     */
    public TabTaggedValues() {
        super("tab.tagged-values");
        tableModel = new TableModelTaggedValues(this);
        table.setModel(tableModel);

        table.setRowSelectionAllowed(false);
        // _table.getSelectionModel().addListSelectionListener(this);
        JScrollPane sp = new JScrollPane(table);
        Font labelFont = LookAndFeelMgr.getInstance().getSmallFont();
        table.setFont(labelFont);
        titleLabel = new JLabel("none");
        resizeColumns();
        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
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
            if (ce != null && !ce.stopCellEditing())
                ce.cancelCellEditing();
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
        }
        else {
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
        Object t = (theTarget instanceof Fig) ? ((Fig) theTarget).getOwner() : theTarget;
        if (!(org.argouml.model.ModelFacade.isAModelElement(t))) {
            shouldBeEnabled = false;
            return shouldBeEnabled;
        }
        else {
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

} /* end class TabTaggedValues */




class TableModelTaggedValues extends AbstractTableModel
    implements VetoableChangeListener,
	       DelayedVChangeListener,
	       MElementListener
{

    ////////////////
    // instance varables
    private Object target;		// ModelElement
    private TabTaggedValues tab = null;

    ////////////////
    // constructor
    public TableModelTaggedValues(TabTaggedValues t) { tab = t; }

    ////////////////
    // accessors
    public void setTarget(Object t) {
        
        if (!ModelFacade.isAModelElement(t))
            throw new IllegalArgumentException();
        
	if (target != null)
	    UmlModelEventPump.getPump().removeModelEventListener(this, target);
	target = t;
	UmlModelEventPump.getPump().addModelEventListener(this, t);
	fireTableDataChanged();
	tab.resizeColumns();
    }

    ////////////////
    // TableModel implemetation
    public int getColumnCount() { return 2; }

    public String  getColumnName(int c) {
	if (c == 0) return Translator.localize("label.taggedvaluespane.tag");
	if (c == 1) return Translator.localize("label.taggedvaluespane.value");
	return "XXX";
    }

    public Class getColumnClass(int c) {
	return String.class;
    }

    public boolean isCellEditable(int row, int col) {
	return true;
    }

    public int getRowCount() {
	if (target == null) return 0;
	Collection tvs = ModelFacade.getTaggedValuesCollection(target);
	//if (tvs == null) return 1;
	return tvs.size() + 1;
    }

    public Object getValueAt(int row, int col) {
	Vector tvs = new Vector(ModelFacade.getTaggedValuesCollection(target));
	//if (tvs == null) return "";
	if (row == tvs.size()) return ""; //blank line allows addition
	Object tv = tvs.elementAt(row);
	if (col == 0) {
	    String n = ModelFacade.getTagOfTag(tv);
	    if (n == null) return "";
	    return n;
	}
	if (col == 1) {
	    String be = ModelFacade.getValueOfTag(tv);
	    if (be == null) return "";
	    return be;
	}
	return "TV-" + row * 2 + col; // for debugging
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	TableModelEvent mEvent = null;

	if (columnIndex != 0 && columnIndex != 1) return;
	if (!(aValue instanceof String)) return;
        if (aValue == null) aValue = "";
        
	Vector tvs = new Vector(ModelFacade.getTaggedValuesCollection(target));
	if (tvs.size() <= rowIndex) {
	    Object tv =
		UmlFactory.getFactory()
		    .getExtensionMechanisms().createTaggedValue();
	    if (columnIndex == 0) ModelFacade.setTag(tv, aValue);
	    if (columnIndex == 1) {
		ModelFacade.setTag(tv, "");
		ModelFacade.setValue(tv, aValue);
	    }
	    tvs.addElement(tv);

	    mEvent =
		new TableModelEvent(this, tvs.size(), tvs.size(),
				    TableModelEvent.ALL_COLUMNS,
				    TableModelEvent.INSERT);
	}
	else if ("".equals(aValue) && columnIndex == 0) {
	    tvs.removeElementAt(rowIndex);
	    mEvent =
		new TableModelEvent(this, rowIndex, rowIndex,
				    TableModelEvent.ALL_COLUMNS,
				    TableModelEvent.DELETE);
	}
	else {
	    Object tv = tvs.elementAt(rowIndex);
	    if (columnIndex == 0) ModelFacade.setTag(tv, aValue);
	    if (columnIndex == 1)  {
                ModelFacade.setValue(tv, aValue);
            }
	    mEvent = new TableModelEvent(this, rowIndex);
	}
	ModelFacade.setTaggedValues(target, tvs);
	if (mEvent != null)
	    fireTableChanged(mEvent);
	tab.resizeColumns();
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
	tab.resizeColumns();
    }

} /* end class TableModelTaggedValues */
