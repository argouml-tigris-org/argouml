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

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.PropPanelButton2;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLInitialValueComboBox;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.util.ConfigLoader;
/**
 * TODO: this property panel needs refactoring to remove dependency on
 *       old gui components.
 */
public class PropPanelParameter extends PropPanelModelElement {

    private JScrollPane behFeatureScroll;

    private static UMLParameterBehavioralFeatListModel behFeatureModel;
    
    /**
     * The constructor.
     * 
     */
    public PropPanelParameter() {
        super(
	      "Parameter",
	      parameterIcon,
	      ConfigLoader.getTabPropsOrientation());
        Class mclass = (Class) ModelFacade.PARAMETER;

        Class[] namesToWatch = {
	    (Class) ModelFacade.STEREOTYPE,
	    (Class) ModelFacade.OPERATION,
	    (Class) ModelFacade.PARAMETER,
	    (Class) ModelFacade.CLASSIFIER 
	};
        setNameEventListening(namesToWatch);

        addField(Translator.localize("UMLMenu", "label.name"), 
                getNameTextField());
 
        addField(Translator.localize("UMLMenu", "label.stereotype"), 
                getStereotypeBox());

        addField(Translator.localize("UMLMenu", "label.owner"), 
                getBehavioralFeatureScroll());
        
        addSeperator();

        addField(Translator.localize("UMLMenu", "label.type"), 
                new UMLComboBox2(new UMLParameterTypeComboBoxModel(), 
                        ActionSetParameterType.getInstance()));

        addField("Initial Value:", new UMLInitialValueComboBox(this));
        
        //      TODO: i18n
        add(new UMLParameterDirectionKindRadioButtonPanel("ParameterKind:", 
                true));

        addButton(new PropPanelButton2(this, 
                new ActionNavigateContainerElement()));
        new PropPanelButton(this, getButtonPanel(), parameterIcon, 
                Translator.localize("UMLMenu", "button.new-parameter"), 
                "addParameter", null);
        addButton(new PropPanelButton2(this, new ActionRemoveFromModel()));
    }

    /**
     * @return the type of the parameter
     */
    public Object getType() {
        Object target = getTarget();
        if (org.argouml.model.ModelFacade.isAParameter(target)) {
            return org.argouml.model.ModelFacade.getType(target);
        }
        return null;
    }

    /**
     * @param type the type of the parameter
     */
    public void setType(Object/*MClassifier*/ type) {
        Object target = getTarget();
        if (org.argouml.model.ModelFacade.isAParameter(target)) {
            ModelFacade.setType(target, type);
        }
    }

    /**
     * @param type the given parameter type
     * @return true if the given type is acceptable for the parameter 
     *         (i.e. it is a Classifier)
     */
    public boolean isAcceptibleType(Object/*MModelElement*/ type) {
	return org.argouml.model.ModelFacade.isAClassifier(type);
    }

    /**
     * @return the behaviouralfeature of this parameter
     */
    public Object getBehavioralFeature() {
        Object feature = null;
        Object target = getTarget();
        if (ModelFacade.isAParameter(target)) {
            feature = ModelFacade.getBehavioralFeature(target);
        }
        return feature;
    }

    /**
     * Create a new datatype.
     */
    public void addDataType() {
        Object target = getTarget();
        if (ModelFacade.isANamespace(target)) {
            Object ns = /*(MNamespace)*/ target;
            Object ownedElem = CoreFactory.getFactory().createDataType();
            ModelFacade.addOwnedElement(ns, ownedElem);
            TargetManager.getInstance().setTarget(ownedElem);
        }
    }


    /**
     * @see org.argouml.uml.ui.foundation.core.PropPanelModelElement#navigateUp()
     */
    public void navigateUp() {
        Object feature = getBehavioralFeature();
        if (feature != null) {
            TargetManager.getInstance().setTarget(feature);
        }
    }

    /**
     * Add a new parameter. 
     */
    public void addParameter() {
        Object feature = null;
        Object target = getTarget();
        if (ModelFacade.isAParameter(target)) {
            feature = ModelFacade.getBehavioralFeature(target);
            if (feature != null) {
                TargetManager.getInstance().setTarget(CoreFactory.getFactory()
                        .buildParameter(feature));
            }
        }
    }

    /**
     * @param element (ignored)
     */
    public void addDataType(Object/*MModelElement*/ element) {
        addDataType();
    }
    
    /**
     * Returns the behavioral Feature Scroll.
     * 
     * @return JScrollPane
     */
    public JScrollPane getBehavioralFeatureScroll() {
        if (behFeatureScroll == null) {
            if (behFeatureModel == null) {
                behFeatureModel = new UMLParameterBehavioralFeatListModel();
            }
            JList list = new UMLLinkedList(behFeatureModel);
            list.setVisibleRowCount(1);
            behFeatureScroll = new JScrollPane(list);
        }
        return behFeatureScroll;
    }


} /* end class PropPanelParameter */