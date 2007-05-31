// $Id$
// Copyright (c) 2003-2007 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.util.Collection;
import java.util.Iterator;

import org.omg.uml.foundation.core.DataType;
import org.omg.uml.foundation.core.Feature;
import org.omg.uml.foundation.core.Interface;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Stereotype;
import org.omg.uml.foundation.core.TagDefinition;
import org.omg.uml.foundation.core.UmlClass;
import org.omg.uml.modelmanagement.UmlPackage;

/**
 * Utility class to facilitate copying model elements.
 *
 * @author Michael Stockman
 * @since 0.13.2
 */
final class CopyHelper implements org.argouml.model.CopyHelper {

    /**
     * The model implementation.
     */
    private MDRModelImplementation modelImpl;

    /**
     * Constructor to forbid creation of this object.
     *
     * @param implementation
     *            To get other helpers and factories.
     */
    CopyHelper(MDRModelImplementation implementation) {
        modelImpl = implementation;
    }

    /**
     * @see org.argouml.model.CopyHelper#copy(java.lang.Object, java.lang.Object)
     *
     * Make a copy of element in the given namespace.<p>
     *
     * This function is a dispatcher that calls the
     * copyElement(Element,Namespace) function from XXXFactory.<p>
     *
     * This function may fail and return null for any of the following reasons:
     * <ol>
     * <li>No copy function is known for element's type.
     * <li>The copy function fails or throws.
     * </ol>
     *
     * @param anelement
     *            is the element to copy.
     * @param ans
     *            the namespace
     * @return a copy of element, or null.
     *
     * @throws IllegalArgumentException
     *             if element is null.
     */
    public Object copy(Object anelement, Object ans) {
        // Don't explicitly check if element is null
        ModelElement element = (ModelElement) anelement;
        Namespace ns = (Namespace) ans;

        if (element instanceof UmlPackage) {
            return modelImpl.getModelManagementFactory().copyPackage(element, ns);
        }
        if (element instanceof UmlClass) {
            return modelImpl.getCoreFactory().copyClass(element, ns);
        }
        if (element instanceof DataType) {
            return modelImpl.getCoreFactory().copyDataType(element, ns);
        }
        if (element instanceof Interface) {
            return modelImpl.getCoreFactory().copyInterface(element, ns);
        }
        if (element instanceof Feature) {
            return modelImpl.getCoreFactory().copyFeature(element, ns);
        }
        if (element instanceof Stereotype) {
            return modelImpl.getExtensionMechanismsFactory().copyStereotype(
                    element, ns);
        }
        if (element instanceof TagDefinition) {
            return modelImpl.getExtensionMechanismsFactory().copyTagDefinition(
                    element, ns);
        }
        throw new IllegalArgumentException("anelement:" + anelement + ", ans: "
                + ans);
    }

    /**
     * Copy an element and its children into a namespace.
     *
     * @param anelement element to be copied
     * @param ans namespace to copy into
     * @return Object copy of given element and its children
     */
    Object fullCopy(Object anelement, Object ans) {
        ModelElement copy = (ModelElement) copy(anelement, ans);
        if (anelement instanceof Namespace) {
            Collection children = ((Namespace) anelement).getOwnedElement();
            if (!children.isEmpty()) {
                Iterator it = children.iterator();
                while (it.hasNext()) {
                    Object childToCopy = it.next();
                    fullCopy(childToCopy, copy);
                }
            }
        }
        return copy;
    }

}
