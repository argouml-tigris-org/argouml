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



// File: PropPanelCompositeState.java
// Classes: PropPanelCompositeState
// Original Author: 5heyden
// $Id:

package org.argouml.uml.ui.behavior.state_machines;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import org.argouml.application.api.*;
import org.argouml.uml.ui.*;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.foundation.core.*;

public class PropPanelCompositeState extends PropPanelState {

    public PropPanelCompositeState() {
        super("Composite State",_compositeStateIcon, 3);

        Class mclass = MCompositeState.class;

        addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
        addField(nameField,1,0,0);

        addCaption(Argo.localize("UMLMenu", "label.stereotype"),2,0,0);
	addField(new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox),2,0,0);

        addCaption("Subvertices:",3,0,1);
        JList subsList = new UMLList(new UMLReflectionListModel(this,"subvertex",false,"getSubvertices",null,null,null),true);
        subsList.setForeground(Color.blue);
        subsList.setFont(smallFont);
        JScrollPane subsScroll = new JScrollPane(subsList);
        addField(subsScroll,3,0,1);

        addCaption(Argo.localize("UMLMenu", "label.incoming"),0,1,0.5);
	addField(incomingScroll,0,1,0.5);

        addCaption(Argo.localize("UMLMenu", "label.outgoing"),1,1,0.5);
	addField(outgoingScroll,1,1,0.5);

        addCaption("Entry-Action:",0,2,0);
	addField(entryScroll,0,2,0);

        addCaption("Exit-Action:",1,2,0);
	addField(exitScroll,1,2,0);

        addCaption("Do-Activity:",2,2,0);
	addField(doScroll,2,2,0);

        addCaption(Argo.localize("UMLMenu", "label.internal-transitions"),3,2,1);
	addField(internalTransitionsScroll,3,2,1);
  }

    public java.util.List getSubvertices() {
        java.util.Collection subs = null;
        Object target = getTarget();
        if(target instanceof MCompositeState) {
            subs = ((MCompositeState) target).getSubvertices();
        }
        return new Vector(subs);
    }


    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("CompositeState");
    }



} /* end class PropPanelCompositeState */



