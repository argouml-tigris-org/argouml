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

import org.argouml.uml.ui.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import javax.swing.*;
import java.awt.*;

public class PropPanelGeneralization extends PropPanel {

  ////////////////////////////////////////////////////////////////
  // constructors

  public PropPanelGeneralization() {
    super("Generalization",2);
  
    Class mclass = MGeneralization.class;
    
    addCaption(new JLabel("Name:"),0,0,0);
    addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),0,0,0);

    
    addCaption(new JLabel("Stereotype:"),1,0,0);
    JComboBox stereotypeBox = new UMLStereotypeComboBox(this);
    addField(stereotypeBox,1,0,0);
    
    addCaption(new JLabel("Discriminator:"),2,0,0);
    addField(new UMLTextField(this,new UMLTextProperty(mclass,"discriminator","getDiscriminator","setDiscriminator")),2,0,0);

    addCaption(new JLabel("Namespace:"),3,0,1);
    JList namespaceList = new UMLList(new UMLNamespaceListModel(this),true);
    namespaceList.setBackground(getBackground());
    namespaceList.setForeground(Color.blue);
    addField(namespaceList,3,0,0);
        
    
    
    addCaption(new JLabel("Parent:"),0,1,0);
    //
    //   misuse of classifier, but only temporary
    //
    addField(new UMLClassifierComboBox(this,MGeneralizableElement.class,"parent","getParentElement","setParentElement",false),0,1,0);

    addCaption(new JLabel("Child:"),1,1,0);
    addField(new UMLClassifierComboBox(this,MGeneralizableElement.class,"child","getChild","setChild",false),1,1,0);

    addCaption(new JLabel("Powertype:"),2,1,1);
    addField(new UMLClassifierComboBox(this,MClassifier.class,"powertype","getPowertype","setPowertype",true),2,1,0);


}

    public MGeneralizableElement getParentElement() {
        MGeneralizableElement parent = null;
        Object target = getTarget();
        if(target instanceof MGeneralization) {
            parent = ((MGeneralization) target).getParent();
        }
        return parent;
    }
    
    public void setParentElement(MGeneralizableElement parent) {
        Object target = getTarget();
        if(target instanceof MGeneralization) {
            ((MGeneralization) target).setParent(parent);
        }
    }
    
    public MGeneralizableElement getChild() {
        MGeneralizableElement child = null;
        Object target = getTarget();
        if(target instanceof MGeneralization) {
            child = ((MGeneralization) target).getChild();
        }
        return child;
    }
    
    public void setChild(MGeneralizableElement child) {
        Object target = getTarget();
        if(target instanceof MGeneralization) {
            ((MGeneralization) target).setChild(child);
        }
    }
    
    public MClassifier getPowertype() {
        MClassifier ptype = null;
        Object target = getTarget();
        if(target instanceof MGeneralization) {
            ptype = ((MGeneralization) target).getPowertype();
        }
        return ptype;
    }
    
    public void setPowertype(MClassifier ptype) {
        Object target = getTarget();
        if(target instanceof MGeneralization) {
            ((MGeneralization) target).setPowertype(ptype);
        }
    }
    
    
    
  
} /* end class PropPanelGeneralization */
