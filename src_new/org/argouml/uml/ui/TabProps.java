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

// 27 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Added MExtendImpl to
// the list of classes in run().

// 4 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Preloaded
// PropPanelDiagram for each of the standard diagrams in initPanels to
// eliminate messages when trying to find the panel.

package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.application.api.PluggablePropertyPanel;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoModuleEvent;
import org.argouml.application.events.ArgoModuleEventListener;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.swingext.Orientable;
import org.argouml.swingext.Orientation;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.NavigationListener;
import org.argouml.ui.TabSpawnable;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.collaboration.ui.UMLCollaborationDiagram;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.state.ui.PropPanelUMLStateDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.ui.PropPanelDiagram;
import org.argouml.uml.diagram.ui.PropPanelString;
import org.argouml.uml.diagram.ui.PropPanelUMLActivityDiagram;
import org.argouml.uml.diagram.ui.PropPanelUMLClassDiagram;
import org.argouml.uml.diagram.ui.PropPanelUMLCollaborationDiagram;
import org.argouml.uml.diagram.ui.PropPanelUMLDeploymentDiagram;
import org.argouml.uml.diagram.ui.PropPanelUMLSequenceDiagram;
import org.argouml.uml.diagram.ui.PropPanelUMLUseCaseDiagram;
import org.argouml.uml.diagram.use_case.ui.UMLUseCaseDiagram;
import org.argouml.uml.ui.behavior.activity_graphs.PropPanelActionState;
import org.argouml.uml.ui.behavior.collaborations.PropPanelAssociationEndRole;
import org.argouml.uml.ui.behavior.collaborations.PropPanelAssociationRole;
import org.argouml.uml.ui.behavior.collaborations.PropPanelClassifierRole;
import org.argouml.uml.ui.behavior.collaborations.PropPanelCollaboration;
import org.argouml.uml.ui.behavior.collaborations.PropPanelInteraction;
import org.argouml.uml.ui.behavior.collaborations.PropPanelMessage;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelCallAction;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelComponentInstance;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelInstance;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelLink;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelNodeInstance;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelObject;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelSignal;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelStimulus;
import org.argouml.uml.ui.behavior.state_machines.PropPanelCallEvent;
import org.argouml.uml.ui.behavior.state_machines.PropPanelCompositeState;
import org.argouml.uml.ui.behavior.state_machines.PropPanelFinalState;
import org.argouml.uml.ui.behavior.state_machines.PropPanelGuard;
import org.argouml.uml.ui.behavior.state_machines.PropPanelPseudostate;
import org.argouml.uml.ui.behavior.state_machines.PropPanelSimpleState;
import org.argouml.uml.ui.behavior.state_machines.PropPanelStateMachine;
import org.argouml.uml.ui.behavior.state_machines.PropPanelTransition;
import org.argouml.uml.ui.behavior.use_cases.PropPanelActor;
import org.argouml.uml.ui.behavior.use_cases.PropPanelExtend;
import org.argouml.uml.ui.behavior.use_cases.PropPanelExtensionPoint;
import org.argouml.uml.ui.behavior.use_cases.PropPanelInclude;
import org.argouml.uml.ui.behavior.use_cases.PropPanelUseCase;
import org.argouml.uml.ui.foundation.core.PropPanelAbstraction;
import org.argouml.uml.ui.foundation.core.PropPanelAssociation;
import org.argouml.uml.ui.foundation.core.PropPanelAssociationEnd;
import org.argouml.uml.ui.foundation.core.PropPanelAttribute;
import org.argouml.uml.ui.foundation.core.PropPanelClass;
import org.argouml.uml.ui.foundation.core.PropPanelComponent;
import org.argouml.uml.ui.foundation.core.PropPanelDataType;
import org.argouml.uml.ui.foundation.core.PropPanelDependency;
import org.argouml.uml.ui.foundation.core.PropPanelGeneralization;
import org.argouml.uml.ui.foundation.core.PropPanelInterface;
import org.argouml.uml.ui.foundation.core.PropPanelNode;
import org.argouml.uml.ui.foundation.core.PropPanelOperation;
import org.argouml.uml.ui.foundation.core.PropPanelParameter;
import org.argouml.uml.ui.foundation.extension_mechanisms.PropPanelStereotype;
import org.argouml.util.ConfigLoader;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;

