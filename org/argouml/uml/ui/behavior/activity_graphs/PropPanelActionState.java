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



// File: PropPanelState.java
// Classes: PropPanelState
// Original Author: your email address here
// $Id$

package org.argouml.uml.ui.behavior.activity_graphs;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.activity_graphs.*;

import org.argouml.application.api.*;
import org.argouml.ui.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.ui.behavior.state_machines.*;

import javax.swing.*;
import java.awt.*;

/** User interface panel shown at the bottom of the screen that allows
 *  the user to edit the properties of the selected UML model
 *  element. */

public class PropPanelActionState extends PropPanelState {

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelActionState() {
    super("Action State",_actionStateIcon, 2);

    Class mclass = MActionState.class;

    addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
    addField(nameField,1,0,0);

    addCaption(Argo.localize("UMLMenu", "label.stereotype"),2,0,0);
    addField(new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox),2,0,0);

    addCaption("Entry-Action:",3,0,0);
    addField(entryScroll,3,0,0);

    addCaption(Argo.localize("UMLMenu", "label.modifiers"),4,0,1);
    JPanel modifiersPanel = new JPanel(new GridLayout(0,2));
    modifiersPanel.add(new UMLCheckBox(localize("dynamic"),this,new UMLReflectionBooleanProperty("isDynamic",mclass,"isDynamic","setDynamic")));
    addField(modifiersPanel,4,0,0);

    addCaption(Argo.localize("UMLMenu", "label.incoming"),0,1,0.5);
    addField(incomingScroll,0,1,0.5);
    
    addCaption(Argo.localize("UMLMenu", "label.outgoing"),1,1,0.5);
    addField(outgoingScroll,1,1,0.5);

  }

  protected boolean isAcceptibleBaseMetaClass(String baseClass) {
    return baseClass.equals("ActionState");
  }

} /* end class PropPanelActionState */

