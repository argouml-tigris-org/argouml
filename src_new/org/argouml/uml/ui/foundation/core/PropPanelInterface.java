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

package org.argouml.uml.ui.foundation.core;

import java.awt.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import org.argouml.uml.ui.*;

public class PropPanelInterface extends PropPanelClassifier {

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelInterface() {
    super("Interface", _interfaceIcon,3);

    Class mclass = MInterface.class;

    addCaption("Name:",1,0,0);
    addField(nameField,1,0,0);

    addCaption("Stereotype:",2,0,0);
    //    stereotypeBox.setEnabled(false);
    addField(new UMLComboBoxNavigator(this,"NavStereo",stereotypeBox),2,0,0);

    addCaption("Namespace:",3,0,0);
    addField(namespaceScroll,3,0,0);

    addCaption("Specializes:",4,0,0);
    addField(extendsScroll,4,0,0);

    addCaption("Modifiers:",5,0,1);
    JPanel modifiersPanel = new JPanel(new GridLayout(0,2));
    modifiersPanel.add(new UMLCheckBox(localize("Public"),this,new UMLEnumerationBooleanProperty("visibility",mclass,"getVisibility","setVisibility",MVisibilityKind.class,MVisibilityKind.PUBLIC,null)));
    modifiersPanel.add(new UMLCheckBox(localize("Abstract"),this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
    modifiersPanel.add(new UMLCheckBox(localize("Final"),this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
    modifiersPanel.add(new UMLCheckBox(localize("Root"),this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
    addField(modifiersPanel,5,0,1);

    addCaption("Associations:",0,1,0.3);
    addField(connectScroll,0,1,0.3);

    addCaption("Implementations:",1,1,0.4);
    JList implementations = new UMLList(new UMLSupplierDependencyListModel(this,null,true),true);
    implementations.setForeground(Color.blue);
    implementations.setVisibleRowCount(1);
    implementations.setFont(smallFont);
    addField(new JScrollPane(implementations),1,1,0.4);

    addCaption("Generalizes:",2,1,0.3);
    addField(derivedScroll,2,1,0.3);

    addCaption("Operations:",0,2,0.5);
    addField(opsScroll,0,2,0.5);

    // addCaption("Attributes:",1,2,0.5);
    // addField(attrScroll,1,2,0.5);

    new PropPanelButton(this,buttonPanel,_navUpIcon,localize("Go up"),"navigateNamespace",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon,localize("Go back"),"navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_navForwardIcon,localize("Go forward"),"navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_addOpIcon,localize("Add operation"),"addOperation",null);
    //new PropPanelButton(this,buttonPanel,_generalizationIcon,localize("Add generalization"),"addGeneralization",null);
    //new PropPanelButton(this,buttonPanel,_realizationIcon,localize("Add realization"),"addRealization",null);
    new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete interface"),"removeElement",null);
    //does this make sense?? new PropPanelButton(this,buttonPanel,_interfaceIcon,localize("New interface"),"newInterface",null);

  }

  public void newInterface() {
    Object target = getTarget();
    if(target instanceof MInterface) {
        MInterface iface = (MInterface) target;
        MInterface newInterface = iface.getFactory().createInterface();
        iface.getNamespace().addOwnedElement(newInterface);
        navigateTo(newInterface);
    }
  }



    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Interface") ||
            baseClass.equals("Classifier") ||
            baseClass.equals("GeneralizableElement");
    }


} /* end class PropPanelInterface */

