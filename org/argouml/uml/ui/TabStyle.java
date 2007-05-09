// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import org.argouml.model.Model;
import org.argouml.ui.AbstractArgoJPanel;
import org.argouml.ui.StylePanel;
import org.argouml.ui.TabFigTarget;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.uml.diagram.ArgoDiagram;
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
 * If a stylepanel had been found, it will be stored in a cache.<p>
 *
 * According the decision taken in issue 502, this tab is renamed "Presentation"
 * for the user. And the Presentation tab shall contain presentation options,
 * and no semantic UML properties (which belong in the "Properties" panel).
 * In contrast, the diagram pop-up menu for a model element
 * may access both presentation options as well as semantic UML properties. <p>
 *
 * Note also that the semantic properties of a UML model element exist in one
 * copy only but the presentation options exist in one copy per diagram
 * that the model element is showing in. E.g. a class could have
 * attributes hidden in one diagram and showing in another. So, for the user
 * it would be very logical to seperate these 2 kinds of settings
 * on different tabs.
 *
 */
public class TabStyle extends AbstractArgoJPanel implements TabFigTarget,
        PropertyChangeListener, DelayedVChangeListener {

    private static final Logger LOG = Logger.getLogger(TabStyle.class);

    private Fig target;

    private boolean shouldBeEnabled = false;

    private JPanel blankPanel = new JPanel();

    private Hashtable panels = new Hashtable();

    private JPanel lastPanel = null;

    /**
     * The stylepanel shown by the tab style.
     */
    private StylePanel stylePanel = null;

    private String[] stylePanelNames;

    private EventListenerList listenerList = new EventListenerList();

    /**
     * The constructor.
     *
     * @param tabName the name of the tab
     * @param spn style panel names
     */
    public TabStyle(String tabName, String[] spn) {
        super(tabName);
        this.stylePanelNames = spn;
        setLayout(new BorderLayout());
    }

    /**
     * Construct a default stylepanel with basenames <code>StylePanel</code>
     * and <code>SP</code>, resulting in the lookup order described above.
     */
    public TabStyle() {
        this("tab.style", new String[] {"StylePanel", "SP"});
    }

    /**
     * Initialize the hashtable of pre lookup panels.
     * @deprecated for 0.24 by tfmorris - noop - remove any references
     */
    protected void initPanels() {
    }

    /**
     * Adds a style panel to the internal list. This allows a plugin to add and
     * register a new style panel at run-time. This property style will then be
     * displayed in the details pane whenever an element of the given metaclass
     * is selected.
     *
     * @param c
     *            the metaclass whose details show be displayed in the property
     *            panel p
     * @param s
     *            an instance of the style panel for the metaclass m
     */
    public void addPanel(Class c, StylePanel s) {
        panels.put(c, s);
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
        if (target != null) target.removePropertyChangeListener(this);
        
        // TODO: Defer most of this work if the panel isn't visible - tfm

        // the responsibility of determining if the given target is a
        // correct one for this tab has been moved from the
        // DetailsPane to the member tabs of th detailpane. Reason for
        // this is that the detailspane is configurable and cannot
        // know what's the correct target for some tab.
        if (!(t instanceof Fig)) {
            if (Model.getFacade().isAModelElement(t)) {
                Project p = ProjectManager.getManager().getCurrentProject();
                Collection col = p.findFigsForMember(t);
                if (col == null || col.isEmpty()) {
                    return;
                }
                t = col.iterator().next();
                if (!(t instanceof Fig)) return;
            } else {
                return;
            }

        }

        target = (Fig) t;
        if (target != null) target.addPropertyChangeListener(this);
        if (lastPanel != null) {
            remove(lastPanel);
            if (lastPanel instanceof TargetListener) {
                removeTargetListener((TargetListener) lastPanel);
            }
        }
        if (t == null) {
            add(blankPanel, BorderLayout.NORTH);
            shouldBeEnabled = false;
            lastPanel = blankPanel;
            return;
        }
        shouldBeEnabled = true;
        stylePanel = null;
        Class targetClass = t.getClass();

        stylePanel = findPanelFor(targetClass);

        if (stylePanel != null) {
            if (stylePanel instanceof TargetListener) {
                // TargetManager now replaces the old
                // functionality of
                // setTarget
                removeTargetListener(stylePanel);
                addTargetListener(stylePanel);
            } else {
                stylePanel.setTarget(target);
            }
            add(stylePanel, BorderLayout.NORTH);
            shouldBeEnabled = true;
            lastPanel = stylePanel;
        } else {
            add(blankPanel, BorderLayout.NORTH);
            shouldBeEnabled = false;
            lastPanel = blankPanel;
        }
        validate();
        repaint();
    }

    /*
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {
        setTarget(target);
    }

    /**
     * Find the stylepanel for a given target class.
     *
     * @param targetClass
     *            the target class
     * @return a Stylepanel object or <code>null</code> on error
     */
    public StylePanel findPanelFor(Class targetClass) {
        Class panelClass = null;
        TabFigTarget p = (TabFigTarget) panels.get(targetClass);
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
                LOG.error(ignore);
                return null;
            } catch (InstantiationException ignore) {
                LOG.error(ignore);
                return null;
            }
            panels.put(targetClass, p);
        }
        LOG.debug("found style for " + targetClass.getName() + "("
                + p.getClass() + ")");
        return (StylePanel) p;

    }

    /**
     * Get the class for the required stylepanel.
     *
     * @param targetClass the class of the current seelcted target.
     * @return the panel class for the class given or
     * null if none available.
     */
    public Class panelClassFor(Class targetClass) {
        if (targetClass == null) return null;

        StringNamespace classNs = (StringNamespace) StringNamespace
                .parse(targetClass);

        StringNamespace baseNs = (StringNamespace) StringNamespace.parse(
                "org.argouml.ui.", Namespace.JAVA_NS_TOKEN);

        StringNamespaceElement targetClassElement =
        	(StringNamespaceElement) classNs.peekNamespaceElement();

        LOG.debug("Attempt to find style panel for: " + classNs);

        classNs.popNamespaceElement();

        Class cls;

        for (int i = 0; i < stylePanelNames.length; i++) {
            try {
                cls = Class.forName(classNs.toString() + "."
                        + stylePanelNames[i] + targetClassElement);
                return cls;
            } catch (ClassNotFoundException ignore) {
                LOG.debug("ClassNotFoundException. Could not find class:"
                        + classNs.toString() + "." + stylePanelNames[i]
                        + targetClassElement);
            }
            try {
                cls = Class.forName(baseNs.toString() + "."
                        + stylePanelNames[i] + targetClassElement);
                return cls;
            } catch (ClassNotFoundException ignore) {
                LOG.debug("ClassNotFoundException. Could not find class:"
                        + classNs.toString() + "." + stylePanelNames[i]
                        + targetClassElement);
            }
        }
        return null;
    }

    /**
     * @return the style panel names
     */
    protected String[] getStylePanelNames() {
        return stylePanelNames;
    }

    /*
     * @see org.argouml.ui.TabTarget#getTarget()
     */
    public Object getTarget() {
        return target;
    }

    /*
     * @see org.argouml.ui.TabTarget#shouldBeEnabled(java.lang.Object)
     */
    public boolean shouldBeEnabled(Object targetItem) {

        if (!(targetItem instanceof Fig)) {
            if (Model.getFacade().isAModelElement(targetItem)) {
                Project p = ProjectManager.getManager().getCurrentProject();
                ArgoDiagram diagram = p.getActiveDiagram();
                if (diagram == null) {
                    shouldBeEnabled = false;
                    return false;
                }

                Fig f = diagram.presentationFor(targetItem);
                if (f == null) {
                    shouldBeEnabled = false;
                    return false;
                }
                targetItem = f;
            } else {
                shouldBeEnabled = false;
                return false;
            }
        }

        shouldBeEnabled = true;

        // TODO: It would be better to defer this initialization until the panel
        // actually needs to be displayed. Perhaps optimistically always return
        // true and figure out later if we've got something to display - tfm -
        // 20070110
        Class targetClass = targetItem.getClass();
        stylePanel = findPanelFor(targetClass);
        targetClass = targetClass.getSuperclass();

        if (stylePanel == null) {
            shouldBeEnabled = false;
        }

        return shouldBeEnabled;
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void propertyChange(PropertyChangeEvent pce) {
        DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
        SwingUtilities.invokeLater(delayedNotify);
    }

    /*
     * @see org.argouml.kernel.DelayedVChangeListener#delayedVetoableChange(java.beans.PropertyChangeEvent)
     */
    public void delayedVetoableChange(PropertyChangeEvent pce) {
        if (stylePanel != null) stylePanel.refresh(pce);
    }

    /*
     * @see TargetListener#targetAdded(TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        setTarget(e.getNewTarget());
        fireTargetAdded(e);

    }

    /*
     * @see TargetListener#targetRemoved(TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        // how to handle empty target lists?
        // probably the TabProps should only show an empty pane in that
        // case
        setTarget(e.getNewTarget());
        fireTargetRemoved(e);

    }

    /*
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
        listenerList.add(TargetListener.class, listener);
    }

    /**
     * Removes a target listener.
     *
     * @param listener
     *            the listener to remove
     */
    private void removeTargetListener(TargetListener listener) {
        listenerList.remove(TargetListener.class, listener);
    }

    /**
     * @param targetEvent
     */
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

    /**
     * @param targetEvent
     */
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

    /**
     * @param targetEvent
     */
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

} /* end class TabStyle */
