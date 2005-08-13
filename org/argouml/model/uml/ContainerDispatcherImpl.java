// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.model.uml;

import java.awt.Container;
import java.beans.PropertyChangeEvent;

import org.argouml.model.ContainerDispatcher;
import org.argouml.model.Model;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

/**
 * This listens for events on a component and dispatches the events to all its
 * interested child components.
 */
public class ContainerDispatcherImpl 
    implements ContainerDispatcher, MElementListener {

    private Container container;
    
    /**
     * The constructor.
     * 
     * @param ctr the container
     */
    public ContainerDispatcherImpl (Container ctr) {
        this.container = ctr;
    }
    
    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent event) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent mee) {
//        UMLChangeDispatch dispatch = new UMLChangeDispatch(container, 0);
//        dispatch.propertySet((MElementEvent)mee);
//        SwingUtilities.invokeLater(dispatch);
    }

    /**
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent mee) {
//        UMLChangeDispatch dispatch = new UMLChangeDispatch(container, 0);
//        dispatch.listRoleItemSet((MElementEvent)mee);
//        SwingUtilities.invokeLater(dispatch);
    }

    /**
     * @see ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent mee) {
//        UMLChangeDispatch dispatch = new UMLChangeDispatch(container, 0);
//        dispatch.recovered((MElementEvent)mee);
//        SwingUtilities.invokeLater(dispatch);
    }

    /**
     * @see ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent mee) {
//        UMLChangeDispatch dispatch = new UMLChangeDispatch(container, 0);
//        dispatch.removed((MElementEvent)mee);
//        SwingUtilities.invokeLater(dispatch);
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent mee) {
//        UMLChangeDispatch dispatch = new UMLChangeDispatch(container, 0);
//        dispatch.roleAdded((MElementEvent)mee);
//        SwingUtilities.invokeLater(dispatch);
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent mee) {
//        UMLChangeDispatch dispatch = new UMLChangeDispatch(container, 0);
//        dispatch.roleRemoved((MElementEvent)mee);
//        SwingUtilities.invokeLater(dispatch);
    }
    
    /**
     * Calling this method with an array of metaclasses (for example,
     * MClassifier.class) will result in the prop panel propagating
     * any name changes or removals on any object that on the same
     * event queue as the target that is assignable to one of the
     * metaclasses.<p>
     *
     * <em>Note</em>. Despite the name, the old implementation tried
     * to listen for ownedElement and baseClass events as well as name
     * events. We incorporate all these.<p>
     *
     * <em>Note</em> Reworked the implementation to use the new
     * UmlModelEventPump mechanism. In the future proppanels should
     * register directly with UmlModelEventPump IF they are really
     * interested in the events themselves. If components on the
     * proppanels are interested, these components should register
     * themselves.<p>
     *
     * @deprecated by Jaap 3 Nov 2002 (ArgoUml version unknown -
     * earlier than 0.13.5), replaced by
     * {@link org.argouml.model.uml.UmlModelEventPump#addModelEventListener(
     *            Object , Object)}.
     *             since components should register themselves.
     *
     * @param metaclasses  The metaclass array we wish to listen to.
     */
    public void setNameEventListening(Object[] metaclasses) {

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
            Object clazz = metaclasses[i];
            if (Model.getCoreHelper().isSubType(
                    Model.getMetaTypes().getNamespace(),
                    clazz)) {
                UmlModelEventPump.getPump()
                    .addClassModelEventListener(this, clazz, "ownedElement");
            }
            if (Model.getCoreHelper().isSubType(
                    Model.getMetaTypes().getModelElement(),
                    clazz)) {
                UmlModelEventPump.getPump()
                    .addClassModelEventListener(this, clazz, "name");
            }
            if (clazz.equals(Model.getMetaTypes().getStereotype())) {
                UmlModelEventPump.getPump()
                    .addClassModelEventListener(this, clazz, "baseClass");
            }
        }
    }
}
