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
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.MMUtil;

public class PropPanelOperation extends PropPanelModelElement {

    ////////////////////////////////////////////////////////////////
    // contructors
    public PropPanelOperation() {
        super("Operation", _operationIcon,3);

        Class mclass = MOperation.class;

        addCaption("Name:",1,0,0);
        addField(nameField,1,0,0);

        addCaption("Stereotype:",2,0,0);
        addField(new UMLComboBoxNavigator(this,"NavStereo",stereotypeBox),2,0,0);

        addCaption("Owner:",3,0,0);
        JList ownerList = new UMLList(new UMLReflectionListModel(this,"owner",false,"getOwner",null,null,null),true);
        ownerList.setBackground(getBackground());
        ownerList.setForeground(Color.blue);
        JScrollPane ownerScroll=new JScrollPane(ownerList,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        addField(ownerScroll,3,0,0);

        addCaption("Visibility:",4,0,1);
        addField(new UMLVisibilityPanel(this,mclass,2,false),4,0,0);

        addCaption("Modifiers:",0,1,0);
        JPanel modPanel = new JPanel(new GridLayout(0,2));
        modPanel.add(new UMLCheckBox(localize("abstract"),this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
        modPanel.add(new UMLCheckBox(localize("final"),this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
        modPanel.add(new UMLCheckBox(localize("root"),this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
        modPanel.add(new UMLCheckBox(localize("query"),this,new UMLReflectionBooleanProperty("isQuery",mclass,"isQuery","setQuery")));
        modPanel.add(new UMLCheckBox(localize("static"),this,new UMLEnumerationBooleanProperty("ownerscope",mclass,"getOwnerScope","setOwnerScope",MScopeKind.class,MScopeKind.CLASSIFIER,MScopeKind.INSTANCE)));
        addField(modPanel,0,1,0);

        addCaption("Concurrency:",1,1,1);
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
        addField(concurPanel,1,1,0);

        addCaption("Parameters:",0,2,.5);
        JList paramList = new UMLList(new UMLReflectionListModel(this,"parameter",true,"getParameters","setParameters","addParameter",null),true);
        paramList.setForeground(Color.blue);
        paramList.setVisibleRowCount(1);
	paramList.setFont(smallFont);
	addField(new JScrollPane(paramList),0,2,0.5);

        addCaption("Raised Signals:",1,2,0.5);
        JList exceptList = new UMLList(new UMLReflectionListModel(this,"signal",true,"getRaisedSignals","setRaisedSignals","addRaisedSignal",null),true);
        exceptList.setForeground(Color.blue);
        exceptList.setVisibleRowCount(1);
	exceptList.setFont(smallFont);
        addField(new JScrollPane(exceptList),1,2,0.5);

	new PropPanelButton(this,buttonPanel,_navUpIcon,localize("Go up"),"navigateUp",null);
	new PropPanelButton(this,buttonPanel,_navBackIcon,localize("Go back"),"navigateBackAction","isNavigateBackEnabled");
	new PropPanelButton(this,buttonPanel,_navForwardIcon,localize("Go forward"),"navigateForwardAction","isNavigateForwardEnabled");
	new PropPanelButton(this,buttonPanel,_operationIcon,localize("New operation"),"buttonAddOperation",null);
// I uncommented this next line. I don't know why it was commented out, it seems to work just fine...--pjs--
        new PropPanelButton(this,buttonPanel,_parameterIcon,localize("Add parameter"),"buttonAddParameter",null);
	//new PropPanelButton(this,buttonPanel,_signalIcon,localize("Add raised signal"),"buttonAddRaisedSignal",null);
	new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete operation"),"removeElement",null);
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
                    retParam = MMUtil.SINGLETON.buildParameter(oper);
                    retParam.setKind(MParameterDirectionKind.RETURN);
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
	buttonAddParameter();
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
        Object target = getTarget();
        if(target instanceof MOperation) {
	    MOperation oper = (MOperation) target;
            MSignal newSignal = oper.getFactory().createSignal();
	    oper.getOwner().getNamespace().addOwnedElement(newSignal);
	    oper.addRaisedSignal(newSignal);
            navigateTo(newSignal);
	}
    }

    public void buttonAddParameter() {
        Object target = getTarget();
        if(target instanceof MOperation) {
            MOperation oper = (MOperation) target;
            MParameter newParam = MMUtil.SINGLETON.buildParameter(oper);
            newParam.setKind(MParameterDirectionKind.INOUT);
            navigateTo(newParam);
            // 2002-07-15
            // Jaap Branderhorst
            // Force an update of the navigation pane to solve issue 323
            ProjectBrowser.TheInstance.getNavPane().forceUpdate();
        }
    }

    public void buttonAddOperation() {
        Object target = getTarget();
        if(target instanceof MOperation) {
            MOperation oper = (MOperation) target;
            MClassifier owner = oper.getOwner();
            if(owner != null) {
		MOperation newOper = MMUtil.SINGLETON.buildOperation(owner);
                navigateTo(newOper);
               
            }
             // 2002-07-15
            // Jaap Branderhorst
            // Force an update of the navigation pane to solve issue 323
            ProjectBrowser.TheInstance.getNavPane().forceUpdate();
        }
    }

    public void buttonAddRaisedSignal() {
        Object target = getTarget();
        if(target instanceof MOperation) {
            MOperation oper = (MOperation) target;
            MSignal newSignal = oper.getFactory().createSignal();
	    oper.getNamespace().addOwnedElement(newSignal);
            oper.addRaisedSignal(newSignal);
            navigateTo(newSignal);
             // 2002-07-15
            // Jaap Branderhorst
            // Force an update of the navigation pane to solve issue 323
            ProjectBrowser.TheInstance.getNavPane().forceUpdate();
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

    /**
     *   Appropriate namespace is the namespace of our class,
     *      not the class itself
     */
    protected MNamespace getDisplayNamespace() {
      MNamespace ns = null;
      Object target = getTarget();
      if(target instanceof MAttribute) {
        MAttribute attr = ((MAttribute) target);
        MClassifier owner = attr.getOwner();
        if(owner != null) {
          ns = owner.getNamespace();
        }
      }
      return ns;
    }



} /* end class PropPanelOperation */

