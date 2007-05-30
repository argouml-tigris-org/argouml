package org.argouml.uml.profile.javabeans;

import java.util.logging.Logger;

import org.argouml.model.Model;
import org.argouml.uml.profile.FigNodeStrategy;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;

public class JavaBeansFigNodeStrategy implements FigNodeStrategy {

    public Fig[] getExtraFiguresForNode(Object element) {
	Fig ret = null;
//	if (Model.getExtensionMechanismsHelper().hasStereoType(element, "Bean")) {
	    ret = new FigRect(10, 10, 16, 16);
//	}
	    
	return new Fig[] { ret };
    }
    
}
