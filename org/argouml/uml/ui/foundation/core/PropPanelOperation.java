// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.argouml.i18n.Translator;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.tigris.swidgets.GridLayout2;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.ActionAddOperation;
import org.argouml.uml.ui.ActionNavigateOwner;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.PropPanelButton2;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLList;
import org.argouml.uml.ui.UMLReflectionListModel;
import org.argouml.util.ConfigLoader;

/**
 * A property panel for operations. TODO: this property panel needs refactoring
 * to remove dependency on old gui components.
 */
public class PropPanelOperation extends PropPanelFeature {

    private static UMLClassifierParameterListModel parameterListModel = 
        new UMLClassifierParameterListModel();

    private JScrollPane parameterScroll;

    ////////////////////////////////////////////////////////////////
    // contructors
    /**
     * The constructor.
     */
    public PropPanelOperation() {
        super("Operation", lookupIcon("Operation"), ConfigLoader
                .getTabPropsOrientation());

        //
        //   this will cause the components on this page to be notified
        //      anytime a stereotype, namespace, operation, etc
        //      has its name changed or is removed anywhere in the model
        Class[] namesToWatch = {(Class) ModelFacade.STEREOTYPE,
            (Class) ModelFacade.NAMESPACE, (Class) ModelFacade.CLASSIFIER };
        setNameEventListening(namesToWatch);

        addField(Translator.localize("label.name"),
                getNameTextField());

        addField(Translator.localize("label.stereotype"),
                getStereotypeBox());

        addField(Translator.localize("label.owner"),
                getOwnerScroll());

        addSeperator();    

        add(getVisibilityPanel());

        JPanel modifiersPanel = new JPanel(new GridLayout2(0, 3,
                GridLayout2.ROWCOLPREFERRED));
        modifiersPanel.setBorder(new TitledBorder(Translator.localize(
                "label.modifiers")));
        modifiersPanel.add(new UMLGeneralizableElementAbstractCheckBox());
        modifiersPanel.add(new UMLGeneralizableElementLeafCheckBox());
        modifiersPanel.add(new UMLGeneralizableElementRootCheckBox());
        modifiersPanel.add(new UMLBehavioralFeatureQueryCheckBox());
        modifiersPanel.add(new UMLFeatureOwnerScopeCheckBox());

        add(modifiersPanel);
        // TODO: i18n
        add(new UMLOperationConcurrencyRadioButtonPanel("Concurrency:", true));

        addSeperator();

        addField(Translator.localize("label.parameters"),
                getParameterScroll());

        JList exceptList = new UMLList(new UMLReflectionListModel(this,
                "signal", true, "getRaisedSignals", "setRaisedSignals",
                "addRaisedSignal", null), true);
        exceptList.setForeground(Color.blue);
        exceptList.setFont(LookAndFeelMgr.getInstance().getSmallFont());
        addField(Translator.localize("label.raisedsignals"),
                new JScrollPane(exceptList));

        addButton(new PropPanelButton2(this, new ActionNavigateOwner()));
        addButton(new PropPanelButton2(this, 
                        new ActionAddOperation()));
        addButton(new PropPanelButton2(this, ActionNewParameter.getInstance()));
        new PropPanelButton(this, getButtonPanel(), lookupIcon("SignalSending"),
                localize("New Raised Signal"), "buttonAddRaisedSignal", null);
        addButton(new PropPanelButton2(this, new ActionRemoveFromModel()));

    }

    /**
     * @return the returntype of the operation
     */
    public Object getReturnType() {
        Object type = null;
        Object target = getTarget();
        if (org.argouml.model.ModelFacade.isAOperation(target)) {
            Collection params = org.argouml.model.ModelFacade
                    .getParameters(target);
            if (params != null) {
                Iterator iter = params.iterator();
                Object param;
                while (iter.hasNext()) {
                    param = /* (MParameter) */iter.next();
                    if (ModelFacade.getKind(param) 
                            == ModelFacade.RETURN_PARAMETERDIRECTIONKIND) {
                        type = ModelFacade.getType(param);
                        break;
                    }
                }
            }
        }
        return type;
    }

