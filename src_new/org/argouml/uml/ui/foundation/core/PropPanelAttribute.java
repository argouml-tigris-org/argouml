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



// File: PropPanelAttribute.java
// Classes: PropPanelAttribute
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.ui.foundation.core;

import java.awt.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import org.argouml.uml.ui.*;
import org.tigris.gef.util.Util;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

public class PropPanelAttribute extends PropPanel {

    private static ImageIcon _attributeIcon = Util.loadIconResource("AddAttribute");
    
    public PropPanelAttribute() {
        super("Attribute Properties",2);

        Class mclass = MAttribute.class;
        
        //
        //   this will cause the components on this page to be notified
        //      anytime a stereotype, namespace, operation, etc
        //      has its name changed or is removed anywhere in the model
        Class[] namesToWatch = { MStereotype.class,MNamespace.class,MClassifier.class };        
        setNameEventListening(namesToWatch);
        

        addCaption(new JLabel("Name:"),0,0,0);
        addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),0,0,0);

        addCaption(new JLabel("Type:"),1,0,0);
        addField(new UMLClassifierComboBox(this,MClassifier.class,null,"type","getType","setType",false),1,0,0);

        addCaption(new JLabel("Multiplicity:"),2,0,0);
        addField(new UMLMultiplicityComboBox(this,MAttribute.class),2,0,0);
        
        addCaption(new JLabel("Stereotype:"),3,0,0);
        JComboBox stereotypeBox = new UMLStereotypeComboBox(this);
        addField(stereotypeBox,3,0,0);

        addCaption(new JLabel("Owner:"),4,0,1);
        JList ownerList = new UMLList(new UMLReflectionListModel(this,"owner",false,"getOwner",null,null,null),true);
        addLinkField(ownerList,4,0,0);
        
        
        addCaption(new JLabel("Initial Value:"),0,1,0);
        addField(new UMLInitialValueComboBox(this),0,1,0);
        
        addCaption(new JLabel("Visibility:"),1,1,0);
        addField(new UMLVisibilityPanel(this,mclass,3,false),1,1,0);
        
        addCaption(new JLabel("Modifiers:"),2,1,1);
        JPanel modPanel = new JPanel(new GridLayout(0,2));
        modPanel.add(new UMLCheckBox("static",this,new UMLEnumerationBooleanProperty("ownerscope",mclass,"getOwnerScope","setOwnerScope",MScopeKind.class,MScopeKind.CLASSIFIER,MScopeKind.INSTANCE)));
        modPanel.add(new UMLCheckBox("final",this,new UMLEnumerationBooleanProperty("changeability",mclass,"getChangeability","setChangeability",MChangeableKind.class,MChangeableKind.FROZEN,MChangeableKind.CHANGEABLE)));
        modPanel.add(new UMLCheckBox("transient",this,new UMLTaggedBooleanProperty("transient")));
        modPanel.add(new UMLCheckBox("volatile",this,new UMLTaggedBooleanProperty("volatile")));
        addField(modPanel,2,1,0);
        
        JPanel buttonBorder = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(0,2));
        buttonBorder.add(buttonPanel,BorderLayout.NORTH);
        add(buttonBorder,BorderLayout.EAST);
    
        
        new PropPanelButton(this,buttonPanel,_deleteIcon,"Delete attribute","removeElement",null);
        new PropPanelButton(this,buttonPanel,_navUpIcon,"Go up","navigateUp",null);
        new PropPanelButton(this,buttonPanel,_attributeIcon,"New attribute","newAttribute",null);
        new PropPanelButton(this,buttonPanel,_navBackIcon,"Go back","navigateBackAction","isNavigateBackEnabled");
        buttonPanel.add(new JPanel());
        new PropPanelButton(this,buttonPanel,_navForwardIcon,"Go forward","navigateForwardAction","isNavigateForwardEnabled");
    }

    
    /**
     *    Gets the type of the current target.  This method is called
     *    by UMLClassifierComboBox which invokes methods on the container
     *    (not the target like most reflection models) so that PropPanelOperation
     *    make return type look like a Operation property.
     */
    public MClassifier getType() {
        MClassifier type = null;
        Object target = getTarget();
        if(target instanceof MAttribute) {
            type = ((MAttribute) target).getType();
        }
        return type;
    }
    
    public void setType(MClassifier type) {
        Object target = getTarget();
        if(target instanceof MAttribute) {
            ((MAttribute) target).setType(type);
        }
    }
            
    public Object getOwner() {
        Object owner = null;
        Object target = getTarget();
        if(target instanceof MAttribute) {
            owner = ((MAttribute) target).getOwner();
        }
        return owner;
    }
    
    public void newAttribute() {
        Object target = getTarget();
        if(target instanceof MAttribute) {
            MClassifier owner = ((MAttribute) target).getOwner();
            if(owner != null) {
                MAttribute newAttr = owner.getFactory().createAttribute();
                owner.addFeature(newAttr);
                navigateTo(newAttr);
            }
        }
    }
    
    public void navigateUp() {
        Object target = getTarget();
        if(target instanceof MAttribute) {
            MClassifier owner = ((MAttribute) target).getOwner();
            if(owner != null) {
                navigateTo(owner);
            }
        }
    }
} /* end class PropPanelAttribute */
