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

import java.awt.Rectangle;

import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.static_structure.ui.FigPackage;

/**
 * Class to display graphics for a UML package in a class diagram,
 * consisting of a "tab" and a "body". <p>
 * 
 * The tab of the Package Fig is build of 2 pieces: 
 * the stereotypes at the top, and the name below it. <p>
 * 
 * The name box covers the whole tab, i.e. its size
 * is always equal to the total size of the tab. 
 * It is not transparent, and has a line border. 
 * Its text sits at the bottom of the fig, to leave room for stereotypes. <p>
 * 
 * The stereotype fig is transparent, and sits at the top
 * inside the name fig. It is drawn on top of the name fig box. <p>
 * 
 * The tab of the Package Fig can only be resized by the user horizontally.
 * The body can be resized horizontally and vertically by the user. <p>
 * 
 * Double clicking on the body has a special consequence: 
 * the user is asked if he wants to create a new class diagram
 * for this package. <p>
 * 
 * ArgoUML does not support the option of showing the name 
 * of the package in the body, 
 * as described in the UML standard (chapter Notation - Package). <p>
 * 
 * Neither does ArgoUML currently support showing properties in the tab, 
 * see issue 1214. <p>
 * 
 * In front of the name, ArgoUML may optionally show the visibility.
 */
class FigPackage2 extends FigPackage {

    /**
     * Construct a package figure with the given owner, bounds, and rendering
     * settings. This constructor is used by the PGML parser.
     * 
     * @param owner owning model element
     * @param bounds position and size or null if fig hasn't been placed
     * @param settings rendering settings
     */
    public FigPackage2(Object owner, Rectangle bounds, 
            DiagramSettings settings) {
        super(owner, bounds, settings);
    }
}
