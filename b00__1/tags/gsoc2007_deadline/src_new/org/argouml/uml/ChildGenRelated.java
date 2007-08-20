// $Id:ChildGenRelated.java 12777 2007-06-08 07:46:22Z tfmorris $
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

package org.argouml.uml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.argouml.model.Model;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.util.ChildGenerator;

/**
 * Generator to find related elements for some model elements, such as for
 * Classes the attributes and operations, for diagrams nodes and elements, for
 * transitions trigger, guard and effects etc.
 *
 * @stereotype singleton
 * @author jrobbins
 */
public class ChildGenRelated implements ChildGenerator {
    /**
     * The instance.
     */
    private static final ChildGenRelated SINGLETON = new ChildGenRelated();

    /**
     * @return Returns the singleton.
     */
    public static ChildGenRelated getSingleton() {
        return SINGLETON;
    }

    /**
     * Reply a java.util.Enumeration of the children of the given Object Returns
     * an enumeration or null if not possible to get the children.
     *
     * @see org.tigris.gef.util.ChildGenerator#gen(java.lang.Object)
     */
    public Enumeration gen(Object o) {

        // This is carried over from previous implementation
        // not sure why we don't want contents of package - tfm - 20060214
        if (Model.getFacade().isAPackage(o)) {
            return null;
        }

        if (o instanceof Diagram) {
            List res = new ArrayList();
            Diagram d = (Diagram) o;
            res.add(d.getGraphModel().getNodes());
            res.add(d.getGraphModel().getEdges());
            return Collections.enumeration(res);
        }

        // For all other model elements, return any elements
        // associated in any way
        if (Model.getFacade().isAUMLElement(o)) {
            return Collections.enumeration(Model.getFacade()
                    .getModelElementAssociated(o));
        }

        throw new IllegalArgumentException("Unknown element type " + o);
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -893946595629032267L;
} /* end class ChildGenRelated */
