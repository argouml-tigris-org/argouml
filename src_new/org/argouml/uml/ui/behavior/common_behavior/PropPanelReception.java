// $Id$
// Copyright (c) 2002 The Regents of the University of California. All
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

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLCheckBox;
import org.argouml.uml.ui.UMLReflectionBooleanProperty;
import org.argouml.uml.ui.UMLTextArea;
import org.argouml.uml.ui.UMLTextField2;
import org.argouml.uml.ui.UMLTextProperty;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.core.UMLModelElementNameDocument;

import ru.novosoft.uml.behavior.common_behavior.MReception;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * @author Jaap
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class PropPanelReception extends PropPanelModelElement {
	
	public PropPanelReception() {
		super("Reception", _receptionIcon,2);
		
		Class mclass = MReception.class;
		 
		addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
        addField(new UMLTextField2(new UMLModelElementNameDocument()),1,0,0);

        addCaption(Argo.localize("UMLMenu", "label.stereotype"),2,0,0);
        addField(getStereotypeBox(),2,0,0);

        addCaption(Argo.localize("UMLMenu", "label.namespace"),3,0,0);
        addField(getNamespaceComboBox(),3,0,0);

        addCaption(Argo.localize("UMLMenu", "label.modifiers"),4,0,1);
        JPanel modPanel = new JPanel(new GridLayout(0,3));
        // next line does not contain typing errors, NSUML is not correct (isabstarct instead of isabstract)
        modPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.abstract-lc"),this,new UMLReflectionBooleanProperty("isAbstarct",mclass,"isAbstarct","setAbstarct")));
        modPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.final-lc"),this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
        modPanel.add(new UMLCheckBox(localize("root"),this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
        addField(modPanel,4,0,0);
        
        addCaption(Argo.localize("UMLMenu", "label.signal"),1,1,0);
        addField(new UMLReceptionSignalComboBox(this, new UMLReceptionSignalComboBoxModel()),1,1,0);
        
        addCaption(Argo.localize("UMLMenu", "label.specification"),3,1,0);
        JScrollPane specificationScroll = new JScrollPane(new UMLTextArea(this, new UMLTextProperty(mclass, "specification", "getSpecification" , "setSpecification")),JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        addField(specificationScroll, 3, 1, 1);
        
        new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateUp",null);
	new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
	new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu" ,"button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
	new PropPanelButton(this,buttonPanel,_deleteIcon, Argo.localize("UMLMenu", "button.delete-operation"),"removeElement",null);		
		
        
	}

	
	/**
	 * Returns true if a given modelelement is an acceptable owner of this reception.
	 * Only classifiers that are no datatype are acceptable.
	 * @param element
	 * @return boolean
	 */
	public boolean isAcceptibleClassifier(MModelElement element) {
		return (element instanceof MClassifier && !(element instanceof MDataType));
	}
	
	/**
	 * Returns the owner of the reception. Necessary for the MClassifierComboBox.
	 * @return MClassifier
	 */
	public MClassifier getOwner() {
		Object target = getTarget();
		if (target instanceof MReception) {
			return ((MReception)target).getOwner();
		}
		return null;
	}
	
	/**
	 * Sets the owner of the reception. Necessary for the MClassifierComboBox.
	 * @param owner
	 */
	public void setOwner(MClassifier owner) {
		Object target = getTarget();
		if (target instanceof MReception) {
			MReception rec = (MReception)target;
			if (rec.getOwner() != null) {
				rec.setOwner(null);
			}
			rec.setOwner(owner);
		}
	}

}


