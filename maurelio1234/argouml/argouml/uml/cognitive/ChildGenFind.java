// $Id: ChildGenFind.java 12753 2007-06-04 18:07:56Z mvw $
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

package org.argouml.uml.cognitive;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import org.argouml.kernel.Project;
import org.argouml.model.Model;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.util.ChildGenerator;

/**
 * Convenience class gives critics access to parts of the project.
 * 
 * It defines a gen() function that returns the "children" of any given part of
 * the UML model. It traverses a Project to Diagrams and Models, then uses
 * getModelElementContents to traverse the Models. <p>
 * 
 * Argo's critic Agency uses this to apply critics where appropriate.
 * 
 * @see org.argouml.cognitive.Agency
 * @stereotype singleton
 * @author jrobbins
 */
public class ChildGenFind implements ChildGenerator {
    private static final ChildGenFind SINGLETON = new ChildGenFind();

    /**
     * Reply a Collection of the children of the given Object
     *
     * @see org.tigris.gef.util.ChildGenerator#gen(java.lang.Object)
     */
    public Enumeration gen(Object o) {
	if (o instanceof Project) {
	    Project p = (Project) o;
	    Vector res = new Vector();
	    res.addAll(p.getUserDefinedModels());
	    res.addAll(p.getDiagrams());
	    return res.elements();
	}
        
        if (o instanceof Diagram) {
            Diagram d = (Diagram) o;

            Vector res = new Vector();
            res.addAll(d.getGraphModel().getNodes());
            res.addAll(d.getGraphModel().getEdges());
            return res.elements();
        }

	if (Model.getFacade().isAModelElement(o)) {
            return Collections.enumeration(Model.getFacade()
                    .getModelElementContents(o));
        }
        
	return new Vector().elements();
    }

    /**
     * @return Returns the SINGLETON.
     */
    public static ChildGenFind getSingleton() {
        return SINGLETON;
    }
} /* end class ChildGenFind */
