// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.cognitive.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.MessageFormat;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

import org.argouml.application.api.QuadrantPanel;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.cognitive.ToDoListEvent;
import org.argouml.cognitive.ToDoListListener;
import org.argouml.i18n.Translator;
import org.argouml.ui.Actions;
import org.argouml.ui.DisplayTextTree;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.SplashScreen;
import org.tigris.gef.ui.ToolBar;

/**
 * The lower-left pane of the main Argo/UML window, which shows the list
 * of active critics and todo items.
 *
 * <p>This pane shows
 *  a list or tree of all the "to do" items that the designer should
 *  condsider.
 *
 * <p>This class is similar to the NavPane. it uses the same treemodel class and
 * JTree implementation.
 *
 * <p>Perspectives are now built here.
 *
 * <p>future plans may involve:
 * 1)DecisionModelListener implementation
 * 2)GoalListener implementation
 * ?
 *
 *<pre>
 * possible future additions:
 *  ToDoPerspective difficulty = new ToDoByDifficulty();
 *  ToDoPerspective skill = new ToDoBySkill();
 *</pre>

 * $Id$
 */
public class ToDoPane extends JPanel
    implements ItemListener,
        TreeSelectionListener,
        MouseListener,
        ToDoListListener,
        QuadrantPanel {
    private static final Logger LOG = Logger.getLogger(ToDoPane.class);
    
    ////////////////////////////////////////////////////////////////
    // constants
    
    public static final int WARN_THRESHOLD = 50;
    public static final int ALARM_THRESHOLD = 100;
    public static final Color WARN_COLOR = Color.yellow;
    public static final Color ALARM_COLOR = Color.pink;
    
    public static int _clicksInToDoPane;
    public static int _dblClicksInToDoPane;
    public static int _toDoPerspectivesChanged;
    
    ////////////////////////////////////////////////////////////////
    // instance variables
    
    protected JTree _tree;
    protected ToolBar _toolbar;
    protected JComboBox _combo;
    
    /** vector of TreeModels */
    protected Vector _perspectives;
    protected ToDoPerspective _curPerspective;
    
    protected ToDoList _root;
    protected Action _flatView;
    protected JToggleButton _flatButton;
    protected JLabel _countLabel;
    protected boolean _flat;
    protected Object _lastSel;
    protected int _oldSize;
    protected char _dir;
    /** shouldn't need this */
    protected ProjectBrowser _pb;
    
    ////////////////////////////////////////////////////////////////
    // constructors
    
    /**
     *
     *<pre>
     * TODO: - Bob Tarling 8 Feb 2003
     * Replace GEF ToolBar class with our own Toolbar class
     * (only rely on GEF for diagram functionality)
     *</pre>
     *
     */
    public ToDoPane(boolean doSplash) {
        
        setLayout(new BorderLayout());
        
        _combo = new JComboBox();
        _tree = new DisplayTextTree();
        _toolbar = new ToolBar();
        _toolbar.putClientProperty("JToolBar.isRollover",  Boolean.TRUE);
        
        _perspectives = new Vector();
        
        _flatView = Actions.FlatToDo;
        _countLabel = new JLabel(formatCountLabel(999));
        
        _toolbar.add(_combo);
        // This is the only reason GEF toolbar is used here.
        // Must find a way to implement the same.
        _flatButton = _toolbar.addToggle(_flatView,
                                   "Flat",
                                   "Hierarchical", 
                                   "Flat");
        _toolbar.add(_countLabel);
        
        JPanel toolbarPanel = new JPanel(new BorderLayout());
        toolbarPanel.add(_toolbar, BorderLayout.WEST);
        
        ImageIcon hierarchicalIcon =
	    ResourceLoaderWrapper
	        .lookupIconResource("Hierarchical", "Hierarchical");
        ImageIcon flatIcon = ResourceLoaderWrapper
	    .lookupIconResource("Flat", "Flat");
        _flatButton.setIcon(hierarchicalIcon);
        _flatButton.setSelectedIcon(flatIcon);
        add(toolbarPanel, BorderLayout.NORTH);
        add(new JScrollPane(_tree), BorderLayout.CENTER);
        
        _combo.addItemListener(this);
        
        _tree.addTreeSelectionListener(this);
        _tree.setCellRenderer(new ToDoTreeRenderer());
        _tree.addMouseListener(this);
        
        // next line coming from projectbrowser
        setRoot(Designer.TheDesigner.getToDoList());
        Designer.TheDesigner.getToDoList().addToDoListListener(this);
        
        if (doSplash) {
            SplashScreen splash = SplashScreen.getInstance();
	    splash.getStatusBar().showStatus(
	            Translator.localize("statusmsg.bar.making-todopane"));
            splash.getStatusBar().showProgress(25);
        }
        
        setPerspectives(buildPerspectives());
        
        _oldSize = 0;
        _dir = ' ';
        
        setMinimumSize(new Dimension(120, 100));
        
        Dimension preferredSize = getPreferredSize();
        preferredSize.height = 120;
        setPreferredSize(preferredSize);
    }
    
    ////////////////////////////////////////////////////////////////
    // accessors
    
    public void setRoot(ToDoList r) {
        _root = r;
        updateTree();
    }
    
    public ToDoList getRoot() { return _root; }
    
    public Vector getPerspectives() { return _perspectives; }
    
    public void setPerspectives(Vector pers) {
        _perspectives = pers;
        if (pers.isEmpty()) {
	    _curPerspective = null;
	} else {
	    _curPerspective = (ToDoPerspective) pers.elementAt(0);
	}
        
        java.util.Enumeration persEnum = _perspectives.elements();
        while (persEnum.hasMoreElements()) {
            _combo.addItem(persEnum.nextElement());
	}
        
        if (pers.isEmpty()) {
	    _curPerspective = null;
	} else if (pers.contains(_curPerspective)) {
            setCurPerspective(_curPerspective);
	} else {
            setCurPerspective((ToDoPerspective) _perspectives.elementAt(0));
	}
        updateTree();
    }
    
    public ToDoPerspective getCurPerspective() { return _curPerspective; }
    public void setCurPerspective(TreeModel per) {
        if (_perspectives == null || !_perspectives.contains(per)) {
	    return;
	}
        _combo.setSelectedItem(per);
        _toDoPerspectivesChanged++;
    }
    
    public Object getSelectedObject() {
        return _tree.getLastSelectedPathComponent();
    }
    
    public void selectItem(ToDoItem item) {
        Object path[] = new Object[3];
        Object category = null;
        int size = _curPerspective.getChildCount(_root);
        for (int i = 0; i < size; i++) {
            category = _curPerspective.getChild(_root, i);
            if (_curPerspective.getIndexOfChild(category, item) != -1) {
                break;
	    }
        }
        if (category == null) {
	    return;
	}
        path[0] = _root;
        path[1] = category;
        path[2] = item;
        TreePath trPath = new TreePath(path);
        _tree.expandPath(trPath);
        _tree.scrollPathToVisible(trPath);
        _tree.setSelectionPath(trPath);
    }
    
    /**
     * Return whether the todo pane is currently in flat hierachy mode.
     *
     * @return true if flat.
     */
    public boolean isFlat() { return _flat; }

    /**
     * Set the todo pane in a specific hierachy mode.
     *
     * @param b is true for flat mode.
     */
    public void setFlat(boolean b) {
        _flat = b;
        _flatButton.getModel().setPressed(_flat);
        if (_flat) {
	    _tree.setShowsRootHandles(false);
	} else {
	    _tree.setShowsRootHandles(true);
	}
        updateTree();
    }
    /** toggle the hierachy mode. */
    public void toggleFlat() {
        setFlat(!isFlat());
    }
    
    // ------------ ItemListener implementation ----------------------
    
    /**
     * Called when the user selects a perspective from the perspective
     * combo.
     *
     * @param e is the event.
     */
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == _combo) {
	    updateTree();
	}
    }
    
    // -------------TreeSelectionListener implementation -----------
    
    /**
     * Called when the user selects an item in the tree, by clicking or
     * otherwise.
     *
     * @param e is the event.
     */
    public void valueChanged(TreeSelectionEvent e) {
        LOG.debug("ToDoPane valueChanged");
        //TODO: should fire its own event and ProjectBrowser
        //should register a listener
        Object sel = getSelectedObject();
        ProjectBrowser.getInstance().setToDoItem(sel);
        LOG.debug("lastselection: " + _lastSel);
	LOG.debug("sel: " + sel);
        if (_lastSel instanceof ToDoItem) {
	    ((ToDoItem) _lastSel).deselect();
	}
        if (sel instanceof ToDoItem) {
	    ((ToDoItem) sel).select();
	}
        _lastSel = sel;
    }
    
    // ------------- MouseListener implementation ---------------
    
    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     *
     * Empty implementation.
     */
    public void mousePressed(MouseEvent e) { }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     *
     * Empty implementation.
     */
    public void mouseReleased(MouseEvent e) { }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     *
     * Empty implementation.
     */
    public void mouseEntered(MouseEvent e) { }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     *
     * Empty implementation.
     */
    public void mouseExited(MouseEvent e) { }
    
    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        int row = _tree.getRowForLocation(e.getX(), e.getY());
        TreePath path = _tree.getPathForLocation(e.getX(), e.getY());
        if (row != -1) {
            if (e.getClickCount() >= 2) {
                myDoubleClick(row, path);               
            } else {
                mySingleClick(row, path);
            }       
        }
        e.consume();
    }
    
    ////////////////////////////////////////////////////////////////
    // ToDoListListener implementation
    
    /**
     * @see org.argouml.cognitive.ToDoListListener#toDoItemsChanged(org.argouml.cognitive.ToDoListEvent)
     */
    public void toDoItemsChanged(ToDoListEvent tde) {
        if (_curPerspective instanceof ToDoListListener) {
            ((ToDoListListener) _curPerspective).toDoItemsChanged(tde);
	}
    }
    
    /**
     * @see org.argouml.cognitive.ToDoListListener#toDoItemsAdded(org.argouml.cognitive.ToDoListEvent)
     */
    public void toDoItemsAdded(ToDoListEvent tde) {
        if (_curPerspective instanceof ToDoListListener) {
            ((ToDoListListener) _curPerspective).toDoItemsAdded(tde);
	}
        updateCountLabel();
    }
    
    /**
     * @see org.argouml.cognitive.ToDoListListener#toDoItemsRemoved(org.argouml.cognitive.ToDoListEvent)
     */
    public void toDoItemsRemoved(ToDoListEvent tde) {
        if (_curPerspective instanceof ToDoListListener) {
            ((ToDoListListener) _curPerspective).toDoItemsRemoved(tde);
	}
        updateCountLabel();
    }
    
    /**
     * @see org.argouml.cognitive.ToDoListListener#toDoListChanged(org.argouml.cognitive.ToDoListEvent)
     */
    public void toDoListChanged(ToDoListEvent tde) {
        if (_curPerspective instanceof ToDoListListener) {
            ((ToDoListListener) _curPerspective).toDoListChanged(tde);
	}
        updateCountLabel();
    }
    
    ////////////////////////////////////////////////////////////////
    // other methods
    
    private static String formatCountLabel(int size) {
        switch (size) {
	case 0:
	    return Translator.localize("label.todopane.no-items");
	case 1:
	    return MessageFormat.
		format(Translator.localize("label.todopane.item"),
		       new Object[] {
			   new Integer(size) 
		       });
	default:
	    return MessageFormat.
		format(Translator.localize("label.todopane.items"),
		       new Object[] {
			   new Integer(size) 
		       });
        }
    }
    
    public void updateCountLabel() {
        int size = Designer.TheDesigner.getToDoList().size();
        if (size > _oldSize) {
	    _dir = '+';
	}
        if (size < _oldSize) {
	    _dir = '-';
	}
        _oldSize = size;
        _countLabel.setText(formatCountLabel(size));
        _countLabel.setOpaque(size > WARN_THRESHOLD);
        _countLabel.setBackground((size >= ALARM_THRESHOLD) ? ALARM_COLOR
				  : WARN_COLOR);
    }
    
    protected void updateTree() {
        ToDoPerspective tm = (ToDoPerspective) _combo.getSelectedItem();
        _curPerspective = tm;
        if (_curPerspective == null) {
	    _tree.setVisible(false);
	} else {
            LOG.debug("ToDoPane setting tree model");
            _curPerspective.setRoot(_root);
            _curPerspective.setFlat(_flat);
            if (_flat) {
		_tree.setShowsRootHandles(false);
	    } else {
		_tree.setShowsRootHandles(true);
	    }
            _tree.setModel(_curPerspective);
            _tree.setVisible(true); // blinks?
        }
    }
    
    /**
     * @see org.argouml.application.api.QuadrantPanel#getQuadrant()
     */
    public int getQuadrant() { return Q_BOTTOM_LEFT; }
    
    
    /** called when the user clicks once on an item in the tree.
     *
     * TODO: what should the difference be between a single
     * and double click?
     */
    public static void mySingleClick(int row, TreePath path) {
        _clicksInToDoPane++;
        /*
	  if (getSelectedObject() == null){return;}
	  Object sel = getSelectedObject();
	  if (sel instanceof ToDoItem){
	  selectItem((ToDoItem)sel);}
	  cat.debug("1: " + getSelectedObject().toString());
        */
    }
    
    /**
     * Called when the user clicks once on an item in the tree.
     * myDoubleClick will invoke the action() on the ToDoItem.
     */
    public void myDoubleClick(int row, TreePath path) {
        _dblClicksInToDoPane++;
        if (getSelectedObject() == null) {
	    return;
	}
        Object sel = getSelectedObject();
        if (sel instanceof ToDoItem) {
            ((ToDoItem) sel).action();
        }
        
        //TODO: should fire its own event and ProjectBrowser
        //should register a listener
        LOG.debug("2: " + getSelectedObject().toString());
    }
    
    /**
     * The perspectives to be chosen in the combobox are built here.
     */
    private static Vector buildPerspectives() {
        
        ToDoPerspective priority = new ToDoByPriority();
        ToDoPerspective decision = new ToDoByDecision();
        ToDoPerspective goal = new ToDoByGoal();
        ToDoPerspective offender = new ToDoByOffender();
        ToDoPerspective poster = new ToDoByPoster();
        ToDoPerspective type = new ToDoByType();
        
        // add the perspetives to a vector for the combobox
        Vector perspectives = new Vector();
        
        perspectives.add(priority);
        perspectives.add(decision);
        perspectives.add(goal);
        perspectives.add(offender);
        perspectives.add(poster);
        perspectives.add(type);
        
        //
        ToDoPerspective.registerRule(new GoListToDecisionsToItems());
        ToDoPerspective.registerRule(new GoListToGoalsToItems());
        ToDoPerspective.registerRule(new GoListToPriorityToItem());
        ToDoPerspective.registerRule(new GoListToTypeToItem());
        ToDoPerspective.registerRule(new GoListToOffenderToItem());
        ToDoPerspective.registerRule(new GoListToPosterToItem());
        
        return perspectives;
    }
    
} /* end class ToDoPane */
