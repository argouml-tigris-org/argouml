// $Id$
// Copyright (c) 1996-2008 The Regents of the University of California. All
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
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.swingext.LeftArrowIcon;
import org.argouml.swingext.UpArrowIcon;
import org.argouml.ui.ProjectBrowser.Position;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.TabProps;
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

    /**
     * The top level pane, which is a tabbed pane.
     */
    private JTabbedPane topLevelTabbedPane = new JTabbedPane();

    /**
     * The current target.
     */
    private Object currentTarget;

    
    /**
     * The list of all the tabs, which are JPanels, in the JTabbedPane tabs.
     */
    private List<JPanel> tabPanelList = new ArrayList<JPanel>();

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
    
    private Orientation orientation;
    
    private boolean hasTabs = false;

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

    /**
     * Gets all of the tabPanels from the ConfigLoader, then
     * adds them to the JTabbedPane.<p>
     *
     * Sets the target to null.<p>
     *
     * Registers listeners.<p>
     *
     * @param compassPoint the position for which to build the pane
     * @param theOrientation is the orientation.
     */
    public DetailsPane(String compassPoint, Orientation theOrientation) {
        LOG.info("making DetailsPane(" + compassPoint + ")");
        
        orientation = theOrientation;

        loadTabs(compassPoint, theOrientation);
        
        setOrientation(orientation);
        
        setLayout(new BorderLayout());
        setFont(new Font("Dialog", Font.PLAIN, 10));
        add(topLevelTabbedPane, BorderLayout.CENTER);

        setTarget(null, true);
        topLevelTabbedPane.addMouseListener(this);
        topLevelTabbedPane.addChangeListener(this);
    }

    /* TODO: Some parts of ArgoUML have preliminary support for multiple
     * details panels, but we currently only support 
     * the default South (bottom) panel
     */
    private void loadTabs(String direction, Orientation theOrientation) {
        if (Position.South.toString().equalsIgnoreCase(direction)
                // Special case for backward compatibility
                || "detail".equalsIgnoreCase(direction)) {
            /* The south panel always has tabs - but they are 
             * added (later) elsewhere.
             */
            hasTabs = true;
        } 
    }
    
    boolean hasTabs() {
        return hasTabs;
    }

    /**
     * Returns the JTabbedPane that contains all details panels.
     * @deprecated by MVW, in V0.25.4. Will become package scope.
     * @return the JTabbedPane.
     */
    @Deprecated
    public JTabbedPane getTabs() {
        return topLevelTabbedPane;
    }

    /**
     * @param p the panel to be added 
     * @param atEnd true = add the panel at the end, 
     *                  false = at the beginning
     */
    public void addTab(AbstractArgoJPanel p, boolean atEnd) {
        Icon icon = p.getIcon();
        String title = Translator.localize(p.getTitle());
        if (atEnd) {
            topLevelTabbedPane.addTab(title, icon, p); 
            tabPanelList.add(p);
        } else { 
            topLevelTabbedPane.insertTab(title, icon, p, null, 0);
            tabPanelList.add(0, p);
        }

    }

    /**
     * Selects the to do tab, and sets the target of that tab.
     * 
     * @param item the selected todo item
     * @return true if todo tab is really selected.
     * @deprecated for 0.25.5 by tfmorris. Send an event that TabToDoTargets
     *             listen to.
     */
    @Deprecated
    public boolean setToDoItem(Object item) {
        enableTabs(item);
        for (JPanel t : tabPanelList) {
            if (t instanceof TabToDoTarget) {
                ((TabToDoTarget) t).setTarget(item);
                topLevelTabbedPane.setSelectedComponent(t);
                return true;
            }
        }
        return false;
    }

    /**
     * Selects the prop tab if it is appropriate for the target.
     *
     * @param target
     *            the target object
     * @return true if props tab is really selected
     */
    private boolean selectPropsTab(Object target) {
        if (getTabProps().shouldBeEnabled(target)) {
            int indexOfPropPanel = topLevelTabbedPane
                    .indexOfComponent(getTabProps());
            topLevelTabbedPane.setSelectedIndex(indexOfPropPanel);
            lastNonNullTab = indexOfPropPanel;
            return true;
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
    private void setTarget(Object target, boolean defaultToProperties) {
        enableTabs(target);
        if (target != null) {
            boolean tabSelected = false;

            // Always select properties panel if defaultToProperties is true,
            // and if properties panel is appropriate for selected perspective
            if (defaultToProperties) {
                tabSelected = selectPropsTab(target);
            } else {
                // Select prop panel if current panel is not appropriate
                // for selected target
                Component selectedTab = topLevelTabbedPane
                        .getComponentAt(lastNonNullTab);
                if (selectedTab instanceof TabTarget) {
                    if (((TabTarget) selectedTab).shouldBeEnabled(target)) {
                        topLevelTabbedPane.setSelectedIndex(lastNonNullTab);
                        tabSelected = true;
                    } else {
                        tabSelected = selectPropsTab(target);
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
                JPanel tab = tabPanelList.get(0);
                if (!(tab instanceof TabToDoTarget)) {
                    for (JPanel panel : tabPanelList) {
                        if (panel instanceof TabToDoTarget) {
                            tab = panel;
                            break;
                        }
                    }
                }
                if (tab instanceof TabToDoTarget) {
                    topLevelTabbedPane.setSelectedComponent(tab);
                    ((TabToDoTarget) tab).setTarget(target);
                    lastNonNullTab = topLevelTabbedPane.getSelectedIndex();
                }
            }

        } else {
            // default tab todo
            JPanel tab =
                tabPanelList.isEmpty() ? null : (JPanel) tabPanelList.get(0);
            if (!(tab instanceof TabToDoTarget)) {
                Iterator it = tabPanelList.iterator();
                while (it.hasNext()) {
                    Object o = it.next();
                    if (o instanceof TabToDoTarget) {
                        tab = (JPanel) o;
                        break;
                    }
                }
            }
            if (tab instanceof TabToDoTarget) {
                topLevelTabbedPane.setSelectedComponent(tab);
                ((TabToDoTarget) tab).setTarget(target);

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
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(100, 100);
    }


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
        for (JPanel panel : tabPanelList) {
            if (panel instanceof TabProps) {
                ((TabProps) panel).addPanel(c, p);
            }
        }
    }

    /**
     * returns the Property panel in the Details Pane.
     *
     * @return the property panel
     */
    public TabProps getTabProps() {
        for (JPanel tab : tabPanelList) {
            if (tab instanceof TabProps) {
                return (TabProps) tab;
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
    public AbstractArgoJPanel getTab(
            Class< ? extends AbstractArgoJPanel> tabClass) {
        for (JPanel tab : tabPanelList) {
            if (tab.getClass().equals(tabClass)) {
                return (AbstractArgoJPanel) tab;
            }
        }
        return null;
    }

    /**
     * Reacts to a change in the selected tab by calling
     * refresh() for TabToDoTarget's
     * &
     * setTarget on a  TabModelTarget or TabFigTarget instance. <p>
     *
     * Called when the user selects a new tab, by clicking or
     * otherwise.
     *
     * {@inheritDoc}
     */
    public void stateChanged(ChangeEvent e) {
        LOG.debug("DetailsPane state changed");
        
        // update the previously selected tab
        if (lastNonNullTab >= 0) {
	    JPanel tab = tabPanelList.get(lastNonNullTab);
	    if (tab instanceof TargetListener) {
                // not visible any more - so remove as listener
	        removeTargetListener((TargetListener) tab);
	    }
	}
        Object target = TargetManager.getInstance().getSingleTarget();

        if (target != null
            && Model.getFacade().isAUMLElement(target)
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
    public void setOrientation(Orientation newOrientation) {
        for (JPanel t : tabPanelList) {
            if (t instanceof Orientable) {
                Orientable o = (Orientable) t;
                o.setOrientation(newOrientation);
            }
        }
    }

    /*
     * @see TargetListener#targetAdded(TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        setTarget(e.getNewTarget(), false);
        fireTargetAdded(e);
    }

    /*
     * @see TargetListener#targetRemoved(TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        setTarget(e.getNewTarget(), false);
        fireTargetRemoved(e);
    }

    /*
     * @see TargetListener#targetSet(TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTarget(), true);
        fireTargetSet(e);
    }

    /**
     * Enables/disables the tabs on the tabbed card. Also selects the tab to
     * show.
     *
     * @param target the target object
     */
    private void enableTabs(Object target) {

        // TODO: Quick return here for target == null? - tfm
        
        // iterate through the tabbed panels to determine whether they
        // should be enabled.
        for (int i = 0; i < tabPanelList.size(); i++) {
            JPanel tab = tabPanelList.get(i);
            boolean shouldEnable = false;
            if (tab instanceof TargetListener) {
                if (tab instanceof TabTarget) {
                    shouldEnable = ((TabTarget) tab).shouldBeEnabled(target);
                } else {
                    if (tab instanceof TabToDoTarget) {
                        shouldEnable = true;
                    }
                }
                // TODO: Do we want all enabled tabs to listen or only the one
                // that is selected/visible? - tfm
                removeTargetListener((TargetListener) tab);
                if (shouldEnable) {
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

}
