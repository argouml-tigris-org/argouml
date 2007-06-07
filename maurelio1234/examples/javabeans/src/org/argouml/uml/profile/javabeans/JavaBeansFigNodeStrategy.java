package org.argouml.uml.profile.javabeans;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.IOException;

import org.argouml.model.Model;
import org.argouml.uml.profile.FigNodeStrategy;

public class JavaBeansFigNodeStrategy implements FigNodeStrategy {

    private Image iconBean = null;
    private Image iconRay  = null;
    
    public JavaBeansFigNodeStrategy() {
	iconBean = readImage("bean.GIF");
	iconRay  = readImage("event.gif");
    }
   
    private Image readImage(String fileName) {
	BufferedInputStream bis = new BufferedInputStream(this.getClass()
		.getResourceAsStream(fileName));
	
	byte[] buf = new byte[10000];
	try {
	    bis.read(buf);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return Toolkit.getDefaultToolkit().createImage(buf);
    }
    
    public Image getIconForStereotype(Object stereotype) {
	if ("Bean".equals(Model.getFacade().getName(stereotype))) {
	    return iconBean;
	} else 	if ("Event".equals(Model.getFacade().getName(stereotype))) {
	    return iconRay;
	} else {
	    return null;
	}
    }
    
}
