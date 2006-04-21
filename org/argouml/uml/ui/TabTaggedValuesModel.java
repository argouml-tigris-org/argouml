// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;
import java.util.Collection;
import java.util.Vector;

import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.DelayedChangeNotify;
import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.model.Model;

/**
 * The model for the table with the tagged values. Implementation for UML 1.4
 * and TagDefinitions.
 */
public class TabTaggedValuesModel extends AbstractTableModel implements
        VetoableChangeListener, DelayedVChangeListener, PropertyChangeListener {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(TabTaggedValuesModel.class);

    // //////////////
    // instance varables
    /**
     * The ModelElement that is the target (see TargetManager).
     */
    private Object target;

    /**
     * The tab, i.e. the JPanel that contains the UI 
     * that represents the tagged values.
     */
    private TabTaggedValues tab;

    // //////////////
    // constructor
    /**
     * The constructor.
     *
     * @param t
     *            the tab
     */
    public TabTaggedValuesModel(TabTaggedValues t) {
        tab = t;
    }

    // //////////////
    // accessors
    /**
     * @param t
     *            the target modelelement
     */
    public void setTarget(Object t) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Set target to " + t);
        }
        if (!Model.getFacade().isAModelElement(t)) {
            throw new IllegalArgumentException();
        }
        if (target != t) {
            if (target != null) {
                Model.getPump().removeModelEventListener(this, target);
            }
            target = t;
            if (t != null) {
                Model.getPump().addModelEventListener(this, t);
            }
        }
        // always fire changes in the case something has changed in the
        // composition of the taggedValues collection.
        fireTableDataChanged();
        tab.resizeColumns();
    }

    // //////////////
    // TableModel implementation
    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return 2;
    }

    /**
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int c) {
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
        if (c == 0) {
            return (Class) Model.getMetaTypes().getTagDefinition();
        }
        if (c == 1) {
            return String.class;
        }
        return null;
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
        Collection tvs = Model.getFacade().getTaggedValuesCollection(target);
        // if (tvs == null) return 1;
        return tvs.size() + 1;
    }

    /**
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
        Vector tvs =
            new Vector(Model.getFacade().getTaggedValuesCollection(target));
        // if (tvs == null) return "";
        if (row == tvs.size()) {
            return ""; // blank line allows addition
        }
        Object tv = tvs.elementAt(row);
        if (col == 0) {
            Object n = Model.getFacade().getTagDefinition(tv);
            if (n == null) {
                return "";
            }
            return n;
        }
        if (col == 1) {
            String be = Model.getFacade().getValueOfTag(tv);
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

        //if (!(aValue instanceof String)) {
        //    return;
        //}
        if (columnIndex == 1 && aValue == null) {
            aValue = "";
        }

        Vector tvs =
            new Vector(Model.getFacade().getTaggedValuesCollection(target));
        if (tvs.size() <= rowIndex) {
            Object tv =
                Model.getExtensionMechanismsFactory()
                    .createTaggedValue();
            Model.getExtensionMechanismsHelper().addTaggedValue(target, tv);
            if (columnIndex == 0) {
                Model.getExtensionMechanismsHelper().setTag(tv, aValue);
            }
            if (columnIndex == 1) {
                // TODO: It's not legal to have a TaggedValue without a type
                // Need to give it a default type or force user to pick one
                Model.getExtensionMechanismsHelper().setTag(tv, "");
                Model.getCommonBehaviorHelper().setValue(tv, aValue);
            }
            tvs.addElement(tv);

            mEvent =
                new TableModelEvent(this, tvs.size(), tvs.size(),
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
        } else if ("".equals(aValue) && columnIndex == 0) {
            tvs.removeElementAt(rowIndex);
            mEvent =
                new TableModelEvent(this, rowIndex, rowIndex,
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
        } else {
            Object tv = tvs.elementAt(rowIndex);
            if (columnIndex == 0) {
                Model.getExtensionMechanismsHelper().setTag(tv, aValue);
            }
            if (columnIndex == 1) {
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

    // //////////////
    // event handlers

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("taggedValue".equals(evt.getPropertyName())) {
            fireTableChanged(new TableModelEvent(this));
        }
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

    /**
     * The UID.
     */
    private static final long serialVersionUID = -5711005901444956345L;
} /* end class TableModelTaggedValues */
