// $Id$
// Copyright (c) 2006-2007 The Regents of the University of California. All
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

package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;

/**
 * Rule for ModelElement -> Contained diagrams. <p>
 *
 * The Contained diagrams are all the diagrams which, if the owner is deleted,
 * will be deleted, too.
 *
 * @author michiel
 */
public class GoModelElementToContainedDiagrams extends AbstractPerspectiveRule {

    /*
     * @see org.argouml.ui.explorer.rules.AbstractPerspectiveRule#getRuleName()
     */
    public String getRuleName() {
        return Translator.localize("misc.model-element.contained-diagrams");
    }

    /*
     * @see org.argouml.ui.explorer.rules.AbstractPerspectiveRule#getChildren(java.lang.Object)
     */
    public Collection getChildren(Object parent) {
        if (Model.getFacade().isAModelElement(parent)) {
            Project p = ProjectManager.getManager().getCurrentProject();
            Collection ret = new ArrayList();
            Collection diagrams = p.getDiagrams();
            Iterator it = diagrams.iterator();
            while (it.hasNext()) {
                ArgoDiagram diagram = (ArgoDiagram) it.next();
                if (diagram.getNamespace() == parent) {
                    ret.add(diagram);
                }
            }
            return ret;
        }
        return null;
    }

    /*
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getDependencies(java.lang.Object)
     */
    public Set getDependencies(Object parent) {
        Set set = new HashSet();
        if (Model.getFacade().isAModelElement(parent)) {
            set.add(parent);
        }
        return set;
    }

}
