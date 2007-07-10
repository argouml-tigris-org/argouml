// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.AbstractArgoJPanel;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargettableModelView;
import org.argouml.uml.profile.ProfileConfiguration;
import org.tigris.gef.presentation.Fig;
import org.tigris.swidgets.GridLayout2;
import org.tigris.swidgets.LabelledLayout;
import org.tigris.swidgets.Orientation;
import org.tigris.swidgets.Vertical;
import org.tigris.toolbar.ToolBarFactory;

/**
 * This abstract class provides the basic layout and event dispatching support
 * for all Property Panels.<p>
 *
 * The property panel is {@link org.tigris.swidgets.LabelledLayout} layed out as
 * a number (specified in the constructor) of equally sized panels that split
 * the available space. Each panel has a column of "captions" and matching
 * column of "fields" which are laid out indepently from the other panels.<p>
 *
 * The Properties panels for UML Model Elements are structured in an inheritance
 * hierarchy that matches the UML metamodel.
 */
public abstract class PropPanel extends AbstractArgoJPanel implements
        TabModelTarget, UMLUserInterfaceContainer, ComponentListener {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(PropPanel.class);

    private Object target;

    private Object modelElement;

    /**
     * List of event listeners to notify. This is computed one time and frozen
     * the first time any target change method (e.g. setTarget, targetAdded) is
     * called.
     */
    private EventListenerList listenerList;

    private JPanel buttonPanel = new JPanel(new GridLayout());

    private JLabel titleLabel;
    
    private List actions = new ArrayList();

    private static Font stdFont =
        LookAndFeelMgr.getInstance().getStandardFont();

    /**
     * Construct new PropPanel using LabelledLayout.<p>
     *
     * @param icon
     *            The icon to display for the panel
     * @param title
     *            The title of the panel
     * @param orientation
     *            the orientation
     */
    public PropPanel(String title, ImageIcon icon, Orientation orientation) {
        super(title);
        setOrientation(orientation);

        LabelledLayout layout =
            new LabelledLayout(orientation == Vertical.getInstance());
        layout.setHgap(5);
        setLayout(layout);

        if (icon != null) {
            setTitleLabel(new JLabel(title, icon, SwingConstants.LEFT));
        } else {
            setTitleLabel(new JLabel(title));
        }
        titleLabel.setLabelFor(buttonPanel);
        add(titleLabel);
        add(buttonPanel);
        
        addComponentListener(this);
    }

    /**
     * Constructs a new Proppanel without an icon. If there is an icon it's
     * updated at runtime via settarget.<p>
     *
     * @param title
     *            the title
     * @param orientation
     *            the orientation
     */
    public PropPanel(String title, Orientation orientation) {
        this(title, null, orientation);
    }

    /*
     * @see org.tigris.swidgets.Orientable#setOrientation(org.tigris.swidgets.Orientation)
     */
    public void setOrientation(Orientation orientation) {
        super.setOrientation(orientation);
    }

    /**
     * Add a button to the toolbar of a property panel using the action to
     * control the behavior of the action.
     *
     * @param action
     *            the action which will be used in the toolbar button.
     */
    protected void addAction(Action action) {
        this.actions.add(action);
    }

    /**
     * Add a button to the toolbar of a property panel using the action to
     * control the behavior of the action.
     *
     * @param action
     *            the action which will be used in the toolbar button.
     * @param tooltip
     *            the tooltip to set, or null to skip setting of a new tooltip.
     */
    protected void addAction(Action action, String tooltip) {
        JButton button = new TargettableButton(action);
        if (tooltip != null) {
            button.setToolTipText(tooltip);
        }
        button.setText("");
        button.setFocusable(false);
        actions.add(button);
    }

    /**
     * Add multiple buttons at once.
     *
     * @param actionArray the Actions.
     */
    protected void addAction(Object[] actionArray) {
        this.actions.add(actionArray);
    }
    
    public void buildToolbar() {
        ToolBarFactory factory = new ToolBarFactory(actions);
        factory.setRollover(true);
        factory.setFloatable(false);
        JToolBar toolBar = factory.createToolBar();

	buttonPanel.removeAll();
        buttonPanel.add(BorderLayout.WEST, toolBar);
        // Set the tooltip of the arrow to open combined tools:
        buttonPanel.putClientProperty("ToolBar.toolTipSelectTool",
                Translator.localize("action.select"));
    }

    private static class TargettableButton extends JButton
        implements TargettableModelView {

        public TargettableButton(Action action) {
            super(action);
        }

        public TargetListener getTargettableModel() {
            if (getAction() instanceof TargetListener) {
                return (TargetListener) getAction();
            }
            return null;
        }

    }

    /**
     * Add a component with the specified label.<p>
     *
     * @param label
     *            the label for the component
     * @param component
     *            the component
     * @return the label added
     */
    public JLabel addField(String label, Component component) {
        JLabel jlabel = new JLabel(label);
        jlabel.setFont(stdFont);
        component.setFont(stdFont);
        jlabel.setLabelFor(component);
        add(jlabel);
        add(component);
        return jlabel;
    }

    /**
     * Add a component with the specified label positioned after another
     * component.
     *
     * @param label
     *            the label for the component
     * @param component
     *            the component
     * @param afterComponent
     *            the component before
     * @return the newly added label
     */
    public JLabel addFieldAfter(String label, Component component,
            Component afterComponent) {
        int nComponent = this.getComponentCount();
        for (int i = 0; i < nComponent; ++i) {
            if (getComponent(i) == afterComponent) {
                JLabel jlabel = new JLabel(label);
                jlabel.setFont(stdFont);
                component.setFont(stdFont);
                jlabel.setLabelFor(component);
                add(jlabel, ++i);
                add(component, ++i);
                return jlabel;
            }
        }
        throw new IllegalArgumentException("Component not found");
    }

    /**
     * Add a component with the specified label positioned before another
     * component.<p>
     *
     * @param label
     *            the label for the component
     * @param component
     *            the to be added component
     * @param beforeComponent
     *            the component before its label we add
     * @return the newly added component
     */
    public JLabel addFieldBefore(String label, Component component,
            Component beforeComponent) {
        int nComponent = this.getComponentCount();
        for (int i = 0; i < nComponent; ++i) {
            if (getComponent(i) == beforeComponent) {
                JLabel jlabel = new JLabel(label);
                jlabel.setFont(stdFont);
                component.setFont(stdFont);
                jlabel.setLabelFor(component);
                add(jlabel, i - 1);
                add(component, i++);
                return jlabel;
            }
        }
        throw new IllegalArgumentException("Component not found");
    }

    /**
     * Add a separator.
     */
    protected final void addSeparator() {
        // Note: the mispelling is in a foreign component's method name
        // so can't be corrected. - tfm
        add(LabelledLayout.getSeperator());
    }

    /**
     * Set the target to be associated with a particular property panel.<p>
     *
     * This involves resetting the third party listeners.
     *
     * @param t
     *            The object to be set as a target.
     */
    public void setTarget(Object t) {
        LOG.debug("setTarget called with " + t + " as parameter (not target!)");
        t = (t instanceof Fig) ? ((Fig) t).getOwner() : t;

        // If the target has changed notify the third party listener if it
        // exists and dispatch a new element event listener to
        // ourself. Otherwise dispatch a target reasserted to ourself.
        Runnable dispatch = null;
        if (t != target) {

            // Set up the target and its model element variant.

            target = t;
            modelElement = null;
            if (listenerList == null) {
                listenerList = collectTargetListeners(this);
            }

            if (Model.getFacade().isAUMLElement(target)) {
                modelElement = target;
            }

            // This will add a new ModelElement event listener
            // after update is complete

            dispatch = new UMLChangeDispatch(this,
                    UMLChangeDispatch.TARGET_CHANGED_ADD);

        } else {
            dispatch = new UMLChangeDispatch(this,
                    UMLChangeDispatch.TARGET_REASSERTED);

        }
        SwingUtilities.invokeLater(dispatch);

        // update the titleLabel
        // MVW: This overrules the icon set initiallly... Why do we need this?
        if (titleLabel != null) {
            Icon icon = null;
            if (t != null) {
                icon = ResourceLoaderWrapper.getInstance().lookupIcon(t);
            }
            if (icon != null) {
                titleLabel.setIcon(icon);
            }
        }
    }

    /**
     * Builds a eventlistenerlist of all targetlisteners that are part of this
     * container and its children. Components do not need to register
     * themselves. They are registered implicitly if they implement the
     * TargetListener interface.
     * 
     * @param container
     *            the container to search for targetlisteners
     * @return an EventListenerList with all TargetListeners on this container
     *         and its children.
     */
    private EventListenerList collectTargetListeners(Container container) {
        Component[] components = container.getComponents();
        EventListenerList list = new EventListenerList();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof TargetListener) {
                list.add(TargetListener.class, (TargetListener) components[i]);
            }
            if (components[i] instanceof TargettableModelView) {
                list.add(TargetListener.class,
                        ((TargettableModelView) components[i])
                                .getTargettableModel());
            }
            if (components[i] instanceof Container) {
                EventListenerList list2 = collectTargetListeners(
                                                (Container) components[i]);
                Object[] objects = list2.getListenerList();
                for (int j = 1; j < objects.length; j += 2) {
                    list.add(TargetListener.class, (TargetListener) objects[j]);
                }
            }
        }
        return list;
    }

    /*
     * @see org.argouml.ui.TabTarget#getTarget()
     */
    public final Object getTarget() {
        return target;
    }

    /*
     * @see org.argouml.ui.TabTarget#refresh()
     */
    public void refresh() {
        SwingUtilities.invokeLater(new UMLChangeDispatch(this, 0));
    }

    /*
     * @see org.argouml.ui.TabTarget#shouldBeEnabled(java.lang.Object)
     */
    public boolean shouldBeEnabled(Object t) {
        t = (t instanceof Fig) ? ((Fig) t).getOwner() : t;
        return Model.getFacade().isAUMLElement(t);
    }

    /**
     * This method can be overriden in derived Panels where the appropriate
     * namespace for display may not be the same as the namespace of the target.
     *
     * @return the namespace
     */
    protected Object getDisplayNamespace() {
        Object ns = null;
        Object theTarget = getTarget();
        if (Model.getFacade().isAModelElement(theTarget)) {
            ns = Model.getFacade().getNamespace(theTarget);
        }
        return ns;
    }

    /*
     * @see org.argouml.uml.ui.UMLUserInterfaceContainer#getProfile()
     */
    public ProfileConfiguration getProfile() {
        return ProjectManager.getManager().getCurrentProject().getProfileConfiguration();
    }

    /*
     * @see org.argouml.uml.ui.UMLUserInterfaceContainer#getModelElement()
     */
    public final Object getModelElement() {
        return modelElement;
    }

    /*
     * @see org.argouml.uml.ui.UMLUserInterfaceContainer#formatElement(java.lang.Object)
     */
    public String formatElement(/* MModelElement */Object element) {
        return getProfile().getFormatingStrategy().formatElement(element, getDisplayNamespace());
    }

    /*
     * @see org.argouml.uml.ui.UMLUserInterfaceContainer#formatNamespace(java.lang.Object)
     */
    public String formatNamespace(/* MNamespace */Object namespace) {
        return getProfile().getFormatingStrategy().formatElement(namespace, null);
    }

    /*
     * @see org.argouml.uml.ui.UMLUserInterfaceContainer#formatCollection(java.util.Iterator)
     */
    public String formatCollection(Iterator iter) {
        Object namespace = getDisplayNamespace();
        return getProfile().getFormatingStrategy().formatCollection(iter, namespace);
    }


    /**
     * Get the delete action.
     *
     * @return the delete action
     */
    protected final Action getDeleteAction() {
        return ActionDeleteModelElements.getTargetFollower();
    }

    /**
     * Check whether this element can be deleted. Currently it only checks
     * whether we delete the main model. ArgoUML does not like that.
     *
     * @since 0.13.2
     * @return whether this element can be deleted
     */
    public boolean isRemovableElement() {
        return ((getTarget() != null) && (getTarget() != (ProjectManager
                .getManager().getCurrentProject().getModel())));
    }

    /*
     * @see TargetListener#targetAdded(TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        if (listenerList == null) {
            listenerList = collectTargetListeners(this);
        }
        setTarget(e.getNewTarget());
        if (isVisible()) {
            fireTargetAdded(e);
        }
    }

    /*
     * @see TargetListener#targetRemoved(TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        setTarget(e.getNewTarget());
        if (isVisible()) {
            fireTargetRemoved(e);
        }
    }

    /*
     * @see TargetListener#targetSet(TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTarget());
        if (isVisible()) {
            fireTargetSet(e);
        }
    }

    private void fireTargetSet(TargetEvent targetEvent) {
        if (listenerList == null) {
            listenerList = collectTargetListeners(this);
        }
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TargetListener.class) {
                ((TargetListener) listeners[i + 1]).targetSet(targetEvent);
            }
        }
    }

    private void fireTargetAdded(TargetEvent targetEvent) {
        if (listenerList == null) {
            listenerList = collectTargetListeners(this);
        }
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TargetListener.class) {
                ((TargetListener) listeners[i + 1]).targetAdded(targetEvent);
            }
        }
    }

    private void fireTargetRemoved(TargetEvent targetEvent) {
        if (listenerList == null) {
            listenerList = collectTargetListeners(this);
        }
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TargetListener.class) {
                ((TargetListener) listeners[i + 1]).targetRemoved(targetEvent);
            }
        }
    }

    /**
     * @param theTitleLabel
     *            the title of the panel shown at the top
     */
    protected void setTitleLabel(JLabel theTitleLabel) {
        this.titleLabel = theTitleLabel;
        titleLabel.setFont(stdFont);
    }

    /**
     * @return the title of the panel shown at the top
     */
    protected JLabel getTitleLabel() {
        return titleLabel;
    }

    protected JPanel createBorderPanel(String title) {
    	JPanel panel = new JPanel(new GridLayout2());
    	TitledBorder border = new TitledBorder(title);
    	border.setTitleFont(stdFont);
    	panel.setBorder(border);
    	return panel;
    }

    /**
     * If there are no buttons to show in the toolbar,
     * then set the height to e.g. 18, so that the title
     * is aligned right by the LabelledLayout.
     *
     * @param height the height
     */
    protected void setButtonPanelSize(int height) {
        /* Set the minimum and preferred equal,
         * so that the size is fixed for the labelledlayout.
         */
        buttonPanel.setMinimumSize(new Dimension(0, height));
        buttonPanel.setPreferredSize(new Dimension(0, height));
    }

    /**
     * Look up an icon.
     *
     * @param name
     *            the resource name.
     * @return an ImageIcon corresponding to the given resource name
     */
    protected static ImageIcon lookupIcon(String name) {
        return ResourceLoaderWrapper.lookupIconResource(name);
    }


    /*
     * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.ComponentEvent)
     */
    public void componentHidden(ComponentEvent e) {
        // TODO: do we want to fire targetRemoved here or is it enough to just
        // stop updating the targets?
    }
    
    /*
     * @see java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent)
     */
    public void componentShown(ComponentEvent e) {
        // Refresh the target for all our children which weren't getting
        // while not visible
        fireTargetSet(new TargetEvent(
                this, TargetEvent.TARGET_SET, null, new Object[] {target}));
    }
    
    /*
     * @see java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent)
     */
    public void componentMoved(ComponentEvent e) {
        // ignored
    }

    /*
     * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
     */
    public void componentResized(ComponentEvent e) {
        // ignored        
    }


    /**
     * Create a single row scroll pane backed by a JList.
     *
     * @param list the list to be used to back the scroll pane
     * @return a scrollpane with a single row
     */
    protected JScrollPane getSingleRowScroll(JList list) {
        // TODO: Temporary fix while we find something better - tfm
        list.setVisibleRowCount(2);
//        list.setVisibleRowCount(1);
        JScrollPane pane = new JScrollPane(list);
        // Scroll bar is not allowed to take up more than 20% of height
//        pane.getHorizontalScrollBar().setMaximumSize(
//                new Dimension(1000, 
//                        list.getPreferredScrollableViewportSize().height / 5));
        return pane;
    }

    /**
     * Create a single row scroll pane backed by a UMLLinkedList which uses
     * the given list model.
     *
     * @param model the list to be used to back the scroll pane
     * @return a scrollpane with a single row
     */
    protected JScrollPane getSingleRowScroll(ListModel model) {
        return getSingleRowScroll(new UMLLinkedList(model));
    }

}
