package org.argouml.uml.diagram.ui;

import org.tigris.gef.presentation.FigText;

/**
 * A specialist FigText for display stereotypes.
 * @author Bob Tarling
 */
public class FigStereotype extends FigText {
    public FigStereotype(int x, int y, int w, int h, boolean expandOnly) {
        super (x, y, w, h, expandOnly);
    }
    
    
	/**
	 * @see org.tigris.gef.presentation.Fig#setLineWidth(int)
	 */
	public void setLineWidth(int arg0) {
		super.setLineWidth(0);
	}
    
    public boolean isVisible() {
        if (getText() == null || getText().trim().length() == 0) {
            return false;
        }
        return super.isVisible();
    }
}
