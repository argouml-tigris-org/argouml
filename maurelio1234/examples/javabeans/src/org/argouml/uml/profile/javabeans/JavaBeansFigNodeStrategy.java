package org.argouml.uml.profile.javabeans;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.IOException;

import org.argouml.model.Model;
import org.argouml.uml.profile.FigNodeStrategy;

/**
 * Sample JavaBeans Profile 
 * 
 * @author Marcos Aurélio
 *
 */
public class JavaBeansFigNodeStrategy implements FigNodeStrategy {

    private static  final Image iconBean = readImage("bean.GIF", 2904);
    private static final Image iconRay  = readImage("event.gif", 243);
       
    private static Image readImage(String fileName, int fileSize) {
	BufferedInputStream bis = new BufferedInputStream(JavaBeansFigNodeStrategy.class
		.getResourceAsStream(fileName));
	
	byte[] buf = new byte[fileSize];
	try {
	    bis.read(buf);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return Toolkit.getDefaultToolkit().createImage(buf);
    }
    
    /**
     * @see org.argouml.uml.profile.FigNodeStrategy#getIconForStereotype(java.lang.Object)
     */
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
