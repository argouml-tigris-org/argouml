// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;

/**
 * Rule for Project->Root (Top level) Elements.
 * 
 * TODO: As currently implemented it returns all top level elements in 
 * the model repository, not just those in a given project.  Since we
 * only support a single project at a time currently, these are equivalent
 * but this will need to be enhanced with additional bookkeeping in 
 * Project when we support multiple open projects - tfm.
 *
 * TODO: I changed it to get the roots from the project. It was showing
 * the profiles that were loaded but were not applied to the current project
 * - maurelio1234.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 */
public class GoProjectToRoots extends AbstractPerspectiveRule {

    
    public String getRuleName() {
	return Translator.localize("misc.project.roots");
    }


    public Collection getChildren(Object parent) {
        if (parent instanceof Project) {
            return ((Project) parent).getRoots();
        }
	return Collections.EMPTY_SET;
    }

    
    public Set getDependencies(Object parent) {
	return Collections.EMPTY_SET;
    }
}
