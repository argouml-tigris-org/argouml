package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLCheckBox;
import org.argouml.uml.ui.UMLReflectionBooleanProperty;
import org.argouml.uml.ui.UMLTextArea;
import org.argouml.uml.ui.UMLTextProperty;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
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
        addField(nameField,1,0,0);

        addCaption(Argo.localize("UMLMenu", "label.stereotype"),2,0,0);
        addField(stereotypeBox,2,0,0);

        addCaption(Argo.localize("UMLMenu", "label.namespace"),3,0,0);
        addField(namespaceComboBox,3,0,0);

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
	 * @see org.argouml.uml.ui.PropPanel#isAcceptibleBaseMetaClass(String)
	 */
	protected boolean isAcceptibleBaseMetaClass(String baseClass) {
		return false;
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


