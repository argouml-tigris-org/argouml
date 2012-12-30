/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram.static_structure.layout;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.uml.diagram.layout.LayoutedObject;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.argouml.uml.diagram.ui.FigAbstraction;
import org.argouml.uml.diagram.ui.FigAssociation;
import org.argouml.uml.diagram.ui.FigGeneralization;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.presentation.FigNode;

/** a class to get the proper layouter for a Fig.
 * Currently this deals only with Generalizations and Realizations.
 *
 * @author Markus Klink
 * @stereotype singleton
*/
public class ClassdiagramModelElementFactory
{
    private static final Logger LOG =
        Logger.getLogger(ClassdiagramModelElementFactory.class.getName());

    /**
     * The singleton.
     */
    public static final ClassdiagramModelElementFactory SINGLETON =
	new ClassdiagramModelElementFactory();

    private ClassdiagramModelElementFactory() { }

    /** create layouter object from a Fig.*
     *
     * @param f Object which contains the Fig
     * @return Layouter for the Edge or Classnode or null if none exists.
     */
    public LayoutedObject getInstance(Object f) {
        if (f instanceof FigComment) {
            return (new ClassdiagramNote((FigComment) f));
        } else if (f instanceof FigNodeModelElement) {
            return (new ClassdiagramNode((FigNode) f));
        } else if (f instanceof FigGeneralization) {
            return new ClassdiagramGeneralizationEdge((FigGeneralization) f);
        } else if (f instanceof FigAbstraction) {
            return (new ClassdiagramRealizationEdge((FigAbstraction) f));
        } else if (f instanceof FigAssociation) {
            return (new ClassdiagramAssociationEdge((FigAssociation) f));
        } else if (f instanceof FigEdgeNote) {
            return (new ClassdiagramNoteEdge((FigEdgeNote) f));
        }
        LOG.log(Level.FINE,
                "Do not know how to deal with: {0}\nUsing standard layout",
                f.getClass().getName());
        return null;
    }
}
