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

// $header$
package org.argouml.uml.ui.foundation.core;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.swingext.Orientation;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLLinkedList;

/**
 * @since Nov 6, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class PropPanelFeature extends PropPanelModelElement {

    private UMLFeatureOwnerScopeCheckBox _ownerScopeCheckbox;
    private UMLComboBox2 _ownerComboBox;   
    private JScrollPane _ownerScroll; 
    
    private static UMLFeatureOwnerListModel ownerListModel; 
    private static UMLFeatureOwnerComboBoxModel ownerComboBoxModel;

    /**
     * Constructor for PropPanelFeature.
     * @param name
     * @param icon
     * @param orientation
     */
    protected PropPanelFeature(
        String name,
        Orientation orientation) {
        super(name, orientation);       
    }


	/**
	 * Returns the ownerComboBox.
	 * @return UMLComboBox2
	 */
	public UMLComboBox2 getOwnerComboBox() {
		if (_ownerComboBox == null) {
			if (ownerComboBoxModel == null) {
				ownerComboBoxModel = new UMLFeatureOwnerComboBoxModel();
			}
			_ownerComboBox = new UMLComboBox2(ownerComboBoxModel, ActionSetFeatureOwner.SINGLETON);
		}
		return _ownerComboBox;
	}

	/**
	 * Returns the ownerScroll.
	 * @return JScrollPane
	 */
	public JScrollPane getOwnerScroll() {
        if (_ownerScroll == null) {
            if (ownerListModel == null) {
                ownerListModel = new UMLFeatureOwnerListModel();    	
            }
            JList list = new UMLLinkedList(ownerListModel);
            list.setVisibleRowCount(1);
            _ownerScroll = new JScrollPane(list);
		}
        return _ownerScroll;
	}

	/**
	 * Returns the ownerScopeCheckbox.
	 * @return UMLFeatureOwnerScopeCheckBox
	 */
	public UMLFeatureOwnerScopeCheckBox getOwnerScopeCheckbox() {
        if (_ownerScopeCheckbox == null) {
            _ownerScopeCheckbox = new UMLFeatureOwnerScopeCheckBox();
        }    
		return _ownerScopeCheckbox;       
	}

}
