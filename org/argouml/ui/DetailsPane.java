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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.api.QuadrantPanel;
import org.argouml.cognitive.ui.TabToDo;
import org.argouml.cognitive.ui.TabToDoTarget;
import org.argouml.model.ModelFacade;
import org.argouml.swingext.Orientable;
import org.argouml.swingext.Orientation;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.TabModelTarget;
import org.argouml.uml.ui.TabProps;
import org.argouml.util.ConfigLoader;

/**
 * The lower-right pane of the main Argo/UML window, which shows
 * the details of a selected model element. 
 * 
 * This panel has several tabs that show details of the selected
 * ToDoItem, or the selected model element in the NavigationPane, or
 * the MultiEditorPane.
 *
 * There are requests to have the cursor automatically
 * be set to the primary field.
 *
 * $Id$
 */
public class DetailsPane
    extends JPanel
    implements ChangeListener, MouseListener,
	       QuadrantPanel,
	       Orientable,
	       TargetListener
{

    protected static Logger cat = Logger.getLogger(DetailsPane.class);
    ////////////////////////////////////////////////////////////////
    // constants

    public static int WIDTH = 690;
    public static int HEIGHT = 520;
    public static int INITIAL_WIDTH = 400;
    public static int INITIAL_HEIGHT = 200;

    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * The currently selected todo item.
     */
    protected Object _item = null;

    /**
     * The top level pane, which is a tabbed pane.
     */
    protected JTabbedPane _tabs = new JTabbedPane();

    /** 
     * The current target
     */
    private Object _target;

    /**
     * a list of all the tabs, which are JPanels, in the JTabbedPane _tabs.
     */
    protected Vector _tabPanels = new Vector();

    /**
     * index of the selected tab in the JTabbedPane.
     */
    protected int _lastNonNullTab = -1;

    /**
     *
     */
    private Orientation orientation;

    /**
     * The list with targetlisteners, this are the property panels
     * managed by TabProps It should only contain one listener at a
     * time.
     */
    private EventListenerList _listenerList = new EventListenerList();

    /**
     * Adds a listener.
     * @param listener the listener to add
     */
    private void addTargetListener(TargetListener listener) {
        _listenerList.add(TargetListener.class, listener);
    }

    /**
     * Removes a target listener.
     * @param listener the listener to remove
     */
    private void removeTargetListener(TargetListener listener) {
        _listenerList.remove(TargetListener.class, listener);
    }
    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Gets all of the _tabPanels from the ConfigLoader, then 
     * adds them to the JTabbedPane.
     *
     * sets the target to null.
     *
     * registers listeners.
     */
    public DetailsPane(String pane, Orientation orientation) {
        Argo.log.info("making DetailsPane(" + pane + ")");

        this.orientation = orientation;
        ConfigLoader.loadTabs(_tabPanels, pane, orientation);
        setLayout(new BorderLayout());
        setFont(new Font("Dialog", Font.PLAIN, 10));
        add(_tabs, BorderLayout.CENTER);

        for (int i = 0; i < _tabPanels.size(); i++) {
            String title = "tab";
            JPanel t = (JPanel) _tabPanels.elementAt(i);
            if (t instanceof TabSpawnable)
                title = ((TabSpawnable) t).getTitle();
            title = Argo.localize("UMLMenu", title);
            if (t instanceof TabToDoTarget) {
                _tabs.addTab(title, _leftArrowIcon, t);
            } else if (t instanceof TabModelTarget) {
                _tabs.addTab(title, _upArrowIcon, t);
            } else if (t instanceof TabFigTarget) {
                _tabs.addTab(title, _upArrowIcon, t);
            } else {
                _tabs.addTab(title, t);
            }
        }

        // set the tab that should be shown on first entrance
        _lastNonNullTab = -1;
        Component[] tabs = _tabs.getComponents();
        for (int i = 0; i < tabs.length; i++) {
            // tabprops should be shown if loaded
            if (tabs[i] instanceof TabProps) {
                _lastNonNullTab = i;
                break;
            }
            // default if there is no tabprops if there is no tabtodo
            // either, this will result in _lastNonNullTab = -1
            if (tabs[i] instanceof TabToDo) {
                _lastNonNullTab = i;
            }
        }
        setTarget(null);
        _item = null;
        _tabs.addMouseListener(this);
        _tabs.addChangeListener(this);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * returns the JTabbedPane that contains all details panels.
     */
    public JTabbedPane getTabs() {
        return _tabs;
    }

    /**
     * selects the to do tab, and sets the target of that tab.
     */
    public boolean setToDoItem(Object item) {
        _item = item;
        enableTabs(item);
        for (int i = 0; i < _tabPanels.size(); i++) {
            JPanel t = (JPanel) _tabPanels.elementAt(i);
            if (t instanceof TabToDoTarget) {
                ((TabToDoTarget) t).setTarget(_item);
                _tabs.setSelectedComponent(t);
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the target of the Details pane to either be a
     * selected model element or
     * the owner(model element) of a selected fig.
     *
     * <p>Decides which panels to enable.
     * @deprecated As of ArgoUml version 0.13.5,
     *             replaced by
     *             {@link org.argouml.ui.targetmanager.TargetListener},
     *             will become non-public in 
     * the future
     */
    public void setTarget(Object target) {
        enableTabs(target);
        if (target != null) {
            boolean tabSelected = false;
            for (int i = _lastNonNullTab; i >= 1; i--) {
                Component tab = _tabs.getComponentAt(i);
                if (tab instanceof TabTarget) {
                    if (((TabTarget) tab).shouldBeEnabled(target)) {
                        if (!(tab instanceof TargetListener))
			    ((TabTarget) tab).setTarget(target);
                        _tabs.setSelectedIndex(i);
                        tabSelected = true;
                        _lastNonNullTab = i;
                        break;
                    }
                }
            }
            if (!tabSelected) {
                for (int i = _lastNonNullTab + 1;
		     i < _tabs.getTabCount();
		     i++) {
                    Component tab = _tabs.getComponentAt(i);
                    if (tab instanceof TabTarget) {
                        if (((TabTarget) tab).shouldBeEnabled(target)) {
                            _tabs.setSelectedIndex(i);
                            ((TabTarget) tab).setTarget(target);
                            _lastNonNullTab = i;
                            tabSelected = true;
                            break;
                        }
                    }
                }
            }
            // default tab todo
            if (!tabSelected) {
                JPanel tab = (JPanel) _tabPanels.get(0);
                if (!(tab instanceof TabToDo)) {
                    Iterator it = _tabPanels.iterator();
                    while (it.hasNext()) {
                        Object o = it.next();
                        if (o instanceof TabToDo) {
                            tab = (TabToDo) o;
                            break;
                        }
                    }
                }
                if (tab instanceof TabToDo) {
                    _tabs.setSelectedComponent(tab);
                    ((TabToDo) tab).setTarget(target);
                    _lastNonNullTab = _tabs.getSelectedIndex();
                }
            }

        } else {
            // default tab todo
            JPanel tab =
                _tabPanels.isEmpty() ? null : (JPanel) _tabPanels.get(0);
            if (!(tab instanceof TabToDo)) {
                Iterator it = _tabPanels.iterator();
                while (it.hasNext()) {
                    Object o = it.next();
                    if (o instanceof TabToDo) {
                        tab = (TabToDo) o;
                        break;
                    }
                }
            }
            if (tab instanceof TabToDo) {
                _tabs.setSelectedComponent(tab);
                ((TabToDo) tab).setTarget(target);

            } else {
                _tabs.setSelectedIndex(-1);
            }
        }
        _target = target;

    }

    /**
     * Returns the current model target.
     */
    public Object getTarget() {
        return _target;
    }

    public Dimension getMinimumSize() {
        return new Dimension(100, 100);
    }

    ////////////////////////////////////////////////////////////////
    // actions

    /**
     * Get the index of the tab with the given name
     *
     * @param tabName the name of the required tab
     * @return index of the tab of the given name
     */
    public int getIndexOfNamedTab(String tabName) {
        for (int i = 0; i < _tabPanels.size(); i++) {
            String title = _tabs.getTitleAt(i);
            if (title != null && title.equals(tabName))
                return i;
        }
        return -1;
    }

    /**
     * Get the JPanel of the tab with the given name
     *
     * @param tabName the name of the required tab
     * @return the tab of the given name
     */
    public JPanel getNamedTab(String tabName) {
        for (int i = 0; i < _tabPanels.size(); i++) {
            String title = _tabs.getTitleAt(i);
            if (title != null && title.equals(tabName))
                return (JPanel) _tabs.getComponentAt(i);
        }
        return null;
    }

    /**
     * Get the number of tabs
     *
     * @return the number of tab pages
     */
    public int getTabCount() {
        return _tabPanels.size();
    }

    public boolean selectTabNamed(String tabName) {
        int index = getIndexOfNamedTab(tabName);
        if (index != -1) {
            _tabs.setSelectedIndex(index);
            return true;
        }
        return false;
    }

    /**
     * Heper method to add a Property panel for a given class.
     */
    public void addToPropTab(Class c, PropPanel p) {
        for (int i = 0; i < _tabPanels.size(); i++) {
            if (_tabPanels.elementAt(i) instanceof TabProps) {
                ((TabProps) _tabPanels.elementAt(i)).addPanel(c, p);
            }
        }
    }

    /**
     * returns the Property panel in the Details Pane.
     */
    public TabProps getTabProps() {
        Iterator iter = _tabPanels.iterator();
        Object o;
        while (iter.hasNext()) {
            o = iter.next();
            if (o instanceof TabProps) {
                return (TabProps) o;
            }
        }
        return null;
    }

    /**
     * returns the tab instance of the specified class
     */
    public TabSpawnable getTab(Class tabClass) {
        Iterator iter = _tabPanels.iterator();
        Object o;
        while (iter.hasNext()) {
            o = iter.next();
            if (o.getClass().equals(tabClass)) {
                return (TabSpawnable) o;
            }
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    /**
     * Reacts to a change in the selected tab by calling
     * 
     * refresh() for TabToDoTarget's
     * &
     * setTarget on a  TabModelTarget or TabFigTarget instance
     * 
     * old notes: called when the user selects a new tab, by clicking or
     *  otherwise.
     */
    public void stateChanged(ChangeEvent e) {
        cat.debug("DetailsPane state changed");
        Component sel = _tabs.getSelectedComponent();

        // update the tab
        if (_lastNonNullTab >= 0) {
	    Object tab = _tabPanels.get(_lastNonNullTab);
	    if (tab instanceof TargetListener)
		removeTargetListener((TargetListener) tab);
	}
        Object target = TargetManager.getInstance().getTarget();
        
        if (!(sel instanceof TargetListener)) {
            if (sel instanceof TabToDoTarget)
		((TabToDoTarget) sel).refresh();

            else if (sel instanceof TabTarget)
		((TabTarget) sel).setTarget(target);
        } else {
            removeTargetListener((TargetListener) sel);
            addTargetListener((TargetListener) sel);
        }
        
        if (target != null
            && ModelFacade.isABase(target)
            && _tabs.getSelectedIndex() > 0)
            _lastNonNullTab = _tabs.getSelectedIndex();

    }

    /**
     * no action currently executed here.
     * called when the user clicks once on a tab.
     */
    public void mySingleClick(int tab) {
        //TODO: should fire its own event and ProjectBrowser
        //should register a listener
        cat.debug("single: " + _tabs.getComponentAt(tab).toString());
    }

    /**
     * Spawns a new tab.
     * called when the user clicks twice on a tab.
     */
    public void myDoubleClick(int tab) {
        //TODO: should fire its own event and ProjectBrowser
        //should register a listener
        cat.debug("double: " + _tabs.getComponentAt(tab).toString());
        JPanel t = (JPanel) _tabPanels.elementAt(tab);
        if (t instanceof TabSpawnable)
	    ((TabSpawnable) t).spawn();
    }

    /**
     * empty, no action taken.
     */
    public void mousePressed(MouseEvent me) {
    }
    /**
     * empty, no action taken.
     */
    public void mouseReleased(MouseEvent me) {
    }
    /**
     * empty, no action taken.
     */
    public void mouseEntered(MouseEvent me) {
    }
    /**
     * empty, no action taken.
     */
    public void mouseExited(MouseEvent me) {
    }

    /**
     * if(the mouse click is not in the bounds of the tabbed panel)
     *      then call mySingleClick() or myDoubleClick()
     */
    public void mouseClicked(MouseEvent me) {
        int tab = _tabs.getSelectedIndex();
        if (tab != -1) {
            Rectangle tabBounds = _tabs.getBoundsAt(tab);
            if (!tabBounds.contains(me.getX(), me.getY()))
                return;
            if (me.getClickCount() == 1)
                mySingleClick(tab);
            else if (me.getClickCount() >= 2)
                myDoubleClick(tab);
        }
    }

    /**
     * graphic that goes on the tab label
     */
    protected Icon _upArrowIcon = new UpArrowIcon();

    /**
     * graphic that goes on the tab label
     */
    protected Icon _leftArrowIcon = new LeftArrowIcon();

    public int getQuadrant() {
        return Q_BOTTOM_RIGHT;
    }

    /**
     * Set the orientation of this details pane;
     * @param the required orientation
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
        for (int i = 0; i < _tabPanels.size(); i++) {
            JPanel t = (JPanel) _tabPanels.elementAt(i);
            if (t instanceof Orientable) {
                Orientable o = (Orientable) t;
                o.setOrientation(orientation);
            }
        } /* end for */
    }

    /**
     * @see
     * org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        // we can neglect this, the detailspane allways selects the first target
        // in a set of targets. The first target can only be 
        // changed in a targetRemoved or a TargetSet event
        fireTargetAdded(e);
    }

    /**
     * @see
     * org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        // how to handle empty target lists?
        // probably the detailspane should only show an empty pane in that case
        setTarget(e.getNewTargets()[0]);
        fireTargetRemoved(e);
    }

    /**
     * @see
     * org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTargets()[0]);
        fireTargetSet(e);
    }

    /**
     * Enables/disables the tabs on the tabbed card. Also selects the tab to 
     * show.
     */
    private void enableTabs(Object target) {

        // iterate through the tabbed panels to determine wether they
        // should be enabled. 
        for (int i = 0; i < _tabPanels.size(); i++) {
            JPanel tab = (JPanel) _tabPanels.elementAt(i);
            boolean shouldEnable = false;
            if (tab instanceof TargetListener) {
                if (tab instanceof TabTarget) {
                    shouldEnable = ((TabTarget) tab).shouldBeEnabled(target);
                } else {
                    if (tab instanceof TabToDo) {
                        shouldEnable = true;
                    }
                }
                if (shouldEnable) {
                    removeTargetListener((TargetListener) tab);
                    addTargetListener((TargetListener) tab);
                }
            }

            _tabs.setEnabledAt(i, shouldEnable);

        }

    }
    private void fireTargetSet(TargetEvent targetEvent) {
        //          Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TargetListener.class) {
                // Lazily create the event:                     
		((TargetListener) listeners[i + 1]).targetSet(targetEvent);
            }
        }
    }

    private void fireTargetAdded(TargetEvent targetEvent) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TargetListener.class) {
                // Lazily create the event:                     
		((TargetListener) listeners[i + 1]).targetAdded(targetEvent);
            }
        }
    }

    private void fireTargetRemoved(TargetEvent targetEvent) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TargetListener.class) {
                // Lazily create the event:                     
                ((TargetListener) listeners[i + 1]).targetRemoved(targetEvent);
            }
        }
    }

} /* end class DetailsPane */

////////////////////////////////////////////////////////////////
// related classes

/**
 * class defining a graphic that goes on the tab label
 */
class UpArrowIcon implements Icon {
    public void paintIcon(Component c, Graphics g, int x, int y) {
        int w = getIconWidth(), h = getIconHeight();
        g.setColor(Color.black);
        Polygon p = new Polygon();
        p.addPoint(x, y + h);
        p.addPoint(x + w / 2 + 1, y);
        p.addPoint(x + w, y + h);
        g.fillPolygon(p);
    }
    public int getIconWidth() {
        return 9;
    }
    public int getIconHeight() {
        return 9;
    }
}

/**
 * class defining a graphic that goes on the tab label
 */
class LeftArrowIcon implements Icon {
    public void paintIcon(Component c, Graphics g, int x, int y) {
        int w = getIconWidth(), h = getIconHeight();
        g.setColor(Color.black);
        Polygon p = new Polygon();
        p.addPoint(x + 1, y + h / 2 + 1);
        p.addPoint(x + w, y);
        p.addPoint(x + w, y + h);
        g.fillPolygon(p);
    }
    public int getIconWidth() {
        return 9;
    }
    public int getIconHeight() {
        return 9;
    }

}
