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



// 21 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Changed to use the
// labels "Generalizes:" and "Specializes:" for inheritance.


package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.Color;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.model.uml.behavioralelements.commonbehavior.CommonBehaviorFactory;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLAddDialog;
import org.argouml.uml.ui.UMLList;
import org.argouml.uml.ui.UMLModelElementListModel;
import org.argouml.uml.ui.UMLReflectionListModel;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import ru.novosoft.uml.behavior.common_behavior.MReception;
import ru.novosoft.uml.behavior.common_behavior.MSignal;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
import ru.novosoft.uml.foundation.core.MNamespace;

public class PropPanelSignal extends PropPanelModelElement {


    ////////////////////////////////////////////////////////////////
    // contructors
    public PropPanelSignal() {
        super("Signal", _signalIcon,2);

        Class mclass = MSignal.class;

        addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
        addField(nameField,1,0,0);


        addCaption(Argo.localize("UMLMenu", "label.stereotype"),2,0,0);
        addField(stereotypeBox,2,0,0);

        addCaption(Argo.localize("UMLMenu", "label.namespace"),3,0,1);
        addField(namespaceComboBox,3,0,0);
        
        addCaption(Argo.localize("UMLMenu", "label.contexts"), 1,1,0);
        JList contextList = new UMLList(new UMLReflectionListModel(this,"contexts",false,"getContexts",null,"addContext","deleteContext"),true);
 		contextList.setBackground(getBackground());
        contextList.setForeground(Color.blue);
        JScrollPane contextScroll=new JScrollPane(contextList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        addField(contextScroll,1,1,0.5);
        
        addCaption(Argo.localize("UMLMenu", "label.receptions"), 2,1,0);
        JList receiverList = new UMLList(new UMLReflectionListModel(this,"receivers",false,"getReceptions",null,"addReception","deleteReception"),true);
 		receiverList.setBackground(getBackground());
        receiverList.setForeground(Color.blue);
        JScrollPane receiverScroll=new JScrollPane(receiverList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        addField(receiverScroll,2,1,0.5);

		new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateNamespace",null);
		new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
		new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
		
		// new PropPanelButton(this,buttonPanel,_addOpIcon, Argo.localize("UMLMenu", "button.add-operation"),"addOperation",null);
		// new PropPanelButton(this,buttonPanel,_addAttrIcon, Argo.localize("UMLMenu", "button.add-attribute"),"addAttribute",null);
		//new PropPanelButton(this,buttonPanel,_addAssocIcon, Argo.localize("UMLMenu", "button.add-association"),"addAssociation",null);
		//new PropPanelButton(this,buttonPanel,_generalizationIcon, Argo.localize("UMLMenu", "button.add-generalization"),"addGeneralization",null);
		//new PropPanelButton(this,buttonPanel,_deleteIcon, Argo.localize("UMLMenu", "button.delete-actor"),"removeElement",null);
		//new PropPanelButton(this,buttonPanel,_realizationIcon, Argo.localize("UMLMenu", "button.add-realization"),"addRealization",null);
		new PropPanelButton(this,buttonPanel,_signalIcon, Argo.localize("UMLMenu", "button.add-signal"),"newSignal",null);
		new PropPanelButton(this,buttonPanel,_receptionIcon, Argo.localize("UMLMenu", "button.add-reception"), "newReception", null);
		new PropPanelButton(this,buttonPanel,_deleteIcon, Argo.localize("UMLMenu", "button.delete-signal"),"removeElement",null);

    }

    public void newSignal() {
        Object target = getTarget();
        if(target instanceof MSignal) {
            MNamespace ns = ((MSignal) target).getNamespace();
            if(ns != null) {
                MSignal newSig = ns.getFactory().createSignal();
                ns.addOwnedElement(newSig);
                navigateTo(newSig);
            }
        }
    }
    
    public void newReception() {
    	Object target = getTarget();
    	if (target instanceof MSignal) {
    		MSignal signal = (MSignal)target;
    		MReception reception = CommonBehaviorFactory.getFactory().buildReception(signal);
    		navigateTo(reception);
    	}
    }


    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Signal") ||
            baseClass.equals("Classifier") ||
            baseClass.equals("GeneralizableElement") ||
            baseClass.equals("Namespace");
    }
    
	/**
	 * Gets all behavioralfeatures that form the contexts that can send the signal
	 * @return Collection
	 */
    public Collection getContexts() {
    	Collection contexts = new Vector();
    	Object target = getTarget();
    	if (target instanceof MSignal) {
    		contexts = ((MSignal)target).getContexts();
    	}
    	return contexts;
    }
    
    
	/**
	 * Opens a new window where existing behavioral features can be added to the signal as context.
	 * @param index
	 */
    public void addContext(Integer index) {
    	Object target = getTarget();
    	if (target instanceof MSignal) {
    		MSignal signal = (MSignal)target;	
	    	Vector choices = new Vector();
	    	Vector selected = new Vector();
	    	choices.addAll(CoreHelper.getHelper().getAllBehavioralFeatures());
	    	selected.addAll(signal.getContexts());
	    	UMLAddDialog dialog = new UMLAddDialog(choices, selected, Argo.localize("UMLMenu", "dialog.title.add-contexts"), true, true);
	    	int returnValue = dialog.showDialog(ProjectBrowser.TheInstance);
	    	if (returnValue == JOptionPane.OK_OPTION) {
	    		signal.setContexts(dialog.getSelected());
	    	}
    	}
    }
    
	/**
	 * Deletes the context at index from the list with contexts.
	 * @param index
	 */
    public void deleteContext(Integer index) {
    	Object target = getTarget();
    	if (target instanceof MSignal) {
    		MSignal signal = (MSignal)target;
    		MBehavioralFeature feature = (MBehavioralFeature)UMLModelElementListModel.elementAtUtil(signal.getContexts(), index.intValue(), null);
    		signal.removeContext(feature);
    	}
    }
    
	/**
	 * Returns all behavioral features that can recept this signal.
	 * @return Collection
	 */
    public Collection getReceptions() {
    	Collection receptions = new Vector();
    	Object target = getTarget();
    	if (target instanceof MSignal) {
    		receptions = ((MSignal)target).getReceptions();
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
    	if (target instanceof MSignal) {
    		MSignal signal = (MSignal)target;
    		MReception reception = (MReception)UMLModelElementListModel.elementAtUtil(signal.getReceptions(), index.intValue(), null);
    		signal.removeReception(reception);
    	}
    }
    
    
  
    


} /* end class PropPanelSignal */

