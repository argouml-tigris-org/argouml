// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.apache.log4j.Category;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.kernel.History;
import org.argouml.kernel.HistoryEvent;
import org.argouml.kernel.HistoryItem;
import org.argouml.kernel.HistoryItemManipulation;
import org.argouml.kernel.HistoryItemResolve;
import org.argouml.kernel.HistoryListener;

public class TabHistory extends TabSpawnable
    implements ListSelectionListener, ListCellRenderer, MouseMotionListener 
{
	// TODO Replace deprecated History* with TargetManager
	
    protected static Category cat = 
        Category.getInstance(TabHistory.class);

    ////////////////////////////////////////////////////////////////
    // class variables
    protected ImageIcon _CritiqueIcon = lookupIconResource("PostIt0");
    protected ImageIcon _ResolveIcon = lookupIconResource("PostIt100");
    protected ImageIcon _ManipIcon = lookupIconResource("PostIt100");
    protected ImageIcon _HistoryItemIcon = lookupIconResource("Rectangle");

    protected static String FILTERS[] = {
	"All History Items",
	"History of Selection" 
    };

    /** Get the ImageIcon.
     *
     * @param tag of the icon.
     * @returns the ImageIcon of the tag.
     */
    private static ImageIcon lookupIconResource(String tag) {
	return ResourceLoaderWrapper.getResourceLoaderWrapper()
	    .lookupIconResource(tag);
    }

    ////////////////////////////////////////////////////////////////
    // instance variables
    private Object _target;
    private Vector _data;
    private JList _list = new JList();
    private JLabel _label = new JLabel();
    private JPanel _affected = new JPanel();
    private JComboBox _filter = new JComboBox(FILTERS);


    private JTextArea _description = new JTextArea();
    private JSplitPane _splitter;

    ////////////////////////////////////////////////////////////////
    // constructor
    public TabHistory() {
	super("History");
	setLayout(new BorderLayout());
	//setFont(new Font("Dialog", Font.PLAIN, 10));
	//_label.setFont(new Font("Dialog", Font.PLAIN, 10));
	_label.setOpaque(true);

	_list.addListSelectionListener(this);
	_list.setCellRenderer(this);
	_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	_list.setEnabled(true);
	_list.addMouseMotionListener(this);
	_list.setModel(new HistoryListModel());

	_affected.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
	_affected.add(new JLabel("Affected: "));
	_affected.setBorder(new EtchedBorder());

	JPanel descPane = new JPanel();
	descPane.setLayout(new BorderLayout());
	descPane.add(new JScrollPane(_description), BorderLayout.CENTER);
	_description.setLineWrap(true);
	_description.setWrapStyleWord(true);
	descPane.add(_affected, BorderLayout.SOUTH);

	// TODO: related design elements

	JPanel listPane = new JPanel();
	listPane.setLayout(new BorderLayout());
	listPane.add(new JScrollPane(_list), BorderLayout.CENTER);
	Box filterPane = Box.createHorizontalBox();
	filterPane.add(new JLabel(" Show: "));
	filterPane.add(_filter);
	listPane.add(filterPane, BorderLayout.NORTH);
	listPane.setMinimumSize(new Dimension(100, 100));
	listPane.setPreferredSize(new Dimension(300, 100));

	setLayout(new BorderLayout());
	_splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				   listPane, descPane);
	_splitter.setDividerSize(2);
	_splitter.setDividerLocation(300);
	add(_splitter, BorderLayout.CENTER);

    }

    //TODO: should be more than just a scrolling list
    // need to examine individual items, filter, sort,...



    /** 
     * Called whenever the value of the selection changes.
     * @param e the event that characterizes the change.
     */
    public void valueChanged(ListSelectionEvent e) {
	// TODO: called on each critique!
	cat.debug("user selected " + _list.getSelectedValue());
	Object sel = _list.getSelectedValue();
	if (sel instanceof HistoryItem) {
	    _description.setText(((HistoryItem) sel).toString());
	}
	else
	    _description.setText("what?");
	_description.setCaretPosition(0);
    }


    // This is the only method defined by ListCellRenderer.  We just
    // reconfigure the Jlabel each time we're called.
    /**
     *
     * @param value to display			  
     * @param index cell index			  
     * @param isSelected is the cell selected		  
     * @param cellHasFocus the list and the cell have the focus
     */
    public Component getListCellRendererComponent(JList list, 
						  Object value, 
						  int index, 
						  boolean isSelected, 
						  boolean cellHasFocus)
    {
	if (!(value instanceof HistoryItem)) {
	    _label.setText("non HistoryItem");
	    return _label;
	}
	HistoryItem hi = (HistoryItem) value;
	String s = hi.getHeadline();
	_label.setText(s);
	_label.setBackground(isSelected ?
			     MetalLookAndFeel.getTextHighlightColor() :
			     MetalLookAndFeel.getWindowBackground());
	_label.setForeground(isSelected ?
			     MetalLookAndFeel.getHighlightedTextColor() :
			     MetalLookAndFeel.getUserTextColor());
	if (hi instanceof HistoryItemManipulation) {
	    _label.setIcon(_ManipIcon);
	}
	else if (hi
		 instanceof org.argouml.cognitive.critics.HistoryItemCritique) {
	    _label.setIcon(_CritiqueIcon);
	}
	else if (hi instanceof HistoryItemResolve) {
	    _label.setIcon(_ResolveIcon);
	}
	else {
	    _label.setIcon(_HistoryItemIcon);
	}
	return _label;
    }

    ////////////////////////////////////////////////////////////////
    // MouseMotionListener implementation

    public void mouseMoved(MouseEvent me) {
	int index = _list.locationToIndex(me.getPoint());
	if (index == -1) return;
	String tip = _list.getModel().getElementAt(index).toString();
	cat.debug("tip=" + tip);
	_list.setToolTipText(tip + " ");
    }

    public void mouseDragged(MouseEvent me) { }

} /* end class TabHistory */



