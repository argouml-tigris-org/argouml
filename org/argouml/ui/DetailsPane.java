// $Id$
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
import org.argouml.cognitive.ui.TabToDo;
import org.argouml.cognitive.ui.TabToDoTarget;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.TabModelTarget;
import org.argouml.uml.ui.TabProps;
import org.argouml.util.ConfigLoader;
import org.tigris.swidgets.Orientable;
import org.tigris.swidgets.Orientation;

/**
 * The lower-right pane of the main ArgoUML window, which shows
 * the details of a selected model element. <p>
 *
 * This panel has several tabs that show details of the selected
 * ToDoItem, or the selected model element in the Explorer (NavigatorPane),
 * or the MultiEditorPane. <p>
 *
 * There are requests to have the cursor automatically
 * be set to the primary field.
 */
public class DetailsPane
    extends JPanel
    implements ChangeListener, MouseListener,
	       Orientable,
	       TargetListener {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(DetailsPane.class);

    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * The top level pane, which is a tabbed pane.
     */
    private JTabbedPane topLevelTabbedPane = new JTabbedPane();

    /**
     * The current target.
     */
    private Object currentTarget;

    /**
     * a list of all the tabs, which are JPanels, in the JTabbedPane tabs.
     */
    private Vector tabPanelList = new Vector();

    /**
     * index of the selected tab in the JTabbedPane.
     */
    private int lastNonNullTab = -1;

    /**
     * The list with targetlisteners, this are the property panels
     * managed by TabProps It should only contain one listener at a
     * time.
     */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * Adds a listener.
     * @param listener the listener to add
     */
    private void addTargetListener(TargetListener listener) {
        listenerList.add(TargetListener.class, listener);
    }

    /**
     * Removes a target listener.
     * @param listener the listener to remove
     */
    private void removeTargetListener(TargetListener listener) {
        listenerList.remove(TargetListener.class, listener);
    }
    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Gets all of the tabPanels from the ConfigLoader, then
     * adds them to the JTabbedPane.<p>
     *
     * Sets the target to null.<p>
     *
     * Registers listeners.<p>
     *
     * @param pane is probably the name of the pane TODO: verify this!
     * @param orientation is the orientation.
     */
    public DetailsPane(String pane, Orientation orientation) {
        LOG.info("making DetailsPane(" + pane + ")");

        ConfigLoader.loadTabs(tabPanelList, pane, orientation);
        setLayout(new BorderLayout());
        setFont(new Font("Dialog", Font.PLAIN, 10));
        add(topLevelTabbedPane, BorderLayout.CENTER);

        for (int i = 0; i < tabPanelList.size(); i++) {
            String titleKey = "tab";
            JPanel t = (JPanel) tabPanelList.elementAt(i);
            if (t instanceof AbstractArgoJPanel) {
                titleKey = ((AbstractArgoJPanel) t).getTitle();
            }
            String title = Translator.localize(titleKey);
            if (t instanceof TabToDoTarget) {
                topLevelTabbedPane.addTab(title, leftArrowIcon, t);
            } else if (t instanceof TabModelTarget) {
                topLevelTabbedPane.addTab(title, upArrowIcon, t);
            } else if (t instanceof TabFigTarget) {
                topLevelTabbedPane.addTab(title, upArrowIcon, t);
            } else {
                topLevelTabbedPane.addTab(title, t);
            }
        }

        // set the tab that should be shown on first entrance
        lastNonNullTab = -1;
        Component[] tabs = topLevelTabbedPane.getComponents();
        for (int i = 0; i < tabs.length; i++) {
            // tabprops should be shown if loaded
            if (tabs[i] instanceof TabProps) {
                lastNonNullTab = i;
                break;
            }
            // default if there is no tabprops if there is no tabtodo
            // either, this will result in lastNonNullTab = -1;
            if (tabs[i] instanceof TabToDo) {
                lastNonNullTab = i;
            }
        }
        setTarget(null);
        topLevelTabbedPane.addMouseListener(this);
        topLevelTabbedPane.addChangeListener(this);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * Returns the JTabbedPane that contains all details panels.
     *
     * @return the JTabbedPane.
     */
    public JTabbedPane getTabs() {
        return topLevelTabbedPane;
    }

    /**
     * Selects the to do tab, and sets the target of that tab.<p>
     * @param item the selected todo item
     * @return true if ? Yes when? TODO: Explain.
     */
    public boolean setToDoItem(Object item) {
        enableTabs(item);
        for (int i = 0; i < tabPanelList.size(); i++) {
            JPanel t = (JPanel) tabPanelList.elementAt(i);
            if (t instanceof TabToDo) {
                ((TabToDo) t).setTarget(item);
                topLevelTabbedPane.setSelectedComponent(t);
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the target of the Details pane to either be a
     * selected model element or
     * the owner(model element) of a selected fig.<p>
     *
     * Decides which panels to enable.
     *
     * @param target the target object
     */
    private void setTarget(Object target) {
        enableTabs(target);
        if (target != null) {
            boolean tabSelected = false;
            for (int i = lastNonNullTab; i >= 1; i--) {
                Component tab = topLevelTabbedPane.getComponentAt(i);
                if (tab instanceof TabTarget) {
                    if (((TabTarget) tab).shouldBeEnabled(target)) {
                        topLevelTabbedPane.setSelectedIndex(i);
                        tabSelected = true;
                        lastNonNullTab = i;
                        break;
                    }
                }
            }
            if (!tabSelected) {
                for (int i = lastNonNullTab + 1;
		     i < topLevelTabbedPane.getTabCount();
		     i++) {
                    Component tab = topLevelTabbedPane.getComponentAt(i);
                    if (tab instanceof TabTarget) {
                        if (((TabTarget) tab).shouldBeEnabled(target)) {
                            topLevelTabbedPane.setSelectedIndex(i);
                            ((TabTarget) tab).setTarget(target);
                            lastNonNullTab = i;
                            tabSelected = true;
                            break;
                        }
                    }
                }
            }
            // default tab todo
            if (!tabSelected) {
                JPanel tab = (JPanel) tabPanelList.get(0);
                if (!(tab instanceof TabToDo)) {
                    Iterator it = tabPanelList.iterator();
                    while (it.hasNext()) {
                        Object o = it.next();
                        if (o instanceof TabToDo) {
                            tab = (TabToDo) o;
                            break;
                        }
                    }
                }
                if (tab instanceof TabToDo) {
                    /* TODO: MVW:I suspect that this code never gets executed.*/
                    topLevelTabbedPane.setSelectedComponent(tab);
                    ((TabToDo) tab).setTarget(target);
                    lastNonNullTab = topLevelTabbedPane.getSelectedIndex();
                }
            }

        } else {
            // default tab todo
            JPanel tab =
                tabPanelList.isEmpty() ? null : (JPanel) tabPanelList.get(0);
            if (!(tab instanceof TabToDo)) {
                Iterator it = tabPanelList.iterator();
                while (it.hasNext()) {
                    Object o = it.next();
                    if (o instanceof TabToDo) {
                        tab = (TabToDo) o;
                        break;
                    }
                }
            }
            if (tab instanceof TabToDo) {
                topLevelTabbedPane.setSelectedComponent(tab);
                ((TabToDo) tab).setTarget(target);

            } else {
                topLevelTabbedPane.setSelectedIndex(-1);
            }
        }
        currentTarget = target;

    }

    /**
     * Returns the current model target.
     * @return the current model target
     */
    public Object getTarget() {
        return currentTarget;
    }

    /*
     * @see java.awt.Component#getMinimumSize()
     */
    public Dimension getMinimumSize() {
        return new Dimension(100, 100);
    }

    ////////////////////////////////////////////////////////////////
    // actions

    /**
     * Get the index of the tab with the given name.
     *
     * @param tabName the name of the required tab
     * @return index of the tab of the given name
     */
    public int getIndexOfNamedTab(String tabName) {
        for (int i = 0; i < tabPanelList.size(); i++) {
            String title = topLevelTabbedPane.getTitleAt(i);
            if (title != null && title.equals(tabName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get the JPanel of the tab with the given name.
     *
     * @param tabName the name of the required tab
     * @return the tab of the given name
     * @deprecated by Andrea Nironi (0.22 August 2006). Replaced by
     *             {@link org.argouml.ui.DetailsPane#getTab(Class tabClass)}.
     *             See issue 3278.
     */
    public JPanel getNamedTab(String tabName) {
        for (int i = 0; i < tabPanelList.size(); i++) {
            String title = topLevelTabbedPane.getTitleAt(i);
            if (title != null && title.equals(tabName)) {
                return (JPanel) topLevelTabbedPane.getComponentAt(i);
            }
        }
        return null;
    }

    /**
     * Get the number of tabs.
     *
     * @return the number of tab pages
     */
    public int getTabCount() {
        return tabPanelList.size();
    }

    /**
     * Selects a tab by given name.
     * @param tabName the given name
     * @return true if the named tab has been found
     */
    public boolean selectTabNamed(String tabName) {
        int index = getIndexOfNamedTab(tabName);
        if (index != -1) {
            topLevelTabbedPane.setSelectedIndex(index);
            return true;
        }
        return false;
    }

    /**
     * Helper method to add a Property panel for a given class.
     *
     * @param c the given class
     * @param p the given property panel
     */
    public void addToPropTab(Class c, PropPanel p) {
        for (int i = 0; i < tabPanelList.size(); i++) {
            if (tabPanelList.elementAt(i) instanceof TabProps) {
                ((TabProps) tabPanelList.elementAt(i)).addPanel(c, p);
            }
        }
    }

    /**
     * returns the Property panel in the Details Pane.
     *
     * @return the property panel
     */
    public TabProps getTabProps() {
        Iterator iter = tabPanelList.iterator();
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
     * Returns the tab instance of the specified class.
     *
     * @param tabClass the given class
     * @return the tab instance for the given class
     */
    public AbstractArgoJPanel getTab(Class tabClass) {
        Iterator iter = tabPanelList.iterator();
        Object o;
        while (iter.hasNext()) {
            o = iter.next();
            if (o.getClass().equals(tabClass)) {
                return (AbstractArgoJPanel) o;
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
     *
     * {@inheritDoc}
     */
    public void stateChanged(ChangeEvent e) {
        LOG.debug("DetailsPane state changed");
        Component sel = topLevelTabbedPane.getSelectedComponent();

        // update the tab
        if (lastNonNullTab >= 0) {
	    Object tab = tabPanelList.get(lastNonNullTab);
	    if (tab instanceof TargetListener) {
	        removeTargetListener((TargetListener) tab);
	    }
	}
        Object target = TargetManager.getInstance().getTarget();

        if (!(sel instanceof TargetListener)) {
            if (sel instanceof TabToDo) {
                ((TabToDo) sel).refresh();
            } else if (sel instanceof TabTarget) {
                ((TabTarget) sel).setTarget(target);
            }
        } else {
            removeTargetListener((TargetListener) sel);
            addTargetListener((TargetListener) sel);
        }

        if (target != null
            && Model.getFacade().isAModelElement(target)
            && topLevelTabbedPane.getSelectedIndex() > 0) {
            lastNonNullTab = topLevelTabbedPane.getSelectedIndex();
        }

    }

    /**
     * no action currently executed here.
     * called when the user clicks once on a tab.
     *
     * @param tab the index of the clicked tab
     */
    public void mySingleClick(int tab) {
        //TODO: should fire its own event and ProjectBrowser
        //should register a listener
        LOG.debug("single: "
                + topLevelTabbedPane.getComponentAt(tab).toString());
    }

    /**
     * Spawns a new tab.
     * called when the user clicks twice on a tab.
     *
     * @param tab the index of the clicked tab
     */
    public void myDoubleClick(int tab) {
        //TODO: should fire its own event and ProjectBrowser
        //should register a listener
        LOG.debug("double: "
                + topLevelTabbedPane.getComponentAt(tab).toString());
//        JPanel t = (JPanel) tabPanelList.elementAt(tab);
        // Currently this feature is disabled for ArgoUML.
//        if (t instanceof AbstractArgoJPanel)
//	    ((AbstractArgoJPanel) t).spawn();
    }

    /*
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent me) {
        // empty implementation - we only handle mouseClicked
    }

    /*
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent me) {
        // empty implementation - we only handle mouseClicked
    }

    /*
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent me) {
        // empty implementation - we only handle mouseClicked
    }

    /*
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent me) {
        // empty implementation - we only handle mouseClicked
    }

    /*
     * if(the mouse click is not in the bounds of the tabbed panel)
     *      then call mySingleClick() or myDoubleClick().
     *
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent me) {
        int tab = topLevelTabbedPane.getSelectedIndex();
        if (tab != -1) {
            Rectangle tabBounds = topLevelTabbedPane.getBoundsAt(tab);
            if (!tabBounds.contains(me.getX(), me.getY())) {
                return;
            }
            if (me.getClickCount() == 1) {
                mySingleClick(tab);
            } else if (me.getClickCount() >= 2) {
                myDoubleClick(tab);
            }
        }
    }

    /**
     * Graphic that goes on the tab label.
     */
    private Icon upArrowIcon = new UpArrowIcon();

    /**
     * Graphic that goes on the tab label.
     */
    private Icon leftArrowIcon = new LeftArrowIcon();

    /*
     * @see org.tigris.swidgets.Orientable#setOrientation(org.tigris.swidgets.Orientation)
     */
    public void setOrientation(Orientation orientation) {
        for (int i = 0; i < tabPanelList.size(); i++) {
            JPanel t = (JPanel) tabPanelList.elementAt(i);
            if (t instanceof Orientable) {
                Orientable o = (Orientable) t;
                o.setOrientation(orientation);
            }
        } /* end for */
    }

    /*
     * @see TargetListener#targetAdded(TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        setTarget(TargetManager.getInstance().getSingleTarget());
        fireTargetAdded(e);
    }

    /*
     * @see TargetListener#targetRemoved(TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        setTarget(TargetManager.getInstance().getSingleTarget());
        fireTargetRemoved(e);
    }

    /*
     * @see TargetListener#targetSet(TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(TargetManager.getInstance().getSingleTarget());
        fireTargetSet(e);
    }

    /**
     * Enables/disables the tabs on the tabbed card. Also selects the tab to
     * show.
     *
     * @param target the target object
     */
    private void enableTabs(Object target) {

        // iterate through the tabbed panels to determine wether they
        // should be enabled.
        for (int i = 0; i < tabPanelList.size(); i++) {
            JPanel tab = (JPanel) tabPanelList.elementAt(i);
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

            topLevelTabbedPane.setEnabledAt(i, shouldEnable);

        }

    }
    private void fireTargetSet(TargetEvent targetEvent) {
        //          Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TargetListener.class) {
                // Lazily create the event:
		((TargetListener) listeners[i + 1]).targetSet(targetEvent);
            }
        }
    }

    private void fireTargetAdded(TargetEvent targetEvent) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TargetListener.class) {
                // Lazily create the event:
		((TargetListener) listeners[i + 1]).targetAdded(targetEvent);
            }
        }
    }

    private void fireTargetRemoved(TargetEvent targetEvent) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
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
 * Class defining a graphic that goes on the tab label.
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
 * Class defining a graphic that goes on the tab label.
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
