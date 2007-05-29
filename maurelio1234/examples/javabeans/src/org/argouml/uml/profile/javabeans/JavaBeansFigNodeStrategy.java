package org.argouml.uml.profile.javabeans;

import java.util.Map;

import org.argouml.model.Model;
import org.argouml.uml.profile.FigNodeStrategy;
import org.tigris.gef.presentation.FigNode;

public class JavaBeansFigNodeStrategy implements FigNodeStrategy {

    public FigNode getFigNodelForElement(Object element, int x, int y, Map styleAttributes) {
	FigNode ret = null;
	if (Model.getExtensionMechanismsHelper().hasStereoType(element, "Bean")) {
	    ret = new FigBean(element, x, y);
	}
	return ret;
    }
    
}
