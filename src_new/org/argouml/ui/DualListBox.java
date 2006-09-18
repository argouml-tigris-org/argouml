// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.argouml.swingext.SpacerPanel;

/**
 * Dual List Box
 * 
 * This dialog box implements that Add-and-Remove idiom
 * described at 
 * http://java.sun.com/products/jlf/at/book/Idioms6.html#57371
 * also sometimes called the dual list selection box
 * @since 2 Nov 2005
 * @author tfmorris@gmail.com
 */
public class DualListBox extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 2261750507931636027L;

    /**
     * Insets in pixels.
     */
    private static final int INSET_PX = 3;

    ////////////////////////////////////////////////////////////////
    // instance variables

    private JButton addButton;
    private JButton removeButton;
    
    private JList   sourceList;
    private JList   destinationList;

    private SortedListModel sourceListModel; 
    private SortedListModel destinationListModel;
    
    private JLabel sourceLabel;
    private String sourceLabelText = "";
    
    private JLabel destinationLabel;
    private String destinationLabelText = "";
    
    private boolean showItemCountEnabled = false;
    
    /**
     * Creates a new instance of Dual List Box.
     */
    public DualListBox() {
        this(new SortedListModel(), new SortedListModel());
    }
    
    // In the future we could allow caller provided ListModels, but not for now
    //     private DualListBox(ListModel source, ListModel destination) {
    private DualListBox(SortedListModel source, SortedListModel destination) {
        this.sourceListModel = source;
        this.destinationListModel = destination;

        destinationList = new JList(destinationListModel);
        sourceList = new JList(sourceListModel);

        destinationList.setBorder(BorderFactory.createEmptyBorder(
                INSET_PX, INSET_PX, INSET_PX, INSET_PX));
        sourceList.setBorder(BorderFactory.createEmptyBorder(
                INSET_PX, INSET_PX, INSET_PX, INSET_PX));

        destinationList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        sourceList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        
        addButton = new JButton(">");
        removeButton = new JButton("<");

        addButton.setEnabled(false);
        removeButton.setEnabled(false);
        
        makeLayout();

        addButton.addActionListener(new ItemListener());
        removeButton.addActionListener(new ItemListener());
        sourceList.addListSelectionListener(new MyListSelectionListener());
        sourceList.addMouseListener(new ListMouseListener());
        destinationList.addListSelectionListener(new MyListSelectionListener());
        destinationList.addMouseListener(new ListMouseListener());
        
        sourceListModel.addListDataListener(new MyListDataListener()); 
        destinationListModel.addListDataListener(new MyListDataListener());
       
    }
    

    /**
     * Make the layout for the dialog box.
     */
    private void makeLayout() {
        GridBagLayout gb = new GridBagLayout();
        setLayout(gb);
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;

        sourceLabel = new JLabel(); 
        sourceLabel.setBorder(BorderFactory.createEmptyBorder(
                INSET_PX, INSET_PX, INSET_PX, INSET_PX));
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.ipadx = 3;
        c.ipady = 3;
        c.insets = new Insets(10, 0, 0, 0);
        gb.setConstraints(sourceLabel, c);
        add(sourceLabel);

        addButton.setMargin(new Insets(2, 15, 2, 15));
        removeButton.setMargin(new Insets(2, 15, 2, 15));
        JPanel xferButtons = new JPanel();
        xferButtons.setLayout(new BoxLayout(xferButtons, BoxLayout.Y_AXIS));
        xferButtons.add(addButton);
        xferButtons.add(new SpacerPanel());
        xferButtons.add(removeButton);
        c.gridx = 2;
        c.gridy = 4;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.insets = new Insets(0, 3, 0, 5);
        gb.setConstraints(xferButtons, c);
        add(xferButtons);

        destinationLabel = new JLabel(); // the text will be set later
        destinationLabel.setBorder(BorderFactory.createEmptyBorder(
                INSET_PX, INSET_PX, INSET_PX, INSET_PX));
        c.gridx = 3;
        c.gridy = 3;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.insets = new Insets(10, 0, 0, 0);
        gb.setConstraints(destinationLabel, c);
        add(destinationLabel);

        c.gridx = 0;
        c.gridy = 4;
        c.weighty = 1.0;
        c.gridwidth = 2;
        c.gridheight = 2;
        c.insets = new Insets(0, 0, 0, 0);
        JScrollPane sourceScroll =
	    new JScrollPane(sourceList,
			    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gb.setConstraints(sourceScroll, c);
        add(sourceScroll);

        c.gridx = 3;
        c.gridy = 4;
        c.gridwidth = 2;
        c.gridheight = 2;
        JScrollPane destinationScroll =
            new JScrollPane(destinationList,
			    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gb.setConstraints(destinationScroll, c);
        add(destinationScroll);
    }


    /**
     * Update the label above the destination list count.
     */
    private void updateLabels() {
        if (showItemCountEnabled) {
            sourceLabel.setText(sourceLabelText 
                    + " (" + sourceListModel.size() + ")");
            destinationLabel.setText(destinationLabelText 
                    + " (" + destinationListModel.size() + ")");
        }
    }

    /**
     * Handles pressing the ">>" or "<<" buttons.
     */
    class ItemListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            if (src == addButton) {
                addItem();
            } else if (src == removeButton) {
                removeItem();
            }
        }
    }

    /**
     * Handles double-clicking on one of the lists.
     * This triggers the same functions as ">>" or "<<".
     */
    class ListMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent me) {
            Object src = me.getSource();
            if (me.getClickCount() != 2) {
		return;
	    }

            if (src == sourceList && addButton.isEnabled()) {
                addItem();
	    }
            if (src == destinationList && removeButton.isEnabled()) {
                removeItem();
	    }
        }
    }

    /**
     * Notify that currently selected item was added
     */
    private void addItem() {
        Object sel = sourceList.getSelectedValue();
        destinationListModel.addElement(sel);
        destinationList.setSelectedValue(sel, true);
        sourceListModel.removeElement(sel);
        if (sourceList.getSelectedIndex() < 0) {
            sourceList.setSelectedIndex(0);
        }
        updateLabels();
    }

    /**
     * Remove the currently selected item from the items list
     */
    private void removeItem() {
        Object sel = destinationList.getSelectedValue();
        destinationListModel.removeElement(sel);
        sourceListModel.addElement(sel);
        sourceList.setSelectedValue(sel, true);
        
        if (destinationList.getSelectedIndex() < 0) {
            destinationList.setSelectedIndex(0);
        }
        updateLabels();
    }

    /**
     * Handles selection changes in the items list.
     */
    class MyListSelectionListener implements ListSelectionListener {
        /**
         * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
         */
        public void valueChanged(ListSelectionEvent lse) {
            if (lse.getValueIsAdjusting()) {
                return;
            }
            Object selItem = null;
            if (lse.getSource() == destinationList) {
                if (destinationListModel.size() > 0) {
                    selItem = destinationList.getSelectedValue();
                }
                removeButton.setEnabled(selItem != null);
            } else if (lse.getSource() == sourceList) {
                if (sourceListModel.size() > 0) {
                    selItem = sourceList.getSelectedValue();
                }
                addButton.setEnabled(selItem != null);
            }
        }
    }
    
    /**
     * Update label count on list changes
     */
    class MyListDataListener implements ListDataListener {
        public void contentsChanged(ListDataEvent e) {
            updateLabels();
        }
        public void intervalAdded(ListDataEvent e) {
            updateLabels();  
        }
        public void intervalRemoved(ListDataEvent e) {
            updateLabels();
        }
    }
    
    /**
     * @return list model for source list
     */
    public DefaultListModel getSourceListModel() {
        return sourceListModel;
    }
    
    /**
     * @return list model for destination list
     */
    public DefaultListModel getDestinationListModel() {
        return destinationListModel;
    }
        
    /**
     * @return title string for destination/to column
     */
    public String getDestinationItemsTitle() {
        return destinationLabelText;
    }

    /**
     * Set title of destination/to column
     * @param text string to use as title
     */
    public void setDestinationItemsTitle(String text) {
        this.destinationLabelText = text;
        destinationLabel.setText(text);
        updateLabels();
    }

    /**
     * @return title string for source/from column
     */
    public String getSourceItemsTitle() {
        return sourceLabelText;
    }

    /**
     * Set title of source/from column
     * @param text string to use as title
     */
    public void setSourceItemsTitle(String text) {
        this.sourceLabelText = text;
        sourceLabel.setText(text);
        updateLabels();
    }

    
    /**
     * @return enable status of item count display
     */
    public boolean isShowItemCountEnabled() {
        return showItemCountEnabled;
    }

    /**
     * Show count of items in each list in parentheses after
     * the title string.
     * @param enable true to enable display
     */
    public void setShowItemCountEnabled(boolean enable) {
        showItemCountEnabled = enable;
    }

}

