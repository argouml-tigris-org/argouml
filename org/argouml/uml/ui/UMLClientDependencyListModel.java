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

// 4 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Fixed use of addAtUtil,
// so test is with getModelElementSize() rather than getSize(). Then fixed the
// associated errors that fell out due to misunderstanding of NSUML (you only
// need to set one end).


package org.argouml.uml.ui;

import ru.novosoft.uml.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import java.util.*;
import java.awt.*;

/**
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by {@link org.argouml.uml.ui.foundation.core.UMLModelElementClientDependencyListModel},
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLClientDependencyListModel extends UMLModelElementListModel  {

    private final static String _nullLabel = "(null)";
    
    public UMLClientDependencyListModel(UMLUserInterfaceContainer container,String property,boolean showNone) {
        super(container,property,showNone);
    }

    private Collection getClientDependencies() {
        Collection dependencies = null;
        Object target = getTarget();
        if(target instanceof MModelElement) {
            MModelElement genElement = (MModelElement) target;
            dependencies = genElement.getClientDependencies();
        }
        return dependencies;
    }
    
    protected int recalcModelElementSize() {
        int size = 0;
        Collection dependencies = getClientDependencies();
        if(dependencies != null) {
            size = dependencies.size();
        }
        return size;
    }
    
    protected MModelElement getModelElementAt(int index) {
        return elementAtUtil(getClientDependencies(),index,MDependency.class);
    }
        
    
    public Object formatElement(MModelElement element) {
        Object value = _nullLabel;
	if ((element!=null) && (element instanceof MDependency)){
	    MDependency dependency = (MDependency) element;
	    Collection target = dependency.getSuppliers();
	    if(target != null) {
		value = getContainer().formatCollection(target.iterator());
	    }
	}
        return value;
    }
    /**
     * <p>Add a new abstraction relationship.</p>
     *
     * <p>Current implementation cures an earlier bug, which set both ends of
     *   the NSUML object, thereby effectively adding the dependency twice (if
     *   you add one end, NSUML does the other for you.</p>
     *
     * @param index  The index of the item in the list on which this add
     *   operation was invoked.
     */

    public void add(int index) {

        // We can work for any model element. UML probably is more restrictive
        // than that, since we are actually creating associations rather than
        // dependencies here. Give up if it isn't a model element or we don't
        // have a namespace.

        Object target = getTarget();

        if (!(target instanceof MModelElement)) {
            return;
        }

        MModelElement modelElement = (MModelElement) target;
        MNamespace    ns           = modelElement.getNamespace();

        if (ns == null) {
            return;
        }

        // Get the new abstraction from the factory and add it to the
        // namespace.

        MAbstraction newAbstraction = ns.getFactory().createAbstraction();
        ns.addOwnedElement(newAbstraction);

        // We need not set the clients array up - that will happen when we add
        // to the client dependency of the model element.

        // Fixed to look at the true list size, not counting the "empty"
        // entry.

        if(index == getModelElementSize()) {    
            modelElement.addClientDependency(newAbstraction);
        }
        else {
            modelElement.setClientDependencies(
                addAtUtil(modelElement.getClientDependencies(),
                          newAbstraction, index));
        }

        // Advise Swing that we have added something at this index and
        // navigate there.

        fireIntervalAdded(this,index,index);
        navigateTo(newAbstraction);
    }
    
    public void delete(int index) {
       Object target = getTarget();
       if(target instanceof MModelElement) { 
            Object obj = elementAtUtil(getClientDependencies(),index,MDependency.class);
            if(obj != null) {
                MDependency dep = (MDependency) obj;
                ((MModelElement) target).removeClientDependency(dep);
            }
        }
    }
    
    public void moveUp(int index) {
        Object target = getTarget();
        if(target instanceof MModelElement) {
            MModelElement targetElement = (MModelElement) target;
            targetElement.setClientDependencies(moveUpUtil(targetElement.getClientDependencies(),index));
        }
    }
    
    public void moveDown(int index) {
        Object target = getTarget();
        if(target instanceof MModelElement) {
            MModelElement targetElement = (MModelElement) target;
            targetElement.setClientDependencies(moveDownUtil(targetElement.getClientDependencies(),index));
        }
    }
    /**
     *  This method builds a context (pop-up) menu for the list.  
     *
     *  @param popup popup menu
     *  @param index index of selected list item
     *  @return "true" if popup menu should be displayed
     */
    public boolean buildPopup(JPopupMenu popup,int index) {
        UMLUserInterfaceContainer container = getContainer();
        UMLListMenuItem open = new UMLListMenuItem(container.localize("Open"),this,"open",index);
        UMLListMenuItem delete = new UMLListMenuItem(container.localize("Delete"),this,"delete",index);
        if(getModelElementSize() <= 0) {
            open.setEnabled(false);
            delete.setEnabled(false);
        }

        popup.add(open);
        UMLListMenuItem add =new UMLListMenuItem(container.localize("Add"),this,"add",index);
        if(_upper >= 0 && getModelElementSize() >= _upper) {
            add.setEnabled(false);
        }
        popup.add(add);
        popup.add(delete);
        /*
        UMLListMenuItem moveUp = new UMLListMenuItem(container.localize("Move Up"),this,"moveUp",index);
        if(index == 0) moveUp.setEnabled(false);
        popup.add(moveUp);
        UMLListMenuItem moveDown = new UMLListMenuItem(container.localize("Move Down"),this,"moveDown",index);
        if(index == getSize()-1) moveDown.setEnabled(false);
        popup.add(moveDown);
        */
        return true;
    }
    
}


