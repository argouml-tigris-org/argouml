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



// File: PropPanelPseudostate.java
// Classes: PropPanelPseudostate
// Original Author: your email address here
// $Id$

package org.argouml.uml.ui.behavior.state_machines;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import org.argouml.application.api.*;
import org.argouml.uml.ui.*;

import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

public class PropPanelPseudostate extends PropPanelStateVertex {

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelPseudostate() {
    super("Pseudostate", null,2);

    Class mclass = MPseudostate.class;

    addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
    addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),1,0,0);

    addCaption("Kind:",2,0,1);

    JPanel kindPanel = new JPanel(new GridLayout(0,2));
    ButtonGroup kindGroup = new ButtonGroup();
    UMLRadioButton junctionButton = new UMLRadioButton("junction",this,new UMLEnumerationBooleanProperty("kind",mclass,"getKind","setKind",MPseudostateKind.class,MPseudostateKind.JUNCTION,null));
    junctionButton.setEnabled(false);
    kindPanel.add(junctionButton);
    kindGroup.add(junctionButton);

    UMLRadioButton branchButton = new UMLRadioButton("branch",this,new UMLEnumerationBooleanProperty("kind",mclass,"getKind","setKind",MPseudostateKind.class,MPseudostateKind.BRANCH,null));
    branchButton.setEnabled(false);
    kindPanel.add(branchButton);
    kindGroup.add(branchButton);

    UMLRadioButton forkButton = new UMLRadioButton("fork",this,new UMLEnumerationBooleanProperty("kind",mclass,"getKind","setKind",MPseudostateKind.class,MPseudostateKind.FORK,null));
    forkButton.setEnabled(false);
    kindPanel.add(forkButton);
    kindGroup.add(forkButton);

    UMLRadioButton joinButton = new UMLRadioButton("join",this,new UMLEnumerationBooleanProperty("kind",mclass,"getKind","setKind",MPseudostateKind.class,MPseudostateKind.JOIN,null));
    joinButton.setEnabled(false);
    kindPanel.add(joinButton);
    kindGroup.add(joinButton);

    UMLRadioButton deepButton = new UMLRadioButton("deep history",this,new UMLEnumerationBooleanProperty("kind",mclass,"getKind","setKind",MPseudostateKind.class,MPseudostateKind.DEEP_HISTORY,null));
    deepButton.setEnabled(false);
    kindPanel.add(deepButton);
    kindGroup.add(deepButton);

    UMLRadioButton shallowButton = new UMLRadioButton("shallow history",this,new UMLEnumerationBooleanProperty("kind",mclass,"getKind","setKind",MPseudostateKind.class,MPseudostateKind.SHALLOW_HISTORY,null));
    shallowButton.setEnabled(false);
    kindPanel.add(shallowButton);
    kindGroup.add(shallowButton);

    UMLRadioButton initialButton = new UMLRadioButton("initial",this,new UMLEnumerationBooleanProperty("kind",mclass,"getKind","setKind",MPseudostateKind.class,MPseudostateKind.INITIAL,null));
    initialButton.setEnabled(false);
    kindPanel.add(initialButton);
    kindGroup.add(initialButton);

    addField(kindPanel,2,0,0); 


    addCaption(Argo.localize("UMLMenu", "label.incoming"),0,1,0.5);
    addField(incomingScroll,0,1,0.5);

    addCaption(Argo.localize("UMLMenu", "label.outgoing"),1,1,0.5);
    addField(outgoingScroll,1,1,0.5);

  }


    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Pseudostate");
    }



} /* end class PropPanelPseudostate */

