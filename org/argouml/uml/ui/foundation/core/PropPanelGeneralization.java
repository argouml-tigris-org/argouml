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
import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.model_management.*;

import org.argouml.application.api.*;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.*;

public class PropPanelGeneralization extends PropPanelModelElement {

  private PropPanelButton _newButton;

  public PropPanelGeneralization() {
    super("Generalization",_generalizationIcon,2);

    Class mclass = MGeneralization.class;

    Class[] namesToWatch = {MStereotype.class,MNamespace.class,MClassifier.class };
    setNameEventListening(namesToWatch);

    addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
    addField(nameField,1,0,0);

    addCaption(Argo.localize("UMLMenu", "label.stereotype"),2,0,0);
    addField(new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox),2,0,0);

    addCaption("Discriminator:",3,0,0);
    addField(new UMLTextField(this,new UMLTextProperty(mclass,"discriminator","getDiscriminator","setDiscriminator")),3,0,0);

    addCaption(Argo.localize("UMLMenu", "label.namespace"),4,0,1);
    addField(namespaceScroll,4,0,0);

    addCaption("Parent:",0,1,0);
    UMLModelElementListModel parentModel = new UMLReflectionListModel(this,"parent",true,"getParentElement",null,null,null);
    parentModel.setUpperBound(1);
    addLinkField(new JScrollPane(new UMLList(parentModel,true),JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),0,1,0);

    addCaption("Child:",1,1,0);
    UMLModelElementListModel childModel = new UMLReflectionListModel(this,"child",true,"getChild",null,null,null);
    childModel.setUpperBound(1);
    addLinkField(new JScrollPane(new UMLList(childModel,true),JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),1,1,0);

    addCaption("Powertype:",2,1,1);
    UMLComboBoxModel powerModel = new UMLComboBoxModel(this,"isAcceptiblePowertype",
        "powertype","getPowertype","setPowertype",false,MClassifier.class,true);
    addField(new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-class"),new UMLComboBox(powerModel)),2,1,0);

    new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateUp",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu" ,"button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete generalization"),"removeElement",null);
    }


    private void updateButton() {
        Object target = getTarget();
        if(target instanceof MGeneralization) {
            MGeneralization gen = (MGeneralization) target;
            MGeneralizableElement parent = gen.getParent();
            MGeneralizableElement child = gen.getChild();
            //
            //   if one and only one of child and parent are set
            //
            if(parent != null ^ child != null) {
                if(parent == null) parent = child;

                if(parent instanceof MClass) {
                    _newButton.setIcon(_classIcon);
                    _newButton.setToolTipText("Add new class");
                }
                else {
                    if(parent instanceof MInterface) {
                        _newButton.setIcon(_interfaceIcon);
                        _newButton.setToolTipText("Add new interface");
                    }
                    else {
                        if(parent instanceof MPackage) {
                            _newButton.setIcon(_packageIcon);
                            _newButton.setToolTipText("Add new package");
                        }
                    }
                }
                _newButton.setEnabled(true);
            }
            else {
                _newButton.setEnabled(false);
            }
        }
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
            MGeneralization gen = (MGeneralization) target;
            MGeneralizableElement child = gen.getChild();
            MGeneralizableElement oldParent = gen.getParent();
            //
            //   can't do immediate circular generalization
            //
            if(parent != child && parent != oldParent) {
                gen.setParent(parent);
            }
            else {
                //
                //   force a refresh of the panel
//                refresh();
            }
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
            MGeneralization gen = (MGeneralization) target;
            MGeneralizableElement parent = gen.getParent();
            MGeneralizableElement oldChild = gen.getChild();
            if(child != parent && child != oldChild) {
                gen.setChild(child);
            }
            else {
//                refresh();
            }
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
            MGeneralization gen = (MGeneralization) target;
            MClassifier oldPtype = gen.getPowertype();
            if(ptype != oldPtype) {
                gen.setPowertype(ptype);
            }
        }
    }


    public void newModelElement() {
        Object target = getTarget();
        if(target instanceof MGeneralization) {
            MGeneralization gen = (MGeneralization) target;
            MGeneralizableElement parent = gen.getParent();
            MGeneralizableElement child = gen.getChild();
            if(parent != null ^ child != null) {
                MGeneralizableElement known = parent;
                if(known == null) known = child;
                MNamespace ns = known.getNamespace();
                if(ns != null) {
                    try {
                        MGeneralizableElement newElement = (MGeneralizableElement)
                        known.getClass().getConstructor(new Class[] {}).newInstance(new Object[] {});
                        ns.addOwnedElement(newElement);
                        if(parent == null) {
                            gen.setParent(newElement);
                        }
                        else {
                            gen.setChild(newElement);
                        }
                        _newButton.setEnabled(false);
                        navigateTo(newElement);
                        // 2002-07-15
            			// Jaap Branderhorst
            			// Force an update of the navigation pane to solve issue 323
            			ProjectBrowser.TheInstance.getNavPane().forceUpdate();
                    }
                    catch(Exception e) {
                        System.out.println(e.toString() + " in PropPanelGeneralization.newElement");
                    }
                }
            }
        }
    }

    public void navigateUp() {
        Object target = getTarget();
        if(target instanceof MModelElement) {
            MNamespace ns = ((MModelElement) target).getNamespace();
            if(ns != null) {
                navigateTo(ns);
            }
        }
    }

    private boolean isAcceptible(MGeneralizableElement fixed,
        MModelElement candidate) {
        boolean isCompatible = true;
        Class[] keys = { MClass.class, MDataType.class,
            MInterface.class, MActor.class, MSignal.class };
        int i;
        for(i = 0; i < keys.length; i++) {
            if(keys[i].isInstance(fixed)) {
                isCompatible = keys[i].isInstance(candidate);
                break;
            }
        }
        return isCompatible;
    }

    public boolean isAcceptibleParent(MModelElement element) {
        boolean isAcceptible = false;
        Object target = getTarget();
        if(target instanceof MGeneralization) {
            MGeneralizableElement child = ((MGeneralization) target).getChild();
            if(child == null) {
              isAcceptible = element instanceof MGeneralizableElement;
            }
            else {
              isAcceptible = isAcceptible(child,element);
            }
        }
        return isAcceptible;
    }

    public boolean isAcceptibleChild(MModelElement element) {
        boolean isAcceptible = false;
	System.out.println("PropPanelGeneralization: testing isAcceptibleChild");
        Object target = getTarget();
        if(target instanceof MGeneralization) {
            MGeneralizableElement parent = ((MGeneralization) target).getParent();
            if(parent == null) {
              isAcceptible = element instanceof MGeneralizableElement;
            }
            else {
              isAcceptible = isAcceptible(parent,element);
            }
        }
	System.out.println("isAcceptible: "+isAcceptible);
        return isAcceptible;
    }


    public boolean isAcceptiblePowertype(MModelElement element) {
        return element instanceof MClassifier;
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Generalization");
    }


} /* end class PropPanelGeneralization */
