package org.argouml.uml.ui.behavior.collaborations;

import javax.swing.ImageIcon;

import org.argouml.uml.ui.foundation.core.PropPanelModelElement;

/**
 * @author Jaap
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class PropPanelInteraction extends PropPanelModelElement {

	public PropPanelInteraction() {
		super("Interaction", _interactionIcon, 3);
	}

	/**
	 * Used to determine which stereotypes are legal with an interaction. At 
     * the moment, only the stereotypes of namespace and generlizable elements
     * are shown.
	 * @see org.argouml.uml.ui.PropPanel#isAcceptibleBaseMetaClass(String)
	 */
	protected boolean isAcceptibleBaseMetaClass(String baseClass) {
		return (baseClass.equals("Interaction") || baseClass.equals("Modelelement"));
	}

}
