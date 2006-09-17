// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.AbstractArgoJPanel;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewTagDefinition;
import org.argouml.uml.ui.foundation.extension_mechanisms.UMLTagDefinitionComboBoxModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.undo.UndoableAction;
import org.tigris.toolbar.ToolBar;

/**
 * Table view of a Model Element's Tagged Values.
 */
public class TabTaggedValues extends AbstractArgoJPanel
    implements TabModelTarget, ListSelectionListener {

    /**
     * Serial version generated for rev 1.58
     */
    private static final long serialVersionUID = -8566948113385239423L;

    private Object target;
    private boolean shouldBeEnabled = false;
    private JTable table = new JTable(10, 2);
    private JLabel titleLabel;
    private JToolBar buttonPanel;

    private UMLComboBox2 tagDefinitionsComboBox;

    private UMLComboBoxModel2 tagDefinitionsComboBoxModel;


    /**
     * Construct a TaggedValues pane for the property panel
     */
    public TabTaggedValues() {
        super("tab.tagged-values");
        buttonPanel = new ToolBar();
        buttonPanel.setFloatable(false);

        JButton b = new JButton();
        buttonPanel.add(b);
        b.setAction(new ActionNewTagDefinition());
        b.setText("");
        b.setFocusable(false);

        b = new JButton();
        buttonPanel.add(b);
        b.setToolTipText(Translator.localize("button.delete"));
        b.setAction(new ActionRemoveTaggedValue(table));
        b.setText("");
        b.setFocusable(false);
  
        table.setModel(new TabTaggedValuesModel());
        table.setRowSelectionAllowed(false);
        tagDefinitionsComboBoxModel = new UMLTagDefinitionComboBoxModel();
        tagDefinitionsComboBox = new UMLComboBox2(tagDefinitionsComboBoxModel);
        Class tagDefinitionClass = (Class) Model.getMetaTypes()
                .getTagDefinition();
        tagDefinitionsComboBox.setRenderer(new UMLListCellRenderer2(false));
        table.setDefaultEditor(tagDefinitionClass,
                new DefaultCellEditor(tagDefinitionsComboBox));
        table.setDefaultRenderer(tagDefinitionClass,
                new UMLTableCellRenderer());
        table.getSelectionModel().addListSelectionListener(this);

        JScrollPane sp = new JScrollPane(table);
        Font labelFont = LookAndFeelMgr.getInstance().getStandardFont();
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
        table.doLayout();
    }

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
        if (!(Model.getFacade().isAModelElement(t))) {
            target = null;
            shouldBeEnabled = false;
            return;
        }
        target = t;
        shouldBeEnabled = true;

        tagDefinitionsComboBoxModel.setTarget(t);

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        ((TabTaggedValuesModel) table.getModel()).setTarget(target);
        table.sizeColumnsToFit(0);

        if (target != null) {
            titleLabel.setText("Target: "
				+ Model.getFacade().getUMLClassName(target)
				+ " ("
				+ Model.getFacade().getName(target) + ")");
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
        if (!(Model.getFacade().isAModelElement(t))) {
            shouldBeEnabled = false;
            return shouldBeEnabled;
        }
        shouldBeEnabled = true;
        return true;
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(
     *         org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        setTarget(e.getNewTarget());
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
    protected TabTaggedValuesModel getTableModel() {
        return (TabTaggedValuesModel) table.getModel();
    }
    /**
     * @return Returns the table.
     */
    protected JTable getTable() {
        return table;
    }

    /**
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            DefaultListSelectionModel sel = 
                (DefaultListSelectionModel) e.getSource();
            ArrayList tvs = new ArrayList(Model.getFacade()
                    .getTaggedValuesCollection(target));
            int index = sel.getLeadSelectionIndex();
            if (index >= 0 && index < tvs.size()) {
                Object tagDef = Model.getFacade().getTagDefinition(
                        tvs.get(index));
                tagDefinitionsComboBoxModel.setSelectedItem(tagDef);
            }
        }
    }

} /* end class TabTaggedValues */

class ActionRemoveTaggedValue extends UndoableAction {

    /**
     * Serial version generated for rev 1.58
     */
    private static final long serialVersionUID = 8276763533039642549L;
    
    /**
     * The table we are bound to.
     */
    private JTable table;

    /**
     * Construct an Action to remove a TaggedValue from the table.
     * 
     * @param tableTv A JTable backed by a TabTaggedValuesModel
     */
    public ActionRemoveTaggedValue(JTable tableTv) {
        super(Translator.localize("button.delete"),
                ResourceLoaderWrapper.lookupIcon("Delete"));
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("button.delete"));
        table = tableTv;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        TabTaggedValuesModel model = (TabTaggedValuesModel) table.getModel();
        model.removeRow(table.getSelectedRow());
    }
}
