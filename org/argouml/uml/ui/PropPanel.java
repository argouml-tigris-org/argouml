// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// File: PropPanel.java
// Classes: PropPanel
// Original Author:
// $Id$

// 23 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Added the third party
// event listener.

// 25 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Reworked
// setNameEventListener to use third party event listeners, and removed the
// promiscuous listener stuff.

package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.EventListener;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.metal.MetalLookAndFeel;
//
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.swingext.LabelledLayout;
import org.argouml.swingext.Orientation;
import org.argouml.swingext.Vertical;
import org.argouml.ui.TabSpawnable;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.ui.targetmanager.TargettableModelView;
import org.argouml.uml.Profile;
import org.argouml.uml.ProfileJava;

import org.tigris.gef.presentation.Fig;
import org.tigris.toolbar.ToolBar;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

/**
 *   <p>This abstract class provides the basic layout and event dispatching
 *   support for all Property Panels.</p>
 *
 *   <p>The property panel is {@link org.argouml.swingext.LabelledLayout layed out}
 *   as a number (specified in the constructor) of equally sized panels
 *   that split the available space.  Each panel has a column of
 *   "captions" and matching column of "fields" which are laid out
 *   indepently from the other panels.
 *   </p>
 *  
 *   <p>The Properties panels for UML Model Elements are structured in an
 *   inheritance hierarchy that matches the UML 1.3 metamodel.
 */
