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



// File: PropPanelInstance.java
// Classes: PropPanelInstance
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.ui.behavior.common_behavior;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.JTree;

import org.argouml.application.api.Argo;
import org.argouml.model.ModelFacade;
import org.argouml.swingext.LabelledLayout;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLClassifierComboBoxModel;
import org.argouml.uml.ui.UMLComboBox;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.behavior.common_behavior.MInstance;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * @todo this property panel needs refactoring to remove dependency on
 *       old gui components.
 */
public class PropPanelInstance extends PropPanelModelElement {


    public PropPanelInstance() {
        super("Instance Properties",_instanceIcon, ConfigLoader.getTabPropsOrientation());

        Class mclass = MInstance.class;

        addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());

        UMLClassifierComboBoxModel classifierModel = new UMLClassifierComboBoxModel(this,"isAcceptibleClassifier","classifier","getClassifier","setClassifier",false,MClassifier.class,true);
        UMLComboBox clsComboBox = new UMLComboBox(classifierModel);
        addField("Classifier:", new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-class"),clsComboBox));

        addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),getStereotypeBox()));
        addField(Argo.localize("UMLMenu", "label.namespace"), getNamespaceComboBox());

    //
    //   temporary
    //

        JTree tempTree = new JTree(new Object[] { "Slots", "Links", "Stimuli [Recieved, Sent, In Arg List]" });
        addField("Related Elements", tempTree);
    //

        new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateNamespace",null);	
    }

    
    public boolean isAcceptibleClassifier(MModelElement classifier) {
        return classifier instanceof MClassifier;
    }

    public MClassifier getClassifier() {
        MClassifier classifier = null;
        Object target = getTarget();
        if(target instanceof MInstance) {
        //    UML 1.3 apparently has this a 0..n multiplicity
        //    I'll have to figure out what that means
        //            classifier = ((MInstance) target).getClassifier();

	    // at the moment , we only deal with one classifier
	    Collection col = ((MInstance)target).getClassifiers();
	    if (col != null) {
		Iterator iter = col.iterator();
		if (iter != null && iter.hasNext()) {
		    classifier = (MClassifier)iter.next();
		}
	    }
		    
        }
        return classifier;
    }

    public void setClassifier(MClassifier element) {
        Object target = getTarget();
	
        if(target instanceof MInstance) {
	    MInstance inst = (MInstance)target;
//            ((MInstance) target).setClassifier((MClassifier) element);

	    // delete all classifiers
	    Collection col = inst.getClassifiers();
	    if (col != null) {
		Iterator iter = col.iterator();
		if (iter != null && iter.hasNext()) {
		    MClassifier classifier = (MClassifier)iter.next();
		    inst.removeClassifier(classifier);
		}
	    }
	    // add classifier
	    inst.addClassifier( element);
        }
    }
} /* end class PropPanelInstance */



