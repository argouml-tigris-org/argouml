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

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.foundation.core.CoreFactory;

import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLAttributesListModel;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLList;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.model_management.MModel;

/**
 * @todo this property panel needs refactoring to remove dependency on
 *       old gui components.
 */
public class PropPanelDataType extends PropPanelClassifier {


    ////////////////////////////////////////////////////////////////
    // contructors
    public PropPanelDataType() {
        super("DataType", _dataTypeIcon, ConfigLoader.getTabPropsOrientation());

        Class mclass = MDataType.class;

        addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
        addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),getStereotypeBox()));
        addField(Argo.localize("UMLMenu", "label.namespace"), getNamespaceComboBox());
        addField(Argo.localize("UMLMenu", "label.modifiers"), _modifiersPanel);

        addSeperator();

        addField("Generalizations:", getGeneralizationScroll());
        addField("Specializations:", getSpecializationScroll());
        addField(Argo.localize("UMLMenu", "label.dependency"), getSupplierDependencyScroll());

        addSeperator();

        addField(Argo.localize("UMLMenu", "label.operations"), getFeatureScroll());

        JList attrList = new UMLList(new UMLAttributesListModel(this,"feature",true),true);
        attrList.setForeground(Color.blue);
        attrList.setVisibleRowCount(1);
        JScrollPane attrScroll= new JScrollPane(attrList);
        addField(Argo.localize("UMLMenu", "label.literals"), attrScroll);

        new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateUp",null);
        new PropPanelButton(this,buttonPanel,_dataTypeIcon, Argo.localize("UMLMenu", "button.add-datatype"),"newDataType",null);
        new PropPanelButton(this,buttonPanel,_addAttrIcon, Argo.localize("UMLMenu", "button.add-enumeration-literal"),"addAttribute",null);
        new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete datatype"),"removeElement",null);
    }

    public void addAttribute() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MStereotype stereo = classifier.getStereotype();
            if(stereo == null) {
                //
                //  if there is not an enumeration stereotype as
                //     an immediate child of the model, add one
                MModel model = classifier.getModel();
                Object ownedElement;
                boolean match = false;
                if(model != null) {
                    Collection ownedElements = model.getOwnedElements();
                    if(ownedElements != null) {
                        Iterator iter = ownedElements.iterator();
                        while(iter.hasNext()) {
                            ownedElement = iter.next();
                            if(ownedElement instanceof MStereotype) {
                                stereo = (MStereotype) ownedElement;
                                String stereoName = stereo.getName();
                                if(stereoName != null && stereoName.equals("enumeration")) {
                                    match = true;
                                    break;
                                }
                            }
                        }
                        if(!match) {
                            stereo = classifier.getFactory().createStereotype();
                            stereo.setName("enumeration");
                            model.addOwnedElement(stereo);
                        }
                        classifier.setStereotype(stereo);
                    }
                }
            }

            MAttribute attr = CoreFactory.getFactory().buildAttribute(classifier);
            TargetManager.getInstance().setTarget(attr);
        }

    }

    public void newDataType() {
        Object target = getTarget();
        if(target instanceof MDataType) {
            MDataType dt = (MDataType) target;
            MNamespace ns = dt.getNamespace();
            MDataType newDt = CoreFactory.getFactory().createDataType();
            ns.addOwnedElement(newDt);
            TargetManager.getInstance().setTarget(newDt);
        }
    }



} /* end class PropPanelDataType */