abstract public class PropPanel
    extends TabSpawnable
    implements TabModelTarget, MElementListener, UMLUserInterfaceContainer 
{
    ////////////////////////////////////////////////////////////////
    // instance vars
    private Object _target;
    private MModelElement _modelElement;
    private static Profile _profile;

    private ResourceBundle _bundle = null;

    private Vector _panels = new Vector();

    private int lastRow;
    
    private EventListenerList _listenerList;

    /**
     * <p>The metaclass/property pairs for the third party listener (if we have
     *   set one up. We use this when creating a new listener on target
     *   change.</p>
     */

    private Vector _targetList = null;
    private JPanel center;

    protected JToolBar buttonPanel;
    private JPanel buttonPanelWithFlowLayout = new JPanel();

    private JLabel _titleLabel;

    private JPanel captionPanel = new JPanel();

    protected static ImageIcon _deleteIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("RedDelete");
    protected static ImageIcon _navUpIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("NavigateUp");

    protected Font smallFont = MetalLookAndFeel.getSubTextFont();

    /**
     * Construct new PropPanel using LabelledLayout
     * @param icon The icon to display for the panel
     * @param title The title of the panel
     * @param sectionCount the number of sections in the proppanel
     */
    public PropPanel(String title, ImageIcon icon, Orientation orientation) {
        super(title);
        setOrientation(orientation);
        buttonPanel = new ToolBar();
        buttonPanel.putClientProperty("JToolBar.isRollover",  Boolean.TRUE);
        buttonPanel.setFloatable(false);
        //buttonPanel.putClientProperty("JToolBar.isRollover",  Boolean.TRUE);
        
        setLayout(new LabelledLayout(orientation == Vertical.getInstance()));

        if (icon != null) {
            _titleLabel = new JLabel(localize(title), icon, SwingConstants.LEFT);
        }
        else {
            _titleLabel = new JLabel(localize(title));
        }
        //buttonPanel = new JPanel(new SerialLayout());
        _titleLabel.setLabelFor(buttonPanel);
        add(_titleLabel);
        add(buttonPanel);
    }
    
    /**
     * Constructs a new Proppanel without an icon. If there is an icon it's
     * updated at runtime via settarget.
     * @param title
     * @param orientation
     */
    public PropPanel(String title, Orientation orientation) {
        this (title, null, orientation);
    }

    /**
     * Set the orientation of the panel
     * @param orientation
     */
    public void setOrientation(Orientation orientation) {
        super.setOrientation(orientation);
    }

    /**
     * Add a component with the specified label
     * @param label the label for the component
     * @param component the component
     */
    public JLabel addField(String label, Component component) {
        JLabel jlabel = new JLabel(localize(label));
        jlabel.setLabelFor(component);
        add(jlabel);
        add(component);
        return jlabel;
    }

    /**
     * Add a component with the specified label positioned after another component
     * @param label the label for the component
     * @param component the component
     */
    public JLabel addFieldAfter(String label, Component component, Component afterComponent) {
        int nComponent = this.getComponentCount();
        for (int i = 0; i < nComponent; ++i) {
            if (getComponent(i) == afterComponent) {
                JLabel jlabel = new JLabel(localize(label));
                jlabel.setLabelFor(component);
                add(jlabel, ++i);
                add(component, ++i);
                return jlabel;
            }
        }
        throw new IllegalArgumentException("Component not found");
    }

    /**
     * Add a component with the specified label positioned before another component
     * @param label the label for the component
     * @param component the component
     * @param beforeComponent the component
     */
    public JLabel addFieldBefore(String label, Component component, Component beforeComponent) {
        int nComponent = this.getComponentCount();
        for (int i = 0; i < nComponent; ++i) {
            if (getComponent(i) == beforeComponent) {
                JLabel jlabel = new JLabel(localize(label));
                jlabel.setLabelFor(component);
                add(jlabel, i);
                add(component, ++i);
                return jlabel;
            }
        }
        throw new IllegalArgumentException("Component not found");
    }

    /**
     *   Adds a component to the fields of the specified panel
     *     and sets the background and color to indicate
     *     the field is a link.
     *   @param label the required string label
     *   @param component Component to be added
     */
    public final void addLinkField(String label, JComponent component) {
        component.setBackground(getBackground());
        component.setForeground(Color.blue);
        addField(label, component);
    }

    final public String localize(String key) {
        String localized = key;
        if (_bundle == null) {
            _bundle = getResourceBundle();
        }
        if (_bundle != null) {
            try {
                localized = _bundle.getString(key);
            } catch (MissingResourceException e) {
            }
            if (localized == null) {
                localized = key;
            }
        }
        return localized;
    }

    final protected void addSeperator() {
        add(LabelledLayout.getSeperator());
    }

    public ResourceBundle getResourceBundle() {
        return null;
    }

    public Profile getProfile() {
        if (_profile == null) {
            _profile = ProfileJava.getInstance();
        }
        return _profile;
    }

    /**
       This method (and addMElementListener) can be overriden if the
       prop panel wants to monitor additional objects.
    
       @param target target of prop panel
    
    */
    protected void removeMElementListener(MBase target) {
        UmlModelEventPump.getPump().removeModelEventListener(this, target);
    }

    /**
       This method (and removeMElementListener) can be overriden if the
       prop panel wants to monitor additional objects.  This method
       is public only since it is called from a Runnable object.
    
       @param target target of prop panel
    */
    public void addMElementListener(MBase target) {
        UmlModelEventPump.getPump().addModelEventListener(this, target);
    }

    /**
     * <p>Set the target to be associated with a particular property panel.</p>
     *
     * <p>This involves resetting the third party listeners.</p>
     * @deprecated As Of Argouml version 0.13.5,
     *             This will change visibility from release 0.16
     * @param t  The object to be set as a target.
     */

    public void setTarget(Object t) {
        t = (t instanceof Fig) ? ((Fig) t).getOwner() : t;

        // If the target has changed notify the third party listener if it
        // exists and dispatch a new NSUML element listener to
        // ourself. Otherwise dispatch a target reasserted to ourself.
        Runnable dispatch = null;
        if (t != _target) {

            // Set up the target and its model element variant.

            _target = t;
            _modelElement = null;
            if (_listenerList == null) {
                _listenerList = registrateTargetListeners(this); 
            }

            if (ModelFacade.isAModelElement(_target)) {
                _modelElement = (MModelElement) _target;
            }

            // This will add a new MElement listener after update is complete

            dispatch = new UMLChangeDispatch(this, UMLChangeDispatch.TARGET_CHANGED_ADD);
           
        } 
        else {
            dispatch = new UMLChangeDispatch(this, UMLChangeDispatch.TARGET_REASSERTED);  
                    
        }
        SwingUtilities.invokeLater(dispatch);
        
        // update the titleLabel 
        if (_titleLabel != null) {
            Icon icon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIcon(t);
            if (icon != null)
                _titleLabel.setIcon(icon);
        }  
    }
    
    /**
     * Builds a eventlistenerlist of all targetlisteners that are part of this
     * container and its children.
     * @param container the container to search for targetlisteners
     * @return an EventListenerList with all TargetListeners on this container and
     * its children.
     */
    private EventListenerList registrateTargetListeners(Container container) {
        Component[] components = container.getComponents();
        EventListenerList list = new EventListenerList();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof TargetListener) {
                list.add(TargetListener.class, (TargetListener) components[i]);
            } 
            if (components[i] instanceof TargettableModelView) {
                list.add(TargetListener.class, ((TargettableModelView) components[i]).getTargettableModel());
            }
            if (components[i] instanceof Container) {
                EventListenerList list2 = registrateTargetListeners((Container) components[i]);
                Object[] objects = list2.getListenerList();
                for (int j = 1; j < objects.length; j += 2) {
                    list.add(TargetListener.class, (EventListener) objects[j]);
                }
            }
        }
        return list;
    }

    public final Object getTarget() {
        return _target;
    }

    public final MModelElement getModelElement() {
        return _modelElement;
    }

    public void refresh() {
        SwingUtilities.invokeLater(new UMLChangeDispatch(this, 0));
    }

    public boolean shouldBeEnabled(Object target) {
        target = (target instanceof Fig) ? ((Fig) target).getOwner() : target;
        return ModelFacade.isAModelElement(target);
    }

    public void propertySet(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this, 0);
        dispatch.propertySet(mee);
        SwingUtilities.invokeLater(dispatch);
    }

    public void listRoleItemSet(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this, 0);
        dispatch.listRoleItemSet(mee);
        SwingUtilities.invokeLater(dispatch);
    }

    public void recovered(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this, 0);
        dispatch.recovered(mee);
        SwingUtilities.invokeLater(dispatch);
    }

    public void removed(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this, 0);
        dispatch.removed(mee);
        SwingUtilities.invokeLater(dispatch);
    }

    public void roleAdded(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this, 0);
        dispatch.roleAdded(mee);
        SwingUtilities.invokeLater(dispatch);
    }

    public void roleRemoved(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this, 0);
        dispatch.roleRemoved(mee);
        SwingUtilities.invokeLater(dispatch);
    }

    /**
     *   This method can be overriden in derived Panels where the
     *   appropriate namespace for display may not be the same as
     *   the namespace of the target
     */
    protected Object getDisplayNamespace() {
        Object ns = null;
        Object target = getTarget();
        if (ModelFacade.isAModelElement(target)) {
            ns = ModelFacade.getNamespace(target);
        }
        return ns;
    }

    public String formatElement(MModelElement element) {
        return getProfile().formatElement(element, (MNamespace)getDisplayNamespace());
    }

    public String formatNamespace(MNamespace namespace) {
        return getProfile().formatElement(namespace, null);
    }

    public String formatCollection(Iterator iter) {
        Object namespace = getDisplayNamespace();
        return getProfile().formatCollection(iter, namespace);
    }

    /**
     * @deprecated As of ArgoUml version 0.13.5,replaced by
     *             {@link org.argouml.ui.targetmanager.TargetManager#setTarget(Object) TargetManager.getInstance().setTarget(Object target)}
     */
    public void navigateTo(Object element) {
        TargetManager.getInstance().setTarget(element);
    }
    
   

    /**
     * @deprecated As of ArgoUml version 0.13.5,replaced by
     *             {@link org.argouml.ui.targetmanager.TargetManager#navigateBackward() TargetManager.getInstance().navigateBackward()}
     */
    public boolean navigateBack(boolean attempt) {
	TargetManager.getInstance().navigateBackward();
	return true;
    }
    
    /**
     * @deprecated As of ArgoUml version 0.13.5,replaced by
     *             {@link org.argouml.ui.targetmanager.TargetManager.getInstance().navigateBack();
     *
     */
    public void navigateBackAction() {
        TargetManager.getInstance().navigateBackward();
    }

    /**
     * @deprecated As of ArgoUml version 0.13.5,replaced by
     *             {@link org.argouml.ui.targetmanager.TargetManager#navigateForward() TargetManager.getInstance().navigateForward()}
     */
    public boolean navigateForward(boolean attempt) {
        TargetManager.getInstance().navigateForward();
        return true;
    }

    /**
     * @deprecated As of ArgoUml version 0.13.5,replaced by
     *             {@link org.argouml.ui.targetmanager.TargetManager#navigateForward() TargetManager.getInstance().navigateForward()}
     */
    public void navigateForwardAction() {
        TargetManager.getInstance().navigateForward();
    }

    /**
     * @deprecated As of ArgoUml version 0.13.5,replaced by
     *             {@link org.argouml.ui.targetmanager.TargetManager#navigateForwardPossible() TargetManager.getInstance().navigateForwardPossible()}
     */
    public boolean isNavigateForwardEnabled() {
        return TargetManager.getInstance().navigateForwardPossible();
    }
    
    /**
     * @deprecated As of ArgoUml version 0.13.5,replaced by
     *             {@link org.argouml.ui.targetmanager.TargetManager#navigateBackPossible() TargetManager.getInstance().navigateBackPossible()}
     */
    public boolean isNavigateBackEnabled() {
        return TargetManager.getInstance().navigateBackPossible();
    }

    /**
     * <p>Calling this method with an array of metaclasses (for example,
     *   MClassifier.class) will result in the prop panel propagating any name
     *   changes or removals on any object that on the same event queue as the
     *   target that is assignable to one of the metaclasses.</p>
     *
     * <p>Reworked to use {@link #addThirdPartyEventListening(Object[])}, so
     *   removing the need for NSUML promiscuous listeners.</p>
     *
     * <p><em>Note</em>. Despite the name, the old implementation tried to
     *   listen for ownedElement and baseClass events as well as name
     *   events. We incorporate all these.</p>
     * 
     * <p><em>Note</em> Reworked the implementation to use the new 
     * UmlModelEventPump mechanism. In the future proppanels should 
     * register directly with UmlModelEventPump IF they are really interested
     * in the events themselves. If components on the proppanels are interested,
     * these components should register themselves.</p>
     * 
     * @deprecated As of ArgoUml version unknown(earlier than 0.13.5),
     *             replaced by {@link org.argouml.model.uml.UmlModelEventPump#addModelEventListener(Object , Object)}.
     *             since components should register themselves.
     *
     * @param metaclasses  The metaclass array we wish to listen to.
     */

    public void setNameEventListening(Class[] metaclasses) {

        /* 
	   old implementation
         
	   // Convert to the third party listening pair list
        
	   Vector targetList = new Vector (metaclasses.length * 6);
        
	   for (int i = 0 ; i < metaclasses.length ; i++) {
	   Class mc = metaclasses[i];
        
	   targetList.add(mc);
	   targetList.add("name");
        
	   targetList.add(mc);
	   targetList.add("baseClass");
        
	   targetList.add(mc);
	   targetList.add("ownedElement");
	   }
        
	   addThirdPartyEventListening(targetList.toArray());
        */
        for (int i = 0; i < metaclasses.length; i++) {
            Class clazz = metaclasses[i];
            if (MNamespace.class.isAssignableFrom(clazz)) {
                UmlModelEventPump.getPump().addClassModelEventListener(this, clazz, "ownedElement");
            }
            if (MModelElement.class.isAssignableFrom(clazz)) {
                UmlModelEventPump.getPump().addClassModelEventListener(this, clazz, "name");
            }
            if (clazz.equals(MStereotype.class)) {
                UmlModelEventPump.getPump().addClassModelEventListener(this, clazz, "baseClass");
            }
        }
    }

    public void removeElement() {
        Object target = getTarget();
        if (ModelFacade.isABase(target)) {
            MModelElement newTarget = ((MModelElement) target).getModelElementContainer();
            MBase base = (MBase) target;
            TargetManager.getInstance().setTarget(base);
            ActionEvent event = new ActionEvent(this, 1, "delete");
            ActionRemoveFromModel.SINGLETON.actionPerformed(event);
            if (newTarget != null) {
                TargetManager.getInstance().setTarget(newTarget);
            }
        }
    }

    /** check whether this element can be deleted. 
     *  Currently it only checks whether we delete the main model.
     *  ArgoUML does not like that.
     *  @since 0.13.2
     */
    public boolean isRemovableElement() {
        return ((getTarget() != null) && (getTarget() != ProjectManager.getManager().getCurrentProject().getModel()));
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

    /**
     *    Constructs the PropPanel.
     *    @param title Title of panel
     *    @param panelCount number of horizontal panels
     *    @deprecated As of ArgoUml version 0.13.2 (7-Dec-2002), replaced by
     *                {@link #PropPanel(String, ImageIcon, Orientation)}
     *                I propose to remove this by version 0.15 (Bob Tarling)
     */
    public PropPanel(String title, int panelCount) {
        this(title, null, panelCount);
    }

    /**
     *    Constructs the PropPanel - DO NOT USE.
     *    @param title Title of panel
     *    @param panelCount number of horizontal panels
     *
     *    @deprecated As of ArgoUml version 0.13.2 (7-Dec-2002), replaced by
     *                {@link #PropPanel(????)}. Use of GridBagLayout is being
     *                dropped in favour of
     *                {@link org.argouml.swingext.LabelledLayout}
     *                I propose to remove this by version 0.15 (Bob Tarling)
     */
    public PropPanel(String title, ImageIcon icon, int panelCount) {
        super(title);
        setLayout(new BorderLayout());
        center = new JPanel();
        center.setLayout(new GridLayout(1, 0));

        JPanel panel;
        for (long i = 0; i < panelCount; i++) {
            panel = new JPanel(new GridBagLayout());
            _panels.add(panel);
            center.add(panel);
        }
        add(center, BorderLayout.CENTER);

        //add caption panel and button panel
        if (icon != null) {
            captionPanel.add(new JLabel(icon));
        }
        captionPanel.add(new JLabel(localize(title)));
        addCaption(captionPanel, 0, 0, 0);

        buttonPanel = new ToolBar();
        buttonPanel.putClientProperty("JToolBar.isRollover",  Boolean.TRUE);
        // TODO buttonPanelWithFlowLayout shouldn't exist any more
        // It's just another useless layer
        buttonPanelWithFlowLayout = new JPanel(new FlowLayout());
        buttonPanelWithFlowLayout.add(buttonPanel);
        addField(buttonPanelWithFlowLayout, 0, 0, 0);
    }
    
    /**
     *   Adds a component to the captions of the specified panel.
     *   @param component Component to be added (typically a JLabel)
     *   @param row row index, zero-based.
     *   @param panel panel index, zero-based.
     *   @param weighty specifies how to distribute extra vertical space,
     *      see GridBagConstraint for details on usage.
     *
     * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
     *         GridBagConstraints is no longer used as a prop panel layout,
     *             replaced by addField(label, component) - Labelled layout method.
     *                I propose to remove this by version 0.15 (Bob Tarling)
     */
    public void addCaption(Component component, int row, int panel, double weighty) {
        if (orientation == Vertical.getInstance()) {
            row = lastRow;
            panel = 0;
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = row;
        gbc.weighty = weighty;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;

        JPanel pane = (JPanel) _panels.elementAt(panel);
        GridBagLayout layout = (GridBagLayout) pane.getLayout();
        layout.setConstraints(component, gbc);
        pane.add(component);
    }

    /**
     * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
     *         GridBagConstraints is no longer used as a prop panel layout,
     *             replaced by addField(label, component) - Labelled layout method
     *                I propose to remove this by version 0.15 (Bob Tarling)
     */
    public void addCaption(String label, int row, int panel, double weighty) {
        addCaption(new JLabel(localize(label)), row, panel, weighty);
    }

    /**
     *   Adds a component to the fields of the specified panel.
     *   @param component Component to be added
     *   @param row row index, zero-based.
     *   @param panel panel index, zero-based.
     *   @param weighty specifies how to distribute extra vertical space,
     *      see GridBagConstraint for details on usage.
     *
     * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
     *         GridBagConstraints is no longer used as a prop panel layout,
     *             replaced by addField(label, component) - Labelled layout method.
     *                I propose to remove this by version 0.15 (Bob Tarling)
     */
    public void addField(Component component, int row, int panel, double weighty) {
        if (orientation == Vertical.getInstance()) {
            row = lastRow;
            panel = 0;
            ++lastRow;
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = row;
        gbc.weighty = weighty;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 1;
        gbc.weightx = 1;
        if (weighty == 0)
            gbc.fill = GridBagConstraints.HORIZONTAL;
        else
            gbc.fill = GridBagConstraints.BOTH;

        JPanel pane = (JPanel) _panels.elementAt(panel);
        GridBagLayout layout = (GridBagLayout) pane.getLayout();
        layout.setConstraints(component, gbc);
        pane.add(component);
    }

    /**
     *   Adds a component to the fields of the specified panel
     *     and sets the background and color to indicate
     *     the field is a link.
     *   @param component Component to be added
     *   @param row row index, zero-based.
     *   @param panel panel index, zero-based.
     *   @param weighty specifies how to distribute extra vertical space,
     *      see GridBagConstraint for details on usage.
     *
     * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
     *     GridBagConstraints is no longer used as a prop panel layout,
     *     replaced by addLinkField(label, component) - Labelled layout method.
     *     The method will be removed in release 0.15
     */
    final public void addLinkField(Component component, int row, int panel, double weighty) {
        component.setBackground(getBackground());
        component.setForeground(Color.blue);
        addField(component, row, panel, weighty);
    }
} /* end class PropPanel */