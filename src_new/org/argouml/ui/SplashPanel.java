// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.argouml.application.ArgoVersion;
import org.argouml.application.helpers.ResourceLoaderWrapper;

/**
 * This panel is used in the splash-screen and the aboutbox.
 * It contains an image and a version text.
 * 
 * @author mvw@tigris.org
 */
class SplashPanel extends JPanel {

    private ImageIcon splashImage = null;
    
    /**
     * The constructor.
     * 
     * @param iconName the name of the image to be shown
     */
    public SplashPanel(String iconName) {
	super();
	splashImage =
	    ResourceLoaderWrapper.lookupIconResource(iconName);

	JLabel splashLabel = new JLabel("", SwingConstants.LEFT) {

	    /**
             * The following values were determined experimentally:
             * left margin 10, top margin 18.
             * 
	     * @see javax.swing.JComponent#paint(java.awt.Graphics)
	     */
	    public void paint(Graphics g) {
	        super.paint(g);
	        g.drawString("v" + ArgoVersion.getVersion(), 
	                getInsets().left + 10, getInsets().top + 18);
	    }
	    
        };
        
        if (splashImage != null) {
	    splashLabel.setIcon(splashImage);
	}
	setLayout(new BorderLayout(0, 0));
	add(splashLabel, BorderLayout.CENTER);
    }

    /**
     * @return the image of the splash
     */
    public ImageIcon getImage() {
	return splashImage;
    }

} /* end class SplashPanel */
