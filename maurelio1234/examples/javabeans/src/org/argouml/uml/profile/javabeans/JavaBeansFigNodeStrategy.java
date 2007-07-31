package org.argouml.uml.profile.javabeans;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.uml.profile.FigNodeStrategy;

/**
 * Sample JavaBeans Profile 
 * 
 * @author Marcos Aurélio
 *
 */
public class JavaBeansFigNodeStrategy implements FigNodeStrategy {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(JavaBeansFigNodeStrategy.class);

    private static  final Image iconBean = readImage("bean.GIF", 2904);
    private static final Image iconRay  = readImage("event.gif", 243);


    private static Image readImage(String fileName, int fileSize) {
		BufferedInputStream bis = new BufferedInputStream(
				JavaBeansFigNodeStrategy.class.getResourceAsStream(fileName));

		byte[] buf = new byte[fileSize];
		try {
			bis.read(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOG.info("IMAGE READ: " + fileName);
		return new ImageIcon(buf).getImage();//Toolkit.getDefaultToolkit().createImage(buf);
	}
    
    /**
     * @see org.argouml.uml.profile.FigNodeStrategy#getIconForStereotype(java.lang.Object)
     */
    public Image getIconForStereotype(Object stereotype) {
		if (stereotype != null) {
			try {
				if ("Bean".equals(Model.getFacade().getName(stereotype))) {
					return iconBean;
				} else if ("Event"
						.equals(Model.getFacade().getName(stereotype))) {
					return iconRay;
				}
			} catch (Throwable e) {

			}
		}
		return null;
	}
    
}
