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

package org.argouml.uml.ui.foundation.core;

import javax.swing.JScrollPane;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.model.ModelFacade;

import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLList;
import org.argouml.uml.ui.UMLModelElementListModel;
import org.argouml.uml.ui.UMLReflectionListModel;
import org.argouml.uml.ui.UMLTextField;
import org.argouml.uml.ui.UMLTextProperty;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.behavior.common_behavior.MSignal;
import ru.novosoft.uml.behavior.use_cases.MActor;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;


/**
 * TODO: this property panel needs refactoring to remove dependency on
 *       old gui components.
 */
public class PropPanelGeneralization extends PropPanelModelElement {
    protected static Logger cat =
        Logger.getLogger(PropPanelGeneralization.class);

    private PropPanelButton _newButton;

    public PropPanelGeneralization() {
        super("Generalization", ConfigLoader.getTabPropsOrientation());
        Class mclass = MGeneralization.class;

        Class[] namesToWatch = {MStereotype.class, MNamespace.class, MClassifier.class };
        setNameEventListening(namesToWatch);

        addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
        addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"), getStereotypeBox()));
        addField("Discriminator:", new UMLTextField(this, new UMLTextProperty(mclass, "discriminator", "getDiscriminator", "setDiscriminator")));
        addField(Argo.localize("UMLMenu", "label.namespace"), getNamespaceComboBox());

        addSeperator();

        UMLModelElementListModel parentModel = new UMLReflectionListModel(this, "parent", true, "getParentElement", null, null, null);
        parentModel.setUpperBound(1);
        UMLList umlParentList = new UMLList(parentModel, true);
        umlParentList.setVisibleRowCount(1);
        addLinkField("Parent:", new JScrollPane(umlParentList, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        UMLModelElementListModel childModel = new UMLReflectionListModel(this, "child", true, "getChild", null, null, null);
        childModel.setUpperBound(1);
        UMLList umlChildList = new UMLList(childModel, true);
        umlChildList.setVisibleRowCount(1);
        addLinkField("Child:", new JScrollPane(umlChildList, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        addField("Powertype:", new UMLComboBox2(new UMLGeneralizationPowertypeComboBoxModel(), ActionSetGeneralizationPowertype.SINGLETON));

        new PropPanelButton(this, buttonPanel, _navUpIcon, Argo.localize("UMLMenu", "button.go-up"), "navigateUp", null);
        new PropPanelButton(this, buttonPanel, _deleteIcon, localize("Delete generalization"), "removeElement", null);
    }


    private void updateButton() {
        Object target = getTarget();
        if (ModelFacade.isAGeneralization(target)) {
            MGeneralization gen = (MGeneralization) target;
            MGeneralizableElement parent = gen.getParent();
            MGeneralizableElement child = gen.getChild();
            //
            //   if one and only one of child and parent are set
            //
            if (parent != null ^ child != null) {
                if (parent == null) parent = child;

                if (ModelFacade.isAClass(parent)) {
                    _newButton.setIcon(_classIcon);
                    _newButton.setToolTipText("Add new class");
                }
                else {
                    if (ModelFacade.isAInterface(parent)) {
                        _newButton.setIcon(_interfaceIcon);
                        _newButton.setToolTipText("Add new interface");
                    }
                    else {
                        if (ModelFacade.isAPackage(parent)) {
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


    public Object getParentElement() {
        Object parent = null;
        Object target = getTarget();
        if (ModelFacade.isAGeneralization(target)) {
            parent = ModelFacade.getParent(target);
        }
        return parent;
    }

    public void setParentElement(MGeneralizableElement parent) {
        Object target = getTarget();
        if (ModelFacade.isAGeneralization(target)) {
            Object generalization = target;
            Object child = ModelFacade.getChild(generalization);
            Object oldParent = ModelFacade.getParent(generalization);
            //
            //   can't do immediate circular generalization
            //
            if (parent != child && parent != oldParent) {
                ModelFacade.setParent(generalization, parent);
            } else {
                //
                //   force a refresh of the panel
		//                refresh();
            }
        }
    }

    public Object getChild() {
        Object child = null;
        Object target = getTarget();
        if (ModelFacade.isAGeneralization(target)) {
            child = ModelFacade.getChild(target);
        }
        return child;
    }

    public void setChild(MGeneralizableElement child) {
        Object target = getTarget();
        if (ModelFacade.isAGeneralization(target)) {
            MGeneralization gen = (MGeneralization) target;
            MGeneralizableElement parent = gen.getParent();
            MGeneralizableElement oldChild = gen.getChild();
            if (child != parent && child != oldChild) {
                gen.setChild(child);
            }
            else {
		//                refresh();
            }
        }
    }



    public Object getPowertype() {
        Object ptype = null;
        Object target = getTarget();
        if (ModelFacade.isAGeneralization(target)) {
            ptype = ModelFacade.getPowertype(target);
        }
        return ptype;
    }

    public void setPowertype(MClassifier ptype) {
        Object target = getTarget();
        if (ModelFacade.isAGeneralization(target)) {
            MGeneralization gen = (MGeneralization) target;
            MClassifier oldPtype = gen.getPowertype();
            if (ptype != oldPtype) {
                gen.setPowertype(ptype);
            }
        }
    }


    public void newModelElement() {
        Object target = getTarget();
        if (ModelFacade.isAGeneralization(target)) {
            MGeneralization gen = (MGeneralization) target;
            MGeneralizableElement parent = gen.getParent();
            MGeneralizableElement child = gen.getChild();
            if (parent != null ^ child != null) {
                MGeneralizableElement known = parent;
                if (known == null) known = child;
                MNamespace ns = known.getNamespace();
                if (ns != null) {
                    try {
                        MGeneralizableElement newElement = (MGeneralizableElement)
			    known.getClass().getConstructor(new Class[] {}).newInstance(new Object[] {});
                        ns.addOwnedElement(newElement);
                        if (parent == null) {
                            gen.setParent(newElement);
                        }
                        else {
                            gen.setChild(newElement);
                        }
                        _newButton.setEnabled(false);
                        TargetManager.getInstance().setTarget(newElement);
                    }
                    catch (Exception e) {
                        cat.error(e.toString() + " in PropPanelGeneralization.newElement", e);
                    }
                }
            }
        }
    }

    public void navigateUp() {
        Object target = getTarget();
        if (ModelFacade.isAModelElement(target)) {
            Object namespace = ModelFacade.getNamespace(target);
            if (namespace != null) {
                TargetManager.getInstance().setTarget(namespace);
            }
        }
    }

    private boolean isAcceptible(MGeneralizableElement fixed,
				 MModelElement candidate) {
        boolean isCompatible = true;
        Class[] keys = {
	    MClass.class, 
	    MDataType.class,
	    MInterface.class, 
	    MActor.class, 
	    MSignal.class 
	};
        int i;
        for (i = 0; i < keys.length; i++) {
            if (keys[i].isInstance(fixed)) {
                isCompatible = keys[i].isInstance(candidate);
                break;
            }
        }
        return isCompatible;
    }

    public boolean isAcceptiblePowertype(MModelElement element) {
        return ModelFacade.isAClassifier(element);
    }

} /* end class PropPanelGeneralization */