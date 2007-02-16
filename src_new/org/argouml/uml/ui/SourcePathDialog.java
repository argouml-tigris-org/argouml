// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import org.argouml.i18n.Translator;
import org.argouml.ui.ArgoDialog;

/**
 * This dialog appears when selecting
 * <code>Generation -> Settings for Generate for Project...</code>
 * in the menu.<p>
 *
 * Provides support for setting a source path tagged value used in Java
 * round trip engineering.
 */
public class SourcePathDialog extends ArgoDialog implements ActionListener {

    private SourcePathController srcPathCtrl = new SourcePathControllerImpl();

    private SourcePathTableModel srcPathTableModel =
        srcPathCtrl.getSourcePathSettings();

    private JTable srcPathTable;

    private JButton delButton;

    private ListSelectionModel rowSM;

    /**
     * The constructor.
     *
     */
    public SourcePathDialog() {
        super(
            Translator.localize("action.generate-code-for-project"),
            ArgoDialog.OK_CANCEL_OPTION,
            true);

        srcPathTable = new JTable();
        srcPathTable.setModel(srcPathTableModel);
        srcPathTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        // Hack: don't show first column, where the model element object is
        // placed.
        TableColumn elemCol = srcPathTable.getColumnModel().getColumn(0);
        elemCol.setMinWidth(0);
        elemCol.setMaxWidth(0);

        delButton = new JButton(Translator.localize("button.delete"));
        delButton.setEnabled(false);
        addButton(delButton, 0);

        rowSM = srcPathTable.getSelectionModel();
        rowSM.addListSelectionListener(new SelectionListener());
        delButton.addActionListener(this);

        setContent(new JScrollPane(srcPathTable));
    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);

        // OK Button ------------------------------------------
        if (e.getSource() == getOkButton()) {
            buttonOkActionPerformed();
        }
        // Delete Button
        if (e.getSource() == delButton) {
            deleteSelectedSettings();
        }
    }

    /**
     * The OK button is pressed.
     */
    private void buttonOkActionPerformed() {
        srcPathCtrl.setSourcePath(srcPathTableModel);
    }

    /**
     * Retrieve the selected rows indexes.
     */
    private int[] getSelectedIndexes() {
        int firstSelectedRow = rowSM.getMinSelectionIndex();
        int lastSelectedRow = rowSM.getMaxSelectionIndex();
        LinkedList selectedIndexesList = new LinkedList();
        int numSelectedRows = 0;
        for (int i = firstSelectedRow; i <= lastSelectedRow; i++) {
            if (rowSM.isSelectedIndex(i)) {
                numSelectedRows++;
                selectedIndexesList.add(new Integer(i));
            }
        }
        int[] indexes = new int[selectedIndexesList.size()];
        java.util.Iterator it = selectedIndexesList.iterator();
        for (int i = 0; i < indexes.length && it.hasNext(); i++) {
            indexes[i] = ((Integer) it.next()).intValue();
        }
        return indexes;
    }

    /**
     * Delete the source path settings of the selected table rows.
     */
    private void deleteSelectedSettings() {
        // find selected rows and make a list of the model elements
        // that are selected
        int[] selectedIndexes = getSelectedIndexes();

        // confirm with the user that he wants to delete, presenting the
        // list of settings to delete
        StringBuffer msg = new StringBuffer();
        msg.append(Translator.localize("dialog.source-path-del.question"));
        for (int i = 0; i < selectedIndexes.length; i++) {
            msg.append("\n");
            msg.append(srcPathTableModel.getMEName(selectedIndexes[i]));
            msg.append(" (");
            msg.append(srcPathTableModel.getMEType(selectedIndexes[i]));
            msg.append(")");
        }

        int res = JOptionPane.showConfirmDialog(this,
            msg.toString(),
            Translator.localize("dialog.title.source-path-del"),
            JOptionPane.OK_CANCEL_OPTION);

        if (res == JOptionPane.OK_OPTION) {
            // procede with the deletion in the model
            int firstSel = rowSM.getMinSelectionIndex();
            for (int i = 0; i < selectedIndexes.length && firstSel != -1; i++) {
                srcPathCtrl.deleteSourcePath(srcPathTableModel
                        .getModelElement(firstSel));
                srcPathTableModel.removeRow(firstSel);
                firstSel = rowSM.getMinSelectionIndex();
            }
            // disable the button since no row will be selected now
            delButton.setEnabled(false);
        }
    }

    /**
     * Class that listens to selection events.
     */
    class SelectionListener implements ListSelectionListener {
        public void valueChanged(javax.swing.event.ListSelectionEvent e) {
            if (!delButton.isEnabled()) {
                delButton.setEnabled(true);
            }
        }
    }
} /* end class SourcePathDialog */
