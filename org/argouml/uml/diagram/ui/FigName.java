package org.argouml.uml.diagram.ui;

import org.tigris.gef.presentation.FigText;

/**
 * A specialist FigText for display the model element name.
 * @author Bob Tarling
 */
public class FigName extends FigText {
    public FigName(int x, int y, int w, int h, boolean expandOnly) {
        super (x, y, w, h, expandOnly);
    }
    
    
	/**
	 * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
	 */
	public void setLineWidth(int arg0) {
		super.setLineWidth(0);
	}
}
