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



// File: PropPanelState.java
// Classes: PropPanelState
// Original Author: your email address here
// $Id$

package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.swingext.LabelledLayout;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionLanguageField;
import org.argouml.uml.ui.UMLExpressionModel;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.util.ConfigLoader;
import ru.novosoft.uml.behavior.state_machines.MGuard;
import ru.novosoft.uml.foundation.data_types.MBooleanExpression;

/**
 * A property panel for Guards. Rewrote this class to comply to Bob Tarling's layout 
 * mechanism and to include all valid properties as defined in the UML 1.3 spec.
 * @since Dec 14, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class PropPanelGuard extends PropPanelModelElement {

    ////////////////////////////////////////////////////////////////
    // contructors
    public PropPanelGuard() {
        super("Guard",_guardIcon, ConfigLoader.getTabPropsOrientation());

        addField(Argo.localize("UMLMenu", "label.name"), nameField);
        addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox));
        addField(Argo.localize("UMLMenu", "label.namespace"),namespaceScroll);
        
        JList transitionList = new UMLLinkedList(this, new UMLGuardTransitionListModel(this));
        addField(Argo.localize("UMLMenu", "label.transition"), new JScrollPane(transitionList));
        
        add(LabelledLayout.getSeperator());
    
        UMLExpressionModel expressionModel = new UMLExpressionModel(this,MGuard.class,"expression",
		    MBooleanExpression.class,"getExpression","setExpression");
        addField(Argo.localize("UMLMenu", "label.expression"), new JScrollPane(new UMLExpressionBodyField(expressionModel,true),JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        addField(Argo.localize("UMLMenu", "label.language"), new UMLExpressionLanguageField(expressionModel,true));        	

	new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateUp",null);
	new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu" ,"button.go-back"),"navigateBackAction","isNavigateBackEnabled");
	new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
	new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete"),"removeElement",null);

  }

} /* end class PropPanelState */



