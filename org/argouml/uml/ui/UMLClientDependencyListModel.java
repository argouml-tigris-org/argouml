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

package org.argouml.uml.ui;
import ru.novosoft.uml.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import java.util.*;
import java.awt.*;

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
        MDependency dependency = (MDependency) element;
        Collection target = dependency.getSuppliers();
        if(target != null) {
            value = getContainer().formatCollection(target.iterator());
        }
        return value;
    }
 
    public void open(int index) {
        navigateTo(elementAtUtil(getClientDependencies(),index,MDependency.class));
    }    

    public void add(int index) {
       Object target = getTarget();
       if(target instanceof MModelElement) {
            MAbstraction newAbstraction = new MAbstractionImpl();
            ArrayList clients = new ArrayList(1);
            clients.add(0,target);
            newAbstraction.setClients(clients);
            MModelElement targetElement = (MModelElement) target;
            if(index == getSize()) {    
                targetElement.addClientDependency(newAbstraction);
            }
            else {
                targetElement.setClientDependencies(addAtUtil(targetElement.getClientDependencies(),newAbstraction,index));
            }
            navigateTo(newAbstraction);
        }
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
    
    
}


