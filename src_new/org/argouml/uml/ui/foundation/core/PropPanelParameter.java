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
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,g
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.


package org.argouml.uml.ui.foundation.core;

import java.awt.*;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;

import ru.novosoft.uml.MElementListener;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.model_management.*;

import org.argouml.application.api.*;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.*;
import org.argouml.uml.ui.*;

public class PropPanelParameter extends PropPanelModelElement {

    public PropPanelParameter() {
        super("Parameter", _parameterIcon,2);

        Class mclass = MParameter.class;
        
         Class[] namesToWatch = { MStereotype.class,MOperation.class,
        MParameter.class,MClassifier.class };
    setNameEventListening(namesToWatch);

        addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
        addField(nameField,1,0,0);

        addCaption(Argo.localize("UMLMenu", "label.stereotype"),2,0,0);
        addField(new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox),2,0,0);

        addCaption(Argo.localize("UMLMenu", "label.owner"),3,0,1);
        JList namespaceList = new UMLList(new UMLReflectionListModel(this,"behaviorialfeature",false,"getBehavioralFeature",null,null,null),true);
        addLinkField(new JScrollPane(namespaceList,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),3,0,0);

        addCaption(Argo.localize("UMLMenu", "label.type"),0,1,0);
        /*
        UMLComboBoxModel typeModel = new UMLTypeModel(this,"isAcceptibleType",
            "type","getType","setType",true,MClassifier.class,
	    MParameter.class,true);
        
	UMLComboBox typeComboBox=new UMLComboBox(typeModel);
        */
        addField(new UMLTypeComboBox(this),0,1,0);

        addCaption("Initial Value:",1,1,0);
        addField(new UMLInitialValueComboBox(this),1,1,0);

	addCaption("Kind:",2,1,1);
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

        addField(kindPanel,2,1,0);

	new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateUp",null);
	new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
	new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu" ,"button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
	new PropPanelButton(this,buttonPanel,_parameterIcon, Argo.localize("UMLMenu", "button.add-parameter"),"addParameter",null);
	//	new PropPanelButton(this,buttonPanel,_dataTypeIcon, Argo.localize("UMLMenu", "button.add-datatype"),"addDataType",null);
	new PropPanelButton(this,buttonPanel,_deleteIcon, Argo.localize("UMLMenu", "button.delete-parameter"),"removeElement",null);
       
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

    public boolean isAcceptibleType(MModelElement type) {
       return type instanceof MClassifier;
    }

    public Object getBehavioralFeature() {
        MBehavioralFeature feature = null;
        Object target = getTarget();
        if(target instanceof MParameter) {
            feature = ((MParameter) target).getBehavioralFeature();
        }
        return feature;
    }
    
     public void addDataType() {
        Object target = getTarget();
        if(target instanceof MNamespace) {
            MNamespace ns = (MNamespace) target;
            MModelElement ownedElem = CoreFactory.getFactory().createDataType();
            ns.addOwnedElement(ownedElem);
            navigateTo(ownedElem);
        }
    }



    public void navigateUp() {
        Object feature = getBehavioralFeature();
        if(feature != null) {
            navigateTo(feature);
        }
    }

    public void addParameter() {
        MBehavioralFeature feature = null;
        Object target = getTarget();
        if(target instanceof MParameter) {
            feature = ((MParameter) target).getBehavioralFeature();
            if(feature != null) {
                MParameter param = CoreFactory.getFactory().buildParameter((MOperation)target);              
                navigateTo(param);
            }
        }
    }

   public void addDataType(MModelElement element) {
        addDataType();
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Parameter");
    }



} /* end class PropPanelParameter */

