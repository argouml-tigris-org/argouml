// $Id: CrEmptyPackage.java 11516 2006-11-25 04:30:15Z tfmorris $
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

package org.argouml.uml.cognitive.critics;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;


/**
 * A critic whether a package/subsystem/model is empty.
 *
 * @author Jason Robbins
 */

//TODO: different critic for packages consisting only
//of references to elements of other packages?

public class CrEmptyPackage extends CrUML {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(CrEmptyPackage.class);

    /**
     * The constructor.
     *
     */
    public CrEmptyPackage() {
        setupHeadAndDesc();
	addSupportedDecision(UMLDecision.MODULARITY);
	addTrigger("ownedElement");
    }

    /*
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     *      java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
//	LOG.debug("predicate2 on " + dm);
	if (!(Model.getFacade().isAPackage(dm))) {
	    return NO_PROBLEM;
	}
	Collection elems = Model.getFacade().getOwnedElements(dm);
	if (elems.size() == 0) {
            LOG.debug("PROBLEM_FOUND on " + dm);
            return PROBLEM_FOUND;
        }
	return NO_PROBLEM;
    }

} /* end class CrEmptyPackage */

