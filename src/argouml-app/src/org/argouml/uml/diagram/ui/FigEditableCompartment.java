/* $Id$
 *******************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */
// $Id$
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

package org.argouml.uml.diagram.ui;

import java.awt.Rectangle;

import org.argouml.uml.diagram.DiagramSettings;

/**
 * Presentation logic for a boxed compartment,
 * which is common to e.g. an operations
 * compartment and an attributes compartment.<p>
 * 
 * It adds a separator line at the top of the compartment, 
 * which follows the line width and color of the parent. <p>
 * 
 * This class adds the possibility to 
 * make the whole compartment invisible, and
 * a NotationProvider is used to handle (generate and parse) 
 * the texts shown in the compartment, i.e. 
 * the compartment texts are editable by the user. <p>
 * 
 * This FigGroup shall only contain its bigPort, 
 * and Figs of type FigSeparator, and CompartmentFigText.
 * @deprecated by Bob Tarling in 0.29.3 use {@link FigCompartment}
 */
@Deprecated
public abstract class FigEditableCompartment extends FigCompartment {

    /**
     * Construct a new FigGroup containing a "bigPort" or rectangle which
     * encloses the entire group for use in attaching edges, etc and a
     * separator.
     * <p>
     * NOTE: Subclasses should call populate() when 
     * they are fully constructed.
     * 
     * @param owner owning UML element
     * @param bounds bounding rectangle of fig
     * @param settings render settings
     */
    public FigEditableCompartment(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, bounds, settings); // This adds bigPort, i.e. number 1
    }
}
