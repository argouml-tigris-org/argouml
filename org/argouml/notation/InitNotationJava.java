// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.notation;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.uml.notation.java.AssociationEndNameNotationJava;
import org.argouml.uml.notation.java.AttributeNotationJava;
import org.argouml.uml.notation.java.ModelElementNameNotationJava;
import org.argouml.uml.notation.java.OperationNotationJava;

/**
 * This class is the only one that has the knowledge of the complete list of
 * NotationProvider4 implementations for Java.
 *
 * @author mvw@tigris.org
 */
class InitNotationJava {

    /**
     * static initializer, register all appropriate notations.
     */
    static void init() {
        NotationProviderFactory2 npf = NotationProviderFactory2.getInstance();
        NotationName name = /*Notation.findNotation("Java");*/
            Notation.makeNotation(
                    "Java",
                    null,
                    ResourceLoaderWrapper.lookupIconResource("JavaNotation"));

        npf.addNotationProvider(
                NotationProviderFactory2.TYPE_NAME,
                name, ModelElementNameNotationJava.class);
        npf.addNotationProvider(
                NotationProviderFactory2.TYPE_ATTRIBUTE,
                name, AttributeNotationJava.class);
        npf.addNotationProvider(
                NotationProviderFactory2.TYPE_OPERATION,
                name, OperationNotationJava.class);
        npf.addNotationProvider(
                NotationProviderFactory2.TYPE_ASSOCIATION_END_NAME,
                name, AssociationEndNameNotationJava.class);
    }

}
