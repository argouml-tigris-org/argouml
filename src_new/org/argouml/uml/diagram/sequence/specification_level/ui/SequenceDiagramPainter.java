/*
 * SequenceDiagramPainter.java
 *
 * Created on 19. Januar 2003, 12:22
 */

package org.argouml.uml.diagram.sequence.specification_level.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Enumeration;

import org.tigris.gef.presentation.ArrowHead;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdgePoly;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigLine;
import org.tigris.gef.presentation.FigPainter;
import org.tigris.gef.presentation.FigPoly;
import org.tigris.gef.presentation.FigText;

/**
 *
 * @author  Administrator
 */
public class SequenceDiagramPainter implements FigPainter {

    /** Creates a new instance of SequenceDiagramPainter */
    public SequenceDiagramPainter() {
    }

    /** Paint Fig f into Graphics g, allowing the
     * figure to be modified according to the current context.
     * This method usually backups and modifies attributes
     * of Fig f, paints the modified figure with <code>f.paint(g)</code>,
     * and restores the original attribute settings.
     * @param g the Graphics used for painting
     * @param f the Figure to be painted
     */
    public void paint(Graphics g, Fig f) {
        if (f instanceof FigSeqClRole || (f.getGroup() instanceof FigSeqClRole && !(f instanceof FigSeqClRolePort))) {
//            System.out.println("Painting (1) fig of type: " + f.getClass().getName());
            f.setLineWidth(1);
            f.setLineColor(Color.black);
            f.setFillColor(Color.white);
        }
        if (f instanceof FigGroup) {
            paintGroup(g, (FigGroup)f);
        } else {
            if (f instanceof FigSeqClRolePort) {
                if (f.getFilled()) {
                    f.setLineWidth(1);
                    f.setLineColor(Color.blue);
                    f.setFillColor(Color.cyan);
                } else {
                    f.setLineWidth(0);
                }
            } else if (f instanceof FigText) {
                f.setLineWidth(0);
            } else {
//                System.out.println("Painting fig of unknown type: " + f.getClass().getName());
                f.setLineWidth(1);
                f.setLineColor(Color.black);
                if (f instanceof FigLine || f instanceof FigPoly || f instanceof FigEdgePoly) {
                    f.setFilled(false);
                    f.setLineColor(Color.black);
                    if (f.getGroup() instanceof FigSeqClRoleLifeline) {
                        if (f instanceof FigPoly) {
//System.out.println(">>> >> > Setting line attribute to dashed");
                            f.setDashed(true);
                        } else if (f instanceof FigLine) {
                            f.setLineColor(new Color(0xC02020));
                        }
                    } else if (f.getGroup() instanceof FigSeqMessage) {
//                        f.setLineColor(Color.red);
                    }
                } else {
                    f.setFilled(true);
                    f.setFillColor(Color.white);
                }
            }
            f.paint(g);
        }
    }

    public void paintGroup(Graphics g, FigGroup fg) {
	Enumeration figs = fg.elements();
	while (figs.hasMoreElements()) {
            Fig f = (Fig)figs.nextElement();
            if (f.isDisplayed()) paint(g, f);
	}
    }

}