    /**
     * @param type the returntype of the operation
     */
    public void setReturnType(Object/* MClassifier */type) {
        Object target = getTarget();
        if (ModelFacade.isAOperation(target)) {
            Object oper = /* (MOperation) */target;
            Collection params = ModelFacade.getParameters(oper);
            Object param;
            //
            //   remove first (hopefully only) return parameters
            //
            if (type == null) {
                if (params != null) {
                    Iterator iter = params.iterator();
                    while (iter.hasNext()) {
                        param = /* (MParameter) */iter.next();
                        if (ModelFacade.getKind(param)  
                                == ModelFacade.RETURN_PARAMETERDIRECTIONKIND) {
                            ModelFacade.removeParameter(oper, param);
                            break;
                        }
                    }
                }
            } else {
                Object retParam = null;
                if (params != null) {
                    Iterator iter = params.iterator();
                    while (iter.hasNext()) {
                        param = /* (MParameter) */iter.next();
                        if (ModelFacade.getKind(param) 
                                == ModelFacade.RETURN_PARAMETERDIRECTIONKIND) {
                            retParam = param;
                            break;
                        }
                    }
                }
                if (retParam == null) {
                    retParam = UmlFactory.getFactory().getCore()
                            .buildParameter(oper,
                                    ModelFacade.RETURN_PARAMETERDIRECTIONKIND);
                }
                ModelFacade.setType(retParam, type);
            }
        }
    }

    /**
     * @return the raised signals of this operation
     */
    public Collection getRaisedSignals() {
        Collection signals = null;
        Object target = getTarget();
        if (org.argouml.model.ModelFacade.isAOperation(target)) {
            signals = ModelFacade.getRaisedSignals(target);
        }
        return signals;
    }

    /**
     * @param signals the raised signals of this operation
     */
    public void setRaisedSignals(Collection signals) {
        Object target = getTarget();
        if (org.argouml.model.ModelFacade.isAOperation(target)) {
            ModelFacade.setRaisedSignals(target, signals);
        }
    }

    /**
     * @param index add a raised signal
     */
    public void addRaisedSignal(Integer index) {
        Object target = getTarget();
        if (org.argouml.model.ModelFacade.isAOperation(target)) {
            Object oper = /* (MOperation) */target;
            Object newSignal = UmlFactory.getFactory().getCommonBehavior()
                    .createSignal(); 
                    //((MOperation)oper).getFactory().createSignal();
            
            ModelFacade.addOwnedElement(ModelFacade.getNamespace(ModelFacade
                    .getOwner(oper)), newSignal);
            ModelFacade.addRaisedSignal(oper, newSignal);
            TargetManager.getInstance().setTarget(newSignal);
        }
    }

    /**
     * The button to add a raised signal is pressed.
     */
    public void buttonAddRaisedSignal() {
        Object target = getTarget();
        if (org.argouml.model.ModelFacade.isAOperation(target)) {
            addRaisedSignal(new Integer(1));
        }
    }

    /**
     * Appropriate namespace is the namespace of our class, not the class itself
     *
     * @see org.argouml.uml.ui.PropPanel#getDisplayNamespace()
     */
    protected Object getDisplayNamespace() {
        Object namespace = null;
        Object target = getTarget();
        if (ModelFacade.isAAttribute(target)) {
            if (ModelFacade.getOwner(target) != null) {
                namespace = ModelFacade.getNamespace(ModelFacade
                        .getOwner(target));
            }
        }
        return namespace;
    }

    /**
     * Returns the parameterScroll.
     * 
     * @return JScrollPane
     */
    public JScrollPane getParameterScroll() {
        if (parameterScroll == null) {
            JList list = new UMLLinkedList(parameterListModel);
            parameterScroll = new JScrollPane(list);
        }
        return parameterScroll;
    }

} /* end class PropPanelOperation */
