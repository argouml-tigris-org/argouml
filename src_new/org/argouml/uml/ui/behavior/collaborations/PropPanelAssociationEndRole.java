// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.collaborations;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.swingext.LabelledLayout;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelAssociationEnd;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.behavior.collaborations.MAssociationEndRole;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

public class PropPanelAssociationEndRole extends PropPanelAssociationEnd {

    public PropPanelAssociationEndRole() {
        super("AssociationEndRole", _assocEndRoleIcon, ConfigLoader.getTabPropsOrientation());
        Class mclass = MAssociationEndRole.class;
        
        //   this will cause the components on this page to be notified
        //      anytime a stereotype, namespace, operation, etc
        //      has its name changed or is removed anywhere in the model
        Class[] namesToWatch = { MStereotype.class, MAssociation.class,MClassifier.class };
        setNameEventListening(namesToWatch);
        
        addField(Argo.localize("UMLMenu", "label.name"), nameField);
        addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox));
        
        JList baseList = new UMLLinkedList(this, new UMLAssociationEndRoleBaseListModel());
        baseList.setVisibleRowCount(1);
        addField(Argo.localize("UMLMenu", "label.base"), 
            new JScrollPane(baseList));
        
        add(LabelledLayout.getSeperator());
        
        
        

        // makeFields(mclass);
    }

    protected void makeFields(Class mclass) {
        super.makeFields(mclass);
        associationsLabel.setText("AssociationRole:");
    }

} /* end class PropPanelAssociationEndRole */

