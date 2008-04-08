// $Id$
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

package org.argouml.uml.diagram.sequence2.ui;

import java.awt.Color;
import java.awt.Dimension;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigRect;
import org.tigris.gef.presentation.FigText;

class FigHead extends FigGroup {
    
    private final FigText nameFig;
    private final Fig stereotypeFig;
    private final FigRect rectFig;

    static final int DEFAULT_WIDTH = 150;
    static final int DEFAULT_HEIGHT = 50;
    
    /**
     * Constructor.
     *
     * @param stereotypeFigure
     * @param nameFigure
     */
    FigHead(Fig stereotypeFigure, FigText nameFigure) {
        this.stereotypeFig = stereotypeFigure;
        this.nameFig = nameFigure;
        rectFig =
            new FigRect(0, 0,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                Color.black, Color.white);
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
        nameFig.setFilled(false);
        nameFig.setLineWidth(0);
        nameFig.setTextColor(Color.black);
        nameFig.setBounds(x, yy, w, nameFig.getHeight());
        _x = x;
        _y = y;
        _w = w;
        _h = h;
    }

    @Override
    public Dimension getMinimumSize() {
        Dimension d = null;
        int minWidth = stereotypeFig.getMinimumSize().width;
        if (minWidth < nameFig.getWidth()) {
            minWidth = nameFig.getWidth();
        }
        d = new Dimension(minWidth, getMinimumHeight());
        return d;
    }
    
    int getMinimumHeight() {
        int h = stereotypeFig.getMinimumSize().height
            + nameFig.getMinimumHeight() + 4;
        if (h < DEFAULT_HEIGHT)
            h = DEFAULT_HEIGHT;
        return h;
    }
    
    @Override
    public void setFilled (boolean filled) {
        rectFig.setFilled(filled);
    }
    
    @Override
    public void setLineWidth(int w) {
        rectFig.setLineWidth(w);
    }
}
