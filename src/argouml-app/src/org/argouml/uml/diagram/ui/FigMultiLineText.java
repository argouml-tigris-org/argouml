/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
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

package org.argouml.uml.diagram.ui;

import java.awt.Rectangle;

import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.FigText;

/**
 * A MultiLine FigText to provide consistency across Figs displaying multiple
 * lines of text.
 * By default -
 * <ul>
 * <li>Text is black
 * <li>The display area is transparent
 * <li>Text is left justified
 * <li>There is no line border
 * </ul>
 *
 * @author Bob Tarling
 */
public class FigMultiLineText extends ArgoFigText {

    private void initFigs() {
        setTextColor(TEXT_COLOR);
        setReturnAction(FigText.INSERT);
        setLineSeparator("\n");
        setTabAction(FigText.END_EDITING);
        setJustification(FigText.JUSTIFY_LEFT);
        setFilled(false);
        setLineWidth(0);
    }
    
    /**
     * Create a multi line text Fig
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings render settings
     * @param expandOnly true if fig should expand, but never contract
     */
    public FigMultiLineText(Object owner, Rectangle bounds,
            DiagramSettings settings, boolean expandOnly) {
        super(owner, bounds, settings, expandOnly);
        initFigs();
    }
}
