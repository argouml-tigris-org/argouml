/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

package org.argouml.ui;

import javax.swing.table.AbstractTableModel;

/**
 * Table model for a table two visible columns and one invisible column. The
 * first column contains a noneditable Object value. The second column contains
 * a Boolean value for the checkbox. The third column is invisible and contains
 * an Object value for arbitrary data. This table model is useful for the code
 * generation dialog, the sequence diagram reverse enginneering dialog and
 * others.
 */
public class CheckboxTableModel extends AbstractTableModel {
    /**
     * Constructor.
     *
     * @param labels The labels to show in column 1 in the table.
     * @param data The data connected to each line.
     * @param colName1 The header of the first column.
     * @param colName2 The header of the second column.
     */
    public CheckboxTableModel(
            Object[] labels, Object[] data,
            String colName1, String colName2) {
        elements = new Object[labels.length][3];
        for (int i = 0; i < elements.length; i++) {
            elements[i][0] = labels[i];
            elements[i][1] = Boolean.TRUE;
            if (data != null && i < data.length) {
                elements[i][2] = data[i];
            } else {
                elements[i][2] = null;
            }
        }
        columnName1 = colName1;
        columnName2 = colName2;
    }

    /*
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return 2;
    }

    /*
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int col) {
        if (col == 0) {
            return columnName1;
        } else if (col == 1) {
            return columnName2;
        }
        return null;
    }

    /*
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return elements.length;
    }

    /*
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
        if (row < elements.length && col < 3) {
            return elements[row][col];
        } else {
            throw new IllegalArgumentException("Index out of bounds");
        }
    }

    /*
     * @see javax.swing.table.TableModel#setValueAt(
     *         java.lang.Object, int, int)
     */
    public void setValueAt(Object ob, int row, int col) {
        elements[row][col] = ob;
    }

    /*
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class getColumnClass(int col) {
        if (col == 0) {
            return String.class;
        } else if (col == 1) {
            return Boolean.class;
        } else if (col == 2) {
            return Object.class;
        }
        return null;
    }

    /*
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int row, int col) {
        return col == 1 && row < elements.length;
    }

    private Object[][] elements;
    private String columnName1, columnName2;

    /**
     * The UID.
     */
    private static final long serialVersionUID = 111532940880908401L;
}
