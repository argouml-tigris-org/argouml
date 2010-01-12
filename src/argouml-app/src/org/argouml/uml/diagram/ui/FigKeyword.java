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

// Copyright (c) 2008-2009 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.awt.Rectangle;

import org.argouml.notation.providers.uml.NotationUtilityUml;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.FigText;

/**
 * Fig to show a keyword within a FigStereotypesGroup,
 * which resembles a stereotype, but isn't one. 
 * E.g. <<interface>>. 
 * <p>
 * The keyword is not editable on the fig, hence we
 * do not use a Notation Provider. <p>
 * This Fig does not need to listen to model changes 
 * and is frozen.<p>
 * 
 * This Fig only supports updates of the text 
 * for changes in guillemet style.
 *
 * @author Michiel
 */
public class FigKeyword extends FigSingleLineText {

    private final String keywordText;

    /**
     * @param keyword the text to show
     * @param bounds position and size
     * @param settings rendering settings
     */
    public FigKeyword(String keyword, Rectangle bounds, 
            DiagramSettings settings) {
        super(bounds, settings, true);
        initialize();
        keywordText = keyword;
        setText(keyword);
    }
    
    private void initialize() {
        setEditable(false);
        setTextColor(TEXT_COLOR);
        setTextFilled(false);
        setJustification(FigText.JUSTIFY_CENTER);
        setRightMargin(3);
        setLeftMargin(3);
        super.setLineWidth(0);
    }
    
    /* Force the line-width to 0, since the FigGroup that contains the 
     * stereotype may want to show a border, but we don't. */
    @Override
    public void setLineWidth(int w) {
        super.setLineWidth(0);
    }

    /**
     * This is needed for updating the guillemet style.
     */
    @Override
    protected void setText() {
        setText(keywordText);
    }

    /**
     * Add guillemets to any text set to this Fig.
     * {@inheritDoc}
     */
    @Override
    public void setText(String text) {
        assert keywordText.equals(text);
        super.setText(NotationUtilityUml.formatStereotype(text,
                getSettings().getNotationSettings().isUseGuillemets()));
    }
}
