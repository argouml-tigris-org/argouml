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

package org.argouml.uml.ui.model_management;

import org.argouml.application.api.*;
import org.argouml.util.ConfigLoader;
import org.argouml.application.ArgoVersion;
import org.argouml.model.uml.*;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.*;
import org.argouml.uml.ui.foundation.core.*;

import javax.swing.JScrollPane;
import javax.swing.JList;
import org.argouml.model.ModelFacade;
import ru.novosoft.uml.model_management.MSubsystemImpl;

/** A property panel for UML subsystems. */
public class PropPanelSubsystem extends PropPanelPackage
    implements PluggablePropertyPanel {
    ////////////////////////////////////////////////////////////////
    // instance vars

    private JScrollPane _featureScroll;

    private static UMLClassifierFeatureListModel featureListModel =
        new UMLClassifierFeatureListModel();
    ////////////////////////////////////////////////////////////////
    // contructors
    public PropPanelSubsystem() {
        super("Subsystem", ConfigLoader.getTabPropsOrientation());

        addField(Argo.localize("UMLMenu", "label.available-features"), getFeatureScroll());

        new PropPanelButton(this, buttonPanel, _addOpIcon, Argo.localize("UMLMenu", "button.add-operation"), "addOperation", null);
    
    }

    public Class getClassForPanel() {
        // TODO Shouldn't refer to NSUML implementation class
        return MSubsystemImpl.class;
    }

    public String getModuleName() { return "PropPanelSubsystem"; }
    public String getModuleDescription() { return "Property Panel for Subsystem"; }
    public String getModuleAuthor() { return "ArgoUML Core"; }
    public String getModuleVersion() { return ArgoVersion.getVersion(); }
    public String getModuleKey() { return "module.propertypanel.model"; }

    
    
    public void addOperation() {
        Object target = getTarget();
        if (ModelFacade.isAClassifier(target)) {
            Object/*MOperation*/ newOper =
                UmlFactory.getFactory().getCore().buildOperation(target);
            TargetManager.getInstance().setTarget(newOper);
        }
    }
    
    /**
     * Returns the featureScroll.
     * @return JScrollPane
     */
    public JScrollPane getFeatureScroll() {
        if (_featureScroll == null) {
            JList list = new UMLLinkedList(featureListModel);
            _featureScroll = new JScrollPane(list);
        }
        return _featureScroll;
    }


} /* end class PropPanelSubsystem */