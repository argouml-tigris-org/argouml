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
import org.argouml.api.FacadeManager;
import org.argouml.model.uml.NsumlModelFacade;
import org.argouml.ui.StylePanel;
import org.argouml.ui.StylePanelFig;
import org.argouml.ui.TabFigTarget;
import org.argouml.ui.TabSpawnable;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.uml.diagram.state.ui.FigSimpleState;
import org.argouml.uml.diagram.state.ui.FigTransition;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.argouml.uml.diagram.static_structure.ui.FigInstance;
import org.argouml.uml.diagram.static_structure.ui.FigInterface;
import org.argouml.uml.diagram.static_structure.ui.FigLink;
import org.argouml.uml.diagram.static_structure.ui.StylePanelFigClass;
import org.argouml.uml.diagram.static_structure.ui.StylePanelFigInterface;
import org.argouml.uml.diagram.ui.FigAssociation;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigGeneralization;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.FigRealization;
import org.argouml.uml.diagram.ui.SPFigEdgeModelElement;
import org.argouml.uml.diagram.use_case.ui.FigActor;
import org.argouml.uml.diagram.use_case.ui.FigUseCase;
import org.argouml.uml.diagram.use_case.ui.StylePanelFigUseCase;
import org.tigris.gef.presentation.Fig;

/**
 * Provides support for changing the appearance of a diagram element.
 */