class HistoryListModel implements ListModel, HistoryListener {
    ////////////////////////////////////////////////////////////////
    // instance variables
    protected EventListenerList listenerList = new EventListenerList();


    ////////////////////////////////////////////////////////////////
    // constructor
    public HistoryListModel() {
	History.TheHistory.addHistoryListener(this);
    }


    ////////////////////////////////////////////////////////////////
    // HistoryListener implementation

    public void historyAdded(HistoryEvent he) {
	fireIntervalAdded(this, he.getIndex(), he.getIndex());
    }

    ////////////////////////////////////////////////////////////////
    // ListModel implementation

    /** 
     * Returns the length of the list.
     */
    public int getSize() {
	History h = History.TheHistory;
	return h.getItems().size();
    }

    /**
     * Returns the value at the specified index.  
     */
    public Object getElementAt(int index) {
	History h = History.TheHistory;
	return h.getItems().elementAt(index);
    }


    /**
     * Add a listener to the list that's notified each time a change
     * to the data model occurs.
     * @param l the ListDataListener
     */
    public void addListDataListener(ListDataListener l) {
	listenerList.add(ListDataListener.class, l);
    }


    /**
     * Remove a listener from the list that's notified each time a 
     * change to the data model occurs.
     * @param l the ListDataListener
     */
    public void removeListDataListener(ListDataListener l) {
	listenerList.remove(ListDataListener.class, l);
    }


    /*
     * AbstractListModel subclasses must call this method <b>after</b>
     * one or more elements are added to the model.  The new elements
     * are specified by a closed interval index0, index1, i.e. the
     * range that includes both index0 and index1.  Note that
     * index0 need not be less than or equal to index1.
     * 
     * @param source The ListModel that changed, typically "this".
     * @param index0 One end of the new interval.
     * @param index1 The other end of the new interval.
     * @see EventListenerList
     * @see DefaultListModel
     */
    protected void fireIntervalAdded(Object source, int index0, int index1)
    {
	Object[] listeners = listenerList.getListenerList();
	ListDataEvent e = null;

	for (int i = listeners.length - 2; i >= 0; i -= 2) {
	    if (listeners[i] == ListDataListener.class) {
		if (e == null) {
		    e = new ListDataEvent(this,
					  ListDataEvent.INTERVAL_ADDED,
					  index0,
					  index1);
		}
		((ListDataListener) listeners[i + 1]).intervalAdded(e);
	    }
	}
    }



} /* end class HistoryListModel */
