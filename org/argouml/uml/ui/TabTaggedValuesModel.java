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

    private Logger LOG = Logger.getLogger(TabTaggedValuesModel.class);
    
    // //////////////
    // instance varables
    private Object target;

    // ModelElement
    private TabTaggedValues tab = null;

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
        if (LOG.isDebugEnabled())
            LOG.debug("Set target to "+t);
        if (!Model.getFacade().isAModelElement(t)) {
            throw new IllegalArgumentException();
        }
        if (target != t) {
            if (target != null)
                Model.getPump().removeModelEventListener(this, target);            
            target = t;
            if (t != null)
                Model.getPump().addModelEventListener(this, t);
        }
        //always fire changes in the case something has changed in the composition
        //of the taggedValues collection.
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
        if (Model.getMetaTypes().getTagDefinition()==null)
            return String.class;
        if (c == 0)
            return (Class) Model.getMetaTypes().getTagDefinition();
        if (c == 1)
            return String.class;
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
        if (target == null || Model.getUmlFactory().isRemoved(target)) {
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
        Vector tvs = new Vector(Model.getFacade().getTaggedValuesCollection(
                target));
        // if (tvs == null) return "";
        if (row == tvs.size()) {
            return ""; // blank line allows addition
        }
        Object tv = tvs.elementAt(row);
        if (col == 0) {
            Object n = (Model.getMetaTypes().getTagDefinition()!=null)?
                    Model.getFacade().getTagDefinition(tv):Model.getFacade().getTagOfTag(tv);
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

        Vector tvs = new Vector(Model.getFacade().getTaggedValuesCollection(
                target));
        if (tvs.size() <= rowIndex) {
            Object tv = Model.getExtensionMechanismsFactory()
                    .createTaggedValue();
            Model.getCoreHelper().setModelElementContainer(tv,target);
            if (columnIndex == 0) {
                Model.getExtensionMechanismsHelper().setTag(tv, aValue);
            }
            if (columnIndex == 1) {
                Model.getExtensionMechanismsHelper().setTag(tv, "");
                Model.getCommonBehaviorHelper().setValue(tv, aValue);
            }
            tvs.addElement(tv);

            mEvent = new TableModelEvent(this, tvs.size(), tvs.size(),
                    TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
        } else if ("".equals(aValue) && columnIndex == 0) {
            tvs.removeElementAt(rowIndex);
            mEvent = new TableModelEvent(this, rowIndex, rowIndex,
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
        // TODO: How to implement this ("mee" was a MElementEvent)?
        // if ("taggedValue".equals(mee.getName()))
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