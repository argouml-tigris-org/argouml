// $Id: ToDoPane.java 12950 2007-07-01 08:10:04Z tfmorris $
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

package org.argouml.cognitive.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.cognitive.ToDoListEvent;
import org.argouml.cognitive.ToDoListListener;
import org.argouml.cognitive.Translator;
import org.argouml.ui.DisplayTextTree;
import org.argouml.ui.PerspectiveSupport;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.SplashScreen;

/**
 * The lower-left pane of the main ArgoUML window, which shows the list
 * of active critics and todo items. <p>
 *
 * This pane shows a list or tree of all the "to do" items that
 * the designer should consider. <p>
 *
 * This class is similar to the NavigatorPane.
 * It uses the same treemodel class and JTree implementation. <p>
 *
 * Perspectives are now built here. <p>
 *
 * Future plans may involve:<ol>
 * <li> DecisionModelListener implementation
 * <li> GoalListener implementation
 * </ol>
 *
 *<pre>
 * possible future additions:
 *  ToDoPerspective difficulty = new ToDoByDifficulty();
 *  ToDoPerspective skill = new ToDoBySkill();
 *</pre>
 */
public class ToDoPane extends JPanel
    implements ItemListener,
        TreeSelectionListener,
        MouseListener,
        ToDoListListener {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ToDoPane.class);

    ////////////////////////////////////////////////////////////////
    // constants

    private static final int WARN_THRESHOLD = 50;
    private static final int ALARM_THRESHOLD = 100;
    private static final Color WARN_COLOR = Color.yellow;
    private static final Color ALARM_COLOR = Color.pink;

    private static int clicksInToDoPane;
    private static int dblClicksInToDoPane;
    private static int toDoPerspectivesChanged;

    ////////////////////////////////////////////////////////////////
    // instance variables

    private JTree tree;
    private JComboBox combo;

    /**
     * Vector of TreeModels.
     */
    private List<ToDoPerspective> perspectives;
    private ToDoPerspective curPerspective;

    private ToDoList root;
    private JLabel countLabel;
    private Object lastSel;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     *
     * @param splash if true, then we have to show progress in the splash
     */
    public ToDoPane(SplashScreen splash) {

        setLayout(new BorderLayout());

        combo = new JComboBox();
        tree = new DisplayTextTree();

        perspectives = new ArrayList<ToDoPerspective>();

        countLabel = new JLabel(formatCountLabel(999));
        countLabel.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));

        JPanel toolbarPanel = new JPanel(new BorderLayout());
        toolbarPanel.add(countLabel, BorderLayout.EAST);
        toolbarPanel.add(combo, BorderLayout.CENTER);
        add(toolbarPanel, BorderLayout.NORTH);

        add(new JScrollPane(tree), BorderLayout.CENTER);

        combo.addItemListener(this);

        tree.addTreeSelectionListener(this);
        tree.setCellRenderer(new ToDoTreeRenderer());
        tree.addMouseListener(this);

        // next line coming from projectbrowser
        setRoot(Designer.theDesigner().getToDoList());
        Designer.theDesigner().getToDoList().addToDoListListener(this);

        if (splash != null) {
            splash.getStatusBar().showStatus(
	            Translator.localize("statusmsg.bar.making-todopane"));
            splash.getStatusBar().showProgress(25);
        }

        setPerspectives(buildPerspectives());

        setMinimumSize(new Dimension(120, 100));

        Dimension preferredSize = getPreferredSize();
        preferredSize.height = 120;
        setPreferredSize(preferredSize);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @param r the root
     */
    public void setRoot(ToDoList r) {
        root = r;
        updateTree();
    }

    /**
     * @return the root
     */
    public ToDoList getRoot() { return root; }

    /**
     * @return the perspectives treemodels
     * @deprecated for 0.25.4 by tfmorris.  Use {@link #getPerspectiveList()}.
     */
    @Deprecated
    public Vector<ToDoPerspective> getPerspectives() {
        return new Vector<ToDoPerspective>(perspectives);
    }

    /**
     * @return the perspectives treemodels
     */
    public List<ToDoPerspective> getPerspectiveList() {
        return perspectives;
    }
    
    /**
     * @param pers the perspectives
     * @deprecated for 0.25.4 by tfmorris. Use {@link #setPerspectives(List)}.
     */
    @Deprecated
    public void setPerspectives(Vector<ToDoPerspective> pers) {
        setPerspectives((List<ToDoPerspective>) pers);
    }

    /**
     * @param pers the perspectives
     */
    public void setPerspectives(List<ToDoPerspective> pers) {
        perspectives = pers;
        if (pers.isEmpty()) {
            curPerspective = null;
        } else {
            curPerspective = pers.get(0);
        }

        for (ToDoPerspective tdp : perspectives) {
            combo.addItem(tdp);
        }

        if (pers.isEmpty()) {
            curPerspective = null;
        } else if (pers.contains(curPerspective)) {
            setCurPerspective(curPerspective);
        } else {
            setCurPerspective(perspectives.get(0));
        }
        updateTree();
    }
    
    /**
     * @return the current perspectives
     */
    public ToDoPerspective getCurPerspective() { return curPerspective; }

    /**
     * @param per the current perspective
     */
    public void setCurPerspective(TreeModel per) {
        if (perspectives == null || !perspectives.contains(per)) {
	    return;
	}
        combo.setSelectedItem(per);
        toDoPerspectivesChanged++;
    }

    /**
     * @return the last <code>Object</code> in the first selected node's
     *      <code>TreePath</code>,
     *      or <code>null</code> if nothing is selected
     */
    public Object getSelectedObject() {
        return tree.getLastSelectedPathComponent();
    }

    /**
     * @param item the item to be selected
     */
    public void selectItem(ToDoItem item) {
        Object[] path = new Object[3];
        Object category = null;
        int size = curPerspective.getChildCount(root);
        for (int i = 0; i < size; i++) {
            category = curPerspective.getChild(root, i);
            if (curPerspective.getIndexOfChild(category, item) != -1) {
                break;
	    }
        }
        if (category == null) {
	    return;
	}
        path[0] = root;
        path[1] = category;
        path[2] = item;
        TreePath trPath = new TreePath(path);
        tree.expandPath(trPath);
        tree.scrollPathToVisible(trPath);
        tree.setSelectionPath(trPath);
    }

    // ------------ ItemListener implementation ----------------------

    /**
     * Called when the user selects a perspective from the perspective
     * combo. <p>
     * {@inheritDoc}
     */
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == combo) {
	    updateTree();
	}
    }

    // -------------TreeSelectionListener implementation -----------

    /*
     * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
     */
    public void valueChanged(TreeSelectionEvent e) {
        LOG.debug("ToDoPane valueChanged");
        //TODO: should fire its own event and ProjectBrowser
        //should register a listener
        Object sel = getSelectedObject();
        ProjectBrowser.getInstance().setToDoItem(sel);
        LOG.debug("lastselection: " + lastSel);
	LOG.debug("sel: " + sel);
        if (lastSel instanceof ToDoItem) {
	    ((ToDoItem) lastSel).deselect();
	}
        if (sel instanceof ToDoItem) {
	    ((ToDoItem) sel).select();
	}
        lastSel = sel;
    }

    // ------------- MouseListener implementation ---------------

    /*
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) { 
        // Empty implementation.
    }

    /*
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {  
        // Empty implementation.
    }

    /*
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {  
        // Empty implementation.
    }

    /*
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) { 
        // Empty implementation.
    }

    /*
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        int row = tree.getRowForLocation(e.getX(), e.getY());
        TreePath path = tree.getPathForLocation(e.getX(), e.getY());
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

    /*
     * @see org.argouml.cognitive.ToDoListListener#toDoItemsChanged(org.argouml.cognitive.ToDoListEvent)
     */
    public void toDoItemsChanged(ToDoListEvent tde) {
        if (curPerspective instanceof ToDoListListener) {
            ((ToDoListListener) curPerspective).toDoItemsChanged(tde);
	}
    }

    /*
     * @see org.argouml.cognitive.ToDoListListener#toDoItemsAdded(org.argouml.cognitive.ToDoListEvent)
     */
    public void toDoItemsAdded(ToDoListEvent tde) {
        if (curPerspective instanceof ToDoListListener) {
            ((ToDoListListener) curPerspective).toDoItemsAdded(tde);
	}
        List<ToDoItem> items = tde.getToDoItemList();
        for (ToDoItem todo : items) {
            if (todo.getPriority() >= ToDoItem.INTERRUPTIVE_PRIORITY) {
                // keep nagging until the user solves the problem:
                // This seems a nice way to nag:
                selectItem(todo);
                break; // Only interrupt for one todoitem
            }
        }
        updateCountLabel();
    }
    
    /*
     * @see org.argouml.cognitive.ToDoListListener#toDoItemsRemoved(org.argouml.cognitive.ToDoListEvent)
     */
    public void toDoItemsRemoved(ToDoListEvent tde) {
        if (curPerspective instanceof ToDoListListener) {
            ((ToDoListListener) curPerspective).toDoItemsRemoved(tde);
	}
        updateCountLabel();
    }

    /*
     * @see org.argouml.cognitive.ToDoListListener#toDoListChanged(org.argouml.cognitive.ToDoListEvent)
     */
    public void toDoListChanged(ToDoListEvent tde) {
        if (curPerspective instanceof ToDoListListener) {
            ((ToDoListListener) curPerspective).toDoListChanged(tde);
	}
        updateCountLabel();
    }

    ////////////////////////////////////////////////////////////////
    // other methods

    /* TODO: Indicate the direction! */
    private static String formatCountLabel(int size) {
        switch (size) {
	case 0:
	    return Translator.localize("label.todopane.no-items");
	case 1:
	    return MessageFormat.
		format(Translator.localize("label.todopane.item"),
		       new Object[] {
			   new Integer(size),
		       });
	default:
	    return MessageFormat.
		format(Translator.localize("label.todopane.items"),
		       new Object[] {
			   new Integer(size),
		       });
        }
    }

    /**
     * Update the count label.
     */
    public void updateCountLabel() {
        int size = Designer.theDesigner().getToDoList().size();
        countLabel.setText(formatCountLabel(size));
        countLabel.setOpaque(size > WARN_THRESHOLD);
        countLabel.setBackground((size >= ALARM_THRESHOLD) ? ALARM_COLOR
				  : WARN_COLOR);
    }

    /**
     * Update the todo tree.
     */
    protected void updateTree() {
        ToDoPerspective tm = (ToDoPerspective) combo.getSelectedItem();
        curPerspective = tm;
        if (curPerspective == null) {
	    tree.setVisible(false);
	} else {
            LOG.debug("ToDoPane setting tree model");
            curPerspective.setRoot(root);
            tree.setShowsRootHandles(true);
            tree.setModel(curPerspective);
            tree.setVisible(true); // blinks?
        }
    }

    /**
     * Called when the user clicks once on an item in the tree. <p>
     *
     * Q: What should the difference be between a single
     * and double click? <p>
     * A: A single click selects the todo item in the tree,
     * shows the red indication on the diagram,
     * and selects the todo tab in the details panel.
     * A double click additionally
     * selects the offender in the explorer,
     * and selects the offender in the diagram (blue selection),
     * and selects the properties tab in the details panel.
     * In both cases, the focus (for keyboard actions) remains in the todo tree.
     *
     * @param row the selected row in the tree
     * @param path the path in the tree of the selected item
     */
    public static void mySingleClick(
            @SuppressWarnings("unused") int row, 
            @SuppressWarnings("unused") TreePath path) {
        clicksInToDoPane++;
    }

    /**
     * Called when the user clicks twice on an item in the tree.
     * myDoubleClick will invoke the action() on the ToDoItem.
     *
     * @param row the selected row in the tree
     * @param path the path in the tree of the selected item
     */
    public void myDoubleClick(
            @SuppressWarnings("unused") int row, 
            @SuppressWarnings("unused") TreePath path) {
        dblClicksInToDoPane++;
        if (getSelectedObject() == null) {
	    return;
	}
        Object sel = getSelectedObject();
        if (sel instanceof ToDoItem) {
            ((ToDoItem) sel).action();
        }

        //TODO: should fire its own event and ProjectBrowser
        //TODO: should register a listener
        LOG.debug("2: " + getSelectedObject().toString());
    }

    /**
     * The perspectives to be chosen in the combobox are built here.
     */
    private static List<ToDoPerspective> buildPerspectives() {

        ToDoPerspective priority = new ToDoByPriority();
        ToDoPerspective decision = new ToDoByDecision();
        ToDoPerspective goal = new ToDoByGoal();
        ToDoPerspective offender = new ToDoByOffender();
        ToDoPerspective poster = new ToDoByPoster();
        ToDoPerspective type = new ToDoByType();

        // add the perspetives to a vector for the combobox
        List<ToDoPerspective> perspectives = new ArrayList<ToDoPerspective>();

        perspectives.add(priority);
        perspectives.add(decision);
        perspectives.add(goal);
        perspectives.add(offender);
        perspectives.add(poster);
        perspectives.add(type);

        PerspectiveSupport.registerRule(new GoListToDecisionsToItems());
        PerspectiveSupport.registerRule(new GoListToGoalsToItems());
        PerspectiveSupport.registerRule(new GoListToPriorityToItem());
        PerspectiveSupport.registerRule(new GoListToTypeToItem());
        PerspectiveSupport.registerRule(new GoListToOffenderToItem());
        PerspectiveSupport.registerRule(new GoListToPosterToItem());

        return perspectives;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 1911401582875302996L;
}
