package org.argouml.uml.diagram.ui;

import org.tigris.gef.presentation.FigRect;

/**
 * A FigRect that is always transparent
 * @author Bob Tarling
 */
public class FigEmptyRect extends FigRect {
    
	/**
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	public FigEmptyRect(int x, int y, int w, int h) {
		super(x, y, w, h);
        super.setFilled(false);
	}
    
    public void setFilled(boolean filled) {
        // Do nothing, this rect will always be transparent
    }
}
