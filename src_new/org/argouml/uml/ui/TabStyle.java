// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

// File: TabStyle.java
// Classes: TabStyle
// Original Author:
// $Id$

// 12 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to support
// use case style panel that handles optional display of extension points.

package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.argouml.kernel.DelayedChangeNotify;
import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.ui.StylePanel;
import org.argouml.ui.TabFigTarget;
import org.argouml.ui.TabSpawnable;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.uml.util.namespace.Namespace;
import org.argouml.uml.util.namespace.StringNamespace;
import org.argouml.uml.util.namespace.StringNamespaceElement;
import org.tigris.gef.presentation.Fig;

/**
 * Provides support for changing the appearance of a diagram element. For each
 * class of a diagram element, the TabStyle class attempts to find an according
 * class of StylePanel which contains the attributes to be modified in terms of
 * style.
 * <p>
 * The constructor of TabStyle takes an array argument which contains possible
 * base names for these style panels, or by default StylePanel and SP,
 * alternating between these two prefixes and the namespace of the Fig class or
 * <code>org.argouml.ui</code>. With this configuration, the stylepanel for
 * e.g. <code>org.argouml.uml.diagram.static.structure.ui.FigClass</code>,
 * will be looked at in the following places:
 * <ul>
 * <li>org.argouml.uml.diagram.static_structure.ui.StylePanelFigClass
 * <li>org.argouml.uml.diagram.static_structure.ui.SPFigClass
 * <li>org.argouml.ui.StylePanelFigClass
 * <li>org.argouml.ui.SPFigClass
 * </ul>
 * It continues to traverse the superclass structure until a matching class has
 * been found, e.g.
 * <ul>
 * <li>org.argouml.uml.diagram.ui.StylePanelFigNodeModelElement
 * <li>org.argouml.uml.diagram.ui.SPFigNodeModelElement
 * <li>org.argouml.ui.StylePanelFigNodeModelElement
 * <li>org.argouml.ui.SPFigNodeModelElement
 * </ul>
 * If a stylepanel had been found, it will be stored in a cache, which can also
 * be initialized in <code>initPanels()</code>
 *  
 */
