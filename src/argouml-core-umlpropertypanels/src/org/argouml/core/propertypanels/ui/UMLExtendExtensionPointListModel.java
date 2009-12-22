// $Id: UMLExtendExtensionPointListModel.java 12801 2007-06-09 15:49:57Z mvw $
// Copyright (c) 2002-2006 The Regents of the University of California. All
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

package org.argouml.core.propertypanels.ui;

import java.util.Collection;

import org.argouml.model.Model;
import org.argouml.uml.ui.behavior.use_cases.ActionAddExtendExtensionPoint;
import org.argouml.uml.ui.behavior.use_cases.ActionNewExtendExtensionPoint;

/**
 * @since Oct 6, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
class UMLExtendExtensionPointListModel
        extends UMLModelElementListModel 
        implements Ordered {

    /**
     * Constructor for UMLExtendExtensionPointListModel.
     */
    public UMLExtendExtensionPointListModel(
            final Object modelElement,
            final String propertyName) {
        super(propertyName,
                Model.getMetaTypes().getExtensionPoint(), 
                ActionAddExtendExtensionPoint.getInstance(),
                ActionNewExtendExtensionPoint.SINGLETON);
        setTarget(modelElement);
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        setAllElements(getModelElements());
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object o) {
        return Model.getFacade().isAExtensionPoint(o)
            && getModelElements().contains(o);
    }

    public Collection getModelElements() {
        return Model.getFacade().getExtensionPoints(getTarget());
    }
}
