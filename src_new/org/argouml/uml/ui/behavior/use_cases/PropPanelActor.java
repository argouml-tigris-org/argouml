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

// File: PropPanelActor.java
// Classes: PropPanelActor
// Original Author: jrobbins@ics.uci.edu
// $Id$

// 21 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Changed to use the
// labels "Generalizes:" and "Specializes:" for inheritance.

// 4 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Labels corrected to
// "Generalizations:" and "Specializations". Problems with these two fields
// fixed. Display of dependencies dropped (why show for actors and not other
// classifiers). Tool tip changed to "Add actor"


package org.argouml.uml.ui.behavior.use_cases;

import org.argouml.application.api.*;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesFactory;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesHelper;
import org.argouml.swingext.LabelledLayout;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.*;
import org.argouml.uml.ui.foundation.core.*;
import org.argouml.util.ConfigLoader;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.model_management.*;


/**
 * <p>Builds the property panel for a actor.</p>
 *
 * <p>This is a type of Classifier, and like other Classifiers can have
 *   attributes and operations (some processes use these to define
 *   requirements). <em>Note</em>. ArgoUML has no way to display these.</p>
 */

public class PropPanelActor extends PropPanelClassifier {


    ///////////////////////////////////////////////////////////////////////////
    //
    // Constructors
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Constructor. Builds up the various fields required.</p>
     */

    public PropPanelActor() {
    	super("Actor", _actorIcon, ConfigLoader.getTabPropsOrientation());
    	addField(Argo.localize("UMLMenu", "label.name"), nameField);
    	addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox));
    	addField(Argo.localize("UMLMenu", "label.namespace"),namespaceScroll);
    	
    	add(LabelledLayout.getSeperator());
    	
    	addField(Argo.localize("UMLMenu", "label.generalizations"), extendsScroll);
    	addField(Argo.localize("UMLMenu", "label.specializations"), derivedScroll);
    	
    	add(LabelledLayout.getSeperator());
    	
    	JList connectList = new UMLList(new UMLActorAssociationListModel(this,null,true),true);
      	connectList.setForeground(Color.blue);
      	connectList.setVisibleRowCount(3);
      	connectScroll= new JScrollPane(connectList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	
    	addField(Argo.localize("UMLMenu", "label.associations"), connectScroll);
    	/*

        // Invoke the Classifier constructor, but passing in our name and
        // representation and requesting 2 columns

        super("Actor", _actorIcon, 2);

        // The first column. All single line entries, so we just let the label
        // at the bottom (modifiers) take the vertical weighting.

        // nameField, stereotypeBox and namespaceScroll are all set up by
        // PropPanelModelElement

        addCaption(Argo.localize("UMLMenu", "label.name"), 1, 0, 0);
        addField(nameField, 1, 0, 0);

        addCaption(Argo.localize("UMLMenu", "label.stereotype"), 2, 0, 0);
        addField(new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox),
                 2, 0, 0);

        addCaption(Argo.localize("UMLMenu", "label.namespace"), 3, 0, 0);
        addLinkField(namespaceScroll, 3, 0, 0);

        // For modifiers we create a grid with three columns. We really ought
        // to inherit this from GeneralizableElement, but since Java can't do
        // multiple inheritance, it gets done here (it would at least be better
        // in PropPanelClassifier).

        PropPanelModifiers mPanel = new PropPanelModifiers(3);
        Class              mclass = MActor.class;

        mPanel.add("isAbstract", mclass, "isAbstract", "setAbstract",
                   Argo.localize("UMLMenu", "checkbox.abstract-lc"), this);
        mPanel.add("isLeaf", mclass, "isLeaf", "setLeaf",
                   Argo.localize("UMLMenu", "checkbox.final-lc"), this);
        mPanel.add("isRoot", mclass, "isRoot", "setRoot",
                   localize("root"), this);

        addCaption(Argo.localize("UMLMenu", "label.modifiers"), 4, 0, 1);
        addField(mPanel, 4, 0, 0);

        // The second column. These are all potentially multi-valued, so share
        // the vertical weighting.

        // Generalization, specialization and associations are inherited from
        // PropPanelClassifier

        addCaption("Generalizations:",0,1,1);
        addLinkField(extendsScroll, 0, 1, 1);

        addCaption("Specializations:", 1, 1, 1);
        addField(derivedScroll, 1, 1, 1);

        addCaption(Argo.localize("UMLMenu", "label.associations"),2,1,1);
        addField(connectScroll,2,1,1);
        */

        // The toolbar buttons that go at the top.

        new PropPanelButton(this, buttonPanel, _navUpIcon,
                            Argo.localize("UMLMenu", "button.go-up"), "navigateNamespace",
                            null);
        new PropPanelButton(this, buttonPanel, _navBackIcon,
                            Argo.localize("UMLMenu", "button.go-back"), "navigateBackAction",
                            "isNavigateBackEnabled");
        new PropPanelButton(this, buttonPanel, _navForwardIcon,
                            Argo.localize("UMLMenu", "button.go-forward"), "navigateForwardAction",
                            "isNavigateForwardEnabled");
        new PropPanelButton(this, buttonPanel, _actorIcon,
                            Argo.localize("UMLMenu", "button.add-actor"), "newActor",
                            null);
        new PropPanelButton(this, buttonPanel, _deleteIcon,
                            localize("Delete"), "removeElement",
                            null);
    }


    /**
     * <p>Invoked by the "Add actor" toolbar button to create a new actor
     *   property panel in the same namespace as the current actor.</p>
     *
     * <p>This code uses getFactory and adds the actor explicitly to the
     *   namespace. Extended to actually navigate to the new actor.</p>
     */

    public void newActor() {
        Object target = getTarget();

        if(target instanceof MActor) {
            MNamespace ns = ((MActor) target).getNamespace();
            navigateTo( UseCasesFactory.getFactory().buildActor(ns));
        }
    }

    /**
     * <p>A predicate to test if a given base class (below ModelElement) is
     *   appropriate to us. Used to determine the stereotypes we can use.</p>
     *
     * <p>For us this means Actor, Classifier, Namespace or
     *   GeneralizableElement.</p>
     *
     * @param baseClass  a string with the name of a UML MetaClass (no leading
     *                   M)
     *
     * @return           <code>true</code> if this is a suitable base class for
     *                   us. <code>false</code> otherwise.
     */

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {

        return baseClass.equals("Actor") ||
            baseClass.equals("Classifier") ||
            baseClass.equals("GeneralizableElement") ||
            baseClass.equals("Namespace");
    }


    /**
     * @see org.argouml.model.uml.behavioralelements.usecases.UseCasesHelper#getAllActors()
     */
	protected Vector getGeneralizationChoices() {
		Vector choices = new Vector();
		choices.addAll(UseCasesHelper.getHelper().getAllActors());
		return choices;
	}
	

} /* end class PropActor */
