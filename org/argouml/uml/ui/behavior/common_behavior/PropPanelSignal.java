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

package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.Color;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.behavioralelements.commonbehavior.CommonBehaviorFactory;
import org.argouml.model.uml.foundation.core.CoreHelper;

import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLAddDialog;
import org.argouml.uml.ui.UMLList;
import org.argouml.uml.ui.UMLModelElementListModel;
import org.argouml.uml.ui.UMLReflectionListModel;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.foundation.core.MNamespace;

/**
 * TODO: this property panel needs refactoring to remove dependency on
 *       old gui components.
 */
public class PropPanelSignal extends PropPanelModelElement {


    ////////////////////////////////////////////////////////////////
    // contructors
    public PropPanelSignal() {
        super("Signal", _signalIcon, ConfigLoader.getTabPropsOrientation());

        Class mclass = (Class)ModelFacade.SIGNAL;

        addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
        addField(Argo.localize("UMLMenu", "label.stereotype"), getStereotypeBox());
        addField(Argo.localize("UMLMenu", "label.namespace"), getNamespaceComboBox());

        addSeperator();

        JList contextList = new UMLList(new UMLReflectionListModel(this, "contexts", false, "getContexts", null, "addContext", "deleteContext"), true);
	contextList.setBackground(getBackground());
        contextList.setForeground(Color.blue);
        JScrollPane contextScroll = new JScrollPane(contextList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        addField(Argo.localize("UMLMenu", "label.contexts"), contextScroll);

        JList receiverList = new UMLList(new UMLReflectionListModel(this, "receivers", false, "getReceptions", null, "addReception", "deleteReception"), true);
	receiverList.setBackground(getBackground());
        receiverList.setForeground(Color.blue);
        JScrollPane receiverScroll = new JScrollPane(receiverList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        addField(Argo.localize("UMLMenu", "label.receptions"), receiverScroll);

        new PropPanelButton(this, buttonPanel, _navUpIcon, Argo.localize("UMLMenu", "button.go-up"), "navigateNamespace", null);
        new PropPanelButton(this, buttonPanel, _signalIcon, Argo.localize("UMLMenu", "button.add-signal"), "newSignal", null);
        new PropPanelButton(this, buttonPanel, _receptionIcon, Argo.localize("UMLMenu", "button.add-reception"), "newReception", null);
        new PropPanelButton(this, buttonPanel, _deleteIcon, Argo.localize("UMLMenu", "button.delete-signal"), "removeElement", null);
    }

    public void newSignal() {
        Object target = getTarget();
        if (ModelFacade.isASignal(target)) {
            Object ns = /*(MNamespace)*/ ModelFacade.getNamespace(target);
            if (ns != null) {
                Object newSig = ((MNamespace)ns).getFactory().createSignal();
                ModelFacade.addOwnedElement(ns, newSig);
                TargetManager.getInstance().setTarget(newSig);
            }
        }
    }

    public void newReception() {
    	Object target = getTarget();
    	if (ModelFacade.isASignal(target)) {
	    Object signal = /*(MSignal)*/ target;
	    Object reception = CommonBehaviorFactory.getFactory().buildReception(signal);
            TargetManager.getInstance().setTarget(reception);
    	}
    }


    /**
     * Gets all behavioralfeatures that form the contexts that can send the signal
     * @return Collection
     */
    public Collection getContexts() {
    	Collection contexts = new Vector();
    	Object target = getTarget();
    	if (org.argouml.model.ModelFacade.isASignal(target)) {
	    contexts = ModelFacade.getContexts(target);
    	}
    	return contexts;
    }


    /**
     * Opens a new window where existing behavioral features can be added to the signal as context.
     * @param index
     */
    public void addContext(Integer index) {
    	Object target = getTarget();
    	if (org.argouml.model.ModelFacade.isASignal(target)) {
	    Object signal = /*(MSignal)*/ target;
	    Vector choices = new Vector();
	    Vector selected = new Vector();
	    choices.addAll(CoreHelper.getHelper().getAllBehavioralFeatures());
	    selected.addAll(ModelFacade.getContexts(signal));
	    UMLAddDialog dialog = new UMLAddDialog(choices, selected, Argo.localize("UMLMenu", "dialog.title.add-contexts"), true, true);
	    int returnValue = dialog.showDialog(ProjectBrowser.getInstance());
	    if (returnValue == JOptionPane.OK_OPTION) {
		ModelFacade.setContexts(signal, dialog.getSelected());
	    }
    	}
    }

    /**
     * Deletes the context at index from the list with contexts.
     * @param index
     */
    public void deleteContext(Integer index) {
    	Object target = getTarget();
    	if (ModelFacade.isASignal(target)) {
	    Object signal = /*(MSignal)*/ target;
	    Object feature = /*(MBehavioralFeature)*/ UMLModelElementListModel.elementAtUtil(ModelFacade.getContexts(signal), index.intValue(), null);
	    ModelFacade.removeContext(signal, feature);
    	}
    }

    /**
     * Returns all behavioral features that can recept this signal.
     * @return Collection
     */
    public Collection getReceptions() {
    	Collection receptions = new Vector();
    	Object target = getTarget();
    	if (ModelFacade.isASignal(target)) {
	    receptions = ModelFacade.getReceptions(target);
    	}
    	return receptions;
    }

    /**
     * Adds a new reception. The user has to fill in the classifier the reception
     * belongs too on the proppanel of the reception
     * @param index
     */
    public void addReception(Integer index) {
    	newReception();
    }

    /**
     * Deletes the reception at index from the list with receptions.
     * @param index
     */
    public void deleteReception(Integer index) {
    	Object target = getTarget();
    	if (ModelFacade.isASignal(target)) {
	    Object signal = /*(MSignal)*/ target;
	    Object reception = /*(MReception)*/ UMLModelElementListModel.elementAtUtil(ModelFacade.getReceptions(signal), index.intValue(), null);
	    ModelFacade.removeReception(signal, reception);
    	}
    }






} /* end class PropPanelSignal */