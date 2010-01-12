/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.core.propertypanels.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;

/**
 * @author mkl
 */
class UMLPartitionContentListModel extends  UMLModelElementListModel {

    public UMLPartitionContentListModel(Object modelElement) {
        super("contents", modelElement.getClass(),
                new ActionAddPartitionContent());
        setTarget(modelElement);
    }

    protected void buildModelList() {
        Object partition = getTarget();
        setAllElements(Model.getFacade().getContents(partition));
    }

    protected boolean isValidElement(Object element) {
        if (!Model.getFacade().isAModelElement(element)) {
            return false;
        }
        Object partition = getTarget();
        return Model.getFacade().getContents(partition).contains(element);
    }

    /**
     * @author mkl
     */
    private static class ActionAddPartitionContent extends AbstractActionAddModelElement2 {

        /**
         * The class uid
         */
        private static final long serialVersionUID = -3371454533555822156L;

        public ActionAddPartitionContent() {
            super();
            setMultiSelect(true);
        }

        @Override
        protected void doIt(Collection selected) {
            Object partition = getTarget();
            if (Model.getFacade().isAPartition(partition)) {
                Model.getActivityGraphsHelper().setContents(
                        partition, selected);
            }
        }

        protected List getChoices() {
            List ret = new ArrayList();
            if (Model.getFacade().isAPartition(getTarget())) {
                Object partition = getTarget();
                Object ag = Model.getFacade().getActivityGraph(partition);
                if (ag != null) {
                    Object top = Model.getFacade().getTop(ag);
                    /* There are no composite states, so this will work: */
                    ret.addAll(Model.getFacade().getSubvertices(top));
                }
            }
            return ret;
        }

        protected String getDialogTitle() {
            return Translator.localize("dialog.title.add-contents");
        }

        protected List getSelected() {
            List ret = new ArrayList();
            ret.addAll(Model.getFacade().getContents(getTarget()));
            return ret;
        }
    }
}
