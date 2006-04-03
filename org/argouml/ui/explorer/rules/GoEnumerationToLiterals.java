// $Id$
// Copyright (c) 2004-2006 The Regents of the University of California. All
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

package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;

/**
 * Go rule to navigate from an Enumeration to its Literals. <p>
 * Enumeration->Literal.
 */
public class GoEnumerationToLiterals extends AbstractPerspectiveRule {

    /**
     * The constructor.
     */
    public GoEnumerationToLiterals() {
        super();
    }

    /**
     * @see org.argouml.ui.explorer.rules.AbstractPerspectiveRule#getChildren(
     *         java.lang.Object)
     */
    public Collection getChildren(Object parent) {
        if (Model.getFacade().isAEnumeration(parent)) {
            List list = new ArrayList();

            if (Model.getFacade().getEnumerationLiterals(parent) != null
                    && (Model.getFacade().getEnumerationLiterals(parent).size()
                        > 0)) {
                list.addAll(Model.getFacade().getEnumerationLiterals(parent));
            }
            return list;
        }
        return null;
    }

    /**
     * @see org.argouml.ui.explorer.rules.AbstractPerspectiveRule#getRuleName()
     */
    public String getRuleName() {
        return Translator.localize ("misc.enumeration.literal");
    }

    /**
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getDependencies(
     *         java.lang.Object)
     */
    public Set getDependencies(Object parent) {
        if (Model.getFacade().isAEnumeration(parent)) {
            Set set = new HashSet();
            set.add(parent);
            set.addAll(Model.getFacade().getEnumerationLiterals(parent));
            return set;
        }
        return null;
    }

}
