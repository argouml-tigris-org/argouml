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

// $header$
package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;

import org.argouml.api.FacadeManager;
import org.argouml.model.uml.NsumlModelFacade;

import ru.novosoft.uml.MElementEvent;

/**
 * The UMLButtonGroup is the buttongroup behind radio buttons that depict some 
 * attribute of a modelelement like visibility.  The radio buttons are controlled
 * by the buttongroup. The UMLButtonGroup can be compared to the UMLComboBoxModel
 * where the items in the model are the radionbuttons in the buttongroup.
 * @since Oct 12, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class UMLButtonGroup
    extends ButtonGroup
    implements UMLUserInterfaceComponent, ActionListener {

    /**
     * This map contains the radiobuttons as key and the action that should
     * be performed when the button is pressed as value. We don't use 
     * the functionallity provided by JDK 1.3 to link an action to a button.
     */
    private Map _actions = new HashMap();
    
    private Object _target = null;
    
    private UMLUserInterfaceContainer _container = null;
    
    /**
     * Constructor for UMLButtonGroup.
     */
    public UMLButtonGroup(UMLUserInterfaceContainer container) {
        super();
        setContainer(container);
    }
    
    /**
     * Adds a button and an action to the group. When the button is selected,
     * the action is fired. If the action is null, no action is done.
     * @param button
     * @param action
     */
    public void add(AbstractButton button, Action action) {
        _actions.put(button, action);
        super.add(button);
        button.addActionListener(this);
    }

    /**
     * When the target changes, the buttonmodel must be updated. Since the 
     * API of targetChanged does not have a parameter describing the old and new 
     * target, the target is retrieved via the container.
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetChanged()
     */
    public void targetChanged() {
        if (getContainer() != null && getContainer().getTarget() != null) {
            setTarget(getContainer().getTarget());
      
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetReasserted()
     */
    public void targetReasserted() {
        if (getContainer() != null && getContainer().getTarget() != null) {
            setTarget(getContainer().getTarget());
      
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent e) {
        if (e.getSource() == getTarget()) {
            buildModel();
        }
    }

    /**
     * Adding roles does not have any influence on the buttongroup (unless you
     * want to add a button...)
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
    }

    /**
     * Removing roles does not have any influence on the buttongroup (unless you
     * want to remove a button...)
     * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent e) {
    }

    /**
     * Removing elements does not have any influence on a buttongroup
     * @see ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent e) {
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        Action action = (Action)_actions.get(o);
        if (action != null) {
            // e.setSource(this);
            action.actionPerformed(e);
        }
    }    

    /**
     * Returns the container.
     * @return UMLUserInterfaceContainer
     */
    public UMLUserInterfaceContainer getContainer() {
        return _container;
    }

    /**
     * Returns the target. The target is directly asked from the _target attribute
     * and not from the _container attribute to make the move to a new targeting
     * system as easy as possible.
     * @return Object
     */
    public Object getTarget() {
        return _target;
    }

    /**
     * Sets the container.
     * @param container The container to set
     */
    public void setContainer(UMLUserInterfaceContainer container) {
        _container = container;
        if (container != null) {
            setTarget(container.getTarget());
        }
    }

    /**
     * Sets the target. This method will not be used untill the target does
     * not come via the container.
     * @param target The target to set
     */
    public void setTarget(Object target) {
        if (target == null || !FacadeManager.getUmlFacade().isAModelElement(target) || _target == target) return;
        _target = target;
        buildModel();
    }
    
    /**
     * Builds the model. That is: it selects the correct button. The name of this
     * method is choosen to be compliant with for example UMLModelElementListModel2
     */
    abstract public void buildModel();

}
