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
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.DelayedChangeNotify;
import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.InvalidElementException;
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

    /**
     * The ModelElement that is the current target.
     */
    private Object target;

    /**
     * Construct a model to be used by the JTable on the TaggedValues tab.
     *
     * @param t ignored
     * @deprecated before 0.21.3 by tfmorris - use no argument constructor
     * @see #TabTaggedValuesModel()
     */
    public TabTaggedValuesModel(TabTaggedValues t) {
        this();
    }

    /**
     * Construct a model to be used by a JTable containing TaggedValues.
     */
    public TabTaggedValuesModel() {
    }
    
    /**
     * Set the current target to the given model element.
     * 
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
                Model.getPump().addModelEventListener(this, t, 
                        new String[] {"taggedValue", "referenceTag"});
            }
        }
        // always fire changes in the case something has changed in the
        // composition of the taggedValues collection.
        fireTableDataChanged();
    }


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
        try {
            Collection tvs = 
                Model.getFacade().getTaggedValuesCollection(target);
            // if (tvs == null) return 1;
            return tvs.size() + 1;
        } catch (InvalidElementException e) {
            // Target has been deleted
            return 0;
        }
    }

    /**
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col) {
        Collection tvs = Model.getFacade().getTaggedValuesCollection(target);
        if (row > tvs.size() || col > 1) {
            throw new IllegalArgumentException();
        }
        // If the row is past the end of our current collection, 
        // return an empty string so they can add a new value
        if (row == tvs.size()) {
            return "";
        }
        Object tv = tvs.toArray()[row];
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
        if (columnIndex != 0 && columnIndex != 1) {
            return;
        }
        if (columnIndex == 1 && aValue == null) {
            aValue = "";
        }
        
        if ("".equals(aValue) && columnIndex == 0) {
            removeRow(rowIndex);
            return;
        }
        
        Collection tvs = Model.getFacade().getTaggedValuesCollection(target);
        if (tvs.size() <= rowIndex) {
            if (columnIndex == 0) {
                addRow(new Object[] {aValue, null});
            }
            if (columnIndex == 1) {
                addRow(new Object[] {null, aValue});
            }
        } else {
            Object tv = getFromCollection(tvs, rowIndex);
            if (columnIndex == 0) {
                Model.getExtensionMechanismsHelper().setTag(tv, aValue);
            } else if (columnIndex == 1) {
                Model.getCommonBehaviorHelper().setValue(tv, aValue);
            }
            fireTableChanged(
                    new TableModelEvent(this, rowIndex, rowIndex, columnIndex));
        }
    }

    /**
     * Add a tagged value to the model with the given type and value.
     * 
     * @param values values for the columns:
     *  values[0] contains type for new TaggedValue
     *  values[1] contains value for new TaggedValue
     */
    public void addRow(Object[] values) {
        Object tagType = values[0];
        Object tagValue = values[1];
        
        if (tagType == null) {
            tagType = "";
        }
        if (tagValue == null) {
            tagValue = "";
        }
        Object tv = Model.getExtensionMechanismsFactory().createTaggedValue();
        
        // We really shouldn't add it until after it is set up, but we
        // need it to have an owner for the following methods
        Model.getExtensionMechanismsHelper().addTaggedValue(target, tv);

        Model.getExtensionMechanismsHelper().setTag(tv, tagType);
        Model.getCommonBehaviorHelper().setValue(tv, tagValue);

        // Since we aren't sure of ordering, fire event for whole table
        fireTableChanged(new TableModelEvent(this));
    }
    
    /**
     * Remove the TaggedValue at the given row from the ModelElement.
     * 
     * @param row row containing TaggedValue to be removed.
     */
    public void removeRow(int row) {
        Collection c = Model.getFacade().getTaggedValuesCollection(target);
        if ((row >= 0) && (row <= c.size())) {
            Object element = getFromCollection(c, row);
            Model.getUmlFactory().delete(element);
            fireTableChanged(new TableModelEvent(this));
        }
    }
    
    /*
     * Return the ith element from a Collection.
     * 
     * @param collection collection to get element from
     * @param index index of the element to be returned
     * @return the object
     */
    private Object getFromCollection(Collection collection, int index) {
        if (collection instanceof List) {
            return ((List) collection).get(index);
        }
        if (index >= collection.size() || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        Iterator it = collection.iterator();
        for (int i = 0; i < index; i++ ) {
            it.next();
        }
        return it.next();
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ("taggedValue".equals(evt.getPropertyName()) 
                || "referenceTag".equals(evt.getPropertyName())) {
            fireTableChanged(new TableModelEvent(this));            
        }
        if (evt instanceof DeleteInstanceEvent
                && evt.getSource() == target) {
            setTarget(null);
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
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -5711005901444956345L;
} /* end class TableModelTaggedValues */
