// Copyright (c) 1996-02 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without ga written
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

import org.argouml.application.api.*;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.ui.*;
import org.argouml.uml.*;
import org.argouml.swingext.*;
import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.use_cases.*;
import java.lang.reflect.*;

import org.tigris.gef.util.*;

/**
 *   This abstract class provides the basic layout and event dispatching
 *   support for all Property Panels.  The property panel is layed out
 *   as a number (specified in the constructor) of equally sized panels
 *   that split the available space.  Each panel has a column of
 *   "captions" and matching column of "fields" which are laid out
 *   indepently from the other panels.
 */
abstract public class PropPanel extends TabSpawnable
implements TabModelTarget, MElementListener, UMLUserInterfaceContainer {
    ////////////////////////////////////////////////////////////////
    // instance vars
    private Object _target;
    private MModelElement _modelElement;
    private static Profile _profile;
    private LinkedList _navListeners = new LinkedList();
    private ResourceBundle _bundle = null;

    private Vector _panels = new Vector();
    private UMLNameEventListener _nameListener;

    private int lastRow;


    /**
     * <p>The metaclass/property pairs for the third party listener (if we have
     *   set one up. We use this when creating a new listener on target
     *   change.</p>
     */

    private Vector _targetList = null;
    private JPanel center;

    protected JPanel buttonPanel=new JPanel();
    private JPanel buttonPanelWithFlowLayout=new JPanel();
    private JPanel captionPanel=new JPanel();

    protected static ImageIcon _navBackIcon = ResourceLoader.lookupIconResource("NavigateBack");
    protected static ImageIcon _navForwardIcon = ResourceLoader.lookupIconResource("NavigateForward");
    protected static ImageIcon _deleteIcon = ResourceLoader.lookupIconResource("RedDelete");
    protected static ImageIcon _navUpIcon = ResourceLoader.lookupIconResource("NavigateUp");

    protected Font smallFont = MetalLookAndFeel.getSubTextFont();


    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     *    Constructs the PropPanel.
     *    @param title Title of panel
     *    @param panelCount number of horizontal panels
     */
    public PropPanel(String title,int panelCount) {
	this(title, null, panelCount);
    }

    public PropPanel(String title, ImageIcon icon, int panelCount) {
	super(title);
        setLayout(new BorderLayout());
        center = new JPanel();
        center.setLayout(new GridLayout(1,0));
        
        JPanel panel;
        for(long i = 0; i < panelCount; i++) {
            panel = new JPanel(new GridBagLayout());
            _panels.add(panel);
            center.add(panel);
        }
        add(center,BorderLayout.CENTER);

	//add caption panel and button panel
	if (icon!=null) captionPanel.add(new JLabel(icon));
	captionPanel.add(new JLabel(localize(title)));
	addCaption(captionPanel,0,0,0);

	buttonPanel = new JPanel(new GridLayout(1,0));
	buttonPanelWithFlowLayout = new JPanel(new FlowLayout());
	buttonPanelWithFlowLayout.add(buttonPanel);
	addField(buttonPanelWithFlowLayout,0,0,0);
	

	/*JPanel namePanel=new JPanel(new FlowLayout());

	if (icon!=null) captionPanel.add(new JLabel(icon));
	captionPanel.add(new JLabel(localize(title)));
	namePanel.add(captionPanel);

	buttonPanel = new JPanel(new GridLayout(1,0));
	buttonPanelWithFlowLayout = new JPanel(new FlowLayout());
	buttonPanelWithFlowLayout.add(buttonPanel);
	namePanel.add(buttonPanelWithFlowLayout);

	add(namePanel, BorderLayout.NORTH);*/
    }

    public void setOrientation(Orientation orientation) {
        super.setOrientation(orientation);
    }

    /**
     * Construct new PropPanel using LabelledLayout
     * @param icon The icon to display for the panel
     * @param title The title of the panel
     * @param sectionCount the number of sections in the proppanel
     */
    public PropPanel(String title, ImageIcon icon, Orientation orientation) {
	super(title);
        setOrientation(orientation);

        setLayout(new LabelledLayout(orientation));

        JLabel titleLabel;
	if (icon!=null) titleLabel = new JLabel(localize(title), icon, SwingConstants.LEFT);
        else titleLabel = new JLabel(localize(title));
        buttonPanel = new JPanel(new GridLayout2(1, 0, GridLayout2.MAXPREFERRED));
        titleLabel.setLabelFor(buttonPanel);
	add(titleLabel);
        add(buttonPanel);
    }


    /**
     *   Adds a component to the captions of the specified panel.
     *   @param component Component to be added (typically a JLabel)
     *   @param row row index, zero-based.
     *   @param panel panel index, zero-based.
     *   @param weighty specifies how to distribute extra vertical space,
     *      see GridBagConstraint for details on usage.
     */
    public void addCaption(Component component,int row,int panel,double weighty)
    {
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
        layout.setConstraints(component,gbc);
        pane.add(component);
    }

    public void addCaption(String label,int row, int panel,double weighty) {
        addCaption(new JLabel(localize(label)),row,panel,weighty);
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
        for (int i=0; i < nComponent; ++i) {
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
        for (int i=0; i < nComponent; ++i) {
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
    public final void addLinkField(String label, Component component) {
        component.setBackground(getBackground());
        component.setForeground(Color.blue);
        addField(label, component);
    }

    final public String localize(String key) {
        String localized = key;
        if(_bundle == null) {
            _bundle = getResourceBundle();
        }
        if(_bundle != null) {
            try {
                localized = _bundle.getString(key);
            }
            catch(MissingResourceException e) {}
            if(localized == null) {
                localized = key;
            }
        }
        return localized;
    }

    public ResourceBundle getResourceBundle() {
        return null;
    }

    /**
     *   Adds a component to the fields of the specified panel.
     *   @param component Component to be added
     *   @param row row index, zero-based.
     *   @param panel panel index, zero-based.
     *   @param weighty specifies how to distribute extra vertical space,
     *      see GridBagConstraint for details on usage.
     */
   public void addField(Component component,int row,int panel,double weighty)
    {
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
        if(weighty == 0)
            gbc.fill = GridBagConstraints.HORIZONTAL;
        else
            gbc.fill = GridBagConstraints.BOTH;

        JPanel pane = (JPanel) _panels.elementAt(panel);
        GridBagLayout layout = (GridBagLayout) pane.getLayout();
        layout.setConstraints(component,gbc);
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
     */
    final public void addLinkField(Component component,int row,int panel,double weighty)
    {
        component.setBackground(getBackground());
        component.setForeground(Color.blue);
        addField(component,row,panel,weighty);
    }



    public Profile getProfile() {
        if(_profile == null) {
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
     *
     * @param t  The object to be set as a target.
     */

    public void setTarget(Object t) {

        // If the target has changed notify the third party listener if it
        // exists and dispatch a new NSUML element listener to
        // ourself. Otherwise dispatch a target reasserted to ourself.

        if(t != _target) {

            // Set up the target and its model element variant.

            _target = t;
            _modelElement = null;

            if(_target instanceof MModelElement) {
                _modelElement = (MModelElement) _target;
            }

           

            // This will add a new MElement listener after update is complete

            SwingUtilities.invokeLater(
                new UMLChangeDispatch(this,
                                      UMLChangeDispatch.TARGET_CHANGED_ADD));
        }
        else {
            UMLChangeDispatch dispatch =
                new UMLChangeDispatch(this,
                                      UMLChangeDispatch.TARGET_REASSERTED);
            dispatch.targetReasserted();
            SwingUtilities.invokeLater(dispatch);
        }
    }

    public final Object getTarget() { return _target; }

    public final MModelElement getModelElement() {
        return _modelElement;
    }

    public void refresh() {
        SwingUtilities.invokeLater(new UMLChangeDispatch(this,0));
    }

    public boolean shouldBeEnabled() { return (_modelElement != null); }


    public void propertySet(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this,0);
        dispatch.propertySet(mee);
        SwingUtilities.invokeLater(dispatch);
    }

    public void listRoleItemSet(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this,0);
        dispatch.listRoleItemSet(mee);
        SwingUtilities.invokeLater(dispatch);
    }

    public void recovered(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this,0);
        dispatch.recovered(mee);
        SwingUtilities.invokeLater(dispatch);
    }

    public void removed(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this,0);
        dispatch.removed(mee);
        SwingUtilities.invokeLater(dispatch);
    }

    public void roleAdded(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this,0);
        dispatch.roleAdded(mee);
        SwingUtilities.invokeLater(dispatch);
    }

    public void roleRemoved(MElementEvent mee) {
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this,0);
        dispatch.roleRemoved(mee);
        SwingUtilities.invokeLater(dispatch);
    }

    /**
     *   This method can be overriden in derived Panels where the
     *   appropriate namespace for display may not be the same as
     *   the namespace of the target
     */
    protected MNamespace getDisplayNamespace() {
      MNamespace ns = null;
      Object target = getTarget();
      if(target instanceof MModelElement) {
        ns = ((MModelElement) target).getNamespace();
      }
      return ns;
    }


    public String formatElement(MModelElement element) {
        return getProfile().formatElement(element,getDisplayNamespace());
    }

    public String formatNamespace(MNamespace ns) {
        return getProfile().formatElement(ns,null);
    }



    public String formatCollection(Iterator iter) {
        MNamespace ns = getDisplayNamespace();
        return getProfile().formatCollection(iter,ns);
    }

    public void navigateTo(Object element) {
        Iterator iter = _navListeners.iterator();
        while(iter.hasNext()) {
            ((NavigationListener) iter.next()).navigateTo(element);
        }
    }
    public void open(Object element) {
        Iterator iter = _navListeners.iterator();
        while(iter.hasNext()) {
            ((NavigationListener) iter.next()).open(element);
        }
    }


    public boolean navigateBack(boolean attempt) {
        boolean navigated = false;
        Iterator iter = _navListeners.iterator();
	    while(iter.hasNext()) {
	        navigated = ((NavigationListener) iter.next()).navigateBack(attempt);
            if(navigated) attempt = false;
	    }
        return navigated;
    }

    public void navigateBackAction() {
        boolean attempt = true;
        navigateBack(attempt);
    }

    public boolean navigateForward(boolean attempt) {
        boolean navigated = false;
        Iterator iter = _navListeners.iterator();
	    while(iter.hasNext()) {
	        navigated = ((NavigationListener) iter.next()).navigateForward(attempt);
            if(navigated) attempt = false;
	    }
        return navigated;
    }

    public void navigateForwardAction() {
        boolean attempt = true;
        navigateForward(attempt);
    }

    public boolean isNavigateForwardEnabled() {
        boolean enabled = false;
        Iterator iter = _navListeners.iterator();
	    while(iter.hasNext() && !enabled) {
	        enabled = ((NavigationListener) iter.next()).isNavigateForwardEnabled();
	    }
        return enabled;
    }

    public boolean isNavigateBackEnabled() {
        boolean enabled = false;
        Iterator iter = _navListeners.iterator();
	    while(iter.hasNext() && !enabled) {
	        enabled = ((NavigationListener) iter.next()).isNavigateBackEnabled();
	    }
        return enabled;
    }



    /**    Registers a listener for navigation events.
     */
    public void addNavigationListener(NavigationListener navListener) {
        _navListeners.add(navListener);
    }

    public void removeNavigationListener(NavigationListener navListener) {
        _navListeners.remove(navListener);
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
     * @deprecated since components should register themselves.
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
        if(target instanceof MBase) { 
            MBase base = (MBase) target;
            Object projectTarget = ProjectBrowser.TheInstance.getTarget();
            ProjectBrowser.TheInstance.setTarget(base);
            ActionEvent event = new ActionEvent(this, 1, "delete");
	        ActionRemoveFromModel.SINGLETON.actionPerformed(event);
	       	if (!target.equals(projectTarget)) {
        		ProjectBrowser.TheInstance.setTarget(projectTarget);
        	}
	   
        }
    }

    /** check whether this element can be deleted. 
     *  Currently it only checks whether we delete the main model.
     *  ArgoUML does not like that.
     *  @since 0.13.2
     */
   public boolean isRemovableElement() {
       return ((getTarget() != null) && 
               (getTarget()!= ProjectBrowser.TheInstance.
                getProject().getModel()));
   } 

} /* end class PropPanel */
