// $Id: ModeChangeHeight.java 11516 2006-11-25 04:30:15Z tfmorris $
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

package org.argouml.uml.diagram.sequence.ui;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.event.MouseEvent;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.FigModifyingModeImpl;
import org.tigris.gef.base.Globals;

import org.argouml.i18n.Translator;

public class ModeExpand extends FigModifyingModeImpl {

    private int startX, startY, currentY;
    private Editor editor;
    private Color rubberbandColor;

    /**
     * The constructor.
     *
     */
    public ModeExpand() {
        editor = Globals.curEditor();
        rubberbandColor = Globals.getPrefs().getRubberbandColor();
    }

    /*
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent me) {
        if (me.isConsumed()) {
            return;
        }

        startY = me.getY();
        startX = me.getX();
        start();
        me.consume();
    }

    /*
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent me) {
        if (me.isConsumed()) {
            return;
        }

        currentY = me.getY();
        editor.damageAll();
        me.consume();
    }

    /*
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent me) {
        if (me.isConsumed()) {
            return;
        }

        SequenceDiagramLayer layer =
            (SequenceDiagramLayer) Globals.curEditor().getLayerManager()
                .getActiveLayer();
        int endY = me.getY();
        int startOffset = layer.getNodeIndex(startY);
        if (startOffset > 0 && endY < startY) {
            startOffset--;
        }
        int diff = layer.getNodeIndex(endY) - startOffset;
        if (diff < 0) {
            diff = -diff;
        }
        if (diff > 0) {
            layer.expandDiagram(startOffset, diff);
        }

        me.consume();
        done();
    }

    /*
     * @see org.tigris.gef.base.FigModifyingMode#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        g.setColor(rubberbandColor);
        g.drawLine(startX, startY, startX, currentY);
    }

    /*
     * @see org.tigris.gef.base.FigModifyingMode#instructions()
     */
    public String instructions() {
        return Translator.localize("action.sequence-expand");
    }
}
