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


package org.argouml.uml.ui;

import javax.swing.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.use_cases.*;

import org.argouml.application.api.*;
import org.argouml.model.uml.UmlFactory;

/**
 *   This class implements a list model for the subpackages of a package.
 *   Used with a UMLList to display a list of attributes.  Since packages
 *   may be mixed with other model elements,
 *   this class implements a cache of packages that are kept synchronized
 *   with the owned elements.
 *
 *   @author Curt Arnold
 *   @see UMLModelElementListModel
 *   @see UMLList
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by ?,
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLClassifiersListModel extends UMLOwnedElementListModel  {

    /**
     *   Creates a new packages list model
     *   @param container the container (typically a PropPanelPackage)
     *                    that provides access to the target classifier.
     *   @param property  a string that specifies the name of an event that should force a refresh
     *                       of the list model.  A null value will cause all events to trigger a refresh.
     *   @param showNone  if true, an element labelled "none" will be shown where there are
     *                        no actual entries in the list.
     */    
    public UMLClassifiersListModel(UMLUserInterfaceContainer container,String property,boolean showNone) {
        super(container,property,showNone);
    }
    
    /**
     *   Called to determine if a particular feature of the underlying collection
     *   should be in the cached list of model elements.
     *   @param obj object to be considered.
     *   @return true if object is appropriate for this list.
     */
    public boolean isProperClass(Object obj) {
        return obj instanceof MClassifier;
    }

    /**
     *  This method builds a context (pop-up) menu for the list.  This method
     *  may be overriden for lists that have additional menu items or when
     *  the default list of actions is inappropriate.
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
        JMenu addmenu = new JMenu(container.localize("Add"));
        addmenu.add(new UMLListMenuItem(Argo.localize("UMLMenu", "menu.popup.add-actor"),this,"addActor",index));
        addmenu.add(new UMLListMenuItem(Argo.localize("UMLMenu", "menu.popup.add-class"),this,"addClass",index));
        addmenu.add(new UMLListMenuItem(Argo.localize("UMLMenu", "menu.popup.add-datatype"),this,"addDatatype",index));
        addmenu.add(new UMLListMenuItem(Argo.localize("UMLMenu", "menu.popup.add-exception"),this,"addException",index));
        addmenu.add(new UMLListMenuItem(Argo.localize("UMLMenu", "menu.popup.add-interface"),this,"addInterface",index));
        addmenu.add(new UMLListMenuItem(Argo.localize("UMLMenu", "menu.popup.add-signal"),this,"addSignal",index));
        addmenu.add(new UMLListMenuItem(Argo.localize("UMLMenu", "menu.popup.add-usecase"),this,"addUseCase",index));
        popup.add(addmenu);
        popup.add(delete);

        UMLListMenuItem moveUp = new UMLListMenuItem(container.localize("Move Up"),this,"moveUp",index);
        if(index == 0) moveUp.setEnabled(false);
        popup.add(moveUp);
        UMLListMenuItem moveDown = new UMLListMenuItem(container.localize("Move Down"),this,"moveDown",index);
        if(index == getSize()-1) moveDown.setEnabled(false);
        popup.add(moveDown);
        return true;
    }
    
    public void addDatatype(int index) {
        addElement(UmlFactory.getFactory().getCore().createDataType(),index);
    }
    
    public void addClass(int index) {
        addElement(UmlFactory.getFactory().getCore().createClass(),index);
    }
    
    public void addInterface(int index) {
        addElement(UmlFactory.getFactory().getCore().createInterface(),index);
    }
    
    public void addException(int index) {
        addElement(UmlFactory.getFactory().getCommonBehavior().createException(),index);
    }
    
    public void addSignal(int index) {
        addElement(UmlFactory.getFactory().getCommonBehavior().createSignal(),index);
    }
    
    public MModelElement createModelElement() {
        return UmlFactory.getFactory().getCore().createClass();
    }
    
    public void addActor(int index) {
        addElement(UmlFactory.getFactory().getUseCases().createActor(),index);
    }
    
    public void addUseCase(int index) {
        addElement(UmlFactory.getFactory().getUseCases().createUseCase(),index);
    }
        
}


