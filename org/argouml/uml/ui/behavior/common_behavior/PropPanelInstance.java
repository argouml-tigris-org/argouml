// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.ModelFacade;
import org.argouml.swingext.Orientation;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.PropPanelButton2;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.util.ConfigLoader;

public class PropPanelInstance extends PropPanelModelElement {

    protected JScrollPane stimuliSenderScroll;

    protected JScrollPane stimuliReceiverScroll;

    protected static UMLInstanceSenderStimulusListModel stimuliSenderListModel = new UMLInstanceSenderStimulusListModel();

    protected static UMLInstanceReceiverStimulusListModel stimuliReceiverListModel = new UMLInstanceReceiverStimulusListModel();

    public PropPanelInstance() {
        super("Instance Properties", _instanceIcon, ConfigLoader
                .getTabPropsOrientation());
        Class mclass = (Class) ModelFacade.INSTANCE;

        addField(Translator.localize("UMLMenu", "label.name"),
                getNameTextField());

        // addField(Translator.localize("UMLMenu", "label.stereotype"), new
        // UMLComboBoxNavigator(this, Translator.localize("UMLMenu",
        // "tooltip.nav-stereo"), getStereotypeBox()));
        addField(Translator.localize("UMLMenu", "label.stereotype"),
                getStereotypeBox());
        addField(Translator.localize("UMLMenu", "label.namespace"),
                getNamespaceComboBox());

        buttonPanel.add(new PropPanelButton2(this,
                new ActionNavigateNamespace()));
    }

    public PropPanelInstance(String name, ImageIcon icon,
            Orientation orientation) {
        super(name, icon, orientation);
    }

    public boolean isAcceptibleClassifier(Object/* MModelElement */classifier) {
        return org.argouml.model.ModelFacade.isAClassifier(classifier);
    }

    public Object getClassifier() {
        Object classifier = null;
        Object target = getTarget();
        if (ModelFacade.isAInstance(target)) {
            //    UML 1.3 apparently has this a 0..n multiplicity
            //    I'll have to figure out what that means
            //            classifier = ((MInstance) target).getClassifier();

            // at the moment , we only deal with one classifier
            Collection col = ModelFacade.getClassifiers(target);
            if (col != null) {
                Iterator iter = col.iterator();
                if (iter != null && iter.hasNext()) {
                    classifier = iter.next();
                }
            }
        }
        return classifier;
    }

    public void setClassifier(Object/* MClassifier */element) {
        Object target = getTarget();

        if (org.argouml.model.ModelFacade.isAInstance(target)) {
            Object inst = /* (MInstance) */target;
            //            ((MInstance) target).setClassifier((MClassifier) element);

            // delete all classifiers
            Collection col = ModelFacade.getClassifiers(inst);
            if (col != null) {
                Iterator iter = col.iterator();
                if (iter != null && iter.hasNext()) {
                    Object classifier = /* (MClassifier) */iter.next();
                    ModelFacade.removeClassifier(inst, classifier);
                }
            }
            // add classifier
            ModelFacade.addClassifier(inst, element);
        }
    }

    protected JScrollPane getStimuliSenderScroll() {
        if (stimuliSenderScroll == null) {
            JList stimuliSenderList = new UMLLinkedList(stimuliSenderListModel);
            stimuliSenderList.setVisibleRowCount(1);
            stimuliSenderScroll = new JScrollPane(stimuliSenderList);
        }
        return stimuliSenderScroll;
    }

    protected JScrollPane getStimuliReceiverScroll() {
        if (stimuliReceiverScroll == null) {
            JList stimuliReceiverList = new UMLLinkedList(
                    stimuliReceiverListModel);
            stimuliReceiverList.setVisibleRowCount(1);
            stimuliReceiverScroll = new JScrollPane(stimuliReceiverList);
        }
        return stimuliReceiverScroll;
    }
} /* end class PropPanelInstance */