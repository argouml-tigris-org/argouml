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


import java.awt.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import org.argouml.uml.ui.*;

public class PropPanelClass extends PropPanelClassifier {


  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelClass() {
    super("Class",_classIcon, 3);

    Class mclass = MClass.class;

    //
    //   this will cause the components on this page to be notified
    //      anytime a stereotype, namespace, operation, etc
    //      has its name changed or is removed anywhere in the model
    Class[] namesToWatch = { MStereotype.class,MNamespace.class,MOperation.class,
        MParameter.class,MAttribute.class,MAssociation.class,MClassifier.class };
    setNameEventListening(namesToWatch);

    addCaption("Name:",1,0,0);
    addField(nameField,1,0,0);

    addCaption("Stereotype:",2,0,0);
    addField(new UMLComboBoxNavigator(this,"NavStereo",stereotypeBox),2,0,0);

    addCaption("Namespace:",3,0,0);
    addLinkField(namespaceScroll,3,0,0);

    addCaption("Generalizations:",4,0,0);
    addField(extendsScroll,4,0,0);

    addCaption("Modifiers:",5,0,1);
    _modifiersPanel.add(new UMLCheckBox(localize("active"),this,new UMLReflectionBooleanProperty("isActive",mclass,"isActive","setActive")));
    addField(_modifiersPanel,5,0,0);

    addCaption("Associations:",0,1,0);
    addField(connectScroll,0,1,0.5);

    addCaption("Implements:",1,1,0);
    addField(implementsScroll,1,1,0.3);

    addCaption("Specializations:",2,1,0);
    addField(derivedScroll,2,1,0.2);

    addCaption("Operations:",0,2,0.4);
    addField(opsScroll,0,2,0.4);

    addCaption("Attributes:",1,2,0.4);
    addField(attrScroll,1,2,0.4);

    addCaption("Owned Elements:",2,2,0.2);
    addField(innerScroll,2,2,0.2);

    new PropPanelButton(this,buttonPanel,_navUpIcon,localize("Go up"),"navigateNamespace",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon,localize("Go back"),"navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_navForwardIcon,localize("Go forward"),"navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_addAttrIcon,localize("Add attribute"),"addAttribute",null);
    new PropPanelButton(this,buttonPanel,_addOpIcon,localize("Add operation"),"addOperation",null);
    //does this make sense??    new PropPanelButton(this,buttonPanel,_addAssocIcon,localize("Add association"),"addAssociation",null);
    //new PropPanelButton(this,buttonPanel,_generalizationIcon,localize("Add generalization"),"addGeneralization",null);
    //new PropPanelButton(this,buttonPanel,_realizationIcon,localize("Add realization"),"addRealization",null);
    //does this make sense??    new PropPanelButton(this,buttonPanel,_classIcon,localize("New class"),"newClass",null);
    new PropPanelButton(this,buttonPanel,_innerClassIcon,localize("Add inner class"),"addInnerClass",null);
    new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete class"),"removeElement",null);

  }

    public void addInnerClass() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MClassifier inner = classifier.getFactory().createClass();
            classifier.addOwnedElement(inner);
            navigateTo(inner);
        }
    }

    public void newClass() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MNamespace ns = classifier.getNamespace();
            if(ns != null) {
                MClassifier peer = classifier.getFactory().createClass();
                ns.addOwnedElement(peer);
                navigateTo(peer);
            }
        }
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Class") ||
            baseClass.equals("Classifier") ||
            baseClass.equals("GeneralizableElement");
    }


} /* end class PropPanelClass */
