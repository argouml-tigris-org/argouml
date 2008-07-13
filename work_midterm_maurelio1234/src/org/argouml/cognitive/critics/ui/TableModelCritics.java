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

import java.beans.PropertyChangeEvent;
import java.beans.VetoableChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;
import org.argouml.cognitive.Agency;
import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Translator;

/**
 * The Table Model from the Critics Browser dialog. <p>
 * 
 * This class used to be part of the CriticBrowserDialog.java file. <p>
 * 
 * In advanced mode, this model also handles 
 * priority, supportedDecisions, knowledgeTypes.
 */
class TableModelCritics extends AbstractTableModel
    implements VetoableChangeListener {

    private static final Logger LOG =
        Logger.getLogger(TableModelCritics.class);

    private List<Critic> critics;
    private boolean advanced;

    /**
     * Constructor.
     *
     * @param advancedMode true if we show advanced columns
     */
    public TableModelCritics(boolean advancedMode) { 
        critics = new ArrayList<Critic>(Agency.getCriticList());
        // Set initial sorting on Critic Headline
        Collections.sort(critics, new Comparator<Critic>() {
            public int compare(Critic o1, Critic o2) {
                return o1.getHeadline().compareTo(o2.getHeadline());
            }
        });
        advanced = advancedMode;
    }

    /**
     * @param row the selected row
     * @return the Critic shown on that row
     */
    public Critic getCriticAtRow(int row) {
        return critics.get(row);
    }

    //  TableModel implemetation
    /*
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() { 
        return advanced ? 6 : 3; 
    }

    /*
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int c) {
        if (c == 0)
            return Translator.localize("dialog.browse.column-name.active");
        if (c == 1)
            return Translator.localize("dialog.browse.column-name.headline");
        if (c == 2)
            return Translator.localize("dialog.browse.column-name.snoozed");
        if (c == 3)
            return Translator.localize("dialog.browse.column-name.priority");
        if (c == 4)
            return Translator.localize(
                    "dialog.browse.column-name.supported-decision");
        if (c == 5)
            return Translator.localize(
                    "dialog.browse.column-name.knowledge-type");
        throw new IllegalArgumentException();
    }

    /*
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class< ? > getColumnClass(int c) {
        if (c == 0) {
            return Boolean.class;
        }
        if (c == 1) {
            return String.class;
        }
        if (c == 2) {
            return String.class;
        }
        if (c == 3) {
            return Integer.class;
        }
        if (c == 4) {
            return String.class;
        }
        if (c == 5) {
            return String.class;
        }
        throw new IllegalArgumentException();
    }

    /*
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int row, int col) {
        return col == 0;
    }

    /*
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        if (critics == null) return 0;
        return critics.size();
    }

    /*
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
        Critic cr = critics.get(row);
        if (col == 0) return cr.isEnabled() ? Boolean.TRUE : Boolean.FALSE;
        if (col == 1) return cr.getHeadline();
        if (col == 2) return cr.isActive() ? "no" : "yes";
        if (col == 3) return cr.getPriority();
        if (col == 4) return listToString(cr.getSupportedDecisions());
        if (col == 5) return listToString(cr.getKnowledgeTypes());
        throw new IllegalArgumentException();
    }
    
    private String listToString(List l) {
        StringBuffer buf = new StringBuffer();
        Iterator i = l.iterator();
        boolean hasNext = i.hasNext();
        while (hasNext) {
            Object o = i.next();
            buf.append(String.valueOf(o));
            hasNext = i.hasNext();
            if (hasNext)
                buf.append(", ");
        }
        return buf.toString();
    }

    /*
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)  {
        LOG.debug("setting table value " + rowIndex + ", " + columnIndex);
        if (columnIndex != 0) return;
        if (!(aValue instanceof Boolean)) return;
        Boolean enable = (Boolean) aValue;
        Critic cr = critics.get(rowIndex);
        cr.setEnabled(enable.booleanValue());
        fireTableRowsUpdated(rowIndex, rowIndex); //TODO:
    }

    /*
     * TODO: Why is this here? Who is calling this?
     * 
     * @see java.beans.VetoableChangeListener#vetoableChange(java.beans.PropertyChangeEvent)
     */
    public void vetoableChange(PropertyChangeEvent pce) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                fireTableStructureChanged();
            }
        });
    }

    /**
     * @param advancedMode true causes advanced mode
     */
    void setAdvanced(boolean advancedMode) {
        advanced = advancedMode;
        fireTableStructureChanged();
    }
}
