// Copyright (c) 1996-02 The Regents of the University of California. All
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


package org.argouml.uml.ui.model_management;

import org.argouml.application.api.*;
import org.argouml.application.ArgoVersion;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.*;
import org.argouml.uml.ui.foundation.core.*;
import ru.novosoft.uml.behavior.use_cases.*;

import org.tigris.gef.util.Util;

/** PropPanelPackage defines the Property Panel for MPackage elements.
 */
public class PropPanelPackage extends PropPanelNamespace
implements PluggablePropertyPanel {

    protected PropPanelButton _stereotypeButton;

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelPackage() {
      this("Package", _packageIcon, 2);
  }

  PropPanelPackage(String name, ImageIcon icon, int columns) {
    super(name, icon, columns);

    Class mclass = MPackage.class;

    addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
    addField(nameField,1,0,0);

    addCaption(Argo.localize("UMLMenu", "label.stereotype"),2,0,0);
    addField(new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox),2,0,0);

    addCaption(Argo.localize("UMLMenu", "label.namespace"),3,0,0);
    addField(namespaceComboBox,3,0,0);

    addCaption(Argo.localize("UMLMenu", "label.modifiers"),4,0,0);
    JPanel modifiersPanel = new JPanel(new GridLayout(0,3));
    modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.abstract-lc"),this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
    modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.final-lc"),this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
    modifiersPanel.add(new UMLCheckBox(localize("root"),this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
    addField(modifiersPanel,4,0,0);

    addCaption("Specializations:",5,0,1);
    JList derivedList = new UMLList(new UMLSpecializationListModel(this,null,true),true);
    derivedList.setForeground(Color.blue);
    derivedList.setVisibleRowCount(1);
    derivedList.setFont(smallFont);
    JScrollPane derivedScroll = new JScrollPane(derivedList,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    addField(derivedScroll,5,0,0);

    addCaption("Generalizations:",0,1,0);
    JList extendsList = new UMLList(new UMLGeneralizationListModel(this,"generalization",true),true);
    extendsList.setBackground(getBackground());
    extendsList.setForeground(Color.blue);
    addField(new JScrollPane(extendsList,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),0,1,0);

    addCaption(Argo.localize("UMLMenu", "label.implements"),1,1,0);
    JList implementsList = new UMLList(new UMLClientDependencyListModel(this,null,true),true);
    implementsList.setBackground(getBackground());
    implementsList.setForeground(Color.blue);
    addField(new JScrollPane(implementsList,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),1,1,0);

    addCaption(Argo.localize("UMLMenu", "label.owned-elements"),2,1,1);
    UMLOwnedElementRootNode root = new UMLOwnedElementRootNode(this,"ownedElement",false);
    UMLTreeModel model = new UMLTreeModel(this,root);
    root.setModel(model);
    addField(new JScrollPane(new UMLTree(this,model,true)),2,1,1);

    new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateNamespace",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_classIcon, Argo.localize("UMLMenu", "button.add-class"),"addClass",null);
    new PropPanelButton(this,buttonPanel,_interfaceIcon, Argo.localize("UMLMenu", "button.add-interface"),"addInterface",null);
    new PropPanelButton(this,buttonPanel,_dataTypeIcon, Argo.localize("UMLMenu", "button.add-datatype"),"addDataType",null);
    _stereotypeButton = new PropPanelButton(this,buttonPanel,_stereotypeIcon, Argo.localize("UMLMenu", "button.add-stereotype"),"addStereotype",null);
    _stereotypeButton.setEnabled(false);
    new PropPanelButton(this,buttonPanel,_actorIcon,localize("Add actor"),"addActor",null);
    new PropPanelButton(this,buttonPanel,_useCaseIcon, Argo.localize("UMLMenu", "button.add-usecase"),"addUseCase",null);
    new PropPanelButton(this,buttonPanel,_packageIcon, Argo.localize("UMLMenu", "button.add-subpackage"),"addPackage",null);
    // new PropPanelButton(this,buttonPanel,_generalizationIcon, Argo.localize("UMLMenu", "button.add-generalization"),"addGeneralization",null);
    // new PropPanelButton(this,buttonPanel,_realizationIcon, Argo.localize("UMLMenu", "button.add-realization"),"addRealization",null);
    new PropPanelButton(this,buttonPanel,_deleteIcon, Argo.localize("UMLMenu", "button.delete-package"),"removeElement","isRemovableElement");

  }

    public void addStereotype() {
        Object target = getTarget();
        if(target instanceof MPackage) {
            MPackage pkg = (MPackage) target;
            MStereotype stereo = pkg.getFactory().createStereotype();
            pkg.addOwnedElement(stereo);
            navigateTo(stereo);
        }
    }

    public void addActor() {
        Object target = getTarget();
        if(target instanceof MPackage) {
            MPackage pkg = (MPackage) target;
            MActor actor = pkg.getFactory().createActor();
            pkg.addOwnedElement(actor);
            navigateTo(actor);
        }
    }

    public void addUseCase() {
        Object target = getTarget();
        if(target instanceof MPackage) {
            MPackage pkg = (MPackage) target;
            MUseCase useCase = pkg.getFactory().createUseCase();
            pkg.addOwnedElement(useCase);
            navigateTo(useCase);
        }
    }  

    public String formatElement(MModelElement element) {
        String formatted = null;
        Object target = getTarget();
        MNamespace ns = null;
        if(target instanceof MNamespace) {
            ns = (MNamespace) target;
        }
        return getProfile().formatElement(element,ns);
    }

    public void addPackage(MModelElement element) {
        addPackage();
    }

    public void addDataType(MModelElement element) {
        addDataType();
    }

    public void addActor(MModelElement element) {
        addActor();
    }

    public void addInterface(MModelElement element) {
        addInterface();
    }

    public void addUseCase(MModelElement element) {
        addUseCase();
    }

    public void deleteElement(MModelElement element) {
        UmlFactory.getFactory().delete(element);
    }

    public void addStereotype(MModelElement element) {
        addStereotype();
    }
    
    public void addDataType() {
        Object target = getTarget();
        if(target instanceof MNamespace) {
            MNamespace ns = (MNamespace) target;
            MModelElement ownedElem = CoreFactory.getFactory().createDataType();
            ns.addOwnedElement(ownedElem);
            navigateTo(ownedElem);
        }
    }


    public void openElement(MModelElement element) {
    }

    public void navigateElement(MModelElement element) {
        navigateTo(element);
    }

    public void addClass(MModelElement element) {
        addClass();
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Package") ||
            baseClass.equals("Namespace");
    }

    public Class getClassForPanel() {
        return MPackageImpl.class;
    }

    public String getModuleName() { return "PropPanelPackage"; }
    public String getModuleDescription() { return "Property Panel for Package"; }
    public String getModuleAuthor() { return "ArgoUML Core"; }
    public String getModuleVersion() { return ArgoVersion.getVersion(); }
    public String getModuleKey() { return "module.propertypanel.package"; }


} /* end class PropPanelPackage */
