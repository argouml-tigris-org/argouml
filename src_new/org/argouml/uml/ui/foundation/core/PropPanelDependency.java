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
import javax.swing.*;
import org.argouml.uml.ui.*;
import java.awt.*;
import java.util.*;

public class PropPanelDependency extends PropPanel {

  ////////////////////////////////////////////////////////////////
  // constructors

    public PropPanelDependency() {
        super("Dependency",2);

        Class mclass = MDependency.class;

        addCaption(new JLabel("Name:"),0,0,0);
        addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),0,0,0);


        addCaption(new JLabel("Stereotype:"),1,0,0);
        JComboBox stereotypeBox = new UMLStereotypeComboBox(this);
        addField(stereotypeBox,1,0,0);


        addCaption(new JLabel("Namespace:"),2,0,1);
        JList namespaceList = new UMLList(new UMLNamespaceListModel(this),true);
        namespaceList.setBackground(getBackground());
        namespaceList.setForeground(Color.blue);
        addField(namespaceList,2,0,0);

        addCaption(new JLabel("Suppliers:"),0,1,0.5);
        JList suppliersList = new UMLList(new UMLReflectionListModel(this,"supplier",true,"getSuppliers","setSuppliers",null,null),true);
        suppliersList.setForeground(Color.blue);
        suppliersList.setVisibleRowCount(1);
        addField(new JScrollPane(suppliersList),1,1,0.5);

        addCaption(new JLabel("Clients:"),1,1,0.5);
        JList clientsList = new UMLList(new UMLReflectionListModel(this,"client",true,"getClients","setClients",null,null),true);
        clientsList.setForeground(Color.blue);
        clientsList.setVisibleRowCount(1);
        addField(new JScrollPane(clientsList),1,1,0.5);
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
        return baseClass.equals("Dependency");
    }

} /* end class PropPanelDependency */