public class TabStyle extends TabSpawnable implements TabFigTarget,
        PropertyChangeListener, DelayedVChangeListener {

    private Logger _cat = Logger.getLogger(this.getClass());

    protected Fig _target;

    protected boolean _shouldBeEnabled = false;

    protected JPanel _blankPanel = new JPanel();

    protected Hashtable _panels = new Hashtable();

    protected JPanel _lastPanel = null;

    /**
     * The stylepanel shown by the tab style.
     */
    protected StylePanel _stylePanel = null;

    private String[] _stylePanelNames;

    /**
     * @deprecated use accessor for getStylePanelBaseNames instead. Remove in
     *             0.16.0
     */
    protected String _panelClassBaseName = "";

    /**
     * @deprecated use accessor for getStylePanelBaseNames instead. Remove in
     *             0.16.0
     */
    protected String _alternativeBase = "";

    private EventListenerList _listenerList = new EventListenerList();

    /**
     * @param tabName
     *            name of the tab (a i18n key)
     * @param panelClassBase
     * @param altBase
     * @deprecated remove in 0.16.0
     */
    public TabStyle(String tabName, String panelClassBase, String altBase) {
        super(tabName);
        _panelClassBaseName = panelClassBase;
        _alternativeBase = altBase;
        setLayout(new BorderLayout());
        initPanels();
    }

    public TabStyle(String tabName, String[] stylePanelNames) {
        super(tabName);
        _stylePanelNames = stylePanelNames;
        setLayout(new BorderLayout());
        initPanels();
    }

    /**
     * construct a default stylepanel with basenames <code>StylePanel</code>
     * and <code>SP</code>, resulting in the lookup order described above.
     */
    public TabStyle() {
        this("tab.style", new String[] { "StylePanel", "SP" });
    }

    /**
     * initialize the hashtable of pre lookup panels.
     *  
     */
    protected void initPanels() {

    }

    /**
     * Adds a style panel to the internal list. This allows a plugin to add and
     * register a new style panel at run-time. This property style will then be
     * displayed in the detatils pane whenever an element of the given metaclass
     * is selected.
     * 
     * @param c
     *            the metaclass whose details show be displayed in the property
     *            panel p
     * @param s
     *            an instance of the style panel for the metaclass m
     */
    public void addPanel(Class c, StylePanel s) {
        _panels.put(c, s);
    }

    /**
     * Sets the target of the style tab.
     * 
     * @deprecated As of ArgoUml version 0.13.5, the visibility of this method
     *             will change in the future, replaced by
     *             {@link org.argouml.ui.targetmanager.TargetManager}.
     * @param t
     *            is the new target
     */
    public void setTarget(Object t) {
        if (_target != null) _target.removePropertyChangeListener(this);

        // the responsibility of determining if the given target is a
        // correct one for this tab has been moved from the
        // DetailsPane to the member tabs of th detailpane. Reason for
        // this is that the detailspane is configurable and cannot
        // know what's the correct target for some tab.
        if (!(t instanceof Fig)) {
            if (ModelFacade.isABase(t)) {
                Project p = ProjectManager.getManager().getCurrentProject();
                Collection col = p.findFigsForMember(t);
                if (col == null || col.isEmpty()) {
                    return;
                } else {
                    t = col.iterator().next();
                    if (!(t instanceof Fig)) return;
                }
            } else {
                return;
            }

        }

        _target = (Fig) t;
        if (_target != null) _target.addPropertyChangeListener(this);
        if (_lastPanel != null) {
            remove(_lastPanel);
            if (_lastPanel instanceof TargetListener) {
                removeTargetListener((TargetListener) _lastPanel);
            }
        }
        if (t == null) {
            add(_blankPanel, BorderLayout.NORTH);
            _shouldBeEnabled = false;
            _lastPanel = _blankPanel;
            return;
        }
        _shouldBeEnabled = true;
        _stylePanel = null;
        Class targetClass = t.getClass();

        _stylePanel = findPanelFor(targetClass);

        if (_stylePanel != null) {
            if (_stylePanel instanceof TargetListener) {
                // TargetManager now replaces the old
                // functionality of
                // setTarget
                removeTargetListener(_stylePanel);
                addTargetListener(_stylePanel);
            } else
                _stylePanel.setTarget(_target);
            add(_stylePanel, BorderLayout.NORTH);
            _shouldBeEnabled = true;
            _lastPanel = _stylePanel;
        } else {
            add(_blankPanel, BorderLayout.NORTH);
            _shouldBeEnabled = false;
            _lastPanel = _blankPanel;
        }
        validate();
        repaint();
    }

    /**
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {
        setTarget(_target);
    }

    /**
     * find stylepanel for a given target class.
     * 
     * @param targetClass
     *            the target class
     * @return a Stylepanel object or <code>null</code> on error
     */
    public StylePanel findPanelFor(Class targetClass) {
        Class panelClass = null;
        TabFigTarget p = (TabFigTarget) _panels.get(targetClass);
        if (p == null) {
            Class newClass = targetClass;
            while (newClass != null && panelClass == null) {
                panelClass = panelClassFor(newClass);
                newClass = newClass.getSuperclass();
            }
            if (panelClass == null) return null;
            try {
                p = (TabFigTarget) panelClass.newInstance();
            } catch (IllegalAccessException ignore) {
                _cat.error(ignore);
                return null;
            } catch (InstantiationException ignore) {
                _cat.error(ignore);
                return null;
            }
            _panels.put(targetClass, p);
        }
        _cat.debug("found style for " + targetClass.getName() + "("
                + p.getClass() + ")");
        return (StylePanel) p;

    }

    /**
     * return the class for a matching stylepanel
     * 
     * @param targetClass
     * @return
     */
    public Class panelClassFor(Class targetClass) {
        if (targetClass == null) return null;

        String pack = "org.argouml.ui";

        StringNamespace classNs = (StringNamespace) StringNamespace
                .parse(targetClass);

        StringNamespace baseNs = (StringNamespace) StringNamespace.parse(
                "org.argouml.ui.", Namespace.JAVA_NS_TOKEN);

        StringNamespaceElement targetClassElement = (StringNamespaceElement) classNs
                .peekNamespaceElement();

        _cat.debug("Attempt to find style panel for: " + classNs);

        classNs.popNamespaceElement();

        Class cls;

        for (int i = 0; i < getStylePanelNames().length; i++) {
            try {
                cls = Class.forName(classNs.toString() + "."
                        + getStylePanelNames()[i] + targetClassElement);
                return cls;
            } catch (ClassNotFoundException ignore) {
                _cat.debug("ClassNotFoundException. Could not find class:"
                        + classNs.toString() + "." + getStylePanelNames()[i]
                        + targetClassElement);
            }
            try {
                cls = Class.forName(baseNs.toString() + "."
                        + getStylePanelNames()[i] + targetClassElement);
                return cls;
            } catch (ClassNotFoundException ignore) {
                _cat.debug("ClassNotFoundException. Could not find class:"
                        + classNs.toString() + "." + getStylePanelNames()[i]
                        + targetClassElement);
            }
        }
        return null;
    }

    protected String[] getStylePanelNames() {
        return _stylePanelNames;
    }

    /**
     * return the current target for this stylepanel.
     * 
     * @see org.argouml.ui.TabTarget#getTarget()
     */
    public Object getTarget() {
        return _target;
    }

    /**
     * @see org.argouml.ui.TabTarget#shouldBeEnabled(java.lang.Object)
     */
    public boolean shouldBeEnabled(Object target) {

        if (!(target instanceof Fig)) {
            if (ModelFacade.isABase(target)) {
                Project p = ProjectManager.getManager().getCurrentProject();
                Fig f = p.getActiveDiagram().presentationFor(target);

                if (f != null)
                    target = f;
                else {
                    _shouldBeEnabled = false;
                    return false;
                }
            } else {
                _shouldBeEnabled = false;
                return false;
            }
        }

        _shouldBeEnabled = true;

        Class targetClass = target.getClass();
        _stylePanel = findPanelFor(targetClass);
        targetClass = targetClass.getSuperclass();

        if (_stylePanel == null) {
            _shouldBeEnabled = false;
        }

        return _shouldBeEnabled;
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void propertyChange(PropertyChangeEvent pce) {
        DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
        SwingUtilities.invokeLater(delayedNotify);
    }

    /**
     * @see org.argouml.kernel.DelayedVChangeListener#delayedVetoableChange(java.beans.PropertyChangeEvent)
     */
    public void delayedVetoableChange(PropertyChangeEvent pce) {
        if (_stylePanel != null) _stylePanel.refresh(pce);
    }

    /**
     * @see TargetListener#targetAdded(TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        setTarget(e.getNewTarget());
        fireTargetAdded(e);

    }

    /**
     * @see TargetListener#targetRemoved(TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        // how to handle empty target lists?
        // probably the TabProps should only show an empty pane in that
        // case
        setTarget(e.getNewTarget());
        fireTargetRemoved(e);

    }

    /**
     * @see TargetListener#targetSet(TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTarget());
        fireTargetSet(e);

    }

    /**
     * Adds a listener.
     * 
     * @param listener
     *            the listener to add
     */
    private void addTargetListener(TargetListener listener) {
        _listenerList.add(TargetListener.class, listener);
    }

    /**
     * Removes a target listener.
     * 
     * @param listener
     *            the listener to remove
     */
    private void removeTargetListener(TargetListener listener) {
        _listenerList.remove(TargetListener.class, listener);
    }

    /**
     * @param targetEvent
     */
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

    /**
     * @param targetEvent
     */
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

    /**
     * @param targetEvent
     */
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

} /* end class TabStyle */