import ru.novosoft.uml.behavior.activity_graphs.MActionStateImpl;
import ru.novosoft.uml.behavior.collaborations.MAssociationEndRoleImpl;
import ru.novosoft.uml.behavior.collaborations.MAssociationRoleImpl;
import ru.novosoft.uml.behavior.collaborations.MClassifierRoleImpl;
import ru.novosoft.uml.behavior.collaborations.MCollaborationImpl;
import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.behavior.collaborations.MMessageImpl;
import ru.novosoft.uml.behavior.common_behavior.MCallActionImpl;
import ru.novosoft.uml.behavior.common_behavior.MComponentInstanceImpl;
import ru.novosoft.uml.behavior.common_behavior.MInstanceImpl;
import ru.novosoft.uml.behavior.common_behavior.MLinkImpl;
import ru.novosoft.uml.behavior.common_behavior.MNodeInstanceImpl;
import ru.novosoft.uml.behavior.common_behavior.MObjectImpl;
import ru.novosoft.uml.behavior.common_behavior.MSignalImpl;
import ru.novosoft.uml.behavior.common_behavior.MStimulusImpl;
import ru.novosoft.uml.behavior.state_machines.MCallEventImpl;
import ru.novosoft.uml.behavior.state_machines.MCompositeStateImpl;
import ru.novosoft.uml.behavior.state_machines.MFinalStateImpl;
import ru.novosoft.uml.behavior.state_machines.MGuardImpl;
import ru.novosoft.uml.behavior.state_machines.MPseudostateImpl;
import ru.novosoft.uml.behavior.state_machines.MStateImpl;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.behavior.state_machines.MTransitionImpl;
import ru.novosoft.uml.behavior.use_cases.MActorImpl;
import ru.novosoft.uml.behavior.use_cases.MExtendImpl;
import ru.novosoft.uml.behavior.use_cases.MExtensionPointImpl;
import ru.novosoft.uml.behavior.use_cases.MIncludeImpl;
import ru.novosoft.uml.behavior.use_cases.MUseCaseImpl;
import ru.novosoft.uml.foundation.core.MAbstractionImpl;
import ru.novosoft.uml.foundation.core.MAssociationEndImpl;
import ru.novosoft.uml.foundation.core.MAssociationImpl;
import ru.novosoft.uml.foundation.core.MAttributeImpl;
import ru.novosoft.uml.foundation.core.MClassImpl;
import ru.novosoft.uml.foundation.core.MComponentImpl;
import ru.novosoft.uml.foundation.core.MDataTypeImpl;
import ru.novosoft.uml.foundation.core.MDependencyImpl;
import ru.novosoft.uml.foundation.core.MGeneralizationImpl;
import ru.novosoft.uml.foundation.core.MInterfaceImpl;
import ru.novosoft.uml.foundation.core.MNodeImpl;
import ru.novosoft.uml.foundation.core.MOperationImpl;
import ru.novosoft.uml.foundation.core.MParameterImpl;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotypeImpl;

/**
 * <p>
 * This is the tab on the details panel (DetailsPane) that holds the property
 * panel. On change of target, the property panel in TabProps is changed. 
 * </p>
 * <p>
 * With the introduction of the TargetManager, this class holds its original power
 * of controlling its target. The property panels (subclasses of PropPanel) for
 * which this class is the container are being registrated as TargetListeners in
 * the setTarget method of this class. They are not registrated with TargetManager
 * but with this class to prevent race-conditions while firing TargetEvents from
 * TargetManager.
 *</p>
 * @author unknown
 */
