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


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import org.argouml.uml.ui.*;

import org.tigris.gef.util.Util;

public class PropPanelOperation extends PropPanel {
  
    private static ImageIcon _parameterIcon = Util.loadIconResource("Parameter");
    private static ImageIcon _operationIcon = Util.loadIconResource("Operation");
    private static ImageIcon _signalIcon = Util.loadIconResource("SignalSending");
    
    public PropPanelOperation() {
        super("Operation Properties",2);

        Class mclass = MOperation.class;

        addCaption("Name:",0,0,0);
        addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),0,0,0);

        addCaption("Stereotype:",1,0,0);
        JComboBox stereotypeBox = new UMLStereotypeComboBox(this);
        addField(stereotypeBox,1,0,0);

        addCaption("Visibility:",2,0,0);
        addField(new UMLVisibilityPanel(this,mclass,3,false),2,0,0);
        
        addCaption("Modifiers:",3,0,0);
        JPanel modPanel = new JPanel(new GridLayout(0,3));
        modPanel.add(new UMLCheckBox(localize("abstract"),this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
        modPanel.add(new UMLCheckBox(localize("final"),this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
        modPanel.add(new UMLCheckBox(localize("root"),this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
        modPanel.add(new UMLCheckBox(localize("query"),this,new UMLReflectionBooleanProperty("isQuery",mclass,"isQuery","setQuery")));
        modPanel.add(new UMLCheckBox(localize("static"),this,new UMLEnumerationBooleanProperty("ownerscope",mclass,"getOwnerScope","setOwnerScope",MScopeKind.class,MScopeKind.CLASSIFIER,MScopeKind.INSTANCE)));
        addField(modPanel,3,0,0);
        
        addCaption("Concurrency:",4,0,0);
        JPanel concurPanel = new JPanel(new GridLayout(0,2));
        ButtonGroup group = new ButtonGroup();
        UMLRadioButton sequential = new UMLRadioButton("sequential",this,new UMLEnumerationBooleanProperty("concurrency",mclass,"getConcurrency","setConcurrency",MCallConcurrencyKind.class,MCallConcurrencyKind.SEQUENTIAL,null));
        group.add(sequential);
        concurPanel.add(sequential);
        
        UMLRadioButton synchd = new UMLRadioButton("synchronized",this,new UMLEnumerationBooleanProperty("concurrency",mclass,"getConcurrency","setConcurrency",MCallConcurrencyKind.class,MCallConcurrencyKind.GUARDED,null));
        group.add(synchd);
        concurPanel.add(synchd);
        
        UMLRadioButton concur = new UMLRadioButton("concurrent",this,new UMLEnumerationBooleanProperty("concurrency",mclass,"getConcurrency","setConcurrency",MCallConcurrencyKind.class,MCallConcurrencyKind.CONCURRENT,null));
        group.add(concur);
        concurPanel.add(concur);
        addField(concurPanel,4,0,0);
        
        
        addCaption("Owner:",5,0,1);
        JList namespaceList = new UMLList(new UMLReflectionListModel(this,"owner",false,"getOwner",null,null,null),true);
        namespaceList.setBackground(getBackground());
        namespaceList.setForeground(Color.blue);
        addField(namespaceList,5,0,0);
        
        addCaption("Parameters:",0,1,.5);
        JList paramList = new UMLList(new UMLReflectionListModel(this,"parameter",true,"getParameters","setParameters","addParameter",null),true);
        paramList.setForeground(Color.blue);
        paramList.setVisibleRowCount(1);
        addField(new JScrollPane(paramList),0,1,0.5);
        
        addCaption("Exceptions:",1,1,.5);
        JList exceptList = new UMLList(new UMLReflectionListModel(this,"signal",true,"getRaisedSignals","setRaisedSignals","addRaisedSignal",null),true);
        exceptList.setForeground(Color.blue);
        exceptList.setVisibleRowCount(1);
        addField(new JScrollPane(exceptList),1,1,0.5);
        
    JPanel buttonBorder = new JPanel(new BorderLayout());
    JPanel buttonPanel = new JPanel(new GridLayout(0,2));
    buttonBorder.add(buttonPanel,BorderLayout.NORTH);
    add(buttonBorder,BorderLayout.EAST);
    
    new PropPanelButton(this,buttonPanel,_parameterIcon,localize("Add parameter"),"buttonAddParameter",null);
    new PropPanelButton(this,buttonPanel,_navUpIcon,localize("Go up"),"navigateUp",null);
    new PropPanelButton(this,buttonPanel,_signalIcon,localize("Add raised signal"),"buttonAddRaisedSignal",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon,localize("Go back"),"navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete operation"),"removeElement",null);
    new PropPanelButton(this,buttonPanel,_navForwardIcon,localize("Go forward"),"navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_operationIcon,localize("New operation"),"buttonAddOperation",null);
    }

    public MClassifier getReturnType() {
        MClassifier type = null;
        Object target = getTarget();
        if(target instanceof MOperation) {
            java.util.List params = ((MOperation) target).getParameters();
            if(params != null) {
                Iterator iter = params.iterator();
                MParameter param;
                while(iter.hasNext()) {
                    param = (MParameter) iter.next();
                    if(param.getKind() == MParameterDirectionKind.RETURN) {
                        type = param.getType();
                        break;
                    }
                }
            }
        }
        return type;
    }
    
    public void setReturnType(MClassifier type) {
        Object target = getTarget();
        if(target instanceof MOperation) {
            MOperation oper = (MOperation) target;
            Collection params = oper.getParameters();
            MParameter param;
            //
            //   remove first (hopefully only) return parameters
            //   
            if(type == null) {
                if(params != null) {
                    Iterator iter = params.iterator();
                    while(iter.hasNext()) {
                        param = (MParameter) iter.next();
                        if(param.getKind() == MParameterDirectionKind.RETURN) {
                            oper.removeParameter(param);
                            break;
                        }
                    }
                }
            }
            else
            {
                MParameter retParam = null;
                if(params != null) {
                    Iterator iter = params.iterator();
                    while(iter.hasNext()) {
                        param = (MParameter) iter.next();
                        if(param.getKind() == MParameterDirectionKind.RETURN) {
                            retParam = param;
                            break;
                        }
                    }
                }
                if(retParam == null) {
                    retParam = new MParameterImpl();
                    retParam.setKind(MParameterDirectionKind.RETURN);
                    oper.addParameter(retParam);
                }
                retParam.setType(type);
            }
        }
    }
    
    public java.util.List getParameters() {
        java.util.List params = null;
        Object target = getTarget();
        if(target instanceof MOperation) {
            params = ((MOperation) target).getParameters();
        }
        return params;
    }
    
    public void setParameters(Collection newParams) {
        Object target = getTarget();
        if(target instanceof MOperation) {
            if(newParams instanceof java.util.List) {
                ((MOperation) target).setParameters((java.util.List) newParams);
            }
            else {
                ((MOperation) target).setParameters(new ArrayList(newParams));
            }
        }
    }
    
    public void addParameter(Integer indexObj) {
        int index = indexObj.intValue();
        java.util.List oldParams = getParameters();
        java.util.List newParams = null;
        MParameter newParam = new MParameterImpl();
        //
        //  if you don't set one, you get an exception in the critics
        //
        newParam.setKind(MParameterDirectionKind.INOUT);
        newParam.setName("param" + index);
        if(oldParams == null || oldParams.size() == 0) {
            newParams = new LinkedList();
            newParams.add(newParam);
        }
        else {
            newParams = new ArrayList(oldParams.size()+1);
            MParameter returnParam = null;
            boolean added = false;
            Iterator iter = oldParams.iterator();
            for(int i = 0; iter.hasNext(); i++) {
                MParameter param = (MParameter) iter.next();
                if(param.getKind() == MParameterDirectionKind.RETURN) {
                    returnParam = param;
                }
                else {
                    newParams.add(param);
                    if(index == i) {
                        newParams.add(newParam);
                        added = true;
                    }
                }
                if(!added) {
                    newParams.add(newParam);
                }
                if(returnParam != null) {
                    newParams.add(returnParam);
                }
            }
        }
        setParameters(newParams);
    }

    public Object getOwner() {
        Object owner = null;
        Object target = getTarget();
        if(target instanceof MOperation) {
            owner = ((MOperation) target).getOwner();
        }
        return owner;
    }
     
    public Collection getRaisedSignals() {
        Collection signals = null;
        Object target = getTarget();
        if(target instanceof MOperation) {
            signals = ((MOperation) target).getRaisedSignals();
        }
        return signals;
    }
    
    public void setRaisedSignals(Collection signals) {
        Object target = getTarget();
        if(target instanceof MOperation) {
            ((MOperation) target).setRaisedSignals(signals);
        }
    }
    
    public void addRaisedSignal(Integer index) {
        MSignal newSignal = new MSignalImpl();
        Object target = getTarget();
        if(target instanceof MOperation) {
            ((MOperation) target).addRaisedSignal(newSignal);
        }
    }
    
    public void buttonAddParameter() {
        Object target = getTarget();
        if(target instanceof MOperation) {
            MOperation oper = (MOperation) target;
            MParameter newParam = oper.getFactory().createParameter();
            newParam.setKind(MParameterDirectionKind.INOUT);
            oper.addParameter(newParam);
            navigateTo(newParam);
        }
    }
    
    public void buttonAddOperation() {
        Object target = getTarget();
        if(target instanceof MOperation) {
            MOperation oper = (MOperation) target;
            MClassifier owner = oper.getOwner();
            if(owner != null) {
                MOperation newOper = owner.getFactory().createOperation();
                owner.addFeature(newOper);
                navigateTo(newOper);
            }
        }
    }
    
    public void buttonAddRaisedSignal() {
        Object target = getTarget();
        if(target instanceof MOperation) {
            MOperation oper = (MOperation) target;
            MSignal newSignal = oper.getFactory().createSignal();
            oper.addRaisedSignal(newSignal);
            navigateTo(newSignal);
        }
    }
    
    public void navigateUp() {
        Object target = getTarget();
        if(target instanceof MOperation) {
            MOperation oper = (MOperation) target;
            MClassifier owner = oper.getOwner();
            if(owner != null) {
                navigateTo(owner);
            }
        }
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Operation") ||
            baseClass.equals("BehavioralFeature") ||
            baseClass.equals("Feature");
    }
  

        
} /* end class PropPanelOperation */

