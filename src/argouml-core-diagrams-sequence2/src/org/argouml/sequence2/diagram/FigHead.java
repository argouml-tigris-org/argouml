/* $Id$
 *******************************************************************************
 * Copyright (c) 2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *    Christian L\u00f3pez Esp\u00ednola
 *******************************************************************************
 *
 * Some portions of this file were previously release using the BSD License:
 */
// $Id$
// Copyright (c) 2007-2009 The Regents of the University of California. All
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

import java.awt.Color;
import java.awt.Dimension;

import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.ArgoFigGroup;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

// TODO: We really want this class to have package scope however the reflection
// used to process PGML.tee forces this to be public. I don't understand why.
// Is there a way around this?
public class FigHead extends ArgoFigGroup  {
    
    private FigText nameFig;
    private Fig stereotypeFig;
    private FigRect rectFig;

    static final int DEFAULT_WIDTH = 150;
    static final int DEFAULT_HEIGHT = 50;
    
    FigHead(Object owner, DiagramSettings settings, Fig stereotypeFigure,
            FigText nameFigure) {
        super(owner, settings);
        initialize(stereotypeFigure, nameFigure);
    }

    private void initialize(Fig stereotypeFigure, FigText nameFigure) {
        this.stereotypeFig = stereotypeFigure;
        this.nameFig = nameFigure;
        
        nameFig.setFilled(false);
        nameFig.setLineWidth(0);
        
        rectFig =
            new FigRect(0, 0,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                LINE_COLOR, FILL_COLOR);
        rectFig.setLineWidth(LINE_WIDTH);
        addFig(rectFig);
        addFig(nameFig);
        addFig(stereotypeFig);
    }

    /**
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param w The width
     * @param h The height
     * @see org.tigris.gef.presentation.FigGroup#setBoundsImpl(int, int, int, int)
     */
    @Override
    protected void setBoundsImpl(int x, int y, int w, int h) {
        rectFig.setBounds(x, y, w, h);
        int yy = y;
        if (stereotypeFig.isVisible()) {
            stereotypeFig.setBounds(x, yy, w,
                    stereotypeFig.getMinimumSize().height);
            yy += stereotypeFig.getMinimumSize().height;
        }

        nameFig.setBounds(x, yy, w, nameFig.getHeight());
        _x = x;
        _y = y;
        _w = w;
        _h = h;
    }

    @Override
    public Dimension getMinimumSize() {
        return new Dimension(getMinimumWidth(), getMinimumHeight());
    }
    
    int getMinimumHeight() {
        int h = stereotypeFig.getMinimumSize().height
        // TODO: Move the magic number 4 to a descriptive constant
            + nameFig.getMinimumHeight() + 4;
        if (h < DEFAULT_HEIGHT) {
            h = DEFAULT_HEIGHT;
        }
        return h;
    }
    
    int getMinimumWidth() {
        int minWidth = 50;
        if (minWidth < stereotypeFig.getMinimumSize().width) {
            minWidth = stereotypeFig.getMinimumSize().width;
        }
        if (minWidth < nameFig.getMinimumSize().width) {
            minWidth = nameFig.getMinimumSize().width;
        }
        return minWidth;
    }
    
    @Override
    public void setFilled (boolean filled) {
        rectFig.setFilled(filled);
    }
    
    @Override
    public void setLineWidth(int w) {
        rectFig.setLineWidth(w);
    }
    
    @Override
    public void setFillColor(Color c) {
        rectFig.setFillColor(c);
    }
    
    @Override
    public Color getFillColor() {
        return rectFig.getFillColor();
    }
    
    @Override
    public void setLineColor(Color c) {
        rectFig.setLineColor(c);
    }
}
