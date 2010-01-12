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

// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.sequence2.diagram;

import java.awt.Point;

import org.tigris.gef.presentation.FigSpline;

/**
 * FigMessageSpline refines the behaviour of a Spline for a self-message
 * in the sequence diagram.
 * @author penyaskito
 */
public class FigMessageSpline extends FigSpline {
    
    public FigMessageSpline () {
        // this constructor is needed for PGMLStackParser to load
        // saved diagrams. It does nothing, because 
        // PGMLStackParser will call setPoints later.
    }
    
    FigMessageSpline (Point start) {
	assert (start != null) : "Point can't be null";
	
	Point end = new Point(start.x, start.y + 20);

	Point middle = new Point();
	middle.x = start.x + 100;
	middle.y = (start.y + end.y) / 2;

	super.setFilled(false);
	super.setComplete(false);
	super.addPoint(start);
	super.addPoint(new Point(start.x + 20, start.y));
	super.addPoint(middle);
	super.addPoint(new Point(end.x + 20, end.y));
	super.addPoint(end);
    }
    
    /**
     * This method is overridden in order to ignore change of the y coordinate
     * during draging. Use translateFig(int dx, int dy) to force change of the
     * y coordinate.
     * @param dx the x offset
     * @param dy the y offset - IGNORED
     * @see org.tigris.gef.presentation.Fig#translate(int, int)
     */
    public void translate(int dx, int dy) {
    	super.translate(dx, 0);
    }
    
    /**
     * Used in order to force translation of both X and Y coordiante.
     * 
     * @param dx the x offset
     * @param dy the y offset
     * @see org.tigris.gef.presentation.Fig#translate(int, int)
     */
    public void translateFig(int dx, int dy) {
    	super.translate(dx, dy);
    }
    
}
