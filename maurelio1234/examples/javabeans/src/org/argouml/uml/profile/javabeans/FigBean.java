package org.argouml.uml.profile.javabeans;

import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.tigris.gef.presentation.FigNode;

public class FigBean extends FigClass {
    
    public FigBean(Object node, int x, int y) {
	super(node, x, y, 10, 10);
	this.getNameFig().append(" [bean]");
    }

}
