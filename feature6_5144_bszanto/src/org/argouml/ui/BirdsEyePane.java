// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JPanel;

import org.argouml.kernel.ProjectManager;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.presentation.FigEdge;

/**
 * This class represents the birds eye view panel. 
 * @author bszanto
 */
public class BirdsEyePane extends JPanel {
    
    private double scale = 0.2;
    
    private JPanel editor;
    
    /**
     * Constructor.
     * @param editorPane that the Bird's Eye represents.
     */
    public BirdsEyePane(JPanel editorPane) {
        editor = editorPane;
    }
    
    public Dimension getPreferredSize() {
        return new Dimension((int) (editor.getWidth() * scale), 
                (int) (editor.getHeight() * scale));
    }
    
    @Override
    public void paint(Graphics g) {
        
        if ((double) getWidth() / editor.getWidth() 
                <= (double) getHeight() / editor.getHeight()) {
            scale = (double) getWidth() / editor.getWidth();
        } else {
            scale = (double) getHeight() / editor.getHeight();
        }
        
        Editor ce = Globals.curEditor();
        Rectangle drawingArea =
            ce.getLayerManager().getActiveLayer().calcDrawingArea();

        if (drawingArea.height > 0 && drawingArea.width > 0) {
            boolean h = ce.getGridHidden();
            ce.setGridHidden(true);
            
            List<FigEdge> l = ProjectManager.getManager().getCurrentProject()
                    .getActiveDiagram().getEdges();
            int minX = drawingArea.width;
            int minY = drawingArea.height;
            for (FigEdge object : l) {
                if (minX > object.getX()) {
                    minX = object.getX();
                }
                if (minY > object.getY()) {
                    minY = object.getY();
                }
            }
            
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, (int) ((drawingArea.width + minX) * scale),
                    (int) ((drawingArea.height + minY) * scale));
            
            Image i = new BufferedImage((int) (drawingArea.width * scale) + 1,
                    (int) (drawingArea.height * scale) + 1, 
                    BufferedImage.TYPE_INT_ARGB);
            
            Graphics2D g2D = (Graphics2D) i.getGraphics();
            g2D.scale(scale, scale);
            g2D.setColor(new Color(0x00efefef, true));
            Composite c = g2D.getComposite();
            g2D.setComposite(AlphaComposite.Src);
            g2D.fillRect(0, 0, (int) (drawingArea.width * scale) + 1, 
                    (int) (drawingArea.height * scale) + 1);
            g2D.setComposite(c);
            g2D.translate(-drawingArea.x, -drawingArea.y);
            ce.print(g2D);
            
            g.drawImage(i, (int) (minX * scale), (int) (minY * scale), this);
            ce.setGridHidden(h);
        }
        
        g.setColor(Color.RED);
        g.drawRect(0, 0, (int) (editor.getWidth() * scale) - 2, 
                (int) (editor.getHeight() * scale) - 2);
    }

}
