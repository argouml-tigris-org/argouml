// $Id$
// Copyright (c) 2003-2005 The Regents of the University of California. All
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

package org.argouml.model.uml;

import org.argouml.model.CopyHelper;

import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.model_management.MPackage;

/**
 * Utility class to facilitate copying model elements.
 *
 * @author Michael Stockman
 * @since 0.13.2
 */
final class CopyHelperImpl implements CopyHelper {

    /**
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * Constructor to forbid creation of this object.
     *
     * @param implementation To get other helpers and factories.
     */
    CopyHelperImpl(NSUMLModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * Make a copy of element in the given namespace.<p>
     *
     * This function is a dispatcher that calls the
     * copyElement(Element,Namespace) function from
     * XXXFactory.<p>
     *
     * This function may fail and return null for any of the following
     * reasons:<ol>
     * <li>No copy function is known for element's type.
     * <li>The copy function fails or throws.
     * </ol>
     *
     * @param anelement is the element to copy.
     * @param ans the namespace
     * @return a copy of element, or null.
     *
     * @throws IllegalArgumentException if element is null.
     */
    public Object copy(Object anelement, Object ans) {
	// Don't explicitly check if element is null
        MModelElement element = (MModelElement) anelement;
        MNamespace ns = (MNamespace) ans;

        if (element instanceof MPackage) {
            return nsmodel.getModelManagementFactory().copyPackage(element, ns);
        }
        if (element instanceof MClass) {
            return nsmodel.getCoreFactory().copyClass(element, ns);
        }
        if (element instanceof MDataType) {
            return nsmodel.getCoreFactory().copyDataType(element, ns);
        }
        if (element instanceof MInterface) {
            return nsmodel.getCoreFactory().copyInterface(element, ns);
        }
        if (element instanceof MStereotype) {
            return
            	nsmodel.getExtensionMechanismsFactory()
            		.copyStereotype(element, ns);
        }
        throw new IllegalArgumentException();
    }
}
