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
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,g
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.ui.foundation.core;

import javax.swing.ButtonGroup;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.foundation.core.CoreFactory;

import org.argouml.swingext.GridLayout2;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLEnumerationBooleanProperty;
import org.argouml.uml.ui.UMLInitialValueComboBox;
import org.argouml.uml.ui.UMLList;
import org.argouml.uml.ui.UMLRadioButton;
import org.argouml.uml.ui.UMLReflectionListModel;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

/**
 * TODO: this property panel needs refactoring to remove dependency on
 *       old gui components.
 */
public class PropPanelParameter extends PropPanelModelElement {

    public PropPanelParameter() {
        super(
	      "Parameter",
	      _parameterIcon,
	      ConfigLoader.getTabPropsOrientation());
        Class mclass = MParameter.class;

        Class[] namesToWatch = {
	    MStereotype.class,
	    MOperation.class,
	    MParameter.class,
	    MClassifier.class 
	};
        setNameEventListening(namesToWatch);

        addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
        addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"), getStereotypeBox()));

        JList namespaceList = new UMLList(new UMLReflectionListModel(this, "behaviorialfeature", false, "getBehavioralFeature", null, null, null), true);
        namespaceList.setVisibleRowCount(1);
        addLinkField(Argo.localize("UMLMenu", "label.owner"), new JScrollPane(namespaceList, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));

        addSeperator();

        addField(Argo.localize("UMLMenu", "label.type"), new UMLComboBox2(new UMLParameterTypeComboBoxModel(), ActionSetParameterType.SINGLETON));

        addField("Initial Value:", new UMLInitialValueComboBox(this));

        JPanel kindPanel = new JPanel(new GridLayout2(0, 2, GridLayout2.ROWCOLPREFERRED));
        ButtonGroup kindGroup = new ButtonGroup();

        UMLRadioButton inout = new UMLRadioButton("in/out", this, new UMLEnumerationBooleanProperty("kind", mclass, "getKind", "setKind", MParameterDirectionKind.class, MParameterDirectionKind.INOUT, null));
        kindGroup.add(inout);
        kindPanel.add(inout);

        UMLRadioButton in = new UMLRadioButton("in", this, new UMLEnumerationBooleanProperty("kind", mclass, "getKind", "setKind", MParameterDirectionKind.class, MParameterDirectionKind.IN, null));
        kindGroup.add(in);
        kindPanel.add(in);

        UMLRadioButton out = new UMLRadioButton("out", this, new UMLEnumerationBooleanProperty("kind", mclass, "getKind", "setKind", MParameterDirectionKind.class, MParameterDirectionKind.OUT, null));
        kindGroup.add(out);
        kindPanel.add(out);

        UMLRadioButton ret = new UMLRadioButton("return", this, new UMLEnumerationBooleanProperty("kind", mclass, "getKind", "setKind", MParameterDirectionKind.class, MParameterDirectionKind.RETURN, null));
        kindGroup.add(ret);
        kindPanel.add(ret);

	addField("Kind:", kindPanel);

	new PropPanelButton(this, buttonPanel, _navUpIcon, Argo.localize("UMLMenu", "button.go-up"), "navigateUp", null);
	new PropPanelButton(this, buttonPanel, _parameterIcon, Argo.localize("UMLMenu", "button.add-parameter"), "addParameter", null);
	//	new PropPanelButton(this,buttonPanel,_dataTypeIcon, Argo.localize("UMLMenu", "button.add-datatype"),"addDataType",null);
	new PropPanelButton(this, buttonPanel, _deleteIcon, Argo.localize("UMLMenu", "button.delete-parameter"), "removeElement", null);

    }

    public Object getType() {
        Object type = null;
        Object target = getTarget();
        if (org.argouml.model.ModelFacade.isAParameter(target)) {
            return org.argouml.model.ModelFacade.getType(target);
        }
        return null;
    }

    public void setType(Object/*MClassifier*/ type) {
        Object target = getTarget();
        if (org.argouml.model.ModelFacade.isAParameter(target)) {
            ModelFacade.setType(target, type);
        }
    }

    public boolean isAcceptibleType(Object/*MModelElement*/ type) {
	return org.argouml.model.ModelFacade.isAClassifier(type);
    }

    public Object getBehavioralFeature() {
        Object feature = null;
        Object target = getTarget();
        if (ModelFacade.isAParameter(target)) {
            feature = ModelFacade.getBehavioralFeature(target);
        }
        return feature;
    }

    public void addDataType() {
        Object target = getTarget();
        if (ModelFacade.isANamespace(target)) {
            Object ns = /*(MNamespace)*/ target;
            Object ownedElem = CoreFactory.getFactory().createDataType();
            ModelFacade.addOwnedElement(ns, ownedElem);
            TargetManager.getInstance().setTarget(ownedElem);
        }
    }



    public void navigateUp() {
        Object feature = getBehavioralFeature();
        if (feature != null) {
            TargetManager.getInstance().setTarget(feature);
        }
    }

    public void addParameter() {
        Object feature = null;
        Object target = getTarget();
        if (ModelFacade.isAParameter(target)) {
            feature = ModelFacade.getBehavioralFeature(target);
            if (feature != null) {
                TargetManager.getInstance().setTarget(CoreFactory.getFactory().buildParameter(feature));
            }
        }
    }

    public void addDataType(Object/*MModelElement*/ element) {
        addDataType();
    }

} /* end class PropPanelParameter */