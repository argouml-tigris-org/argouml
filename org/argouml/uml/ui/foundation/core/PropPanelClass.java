// Copyright (c) 1996-2001 The Regents of the University of California. All
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

// 21 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Changed to use the
// labels "Generalizes:" and "Specializes:" for inheritance.

// 4 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Labels corrected to
// "Generalizations:" and "Specializations".


package org.argouml.uml.ui.foundation.core;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.swingext.LabelledLayout;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLCheckBox;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLReflectionBooleanProperty;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MNamespace;

/**
 * @todo this property panel needs refactoring to remove dependency on
 *       old gui components.
 */
public class PropPanelClass extends PropPanelClassifier {

    private JScrollPane _attributeScroll;
    private JScrollPane _operationScroll;
    
    private static UMLClassAttributeListModel attributeListModel =
        new UMLClassAttributeListModel();
    
    private static UMLClassOperationListModel operationListModel =
        new UMLClassOperationListModel();
    
  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelClass() { 
    super("Class", ConfigLoader.getTabPropsOrientation());
    Class mclass = MClass.class;

    addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
    addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),getStereotypeBox()));
    addField(Argo.localize("UMLMenu", "label.namespace"), getNamespaceComboBox());
    _modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.active-uc"),this,new UMLReflectionBooleanProperty("isActive",mclass,"isActive","setActive")));
    addField(Argo.localize("UMLMenu", "label.modifiers"), _modifiersPanel);
    addField(Argo.localize("UMLMenu", "label.namespace-visibility"), getNamespaceVisibilityPanel());
    
    add(LabelledLayout.getSeperator());
    
    addField(Argo.localize("UMLMenu", "label.clientdependency"), getClientDependencyScroll());
    addField(Argo.localize("UMLMenu", "label.supplierdependency"), getSupplierDependencyScroll());
    addField(Argo.localize("UMLMenu", "label.generalizations"), getGeneralizationScroll());
    addField(Argo.localize("UMLMenu", "label.specializations"), getSpecializationScroll());
    
    add(LabelledLayout.getSeperator());
     
    addField(Argo.localize("UMLMenu", "label.attributes"), getAttributeScroll());
    addField(Argo.localize("UMLMenu", "label.association-ends"), getAssociationEndScroll());
    addField(Argo.localize("UMLMenu", "label.operations"), getOperationScroll());
    addField(Argo.localize("UMLMenu", "label.owned-elements"), getOwnedElementsScroll());
    
  
    new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateUp",null);   
    new PropPanelButton(this,buttonPanel,_addAttrIcon, Argo.localize("UMLMenu", "button.add-attribute"),"addAttribute",null);
    new PropPanelButton(this,buttonPanel,_addOpIcon, Argo.localize("UMLMenu", "button.add-operation"),"addOperation",null);
    new PropPanelButton(this,buttonPanel,_innerClassIcon, Argo.localize("UMLMenu", "button.add-inner-class"),"addInnerClass",null);
    new PropPanelButton(this,buttonPanel,_classIcon, Argo.localize("UMLMenu", "button.add-new-class"),"newClass",null);
    new PropPanelButton(this,buttonPanel,_deleteIcon, Argo.localize("UMLMenu", "button.delete-class"),"removeElement",null);

  }

    public void addInnerClass() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MClass inner = CoreFactory.getFactory().buildClass(classifier);
            TargetManager.getInstance().setTarget(inner);
        }
    }

    public void newClass() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MNamespace ns = classifier.getNamespace();
            if(ns != null) {
                MClassifier peer = CoreFactory.getFactory().buildClass(ns);
                TargetManager.getInstance().setTarget(peer);
            }
        }
       
    }

    /**
     * Returns the operationScroll.
     * @return JScrollPane
     */
    public JScrollPane getOperationScroll() {
        if (_operationScroll == null) {
            JList list = new UMLLinkedList(operationListModel);
            _operationScroll = new JScrollPane(list);
        }
        return _operationScroll;
    }

    /**
     * Returns the attributeScroll.
     * @return JScrollPane
     */
    public JScrollPane getAttributeScroll() {
        if (_attributeScroll == null) {
            JList list = new UMLLinkedList(attributeListModel);
            _attributeScroll = new JScrollPane(list);
        }
        return _attributeScroll;
    }
    

} /* end class PropPanelClass */
