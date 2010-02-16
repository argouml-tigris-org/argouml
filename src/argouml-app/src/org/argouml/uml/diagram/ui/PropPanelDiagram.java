/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import static org.argouml.model.Model.getModelManagementFactory;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

import org.argouml.i18n.Translator;
import org.argouml.ui.TabModelTarget;
import org.argouml.ui.UndoableAction;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.Relocatable;
import org.argouml.uml.ui.AbstractActionNavigate;
import org.argouml.uml.ui.ActionDeleteModelElements;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLSearchableComboBox;
import org.argouml.uml.util.PathComparator;

/**
 * This class represents the properties panel for a Diagram.
 *
 */
public class PropPanelDiagram extends PropPanel implements TabModelTarget {

    private JComboBox homeModelSelector;
    private UMLDiagramHomeModelComboBoxModel homeModelComboBoxModel =
        new UMLDiagramHomeModelComboBoxModel();

    /**
     * Construct a property panel with a given name and icon.
     * 
     * @param diagramName the diagram name to use as the title of the panel
     * @param icon an icon to display on the panel
     */
    protected PropPanelDiagram(String diagramName, ImageIcon icon) {
        super(diagramName, icon);

        JTextField field = new JTextField();
        // TODO: This should probably only update the project when the user
        // presses Return or focus is lost
        field.getDocument().addDocumentListener(new DiagramNameDocument(field));
        addField("label.name", field);

        addField("label.home-model", getHomeModelSelector());

        addAction(new ActionNavigateUpFromDiagram());
        addAction(ActionDeleteModelElements.getTargetFollower());
    }

    /**
     * Default constructor if there is no child of this class that can show the
     * diagram.
     */
    public PropPanelDiagram() {
        this("Diagram", null);
    }

    /**
     * Returns the home-model selector. This is a component which allows the
     * user to select a single item as the home-model, 
     * i.e. the "owner" of the diagram.
     *
     * @return a component for selecting the home-model
     */
    protected JComponent getHomeModelSelector() {
        if (homeModelSelector == null) {
            homeModelSelector = new UMLSearchableComboBox(
                    homeModelComboBoxModel,
                    new ActionSetDiagramHomeModel(), true);
        }
        return new UMLComboBoxNavigator(
                Translator.localize("label.namespace.navigate.tooltip"),
                homeModelSelector);
    }

}

class UMLDiagramHomeModelComboBoxModel extends UMLComboBoxModel2 {

    public UMLDiagramHomeModelComboBoxModel() {
        super(ArgoDiagram.NAMESPACE_KEY, false);
    }

    @Override
    protected void buildModelList() {
        Object target = getTarget();
        List list = new ArrayList();
        if (target instanceof Relocatable) {
            Relocatable diagram = (Relocatable) target;
            for (Object obj : diagram.getRelocationCandidates(
                    getModelManagementFactory().getRootModel())) {
                if (diagram.isRelocationAllowed(obj)) {
                    list.add(obj);
                }
            }
        }
        /* This should not be needed if the above is correct, 
         * but let's be sure: */
        list.add(getSelectedModelElement());
        Collections.sort(list, new PathComparator());
        setElements(list);
    }
    
    @Override
    protected void buildMinimalModelList() {
        Collection list = new ArrayList(1);
        list.add(getSelectedModelElement());
        setElements(list);
        setModelInvalid();
    }
    
    @Override
    protected boolean isLazy() {
        return true;
    }

    @Override
    protected Object getSelectedModelElement() {
        Object t = getTarget();
        if (t instanceof ArgoDiagram) {
            return ((ArgoDiagram) t).getOwner();
        }
        return null;
    }

    @Override
    protected boolean isValidElement(Object element) {
        Object t = getTarget();
        if (t instanceof Relocatable) {
            return ((Relocatable) t).isRelocationAllowed(element);
        }
        return false;
    }
}

class ActionSetDiagramHomeModel extends UndoableAction {
    protected ActionSetDiagramHomeModel() {
        super();
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof UMLComboBox2) {
            UMLComboBox2 box = (UMLComboBox2) source;
            Object diagram = box.getTarget();
            Object homeModel = box.getSelectedItem();
            if (diagram instanceof Relocatable) {
                Relocatable d = (Relocatable) diagram;
                if (d.isRelocationAllowed(homeModel)) {
                    d.relocate(homeModel);
                }
            }
        }
    }
}

class ActionNavigateUpFromDiagram extends AbstractActionNavigate {

    /**
     * The constructor.
     */
    public ActionNavigateUpFromDiagram() {
        super("button.go-up", true);
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionNavigate#navigateTo(java.lang.Object)
     */
    protected Object navigateTo(Object source) {
        if (source instanceof ArgoDiagram) {
            return ((ArgoDiagram) source).getNamespace();
        }
        return null;
    }
    
    /*
     * @see javax.swing.Action#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object target = TargetManager.getInstance().getTarget();
        Object destination = navigateTo(target);
        if (destination != null) {
            TargetManager.getInstance().setTarget(destination);
        }
    }
}
