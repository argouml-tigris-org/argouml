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


// File: PropPanelNode.java
// Classes: PropPanelNode
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package org.argouml.uml.ui.foundation.core;

import java.awt.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import org.argouml.uml.ui.*;
import java.util.*;

public class PropPanelNode extends PropPanelClassifier {

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelNode() {
    super("Node",_nodeIcon,2);

    Class mclass = MNode.class;

    addCaption("Name:",1,0,0);
    addField(nameField,1,0,0);

    addCaption("Extends:",2,0,0);
    addField(extendsScroll,2,0,0);

    addCaption("Modifiers:",3,0,0);
    JPanel modifiersPanel = new JPanel(new GridLayout(0,3));
    modifiersPanel.add(new UMLCheckBox(localize("abstract"),this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
    modifiersPanel.add(new UMLCheckBox(localize("final"),this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
    modifiersPanel.add(new UMLCheckBox(localize("root"),this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
    addField(modifiersPanel,3,0,0);

    addCaption("Namespace:",4,0,0);
   addField(namespaceScroll,4,0,0);

    addCaption("Derived:",5,0,1);
    addField(derivedScroll,5,0,1);

    addCaption("Components:",0,1,1);
    JList compList = new UMLList(new UMLReflectionListModel(this,"component",true,"getResidents","setResidents",null,null),true);
    compList.setForeground(Color.blue);
    compList.setVisibleRowCount(1);
    addField(new JScrollPane(compList),0,1,1);

    new PropPanelButton(this,buttonPanel,_navUpIcon,localize("Go up"),"navigateUp",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon,localize("Go back"),localize("navigateBackAction"),"isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_navForwardIcon,localize("Go forward"),localize("navigateForwardAction"),"isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete node"),"removeElement",null);

  }

  public Collection getResidents() {
    Collection components = null;
    Object target = getTarget();
    if(target instanceof MNode) {
        components = ((MNode) target).getResidents();
    }
    return components;
  }

    public void setResidents(Collection components) {
        Object target = getTarget();
        if(target instanceof MNode) {
            ((MNode) target).setResidents(components);
        }
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Node");
    }



} /* end class PropPanelNode */

