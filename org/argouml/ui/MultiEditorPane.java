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
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.application.api.QuadrantPanel;
import org.argouml.swingext.Horizontal;
import org.argouml.uml.diagram.ui.TabDiagram;
import org.argouml.uml.ui.TabModelTarget;
import org.argouml.uml.ui.TabProps;
import org.argouml.util.ConfigLoader;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.graph.presentation.JGraph;
import org.tigris.gef.presentation.Fig;

/** The upper right pane in the Argo/UML user interface.  It has
 *  several tabs with different kinds of "major" editors that allow
 *  the user to edit whatever is selected in the NavigatorPane. */

public class MultiEditorPane
    extends JPanel
    implements ChangeListener, MouseListener, QuadrantPanel {
    protected static Category cat = Category.getInstance(MultiEditorPane.class);

    ////////////////////////////////////////////////////////////////
    // instance variables

    protected Object _target;
    protected JTabbedPane _tabs = new JTabbedPane(JTabbedPane.BOTTOM);
    protected Editor _ed;
    // protected ForwardingPanel _awt_comp;
    protected Vector _tabPanels = new Vector();
    protected Component _lastTab;

    ////////////////////////////////////////////////////////////////
    // constructors

    public MultiEditorPane() {
        Argo.log.info("making MultiEditorPane");
        ConfigLoader.loadTabs(_tabPanels, "multi", Horizontal.getInstance());

        setLayout(new BorderLayout());
        add(_tabs, BorderLayout.CENTER);

        // _tabs.addChangeListener(this);
        for (int i = 0; i < _tabPanels.size(); i++) {
            String title = "tab";
            JPanel t = (JPanel) _tabPanels.elementAt(i);
            if (t instanceof TabSpawnable)
                title = ((TabSpawnable) t).getTitle();
            _tabs.addTab("As " + title, t);
        } /* end for */

        for (int i = 0; i < _tabPanels.size(); i++)
            _tabs.setEnabledAt(i, false);

        _tabs.addChangeListener(this);
        _tabs.addMouseListener(this);
        setTarget(null);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public Dimension getPreferredSize() {
        return new Dimension(400, 500);
    }
    public Dimension getMinimumSize() {
        return new Dimension(100, 100);
    }

    /**
     * Sets the target of the multieditorpane. The multieditorpane can have several 
     * tabs. If a tab is an instance of tabmodeltarget (that is a tab that displays
     * model elements) that tab should display the target if the target is an 
     * ArgoDiagram.
     * @param target
     */
    public void setTarget(Object target) {
        if (_target == target)
            return;
        int nextTab = -1;
        int currentTab = _tabs.getSelectedIndex();
        int tabCount = _tabs.getTabCount();
        _target = target;
        for (int i = 0; i < tabCount; i++) {
            Component tab = _tabs.getComponentAt(i);
            if (tab instanceof TabModelTarget) {
                TabModelTarget tabMT = (TabModelTarget) tab;
                tabMT.setTarget(_target);
                boolean shouldEnable = tabMT.shouldBeEnabled();
                _tabs.setEnabledAt(i, shouldEnable);
                if (shouldEnable && (nextTab == -1 || i == currentTab))
                    nextTab = i;
            }
        }
        if (target != null)
            select(target);
        //    if this target doesn't match the tabs expectation    
        //        leave the previous tab displayed

        //    
        //    if (nextTab != -1 && nextTab != currentTab) 
        //        _tabs.setSelectedIndex(nextTab);
        //    _tabs.setVisible(nextTab != -1);
    }

    public Object getTarget() {
        return _target;
    }

    ////////////////////////////////////////////////////////////////
    // actions

    public int getIndexOfNamedTab(String tabName) {
        for (int i = 0; i < _tabPanels.size(); i++) {
            String title = _tabs.getTitleAt(i);
            if (title != null && title.equals(tabName))
                return i;
        }
        return -1;
    }

    public void selectTabNamed(String tabName) {
        int index = getIndexOfNamedTab(tabName);
        if (index != -1)
            _tabs.setSelectedIndex(index);
    }

    public void selectNextTab() {
        int size = _tabPanels.size();
        int currentTab = _tabs.getSelectedIndex();
        for (int i = 1; i < _tabPanels.size(); i++) {
            int newTab = (currentTab + i) % size;
            if (_tabs.isEnabledAt(newTab)) {
                _tabs.setSelectedIndex(newTab);
                return;
            }
        }
    }

    public void select(Object o) {
        Component curTab = _tabs.getSelectedComponent();
        if (curTab instanceof TabDiagram) {
            JGraph jg = ((TabDiagram) curTab).getJGraph();
            jg.selectByOwnerOrFig(o);
        }
        //TODO: handle tables
    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    /** called when the user selects a tab, by clicking or otherwise. */
    public void stateChanged(ChangeEvent e) {
        //TODO: should fire its own event and ProjectBrowser
        //should register a listener
        if (_lastTab != null) {
            _lastTab.setVisible(false);
        }
        _lastTab = _tabs.getSelectedComponent();
        cat.debug(
            "MultiEditorPane state changed:" + _lastTab.getClass().getName());
        _lastTab.setVisible(true);
        if (_lastTab instanceof TabModelTarget)
             ((TabModelTarget) _lastTab).refresh();
    }

    public void mousePressed(MouseEvent me) {
    }
    public void mouseReleased(MouseEvent me) {
    }
    public void mouseEntered(MouseEvent me) {
    }
    public void mouseExited(MouseEvent me) {
    }

    public void mouseClicked(MouseEvent me) {
        int tab = _tabs.getSelectedIndex();
        if (tab != -1) {
            Rectangle tabBounds = _tabs.getBoundsAt(tab);
            if (!tabBounds.contains(me.getX(), me.getY()))
                return;
            if (me.getClickCount() == 1) {
                mySingleClick(tab);
                me.consume();
            } else if (me.getClickCount() >= 2) {
                myDoubleClick(tab);
                me.consume();
            }
        }
    }

    /** called when the user clicks once on a tab. */
    public void mySingleClick(int tab) {
        //TODO: should fire its own event and ProjectBrowser
        //should register a listener
        cat.debug("single: " + _tabs.getComponentAt(tab).toString());
    }

    /** called when the user clicks twice on a tab. */
    public void myDoubleClick(int tab) {
        //TODO: should fire its own event and ProjectBrowser
        //should register a listener
        cat.debug("double: " + _tabs.getComponentAt(tab).toString());
        JPanel t = (JPanel) _tabPanels.elementAt(tab);
        if (t instanceof TabSpawnable)
             ((TabSpawnable) t).spawn();
    }

    public void addNavigationListener(NavigationListener navListener) {
        Iterator iter = _tabPanels.iterator();
        Object panel;
        while (iter.hasNext()) {
            panel = iter.next();
            if (panel instanceof TabProps) {
                ((TabProps) panel).addNavigationListener(navListener);
            }
        }
    }

    public void removeNavigationListener(NavigationListener navListener) {
        Iterator iter = _tabPanels.iterator();
        Object panel;
        while (iter.hasNext()) {
            panel = iter.next();
            if (panel instanceof TabProps) {
                ((TabProps) panel).removeNavigationListener(navListener);
            }
        }
    }

    public int getQuadrant() {
        return Q_TOP_RIGHT;
    }

    /**
     * Removes all figs from all diagrams for some object obj. Does not remove 
     * the owner of the objects (does not do a call to dispose).
     * @param obj
     */
    public void removePresentationFor(Object obj, Vector diagrams) {
        for (int i = 0; i < _tabs.getComponentCount(); i++) {
            Component comp = _tabs.getComponentAt(i);
            if (comp instanceof TabDiagram) {
                TabDiagram tabDia = (TabDiagram) comp;
                Object oldDia = tabDia.getTarget();
                Iterator it = diagrams.iterator();
                while (it.hasNext()) {
                    Diagram diagram = (Diagram) it.next();
                    Fig aFig = diagram.presentationFor(obj);
                    if (aFig != null) {
                        tabDia.getJGraph().setDiagram(diagram);
                        if (aFig.getOwner() == obj) {
                            aFig.delete();
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns a list with all figs for some object o on the given diagrams.
     * @param o
     * @param diagrams
     * @return List
     */
    public List findPresentationsFor(Object o, Vector diagrams) {
        List returnList = new ArrayList();
        for (int i = 0; i < _tabs.getComponentCount(); i++) {
            Component comp = _tabs.getComponentAt(i);
            if (comp instanceof TabDiagram) {
                TabDiagram tabDia = (TabDiagram) comp;
                Object oldDia = tabDia.getTarget();
                Iterator it = diagrams.iterator();
                while (it.hasNext()) {
                    Diagram diagram = (Diagram) it.next();
                    Fig aFig = diagram.presentationFor(o);
                    if (aFig != null)
                        returnList.add(aFig);
                }
            }
        }
        return returnList;
    }

}

/* end class MultiEditorPane */
