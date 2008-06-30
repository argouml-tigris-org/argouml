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

package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.argouml.application.api.AbstractArgoJPanel;
import org.argouml.cognitive.Critic;
import org.argouml.model.Model;
import org.argouml.swingext.UpArrowIcon;
import org.argouml.ui.TabModelTarget;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.ui.PropPanelString;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;
import org.tigris.swidgets.Horizontal;
import org.tigris.swidgets.Orientable;
import org.tigris.swidgets.Orientation;

/**
 * This is the tab on the details panel (DetailsPane) that holds the property
 * panel. On change of target, the property panel in TabProps is changed.
 * <p>
 * With the introduction of the TargetManager, this class holds its original
 * power of controlling its target. The property panels (subclasses of
 * PropPanel) for which this class is the container are being registered as
 * TargetListeners in the setTarget method of this class. They are not
 * registered with TargetManager but with this class to prevent race-conditions
 * while firing TargetEvents from TargetManager.
 */
public class TabProps
    extends AbstractArgoJPanel
    implements TabModelTarget {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(TabProps.class);

    private boolean shouldBeEnabled = false;
    private JPanel blankPanel = new JPanel();
    private Hashtable<Class, TabModelTarget> panels = 
        new Hashtable<Class, TabModelTarget>();
    private JPanel lastPanel;
    private String panelClassBaseName = "";

    private Object target;

    /**
     * The list with targetlisteners, these are the property panels
     * managed by TabProps.
     * It should only contain one listener at a time.
     */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * The constructor.
     *
     */
    public TabProps() {
        this("tab.properties", "ui.PropPanel");
    }

    /**
     * The constructor.
     *
     * @param tabName the name of the tab
     * @param panelClassBase the panel class base
     */
    public TabProps(String tabName, String panelClassBase) {
        super(tabName);
        setIcon(new UpArrowIcon());
        // TODO: This should be managed by the DetailsPane TargetListener - tfm
        // remove the following line
        TargetManager.getInstance().addTargetListener(this);
        setOrientation(Horizontal.getInstance());
        panelClassBaseName = panelClassBase;
        setLayout(new BorderLayout());
    }

    /**
     * Set the orientation of the property panel.
     *
     * @param orientation the new orientation for this property panel
     *
     * @see org.tigris.swidgets.Orientable#setOrientation(org.tigris.swidgets.Orientation)
     */
    @Override
    public void setOrientation(Orientation orientation) {
        super.setOrientation(orientation);
        Enumeration pps = panels.elements();
        while (pps.hasMoreElements()) {
            Object o = pps.nextElement();
            if (o instanceof Orientable) {
                Orientable orientable = (Orientable) o;
                orientable.setOrientation(orientation);
            }
        }
    }

    /**
     * Adds a property panel to the internal list. This allows a plugin to
     * add / register a new property panel at run-time.
     * This property panel will then
     * be displayed in the detatils pane whenever an element
     * of the given metaclass is selected.
     *
     * @param clazz the metaclass whose details show be displayed
     *          in the property panel p
     * @param panel an instance of the property panel for the metaclass m
     *
     */
    public void addPanel(Class clazz, PropPanel panel) {
        panels.put(clazz, panel);
    }


    ////////////////////////////////////////////////////////////////
    // accessors
    /**
     * Sets the target of the property panel. The given target t
     * may either be a Diagram or a modelelement. If the target
     * given is a Fig, a check is made if the fig has an owning
     * modelelement and occurs on the current diagram.
     * If so, that modelelement is the target.
     *
     * @deprecated As of ArgoUml version 0.13.5,
     *         the visibility of this method will change in the future,
     *         replaced by {@link org.argouml.ui.targetmanager.TargetManager}.
     *         TODO: MVW: I think this should not be deprecated.
     *
     * @param target the new target
     * @see org.argouml.ui.TabTarget#setTarget(java.lang.Object)
     */
    @Deprecated
    public void setTarget(Object target) {
        // targets ought to be UML objects or diagrams
        target = (target instanceof Fig) ? ((Fig) target).getOwner() : target;
        if (!(target == null || Model.getFacade().isAUMLElement(target) 
                || target instanceof ArgoDiagram
                // TODO Improve extensibility of this!
                || target instanceof Critic)) {
            return;
        }

        if (lastPanel != null) {
            remove(lastPanel);
            if (lastPanel instanceof TargetListener) {
                removeTargetListener((TargetListener) lastPanel);
            }
        }
  
        // TODO: No need to do anything if we're not visible      
//        if (!isVisible()) {
//            return;
//        }
        
        this.target = target;
        if (target == null) {
            add(blankPanel, BorderLayout.CENTER);
            shouldBeEnabled = false;
            lastPanel = blankPanel;
        } else {
            shouldBeEnabled = true;
            TabModelTarget newPanel = null;
            newPanel = findPanelFor(target);
            if (newPanel != null) {
                addTargetListener(newPanel);
            }
            if (newPanel instanceof JPanel) {
                add((JPanel) newPanel, BorderLayout.CENTER);
                shouldBeEnabled = true;
                lastPanel = (JPanel) newPanel;
            } else {
                add(blankPanel, BorderLayout.CENTER);
                shouldBeEnabled = false;
                lastPanel = blankPanel;
            }

        }
    }

    /*
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {
        setTarget(TargetManager.getInstance().getTarget());
    }

    /**
     * Find the correct properties panel for the target.
     *
     * @param trgt the target class
     * @return the tab panel
     */
    private TabModelTarget findPanelFor(Object trgt) {
        // TODO: No test coverage for this or createPropPanel? - tfm
        
        /* 1st attempt: get a panel that we created before: */
        TabModelTarget panel = panels.get(trgt.getClass());
        if (panel != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Getting prop panel for: " + trgt.getClass().getName()
                        + ", " + "found (in cache?) " + panel);
            }
            return panel;
        }

        /* 2nd attempt: If we didn't find the panel then
         * use the factory to create a new one
	 */
        panel = createPropPanel(trgt);
        if (panel != null) {
            LOG.debug("Factory created " + panel.getClass().getName()
                    + " for " + trgt.getClass().getName());
            panels.put(trgt.getClass(), panel);
            if (panel instanceof PropPanel) {
        	((PropPanel) panel).buildToolbar();
            }
            return panel;
        }

        LOG.error("Failed to create a prop panel for : " + trgt);
        return null;
    }

    /**
     * A factory method to create a PropPanel for a particular target (Diagram,
     * UML Element or GEF Fig).
     *
     * @param targetObject the target object
     * @return A new prop panel to display any model element of the given type
     */
    private TabModelTarget createPropPanel(Object targetObject) {
	TabModelTarget propPanel = null;

	for (PropPanelFactory factory
	        : PropPanelFactoryManager.getFactories()) {
	    propPanel = factory.createPropPanel(targetObject);
	    if (propPanel != null) return propPanel;
	}        
        
	/* This does not work (anymore/yet?), 
	 * since we never have a FigText here: */
	if (targetObject instanceof FigText) {
            propPanel = new PropPanelString();
        }

        if (propPanel instanceof Orientable) {
            ((Orientable) propPanel).setOrientation(getOrientation());
        }
        
        if (propPanel instanceof PropPanel) {
            ((PropPanel) propPanel).setOrientation(getOrientation());
            ((PropPanel) propPanel).buildToolbar();
        }

        return propPanel;
    }

    /**
     * @return the name
     */
    protected String getClassBaseName() {
        return panelClassBaseName;
    }

    /**
     * Returns the current target.
     * @deprecated As of ArgoUml version 0.13.5,
     * the visibility of this method will change in the future, replaced by
     * {@link org.argouml.ui.targetmanager.TargetManager#getTarget()
     * TargetManager.getInstance().getTarget()}.
     * TODO: MVW: I think this should not be deprecated.
     *
     * @return the target
     * @see org.argouml.ui.TabTarget#getTarget()
     */
    @Deprecated
    public Object getTarget() {
        return target;
    }

    /**
     * Determines if the property panel should be enabled.
     * The property panel should always be enabled if the
     * target is an instance of a modelelement or an argodiagram.
     * If the target given is a Fig, a check is made if the fig
     * has an owning modelelement and occurs on
     * the current diagram. If so, that modelelement is the target.
     * @param target the target
     * @return true if property panel should be enabled
     * @see org.argouml.ui.TabTarget#shouldBeEnabled(Object)
     */
    public boolean shouldBeEnabled(Object target) {
        if (target instanceof Fig) {
            target = ((Fig) target).getOwner();
        }
        
        // TODO: this should be more extensible... may be only 
        // "findPanelFor(target)" if there is a panel why not show it?
        return ((target instanceof Diagram || Model.getFacade().isAUMLElement(
                target)) || target instanceof Critic
                && findPanelFor(target) != null);
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent targetEvent) {
        setTarget(TargetManager.getInstance().getSingleTarget());
        fireTargetAdded(targetEvent);
        if (listenerList.getListenerCount() > 0) {
            validate();
            repaint();
        }

    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent targetEvent) {
        setTarget(TargetManager.getInstance().getSingleTarget());
        fireTargetRemoved(targetEvent);
        validate();
        repaint();
    }

    /*
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent targetEvent) {
        setTarget(TargetManager.getInstance().getSingleTarget());
        fireTargetSet(targetEvent);
        validate();
        repaint();
    }

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

    private void fireTargetSet(TargetEvent targetEvent) {
        //      Guaranteed to return a non-null array
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

} /* end class TabProps */
