// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBoxModel2;

/**
 * A model for stereotypes to handle their base class, or as it is a class from
 * the UML metamodel its metaclass.
 * 
 * TODO: This class needs to be replaced with a mechanism that supports multiple
 * base classes as added in UML 1.4.
 * 
 * @author mkl
 */
public class UMLMetaClassComboBoxModel extends UMLComboBoxModel2 {

    private Collection metaClasses = 
            Model.getCoreHelper().getAllMetatypeNames();

    /**
     * Constructor.
     */
    @SuppressWarnings("unchecked")
    public UMLMetaClassComboBoxModel() {
        super("baseClass", true);
        Collections.sort(new Vector(metaClasses));
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        if (getTarget() != null) {
            Collection baseClasses =
                    Model.getFacade().getBaseClasses(getTarget());
            if (baseClasses != null) {
                Iterator iter = baseClasses.iterator();
                if (iter.hasNext()) {
                    return iter.next();
                }
            }
        }
        return null;
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        setElements(metaClasses);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
        return metaClasses.contains(element);
    }

}