public class TabStyle
    extends TabSpawnable
    implements TabFigTarget, PropertyChangeListener, DelayedVChangeListener {
        
    private Logger _cat = Logger.getLogger(this.getClass());
    
    ////////////////////////////////////////////////////////////////
    // instance variables
    protected Fig _target;
    protected boolean _shouldBeEnabled = false;
    protected JPanel _blankPanel = new JPanel();
    protected Hashtable _panels = new Hashtable();
    protected JPanel _lastPanel = null;
    /**
     * The stylepanel shown by the tab style. 
     */
    protected StylePanel _stylePanel = null;
    protected String _panelClassBaseName = "";
    protected String _alternativeBase = "";

    private EventListenerList _listenerList = new EventListenerList();

    ////////////////////////////////////////////////////////////////
    // constructor
    public TabStyle(String tabName, String panelClassBase, String altBase) {
        super(tabName);
        _panelClassBaseName = panelClassBase;
        _alternativeBase = altBase;
        setLayout(new BorderLayout());
        //setFont(new Font("Dialog", Font.PLAIN, 10));
        initPanels();
    }

    public TabStyle() {
        this("tab.style", "style.StylePanel", "style.SP");
    }

    protected void initPanels() {
        StylePanelFigClass spfc = new StylePanelFigClass();
        StylePanelFigInterface spfi = new StylePanelFigInterface();
        StylePanelFigUseCase spfuc = new StylePanelFigUseCase();
        SPFigEdgeModelElement spfeme = new SPFigEdgeModelElement();
        StylePanelFig spf = new StylePanelFig();

        _panels.put(FigClass.class, spfc);
        _panels.put(FigUseCase.class, spfuc);
        _panels.put(FigNodeModelElement.class, spf);
        _panels.put(FigEdgeModelElement.class, spfeme);
        _panels.put(FigInterface.class, spfi);
        _panels.put(FigAssociation.class, spfeme);
        _panels.put(FigSimpleState.class, spf);
        _panels.put(FigTransition.class, spfeme);
        _panels.put(FigActor.class, spf);
        _panels.put(FigInstance.class, spf);
        _panels.put(FigLink.class, spfeme);
        _panels.put(FigGeneralization.class, spfeme);
        _panels.put(FigRealization.class, spfeme);
    }

    /** Adds a style panel to the internal list. This allows a plugin to
    *  add and register a new style panel at run-time. This property style will
    *  then be displayed in the detatils pane whenever an element of the given 
    *  metaclass is selected.
    *
    * @param c the metaclass whose details show be displayed in the property panel p
    * @param s an instance of the style panel for the metaclass m
    *
    */
    public void addPanel(Class c, StylePanel s) {
        _panels.put(c, s);
    }

    /**
     * Sets the target of the style tab. 
     *
     * @deprecated As of ArgoUml version 0.13.5,
     *             the visibility of this method will change in the future,
     *             replaced by {@link org.argouml.ui.targetmanager.TargetManager}.
     * @param Object the new target
     */
    public void setTarget(Object t) {
        if (_target != null)
            _target.removePropertyChangeListener(this);

        // the responsibility of determining if the given target is a correct one 
        // for this tab has been moved from the DetailsPane to the member tabs of th
        // detailpane. Reason for this is that the detailspane is configurable and
        // cannot know what's the correct target for some tab.
        if (!(t instanceof Fig)) {
            if (FacadeManager.getUmlFacade().isABase(t)) {
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
        if (_target != null)
            _target.addPropertyChangeListener(this);
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
        while (targetClass != null && _stylePanel == null) {
            _stylePanel = findPanelFor(targetClass);
            targetClass = targetClass.getSuperclass();
        }
        if (_stylePanel != null) {
            if (_stylePanel instanceof TargetListener) {
                // TargetManager now replaces the old functionality of setTarget
                removeTargetListener(_stylePanel);
                addTargetListener(_stylePanel);
            } else
                _stylePanel.setTarget(_target);
            add((JPanel) _stylePanel, BorderLayout.NORTH);
            _shouldBeEnabled = true;
            _lastPanel = (JPanel) _stylePanel;
        } else {
            add(_blankPanel, BorderLayout.NORTH);
            _shouldBeEnabled = false;
            _lastPanel = _blankPanel;
        }
        validate();
        repaint();
    }

    public void refresh() {
        setTarget(_target);
    }
      

    public StylePanel findPanelFor(Class targetClass) {
        TabFigTarget p = (TabFigTarget) _panels.get(targetClass);
        if (p == null) {
            Class panelClass = panelClassFor(targetClass);
            if (panelClass == null)
                return null;
            try {
                p = (TabFigTarget) panelClass.newInstance();
            } catch (IllegalAccessException ignore) {
                return null;
            } catch (InstantiationException ignore) {
                return null;
            }
            _panels.put(targetClass, p);
        } else
            _cat.debug("found style for " + targetClass.getName());
        return (StylePanel)p;
    }

    public Class panelClassFor(Class targetClass) {
        String pack = "org.argouml.ui";
        String base = getClassBaseName();
        String alt = getAlternativeClassBaseName();

        String targetClassName = targetClass.getName();
        int lastDot = targetClassName.lastIndexOf(".");
        if (lastDot > 0)
            targetClassName = targetClassName.substring(lastDot + 1);
        try {
            String panelClassName = pack + "." + base + targetClassName;
            Class cls = Class.forName(panelClassName);
            return cls;
        } catch (ClassNotFoundException ignore) {
        }
        try {
            String panelClassName = pack + "." + alt + targetClassName;
            Class cls = Class.forName(panelClassName);
            return cls;
        } catch (ClassNotFoundException ignore) {
        }
        return null;
    }

    protected String getClassBaseName() {
        return _panelClassBaseName;
    }

    protected String getAlternativeClassBaseName() {
        return _alternativeBase;
    }

    public Object getTarget() {
        return _target;
    }

    public boolean shouldBeEnabled(Object target) {

        if (!(target instanceof Fig)) {
            if (FacadeManager.getUmlFacade().isABase(target)) {
                Project p = ProjectManager.getManager().getCurrentProject();
                Collection col = p.findFigsForMember(target);
                if (col == null || col.isEmpty()) {
                    _shouldBeEnabled = false;
                    return false;
                } else {
                    target = col.iterator().next();
                }
            } else {
                _shouldBeEnabled = false;
                return false;
            }
        }

        _shouldBeEnabled = true;
        _stylePanel = null;

        Class targetClass = target.getClass();
        while (targetClass != null && _stylePanel == null) {
            _stylePanel = findPanelFor(targetClass);
            targetClass = targetClass.getSuperclass();
        }
        if (_stylePanel == null) {
            _shouldBeEnabled = false;
        }

        return _shouldBeEnabled;
    }

    ////////////////////////////////////////////////////////////////
    // PropertyChangeListener implementation

    public void propertyChange(PropertyChangeEvent pce) {
        DelayedChangeNotify delayedNotify = new DelayedChangeNotify(this, pce);
        SwingUtilities.invokeLater(delayedNotify);
    }

    public void delayedVetoableChange(PropertyChangeEvent pce) {
        if (_stylePanel != null)
            _stylePanel.refresh(pce);
    }

    /**
         * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
         */
    public void targetAdded(TargetEvent e) {
        // we can neglect this, the TabProps allways selects the first target
        // in a set of targets. The first target can only be 
        // changed in a targetRemoved or a TargetSet event
        fireTargetAdded(e);

    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        // how to handle empty target lists?
        // probably the TabProps should only show an empty pane in that case
        setTarget(e.getNewTargets()[0]);
        fireTargetRemoved(e);

    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTargets()[0]);
        fireTargetSet(e);

    }

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

} /* end class TabStyle */
