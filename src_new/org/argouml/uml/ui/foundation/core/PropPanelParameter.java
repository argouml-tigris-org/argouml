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
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import javax.swing.*;
import org.argouml.uml.ui.*;
import java.awt.*;


public class PropPanelParameter extends PropPanel {

    public PropPanelParameter() {
        super("Parameter Properties",2);

        Class mclass = MParameter.class;

        addCaption(new JLabel("Name:"),0,0,0);
        addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),0,0,0);

        addCaption(new JLabel("Stereotype:"),1,0,0);
        JComboBox stereotypeBox = new UMLStereotypeComboBox(this);
        addField(stereotypeBox,1,0,0);

        addCaption(new JLabel("Type:"),2,0,0);
        addField(new UMLClassifierComboBox(this,MClassifier.class,null,"type","getType","setType",true),0,1,0);
        
        addCaption(new JLabel("Owner:"),3,0,1);
        JList namespaceList = new UMLList(new UMLReflectionListModel(this,"behaviorialfeature",false,"getBehavioralFeature",null,null,null),true);
        addLinkField(namespaceList,3,0,0);
        

        addCaption(new JLabel("Kind:"),0,1,0);
        JPanel kindPanel = new JPanel(new GridLayout(0,2));
        ButtonGroup kindGroup = new ButtonGroup();

        UMLRadioButton inout = new UMLRadioButton("in/out",this,new UMLEnumerationBooleanProperty("kind",mclass,"getKind","setKind",MParameterDirectionKind.class,MParameterDirectionKind.INOUT,null));
        kindGroup.add(inout);
        kindPanel.add(inout);
        
        UMLRadioButton in = new UMLRadioButton("in",this,new UMLEnumerationBooleanProperty("kind",mclass,"getKind","setKind",MParameterDirectionKind.class,MParameterDirectionKind.IN,null));
        kindGroup.add(in);
        kindPanel.add(in);
        
        UMLRadioButton out = new UMLRadioButton("out",this,new UMLEnumerationBooleanProperty("kind",mclass,"getKind","setKind",MParameterDirectionKind.class,MParameterDirectionKind.OUT,null));
        kindGroup.add(out);
        kindPanel.add(out);
        
        UMLRadioButton ret = new UMLRadioButton("return",this,new UMLEnumerationBooleanProperty("kind",mclass,"getKind","setKind",MParameterDirectionKind.class,MParameterDirectionKind.RETURN,null));
        kindGroup.add(ret);
        kindPanel.add(ret);
        
        addField(kindPanel,0,1,0);
        
        
        addCaption(new JLabel("Initial Value:"),1,1,1);
        addField(new UMLInitialValueComboBox(this),1,1,0);
        
    }

    public MClassifier getType() {
        MClassifier type = null;
        Object target = getTarget();
        if(target instanceof MParameter) {
            type = ((MParameter) target).getType();
        }
        return type;
    }
    
    public void setType(MClassifier type) {
        Object target = getTarget();
        if(target instanceof MParameter) {
            ((MParameter) target).setType(type);
        }
    }
    

    public Object getBehavioralFeature() {
        MBehavioralFeature feature = null;
        Object target = getTarget();
        if(target instanceof MParameter) {
            feature = ((MParameter) target).getBehavioralFeature();
        }
        return feature;
    }
    
    
} /* end class PropPanelParameter */

