package org.argouml.uml.profile.javabeans;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.IOException;

import org.argouml.model.Model;
import org.argouml.uml.profile.FigNodeStrategy;
import org.tigris.gef.presentation.FigImage;

public class JavaBeansFigNodeStrategy implements FigNodeStrategy {

    private FigImage icon = null;
    
    public JavaBeansFigNodeStrategy() {
	BufferedInputStream bis = new BufferedInputStream(this.getClass()
		.getResourceAsStream("bean.GIF"));
	byte[] buf = new byte[10000];
	try {
	    bis.read(buf);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	Image img = Toolkit.getDefaultToolkit().createImage(buf);
	icon = new FigImage(0, 0, img);
    }
    
    public FigImage getIconForStereotype(Object stereotype) {
	FigImage ret = null;
	String s = Model.getFacade().getName(stereotype);
	
	if ("Bean".equals(s)) {
	    ret = icon; 
	}
	    
	return ret;
    }
    
}
