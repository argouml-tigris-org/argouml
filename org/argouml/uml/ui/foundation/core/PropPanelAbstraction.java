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

import ru.novosoft.uml.foundation.core.*;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import org.argouml.application.api.*;
import org.argouml.uml.ui.*;

public class PropPanelAbstraction extends PropPanelModelElement {

  ////////////////////////////////////////////////////////////////
  // constructors

    public PropPanelAbstraction() {
        super("Abstraction",_realizationIcon, 2);

        Class mclass = MDependency.class;

        addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
        addField(nameField,1,0,0);

        addCaption(Argo.localize("UMLMenu", "label.stereotype"),2,0,0);
        addField(new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox),2,0,0);

        addCaption(Argo.localize("UMLMenu", "label.namespace"),3,0,1);
        addField(namespaceComboBox,3,0,0);

        addCaption("Suppliers:",0,1,0.5);
        JList suppliersList = new UMLList(new UMLReflectionListModel(this,"supplier",true,"getSuppliers","setSuppliers",null,null),true);
        suppliersList.setForeground(Color.blue);
        suppliersList.setVisibleRowCount(1);
        addField(new JScrollPane(suppliersList),0,1,0.5);

        addCaption("Clients:",1,1,0.5);
        JList clientsList = new UMLList(new UMLReflectionListModel(this,"client",true,"getClients","setClients",null,null),true);
        clientsList.setForeground(Color.blue);
        clientsList.setVisibleRowCount(1);
        addField(new JScrollPane(clientsList),1,1,0.5);


	new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateNamespace",null);
	new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
	new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
	new PropPanelButton(this,buttonPanel,_deleteIcon, Argo.localize("UMLMenu", "button.delete-association"),"removeElement",null);

    }

    public Collection getSuppliers() {
        Collection suppliers = null;
        Object target = getTarget();
        if(target instanceof MDependency) {
            suppliers = ((MDependency) target).getSuppliers();
        }
        return suppliers;
    }

    public void setSuppliers(Collection suppliers) {
        Object target = getTarget();
        if(target instanceof MDependency) {
            ((MDependency) target).setSuppliers(suppliers);
        }
    }

    public Collection getClients() {
        Collection suppliers = null;
        Object target = getTarget();
        if(target instanceof MDependency) {
            suppliers = ((MDependency) target).getClients();
        }
        return suppliers;
    }

    public void setClients(Collection suppliers) {
        Object target = getTarget();
        if(target instanceof MDependency) {
            ((MDependency) target).setClients(suppliers);
        }
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Abstraction");
    }

} /* end class PropPanelAbstraction */