/*
 * ListModel which maintains items in sorted order by their
 * string representation.  Implemented using a SortedSet.
 * Not optimized for large lists.
 * <p>
 * We extend DefaultListModel for the convience methods
 * it has, but we don't implement everything.  Not really the
 * best approach... 
 * <p>
 * extended from Definitive Guide to Swing for Java 2, Second Edition
 */
class SortedListModel extends DefaultListModel {

    private SortedSet model;
    private EventListenerList listenerList = new EventListenerList();
    private ListDataEvent listDataEvent;

    public SortedListModel() {
        model = new TreeSet(new Comparator() {
            public int compare(Object o1, Object o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
    }

    public void add(Object element) {
        ItemWrapper item = new ItemWrapper(element);
        if (model.add(item)) {
            int position = indexOf(item);
            fireIntervalAdded(this, position, position);
        }
    }
    
    public void addElement(Object element) {
        add(element);
    }
    
    public void addAll(Object elements[]) {
        for (int i = 0; i < elements.length; i++) {
            add(elements[i]);
        }
    }
    
    public int capacity() {
        return Integer.MAX_VALUE;
    }
    
    public void clear() {
        int size = getSize();
        if (size > 0) {
            model.clear();
            fireIntervalRemoved(this, 0, size);
        }
    }
    
    public boolean contains(Object element) {
        return model.contains(new ItemWrapper(element));
    }
    
    public void copyInto(Object[] anArray) {
        throw new RuntimeException("Not implemented");
    }
    
    public Object elementAt(int index) {
        return getElementAt(index);
    }
    
    public Enumeration elements() {
        throw new RuntimeException("Not implemented");
    }
    public void ensureCapacity(int i) {
        // noop - we are infinite!
    }
    
    public Object firstElement() {
        return model.first();
    }
    
    public Object get(int index) {
        if (index >= model.size()) {
            return null;
        }
        return ((ItemWrapper) model.toArray()[index]).getItem();
    }
    
    public Object getElementAt(int index) {
        return get(index);
    }
    
    public int getSize() {
        return model.size();
    }

    public int indexOf(Object elem) {
        return indexOf(elem, 0);

    }

    public int indexOf(Object elem, int index) {
        return indexOfInternal(new ItemWrapper(elem), index);
    }
    
    private int indexOfInternal(ItemWrapper item, int index) {
//      if (!contains(elem)) {
//          return -1;
//      }
//      List l = new ArrayList(model);
//      int i = Collections.binarySearch(l.subList(index, l.size()),
//              elem);
//      if (i == -1) {
//          return i;
//      } else {
//          return i + index;
//      }
        Object[] array = model.toArray();
        for (int i = index; i < model.size(); i++) {
            if (item.equals((ItemWrapper) array[i])) {
                return i;
            }
        }
        return -1;
    }
    
    public void insertElementAt(Object obj, int index) {
        throw new RuntimeException("Not allowed");
    }
    
    public boolean isEmpty() {
        return model.isEmpty();
    }
    
    public Iterator iterator() {
        return model.iterator();
    }

    public Object lastElement() {
        return model.last();
    }
    
    public int lastIndexOf(Object elem) {
        return lastIndexOf(elem, 0);
    }

    public int lastIndexOf(Object elem, int index) {
        throw new RuntimeException("Not implemented");

    }
    
    public Object remove(int index) {
        Object o = getElementAt(index);
        removeElement(o);
        return o;
    }

    public boolean removeElement(Object element) {
        int position = indexOf(element);
        boolean removed = model.remove(element);
        if (removed) {
            fireIntervalRemoved(this, position, position);
        }
        return removed;
    }
    
    public void  removeElementAt(int index) {
        remove(index);
        return;
    }
    
    public void removeRange(int fromIndex, int toIndex) {
        throw new RuntimeException("Not implemented");
    }
    
    public Object set(int index, Object element) {
        throw new RuntimeException("Not allowed");
    }
    
    public void setElementAt(Object obj, int index) {
        throw new RuntimeException("Not allowed");
    }
    
    public void setSize(int newSize) {
        // noop
    }
    
    // having both size & getSize is redundant, but
    // we follow the DefaultListModel implementation
    public int size() {
        return getSize();
    }
    
    public Object[] toArray() {
        return model.toArray();
    }
    
    public String toString() {
        return model.toString();
    }
    
    public void trimToSize() {
        // noop
    }
    
    /*
     * AbstractListModel methods
     */
    
    public void addListDataListener(ListDataListener l) {
        listenerList.add(ListDataListener.class, l);
    }

    protected void fireContentsChanged(Object source, int index0, int index1) {
        // Notify all listeners that have registered interest for
        // notification on this event type. The event instance
        // is lazily created using the parameters passed into
        // the fire method.

        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        // Process the listeners last to first, notifying
        // those that are interested in this event
        
        // TODO: these loop counters are from the Sun docs, but they seem wrong
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                // Lazily create the event:
                if (listDataEvent == null)
                    listDataEvent = new ListDataEvent(source,
                            ListDataEvent.CONTENTS_CHANGED, index0, index1);
                ((ListDataListener) listeners[i + 1])
                        .contentsChanged(listDataEvent);
            }
        }
    }

    protected void fireIntervalAdded(Object source, int index0, int index1) {
        Object[] listeners = listenerList.getListenerList();

        // Process the listeners last to first, notifying
        // those that are interested in this event
        
        // TODO: these loop counters are from the Sun docs, but they seem wrong
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                // Lazily create the event:
                if (listDataEvent == null)
                    listDataEvent = new ListDataEvent(source,
                            ListDataEvent.INTERVAL_ADDED, index0, index1);
                ((ListDataListener) listeners[i + 1])
                        .intervalAdded(listDataEvent);
            }
        }
    }

    protected void fireIntervalRemoved(Object source, int index0, int index1) {
        Object[] listeners = listenerList.getListenerList();

        // Process the listeners last to first, notifying
        // those that are interested in this event
        
        // TODO: these loop counters are from the Sun docs, but they seem wrong
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ListDataListener.class) {
                // Lazily create the event:
                if (listDataEvent == null)
                    listDataEvent = new ListDataEvent(source,
                            ListDataEvent.INTERVAL_REMOVED, index0, index1);
                ((ListDataListener) listeners[i + 1])
                        .intervalRemoved(listDataEvent);
            }
        }
    }

    public ListDataListener[] getListDataListeners() {
        return (ListDataListener[]) listenerList.getListenerList();
    }

    public EventListener[] getListeners(Class listenerType) {
        return listenerList.getListeners(listenerType);
    }

    public void removeListDataListener(ListDataListener l) {
        listenerList.remove(ListDataListener.class, l);
    }
    
    /**
     * Wrapper class to allow arbitrary objects to be compared
     * and sorted based on their string representation.
     */
    class ItemWrapper {
        private Object myItem;
        
        ItemWrapper(Object item) {
            myItem = item;
        }
        
        public int compareTo(ItemWrapper o) {
            return toString().compareTo(o.toString());
        }
        
        public boolean equals(ItemWrapper o) {
            return toString().equals(o.toString());
        }

        public int hashCode() {
            return toString().hashCode();
        }
        
        public Object getItem() {
            return myItem;
        }
        
        public String toString() {
            return myItem.toString();
        }
    }

}
