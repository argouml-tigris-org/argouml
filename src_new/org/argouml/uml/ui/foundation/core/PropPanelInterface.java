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

// 21 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Changed to use the
// labels "Generalizes:" and "Specializes:" for inheritance.

// 4 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Labels corrected to
// "Generalizations:" and "Specializations".


package org.argouml.uml.ui.foundation.core;

import org.argouml.api.ObjectFactoryManager;
import org.argouml.api.model.uml.Uml;
import org.argouml.application.api.Argo;
import org.argouml.model.uml.foundation.core.CoreFactory;

import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.foundation.core.MInterface;

public class PropPanelInterface extends PropPanelClassifier {

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelInterface() {
    super("Interface", ConfigLoader.getTabPropsOrientation());

    Class mclass = MInterface.class;

    addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
    addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),getStereotypeBox()));
    addField(Argo.localize("UMLMenu", "label.namespace"),getNamespaceComboBox());
    /*
    JPanel modifiersPanel = new JPanel(new GridLayout(0,2));
    modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.visibility.public-uc"),this,new UMLEnumerationBooleanProperty("visibility",mclass,"getVisibility","setVisibility",MVisibilityKind.class,MVisibilityKind.PUBLIC,null)));
    modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.abstract-uc"),this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
    modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.final-uc"),this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
    modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.root-uc"),this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
    */
    addField(Argo.localize("UMLMenu", "label.modifiers"), _modifiersPanel);
    addField(Argo.localize("UMLMenu", "label.namespace-visibility"), getNamespaceVisibilityPanel());

    addSeperator();

    addField(Argo.localize("UMLMenu", "label.generalizations"), getGeneralizationScroll());
    addField(Argo.localize("UMLMenu", "label.specializations"), getSpecializationScroll());

    addSeperator();

    addField(Argo.localize("UMLMenu", "label.association-ends"), getAssociationEndScroll());
    addField(Argo.localize("UMLMenu", "label.operations"), getFeatureScroll());

    new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateNamespace",null);
    new PropPanelButton(this,buttonPanel,_addOpIcon, Argo.localize("UMLMenu", "button.add-operation"),"addOperation",null);
    //new PropPanelButton(this,buttonPanel,_generalizationIcon, Argo.localize("UMLMenu", "button.add-generalization"),"addGeneralization",null);
    //new PropPanelButton(this,buttonPanel,_realizationIcon, Argo.localize("UMLMenu", "button.add-realization"),"addRealization",null);
    new PropPanelButton(this,buttonPanel,_deleteIcon, Argo.localize("UMLMenu", "button.delete-interface"),"removeElement",null);
    //does this make sense?? new PropPanelButton(this,buttonPanel,_interfaceIcon, Argo.localize("UMLMenu", "button.add-new-interface"),"newInterface",null);

  }

  public void newInterface() {
    Object target = getTarget();
    if(target instanceof MInterface) {
        MInterface iface = (MInterface) target;

//        MInterface newInterface = (MInterface)ObjectFactoryManager.getUmlFactory().create(Uml.INTERFACE);
		  MInterface newInterface = (MInterface)ObjectFactoryManager.getUmlFactory().create(Uml.INTERFACE);
        iface.getNamespace().addOwnedElement(newInterface);
        TargetManager.getInstance().setTarget(newInterface);
    }
  }

} /* end class PropPanelInterface */

