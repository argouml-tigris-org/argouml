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

// File: PropPanelActor.java
// Classes: PropPanelActor
// Original Author: jrobbins@ics.uci.edu
// $Id$

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

public class PropPanelActor extends PropPanelClassifier {


  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelActor() {
    super("Actor", _actorIcon, 2);

    Class mclass = MActor.class;

    addCaption("Name:",1,0,0);
    addField(nameField,1,0,0);

    addCaption("Stereotype:",2,0,0);
    addField(new UMLComboBoxNavigator(this,"NavStereo",stereotypeBox),2,0,0);

    addCaption("Namespace:",3,0,0);
    addLinkField(namespaceScroll,3,0,0);

    addCaption("Modifiers:",4,0,1);
    JPanel modifiersPanel = new JPanel(new GridLayout(0,3));
    modifiersPanel.add(new UMLCheckBox(localize("Abstract"),this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
    modifiersPanel.add(new UMLCheckBox(localize("Final"),this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
    modifiersPanel.add(new UMLCheckBox(localize("Root"),this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
    addField(modifiersPanel,4,0,0);

    addCaption("Derived:",0,1,0);
    addField(derivedScroll,0,1,0);

    addCaption("Extends:",1,1,0);
    addLinkField(extendsScroll,1,1,0);

    addCaption("Associations:",2,1,0.5);
    addField(connectScroll,2,1,0.5);

    addCaption("Implements:",3,1,0.5);
    addField(implementsScroll,3,1,0.5);

    new PropPanelButton(this,buttonPanel,_navUpIcon,localize("Go up"),"navigateNamespace",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon,localize("Go back"),"navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_navForwardIcon,localize("Go forward"),"navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_actorIcon,localize("New actor"),"newActor",null);
    new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete"),"removeElement",null);

  }


    public void newActor() {
        Object target = getTarget();
        if(target instanceof MActor) {
            MNamespace ns = ((MActor) target).getNamespace();
            if(ns != null) {
                MActor actor = ns.getFactory().createActor();
                ns.addOwnedElement(actor);
            }
        }
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        boolean isAcceptible = baseClass.equals("Actor");
        if(!isAcceptible) {
            isAcceptible = super.isAcceptibleBaseMetaClass(baseClass);
        }
        return isAcceptible;
    }


} /* end class PropActor */
