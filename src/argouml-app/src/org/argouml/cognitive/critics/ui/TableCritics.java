// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.cognitive.critics.ui;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.argouml.cognitive.Critic;

/**
 * This class represents the table shown in the Critics Browser dialog. <p>
 * 
 * This is a seperate class so that 
 * the CriticsBrowser does not need to know 
 * what the table contains, i.e. how many columns it has, etc. 
 *
 * @author Michiel
 */
class TableCritics extends JTable {

    private boolean initialised;
    private static final String DESC_WIDTH_TEXT =
        "This is Sample Text for determining Column Width";

    public TableCritics(TableModel model, 
            ListSelectionListener lsl, TableModelListener tml) {
        super(model);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setShowVerticalLines(false);
        getSelectionModel().addListSelectionListener(lsl);
        getModel().addTableModelListener(tml);
        setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        initialised = true;
        setColumnWidths();
    }

    private void setColumnWidths() {
        if (!initialised) return;
        TableColumn checkCol = getColumnModel().getColumn(0);
        TableColumn descCol = getColumnModel().getColumn(1);
        TableColumn actCol = getColumnModel().getColumn(2);
        checkCol.setMinWidth(35);
        checkCol.setMaxWidth(35);
        checkCol.setWidth(30);
        int descWidth = getFontMetrics(getFont())
                .stringWidth(DESC_WIDTH_TEXT);
        descCol.setMinWidth(descWidth);
        descCol.setWidth(descWidth); // no maximum set, so it will stretch...
        actCol.setMinWidth(50);
        actCol.setMaxWidth(55);
        actCol.setWidth(55);
        /* and for advanced mode: */
        if (getColumnModel().getColumnCount() > 3) {
            descCol.setMinWidth(descWidth / 2);
            TableColumn prioCol = getColumnModel().getColumn(3);
            prioCol.setMinWidth(45);
            prioCol.setMaxWidth(50);
            prioCol.setWidth(50);
        }
    }
    
    public Critic getCriticAtRow(int row) {
        TableModelCritics model = (TableModelCritics) getModel();
        return model.getCriticAtRow(row);
    }

    public Dimension getInitialSize() {
        return new Dimension(getColumnModel().getTotalColumnWidth() + 20, 0);
    }
    
    public void setAdvanced(boolean mode) {
        TableModelCritics model = (TableModelCritics) getModel();
        model.setAdvanced(mode);
    }

    /**
     * @see javax.swing.JTable#tableChanged(javax.swing.event.TableModelEvent)
     */
    @Override
    public void tableChanged(TableModelEvent e) {
        super.tableChanged(e);
        /* This changes the complete structure of the table, 
         * so we need to set the column widths again. */
        setColumnWidths();
    }

    
}
