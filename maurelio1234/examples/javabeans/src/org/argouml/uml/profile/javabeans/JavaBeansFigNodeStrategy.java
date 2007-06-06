package org.argouml.uml.profile.javabeans;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.IOException;

import org.argouml.model.Model;
import org.argouml.uml.profile.FigNodeStrategy;

public class JavaBeansFigNodeStrategy implements FigNodeStrategy {

    private Image icon = null;
    
    public JavaBeansFigNodeStrategy() {
	BufferedInputStream bis = new BufferedInputStream(this.getClass()
		.getResourceAsStream("bean.GIF"));
	byte[] buf = new byte[10000];
	try {
	    bis.read(buf);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	icon = Toolkit.getDefaultToolkit().createImage(buf);
    }
    
    public Image getIconForStereotype(Object stereotype) {
	if ("Bean".equals(Model.getFacade().getName(stereotype))) {
	    return icon;
	} else {
	    return null;
	}
    }
    
}
