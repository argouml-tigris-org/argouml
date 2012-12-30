/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *    Thomas Neustupny
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2005-2009 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.model.UmlChangeEvent;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.util.PathComparator;

/**
 * A model for tagdefinitions.
 * @author lmaitre
 * @since October 27, 2005
 */
public class UMLTagDefinitionComboBoxModel  extends UMLComboBoxModel2 {

    private static final Logger LOG =
        Logger.getLogger(UMLTagDefinitionComboBoxModel.class.getName());

    /**
     * Constructor for UMLTagDefinitionComboBoxModel.
     */
    public UMLTagDefinitionComboBoxModel() {
        // stereotypes applied to the target mostly control which TDs
        // (but see below for other listeners too)
        super("stereotype", false);
    }

    @Override
    protected void addOtherModelEventListeners(Object target) {
        // Ask to be notified of any changes to TagDefinitions so that we
        // can track new ones, name changes, etc
        Model.getPump().addClassModelEventListener(this,
                Model.getMetaTypes().getTagDefinition(), (String[]) null);
    }

    @Override
    protected void removeOtherModelEventListeners(Object target) {
        Model.getPump().addClassModelEventListener(this,
                Model.getMetaTypes().getTagDefinition(), (String[]) null);
    }

    @Override
    public void modelChanged(UmlChangeEvent evt) {
        // because we're listening for stereotypes being added and removed
        // but we're really interested in their owned tag definitions,
        // the default implementation won't work for us

        if (Model.getFacade().isATagDefinition(evt.getSource())) {
            LOG.log(Level.FINE, "Got TagDefinition event {0}", evt);

            // Just mark for rebuild next time since we use lazy loading
            setModelInvalid();
        } else if ("stereotype".equals(evt.getPropertyName())) {
            LOG.log(Level.FINE, "Got stereotype event {0}", evt);
            // A stereotype got applied or removed
            // Just mark for rebuild next time since we use lazy loading
            setModelInvalid();
        } else {
            LOG.log(Level.FINE, "Got other event {0}", evt);
        }
    }


    @Override
    public boolean isLazy() {
        return true;
    }


    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
        Object owner = Model.getFacade().getOwner(element);
        return (Model.getFacade().isATagDefinition(element)
                && (owner == null || Model
                .getFacade().getStereotypes(getTarget()).contains(owner)));
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#setSelectedItem(java.lang.Object)
     */
    @Override
    public void setSelectedItem(Object o) {
        setFireListEvents(false);
        super.setSelectedItem(o);
        setFireListEvents(true);
    }


    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        setElements(getApplicableTagDefinitions(getTarget()));
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        return getSelectedItem();
    }

    Collection getApplicableTagDefinitions(Object element) {
        Set<List<String>> paths = new HashSet<List<String>>();
        Set<Object> availableTagDefs =
            new TreeSet<Object>(new PathComparator());
        Collection stereotypes = Model.getFacade().getStereotypes(element);
        Project project = ProjectManager.getManager().getCurrentProject();
        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
            for (Object model : project.getModels()) {
                // TODO: Won't our use of PathComparator take care of uniqueness?
                addAllUniqueModelElementsFrom(availableTagDefs, paths,
                        Model.getModelManagementHelper().getAllModelElementsOfKind(
                                model,
                                Model.getMetaTypes().getTagDefinition()));
            }
            // TODO: Won't our use of PathComparator take care of uniqueness?
            addAllUniqueModelElementsFrom(availableTagDefs, paths, project
                    .getProfileConfiguration().findByMetaType(
                            Model.getMetaTypes().getTagDefinition()));

            List notValids = new ArrayList();
            for (Object tagDef : availableTagDefs) {
                Object owner = Model.getFacade().getOwner(tagDef);
                if (owner != null && !stereotypes.contains(owner)) {
                    notValids.add(tagDef);
                }
            }
            availableTagDefs.removeAll(notValids);
        } else {
            // since UML2 it's easier: TDs only via stereotypes
            for (Object st : stereotypes) {
                availableTagDefs.addAll(Model.getFacade().getAttributes(st));
            }
        }
        return availableTagDefs;
    }

    /**
     * Helper method for buildModelList.<p>
     *
     * Adds those elements from source that do not have the same path as any
     * path in paths to elements, and its path to paths. Thus elements will
     * never contain two objects with the same path, unless they are added by
     * other means.
     */
    private static void addAllUniqueModelElementsFrom(Set elements,
            Set<List<String>> paths, Collection sources) {

        for (Object source : sources) {
            List<String> path = Model.getModelManagementHelper().getPathList(
                    source);
            if (!paths.contains(path)) {
                paths.add(path);
                elements.add(source);
            }
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -4194727034416788372L;
}
