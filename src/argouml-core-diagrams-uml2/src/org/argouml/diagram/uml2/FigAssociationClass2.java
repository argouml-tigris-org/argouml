/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.diagram.uml2;

import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.FigAssociationClass;

/**
 * An Association Class is represented by 3 separate Figs:
 * <nl>
 * <li>FigAssociationClass is the association edge drawn between two
 * classifiers this displays that association properties of the association
 * class.</li>
 * <li>FigClassAssociationClass is the classifier box that displays the class
 * properties of the association class.</li>
 * </li>
 * FigEdgeAssociationClass is the dashed line that joins these two.</li>
 * </nl>
 *
 * Whenever the user attempts to remove or delete one of these parts then all
 * parts must go. Delete would be handled because the model element is deleted
 * and all parts are listening for such an event and will remove themselves.
 * However if the user attempts to just remove from diagram one of these parts
 * then there is no such event. Hence the removeFromDiagram method is overridden
 * to delegate removal from a single removeFromDiagram method on
 * FigAssociationClass.
 */
class FigAssociationClass2 extends FigAssociationClass {

    /**
     * Construct an association class figure for the given AssociationClass
     * model element using the rendering settings.
     * 
     * @param element model element
     * @param settings rendering settings
     */
    public FigAssociationClass2(Object element, DiagramSettings settings) {
        super(element, settings);
    }
}