public class TabProps
    extends TabSpawnable
    implements TabModelTarget, NavigationListener, ArgoModuleEventListener {
    protected static Category cat = Category.getInstance(TabProps.class);
    ////////////////////////////////////////////////////////////////
    // instance variables
    protected boolean _shouldBeEnabled = false;
    protected JPanel _blankPanel = new JPanel();
    protected Hashtable _panels = new Hashtable();
    protected JPanel _lastPanel = null;
    protected String _panelClassBaseName = "";
    private LinkedList _navListeners = new LinkedList();

    /**
    * The list with targetlisteners, this are the property panels managed by TabProps
    * It should only contain one listener at a time.
    */
    private EventListenerList _listenerList = new EventListenerList();

    ////////////////////////////////////////////////////////////////
    // constructor
    public TabProps() {
        this("tab.properties", "ui.PropPanel");
    }

    public TabProps(String tabName, String panelClassBase) {
        super(tabName);
        setOrientation(ConfigLoader.getTabPropsOrientation());
        _panelClassBaseName = panelClassBase;
        setLayout(new BorderLayout());
        //setFont(new Font("Dialog", Font.PLAIN, 10));

        ArrayList list = Argo.getPlugins(PluggablePropertyPanel.class);
        ListIterator iterator = list.listIterator();
        while (iterator.hasNext()) {
            Object o = iterator.next();
            PluggablePropertyPanel ppp = (PluggablePropertyPanel) o;
            _panels.put(ppp.getClassForPanel(), ppp.getPropertyPanel());
        }

        ArgoEventPump.addListener(ArgoEventTypes.ANY_MODULE_EVENT, this);

        initPanels();
    }

    public void finalize() {
        ArgoEventPump.removeListener(ArgoEventTypes.ANY_MODULE_EVENT, this);
    }

    /*
     * Set the orientation of the property panel
     * @param orientation the new orientation for this preoprty panel
     */
    public void setOrientation(Orientation orientation) {
        super.setOrientation(orientation);
        Enumeration enum = _panels.elements();
        while (enum.hasMoreElements()) {
            Object o = enum.nextElement();
            if (o instanceof Orientable) {
                Orientable orientable = (Orientable) o;
                orientable.setOrientation(orientation);
            }
        }
    }

    /** Preload property panels that are commonly used within the first
     *  few seconds after the tool is launched. */
    protected void initPanels() {

        _panels.put(MClassImpl.class, new PropPanelClass());
        _panels.put(ArgoDiagram.class, new PropPanelDiagram());

        // Put all the diagram PropPanels here explicitly. They would eventually
        // pick up their superclass ArgoDiagram, but in the meantime
        // panelClassFor() would moan that they can't be found.

        // Note that state digrams do actually have a diagram property panel!

        _panels.put(
            UMLActivityDiagram.class,
            new PropPanelUMLActivityDiagram());
        _panels.put(UMLClassDiagram.class, new PropPanelUMLClassDiagram());
        _panels.put(
            UMLCollaborationDiagram.class,
            new PropPanelUMLCollaborationDiagram());
        _panels.put(
            UMLDeploymentDiagram.class,
            new PropPanelUMLDeploymentDiagram());
        _panels.put(
            UMLSequenceDiagram.class,
            new PropPanelUMLSequenceDiagram());
        _panels.put(UMLStateDiagram.class, new PropPanelUMLStateDiagram());
        _panels.put(UMLUseCaseDiagram.class, new PropPanelUMLUseCaseDiagram());

        // FigText has no owner, so we do it directly
        _panels.put(FigText.class, new PropPanelString());
        // now a plugin
        // _panels.put(MModelImpl.class, new PropPanelModel());
        _panels.put(MUseCaseImpl.class, new PropPanelUseCase());
        //important: MStateImpl corresponds to PropPanelSimpleState not to PropPanelState!!
        //otherwise, spawing will not ne successful!!
        _panels.put(MStateImpl.class, new PropPanelSimpleState());

        org.argouml.application.Main.addPostLoadAction(
            new InitPanelsLater(_panels, this, orientation));
    }

    /** Adds a property panel to the internal list. This allows a plugin to
     *  add a register a new property panel an run-time. This property panel will then
     *  be displayed in the detatils pane whenever an element of the given metaclass is
     *  selected.
     *
     * @param c the metaclass whose details show be displayed in the property panel p
     * @param p an instance of the property panel for the metaclass m
     *
     */
    public void addPanel(Class c, PropPanel p) {
        _panels.put(c, p);
    }

    public void addNavigationListener(NavigationListener navListener) {
        _navListeners.add(navListener);
    }

    public void removeNavigationListener(NavigationListener navListener) {
        _navListeners.remove(navListener);
    }

    /**    Called by a user interface element when a request to
     *    navigate to a model element has been received.
     */
    public void navigateTo(Object element) {
        Iterator iter = _navListeners.iterator();
        while (iter.hasNext()) {
            ((NavigationListener) iter.next()).navigateTo(element);
        }
    }

    /**   Called by a user interface element when a request to
     *   open a model element in a new window has been recieved.
     */
    public void open(Object element) {
        Iterator iter = _navListeners.iterator();
        while (iter.hasNext()) {
            ((NavigationListener) iter.next()).open(element);
        }
    }

    public boolean navigateBack(boolean attempt) {
        boolean navigated = false;
        Iterator iter = _navListeners.iterator();
        while (iter.hasNext()) {
            navigated =
                ((NavigationListener) iter.next()).navigateBack(attempt);
            if (navigated)
                attempt = false;
        }
        return navigated;
    }

    public boolean navigateForward(boolean attempt) {
        boolean navigated = false;
        Iterator iter = _navListeners.iterator();
        while (iter.hasNext()) {
            navigated =
                ((NavigationListener) iter.next()).navigateForward(attempt);
            if (navigated)
                attempt = false;
        }
        return navigated;
    }

    public boolean isNavigateForwardEnabled() {
        boolean enabled = false;
        Iterator iter = _navListeners.iterator();
        while (iter.hasNext() && !enabled) {
            enabled =
                ((NavigationListener) iter.next()).isNavigateForwardEnabled();
        }
        return enabled;
    }

    public boolean isNavigateBackEnabled() {
        boolean enabled = false;
        Iterator iter = _navListeners.iterator();
        while (iter.hasNext() && !enabled) {
            enabled =
                ((NavigationListener) iter.next()).isNavigateBackEnabled();
        }
        return enabled;
    }

    ////////////////////////////////////////////////////////////////
    // accessors
    /**
     * Sets the target of the property panel. The given target t may either be a 
     * Diagram or a modelelement. If the target given is a Fig, a check is made if the fig has an owning modelelement and occurs on
     * the current diagram. If so, that modelelement is the target.
     * @deprecated in a feature release, this will become non-public. It is replaced
     * by the TargetManager
     */
    public void setTarget(Object t) {
        // targets ought to be modelelements or diagrams 
        t = (t instanceof Fig) ? ((Fig) t).getOwner() : t;
        if (!(ModelFacade.isABase(t) || t instanceof ArgoDiagram))
            return;

        if (_lastPanel != null) {
            remove(_lastPanel);
            if (_lastPanel instanceof TargetListener)
                removeTargetListener((TargetListener) _lastPanel);
        }
        if (t == null) {
            add(_blankPanel, BorderLayout.CENTER);
            _shouldBeEnabled = false;
            _lastPanel = _blankPanel;
        } else {
            _shouldBeEnabled = true;
            TabModelTarget newPanel = null;
            Class targetClass = t.getClass();
            while (newPanel == null) {
                newPanel = findPanelFor(targetClass);
                targetClass = targetClass.getSuperclass();
                if (targetClass == java.lang.Object.class)
                    break;
            }
            if (newPanel != null) {
                addTargetListener(newPanel);
                // TODO remove next call as soon as possible
                newPanel.setTarget(t);
            }
            if (newPanel instanceof JPanel) {
                add((JPanel) newPanel, BorderLayout.CENTER);
                _shouldBeEnabled = true;
                _lastPanel = (JPanel) newPanel;
            } else {
                add(_blankPanel, BorderLayout.CENTER);
                _shouldBeEnabled = false;
                _lastPanel = _blankPanel;
            }
            validate();
            repaint();
        }
    }

    public void refresh() {
        setTarget(TargetManager.getInstance().getTarget());
    }

    public TabModelTarget findPanelFor(Class targetClass) {
        TabModelTarget p = (TabModelTarget) _panels.get(targetClass);
        cat.debug("Getting prop panel for:" + targetClass + ", found" + p);
        if (p == null) {
            Class panelClass = panelClassFor(targetClass);
            if (panelClass == null)
                return null;
            try {
                p = (TabModelTarget) panelClass.newInstance();
                // moved next line inside try block to avoid filling the hashmap with
                // bogus values. 
                _panels.put(targetClass, p);
            }
            // doubtfull if the next ones must be ignored.
            catch (IllegalAccessException ignore) {
                return null;
            } catch (InstantiationException ignore) {
                return null;
            }

        } else
            cat.debug("found props for " + targetClass.getName());
        return p;
    }

    public Class panelClassFor(Class targetClass) {
        String panelClassName = "";
        String pack = "org.argouml.uml";
        String base = "";

        String targetClassName = targetClass.getName();
        int lastDot = targetClassName.lastIndexOf(".");

        //remove "ru.novosoft.uml"
        if (lastDot > 0)
            base = targetClassName.substring(16, lastDot + 1);
        else
            base = targetClassName.substring(16);

        if (lastDot > 0)
            targetClassName = targetClassName.substring(lastDot + 1);

        if (targetClassName.startsWith("M"))
            targetClassName = targetClassName.substring(1); //remove M
        if (targetClassName.endsWith("Impl"))
            targetClassName =
                targetClassName.substring(0, targetClassName.length() - 4);
        //remove Impl

        // This doesn't work for panel property tabs - they are being put in the
        // wrong place. Really we should have defined these are preloaded them
        // along with ArgoDiagram in initPanels above.

        try {
            panelClassName =
                pack + ".ui." + base + "PropPanel" + targetClassName;
            return Class.forName(panelClassName);
        } catch (ClassNotFoundException ignore) {
            cat.error(
                "Class " + panelClassName + " for Panel not found!",
                ignore);
        }
        return null;
    }

    protected String getClassBaseName() {
        return _panelClassBaseName;
    }

    /**
     * Returns the current target.
     * @deprecated use TargetManager.getInstance().getTarget() instead
     */
    public Object getTarget() {
        return TargetManager.getInstance().getModelTarget();
    }

    /**
     * Determines if the property panel should be enabled. Returns true if it
     * should be enabled. The property panel should allways be enabled if the
     * target is an instance of a modelelement or an argodiagram. If the target given
     * is a Fig, a check is made if the fig has an owning modelelement and occurs on
     * the current diagram. If so, that modelelement is the target.
     * @see org.argouml.ui.TabTarget#shouldBeEnabled(Object)
     */
    public boolean shouldBeEnabled(Object target) {
        ArgoDiagram diagram =
            ProjectManager.getManager().getCurrentProject().getActiveDiagram();
        target = (target instanceof Fig) ? ((Fig) target).getOwner() : target;
        if (ModelFacade.isADiagram(target) || ModelFacade.isABase(target)) {
            _shouldBeEnabled = true;
        } else {
            _shouldBeEnabled = false;
        }

        return _shouldBeEnabled;
    }

    public void moduleLoaded(ArgoModuleEvent event) {
        if (event.getSource() instanceof PluggablePropertyPanel) {
            PluggablePropertyPanel p =
                (PluggablePropertyPanel) event.getSource();
            _panels.put(p.getClassForPanel(), p.getPropertyPanel());
            if (p.getPropertyPanel() instanceof UMLUserInterfaceContainer) {
                (
                    (UMLUserInterfaceContainer) p
                        .getPropertyPanel())
                        .addNavigationListener(
                    this);
            }

        }
    }
    public void moduleUnloaded(ArgoModuleEvent event) {
    }
    public void moduleEnabled(ArgoModuleEvent event) {
    }
    public void moduleDisabled(ArgoModuleEvent event) {
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
//      Guaranteed to return a non-null array
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

} /* end class TabProps */

class InitPanelsLater implements Runnable {
    protected static Category cat = Category.getInstance(InitPanelsLater.class);
    private Hashtable _panels = null;
    private TabProps _tabProps;
    private Orientation _orientation;
    public InitPanelsLater(
        Hashtable p,
        TabProps tabProps,
        Orientation orientation) {
        _panels = p;
        _tabProps = tabProps;
        _orientation = orientation;
    }

    /** Load commonly used property panels, but not those that are
     *  commonly used within a few seconds of the tool being launched. */
    public void run() {
        //   // preload commonly used property panels

        //fill the Hashtable. alphabetical order please... ;-)
        try {
            _panels.put(MActionStateImpl.class, new PropPanelActionState());
            _panels.put(MActorImpl.class, new PropPanelActor());
            _panels.put(MAssociationImpl.class, new PropPanelAssociation());
            _panels.put(
                MAssociationRoleImpl.class,
                new PropPanelAssociationRole());
            _panels.put(MAttributeImpl.class, new PropPanelAttribute());
            // _panels.put(MClassImpl.class, new PropPanelClass());
            _panels.put(MCollaborationImpl.class, new PropPanelCollaboration());
            _panels.put(
                MClassifierRoleImpl.class,
                new PropPanelClassifierRole());
            _panels.put(MDependencyImpl.class, new PropPanelDependency());
            _panels.put(MExtendImpl.class, new PropPanelExtend());
            _panels.put(
                MExtensionPointImpl.class,
                new PropPanelExtensionPoint());
            //_panels.put(ArgoDiagram.class, new PropPanelDiagram());
            _panels.put(
                MGeneralizationImpl.class,
                new PropPanelGeneralization());
            _panels.put(MIncludeImpl.class, new PropPanelInclude());
            _panels.put(MInstanceImpl.class, new PropPanelInstance());
            _panels.put(
                MComponentInstanceImpl.class,
                new PropPanelComponentInstance());
            _panels.put(MComponentImpl.class, new PropPanelComponent());
            _panels.put(MNodeInstanceImpl.class, new PropPanelNodeInstance());
            _panels.put(MNodeImpl.class, new PropPanelNode());
            _panels.put(MObjectImpl.class, new PropPanelObject());
            _panels.put(MInstanceImpl.class, new PropPanelInstance());
            _panels.put(MInterfaceImpl.class, new PropPanelInterface());
            _panels.put(MLinkImpl.class, new PropPanelLink());
            _panels.put(MStimulusImpl.class, new PropPanelStimulus());
            _panels.put(MMessageImpl.class, new PropPanelMessage());
            //_panels.put(MModelImpl.class, new PropPanelModel());

            // how are Notes handled? Toby, nsuml
            //_panels.put(MNoteImpl.class, new PropPanelNote());
            _panels.put(MOperationImpl.class, new PropPanelOperation());
            _panels.put(MPseudostateImpl.class, new PropPanelPseudostate());
            //    _panels.put(Realization.class, new PropPanelRealization());
            // Realization in nsuml!!!
            _panels.put(UMLStateDiagram.class, new PropPanelUMLStateDiagram());
            _panels.put(MStateImpl.class, new PropPanelSimpleState());
            _panels.put(
                MCompositeStateImpl.class,
                new PropPanelCompositeState());
            _panels.put(MFinalStateImpl.class, new PropPanelFinalState());
            _panels.put(String.class, new PropPanelString());
            _panels.put(MTransitionImpl.class, new PropPanelTransition());
            //_panels.put(MUseCaseImpl.class, new PropPanelUseCase());
            _panels.put(
                MAssociationEndImpl.class,
                new PropPanelAssociationEnd());
            _panels.put(
                MAssociationEndRoleImpl.class,
                new PropPanelAssociationEndRole());
            _panels.put(MParameterImpl.class, new PropPanelParameter());
            _panels.put(MSignalImpl.class, new PropPanelSignal());
            _panels.put(MStereotypeImpl.class, new PropPanelStereotype());
            _panels.put(MDataTypeImpl.class, new PropPanelDataType());
            // now a plugin
            // _panels.put(MPackageImpl.class, new PropPanelPackage());
            _panels.put(MAbstractionImpl.class, new PropPanelAbstraction());
            _panels.put(MGuardImpl.class, new PropPanelGuard());
            _panels.put(MCallEventImpl.class, new PropPanelCallEvent());
            _panels.put(MCallActionImpl.class, new PropPanelCallAction());
            _panels.put(MInteraction.class, new PropPanelInteraction());
            _panels.put(MStateMachine.class, new PropPanelStateMachine());
        } catch (Exception e) {
            cat.error("Exception in InitPanelsLater.run()", e);

        }

        Iterator iter = _panels.values().iterator();
        Object panel;
        while (iter.hasNext()) {
            panel = iter.next();
            if (panel instanceof UMLUserInterfaceContainer) {
                ((UMLUserInterfaceContainer) panel).addNavigationListener(
                    _tabProps);
            }
        }

        Argo.log.info("done preloading Property Panels");
    }
} /* end class InitPanelsLater */
