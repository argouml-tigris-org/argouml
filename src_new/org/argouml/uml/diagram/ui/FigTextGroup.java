package org.argouml.uml.diagram.ui;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.Vector;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigText;

/**
 * Custom class to group FigTexts in such a way that they don't overlap and that 
 * the group is shrinked to fit (no whitespace in group).
 * 
 * @author jaap.branderhorst@xs4all.nl
 */
public class FigTextGroup extends FigGroup {

	public final static int ROWHEIGHT = 17;
     protected boolean supressCalcBounds = false;

	/**
     * Adds a FigText to the list with figs. Makes sure that the figtexts do not overlap.
	 * @see org.tigris.gef.presentation.FigGroup#addFig(Fig)
	 */
	public void addFig(Fig f) {
		super.addFig(f);
        updateFigTexts();
        calcBounds();
	}
    
	/**
	 * Updates the FigTexts. FigTexts without text (equals "") are not shown. 
     * The rest of the figtexts are shown non-overlapping. The first figtext 
     * added (via addFig) is shown at the bottom of the FigTextGroup.
	 */
    protected void updateFigTexts() {
        Iterator it = getFigs().iterator();
        int height = 0;
        while (it.hasNext()) {
            FigText fig = (FigText)it.next();
            if (fig.getText().equals("")) {
                fig.setHeight(0);
            } else {
                fig.setHeight(ROWHEIGHT);
            }
            fig.startTrans();
            fig.setX(getX());
            fig.setY(getY()+height);
            fig.endTrans();
            height += fig.getHeight();
        }
        // calcBounds();
    }
            

	/**
	 * @see org.tigris.gef.presentation.Fig#calcBounds()
	 */
	public void calcBounds() {
		updateFigTexts();
        if (!supressCalcBounds) {
    		super.calcBounds();
            // get the widest of all textfigs
            // calculate the total height
            int maxWidth = 0;
            int height = 0;
            for (int i = 0;i < getFigs().size();i++) {
                FigText fig = (FigText)getFigs().get(i);
                if (fig.getText().equals("")) {
                    fig.setBounds(fig.getX(), fig.getY(), fig.getWidth(), 0);
                } 
                else {
                    if (fig.getWidth() > maxWidth) {
                        maxWidth = fig.getWidth();
                    }
                    if (!fig.getText().equals("")) {
                        fig.setHeight(ROWHEIGHT);
                    }
                    height += fig.getHeight();
                }
            }        
            _w = maxWidth;
            _h = height;
        }
	}   

}
