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

// File: PropPanelUseCase.java
// Classes: PropPanelUseCase
// Original Author: your email address here
// $Id$

// 21 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Changed to use the
// labels "Generalizes:" for inheritance (needs Specializes some time).


package org.argouml.uml.ui.behavior.use_cases;

import org.argouml.uml.ui.*;
import org.argouml.uml.ui.foundation.core.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.model_management.*;

public class PropPanelUseCase extends PropPanelClassifier {


  public PropPanelUseCase() {
    super("UseCase", _useCaseIcon,3);

    Class mclass = MUseCase.class;

    addCaption("Name:",1,0,0);
    addField(nameField,1,0,0);

    addCaption("Stereotype:",2,0,0);
    addField(new UMLComboBoxNavigator(this,"NavStereo",stereotypeBox),2,0,0);

    addCaption("Namespace:",3,0,0);
    addField(namespaceScroll,3,0,0);

    addCaption("Modifiers:",4,0,1);
    JPanel modifiersPanel = new JPanel(new GridLayout(0,2));
    modifiersPanel.add(new UMLCheckBox(localize("Abstract"),this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
    modifiersPanel.add(new UMLCheckBox(localize("Final"),this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
    modifiersPanel.add(new UMLCheckBox(localize("Root"),this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
    addField(modifiersPanel,4,0,0);


    //
    //  Generalization was labeled "Extends" in PropPanelClass and others
    //     but since extension has a specific meaning in use cases a
    //     different term had to be used
    //
    addCaption("Generalizes:",0,1,0);
    addField(extendsScroll,0,1,0);

    addCaption("Extends:",1,1,0);
    JList extendList = new UMLList(new UMLExtendListModel(this,"extend",true),true);
    extendList.setBackground(getBackground());
    extendList.setForeground(Color.blue);
    JScrollPane extendScroll = new JScrollPane(extendList,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    addField(extendScroll,1,1,0);

    addCaption("Includes:",2,1,0);
    JList includeList = new UMLList(new UMLIncludeListModel(this,"include",true),true);
    includeList.setBackground(getBackground());
    includeList.setForeground(Color.blue);
    JScrollPane includeScroll = new JScrollPane(includeList,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    addField(includeScroll,2,1,0);

    addCaption("Extension Points:",3,1,0.25);
    JList extensionPoints = new UMLList(new UMLExtensionPointListModel(this,null,true),true);
    extensionPoints.setForeground(Color.blue);
    extensionPoints.setVisibleRowCount(1);
    JScrollPane extensionPointsScroll = new JScrollPane(extensionPoints);
    addField(extensionPointsScroll,3,1,0.25);

    addCaption("Associations:",0,2,0.25);
    addField(connectScroll,0,2,0.25);

    addCaption("Operations:",1,2,0.25);
    addField(opsScroll,1,2,0.25);

    addCaption("Attributes:",2,2,0.25);
    addField(attrScroll,2,2,0.25);

    new PropPanelButton(this,buttonPanel,_navUpIcon,localize("Go up"),"navigateNamespace",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon,localize("Go back"),"navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_navForwardIcon,localize("Go forward"),"navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_useCaseIcon,localize("New use case"),"newUseCase",null);
    new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete"),"removeElement",null);

  }


    public void newUseCase() {
        Object target = getTarget();
        if(target instanceof MUseCase) {
            MNamespace ns = ((MUseCase) target).getNamespace();
            if(ns != null) {
                MUseCase useCase = ns.getFactory().createUseCase();
                ns.addOwnedElement(useCase);
            }
        }
    }

   protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("UseCase") ||
            baseClass.equals("Classifier") ||
            baseClass.equals("GeneralizableElement") ||
            baseClass.equals("Namespace");
    }


} /* end class PropPanelUseCase */
