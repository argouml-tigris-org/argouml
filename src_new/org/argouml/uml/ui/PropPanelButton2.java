// $Id$
// Copyright (c) 2002-2003 The Regents of the University of California. All
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

import java.awt.Dimension;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import ru.novosoft.uml.MElementEvent;

/**
 * @since Oct 3, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class PropPanelButton2 extends JButton implements UMLUserInterfaceComponent {

    private Action _action = null;
    private UMLUserInterfaceContainer _container = null;
    
    /**
     * Constructor for PropPanelButton2.
     * @param a
     */
    public PropPanelButton2(UMLUserInterfaceContainer container, Action a) {
        super(); // canot use the action constructor since we have to support 1.2.2
        setAction(a);
        setContainer(container);
    }

    /**
     * Factory method which sets the <code>ActionEvent</code>
     * source's properties according to values from the
     * <code>Action</code> instance.  The properties 
     * which are set may differ for subclasses.  By default,
     * the properties which get set are <code>Text</code>, <code>Icon
     * Enabled</code>, <code>ToolTipText</code> and <code>Mnemonic</code>.
     * <p>
     * If the <code>Action</code> passed in is <code>null</code>, 
     * the following things will occur:
     * <ul>
     * <li>the text is set to <code>null</code>,
     * <li>the icon is set to <code>null</code>,
     * <li>enabled is set to true,
     * <li>the tooltip text is set to <code>null</code>
     * </ul>
     *
     * @param a the <code>Action</code> from which to get the properties,
     *      or <code>null</code>
     * @see Action
     * @see #setAction
     */
    protected void configurePropertiesFromAction(Action a) {
        setText((a != null ? (String) a.getValue(Action.NAME) : null));
        setIcon((a != null ? (Icon) a.getValue(Action.SMALL_ICON) : null));
        setEnabled((a != null ? a.isEnabled() : true));
        setToolTipText((a != null ? (String) a.getValue(Action.SHORT_DESCRIPTION) : null));    
        if (a != null)  {
 	    // TODO: When no longer requiring support for JDK1.2 this constant
	    // can be changed to Action.MNEMONIC_KEY.
	    final String MNEMONIC_KEY = "MnemonicKey";
            Integer i = (Integer) a.getValue(MNEMONIC_KEY);
            if (i != null)
                setMnemonic(i.intValue());
        }
    }
    
    /**
     * Returns the action.
     * @return Action
     */
    public Action     getAction() {
        return _action;
    }

    /**
     * Sets the action.
     * @param action The action to set
     */
    public void setAction(Action action) {
        _action = action;
        removeActionListener(action);
        addActionListener(action);
        configurePropertiesFromAction(action);     
         
    }
    
    

    /**
     * @see java.awt.Component#isEnabled()
     */
    public boolean isEnabled() {
        if (_action != null) {
            return _action.isEnabled();
        } else
            return super.isEnabled();
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetChanged()
     */
    public void targetChanged() {
        setEnabled(isEnabled());
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetReasserted()
     */
    public void targetReasserted() {
        setEnabled(isEnabled());
    }

    /**
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
    }

    /**
     * Returns the container.
     * @return UMLUserInterfaceContainer
     */
    public UMLUserInterfaceContainer getContainer() {
        return _container;
    }

    /**
     * Sets the container.
     * @param container The container to set
     */
    public void setContainer(UMLUserInterfaceContainer container) {
        _container = container;
    }
}
