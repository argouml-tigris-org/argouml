// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import org.argouml.application.ArgoVersion;
import org.argouml.application.helpers.ResourceLoaderWrapper;

class SplashPanel extends JPanel {

    ImageIcon splashImage = null;
    public SplashPanel(String iconName) {
	super();
	splashImage = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource(iconName);

	JPanel topNorth = new JPanel(new BorderLayout());
	topNorth.setPreferredSize(new Dimension(6, 6));
	topNorth.setBorder(new BevelBorder(BevelBorder.RAISED));
	topNorth.add(new JLabel(""), BorderLayout.CENTER);

	JPanel topSouth = new JPanel(new BorderLayout());
	topSouth.setPreferredSize(new Dimension(6, 6));
	topSouth.setBorder(new BevelBorder(BevelBorder.RAISED));
	topSouth.add(new JLabel(""), BorderLayout.CENTER);

	JLabel topCenter = new JLabel("ArgoUML v" + ArgoVersion.getVersion(),
				      SwingConstants.CENTER);
	// 40 works for 0.10
	topCenter.setFont(new Font("SansSerif", Font.BOLD, 35));
	topCenter.setPreferredSize(new Dimension(60, 60));
	topCenter.setOpaque(false);
	topCenter.setForeground(Color.white);

	JPanel top = new JPanel(new BorderLayout());
	top.setBackground(Color.darkGray);
	top.add(topNorth, BorderLayout.NORTH);
	top.add(topCenter, BorderLayout.CENTER);
	top.add(topSouth, BorderLayout.SOUTH);

	JLabel splashButton = new JLabel("");
	if (splashImage != null) {
	    // int imgWidth = splashImage.getIconWidth();
	    // int imgHeight = splashImage.getIconHeight();
	    // Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
	    // setLocation(scrSize.width/2 - imgWidth/2,
	    // scrSize.height/2 - imgHeight/2);
	    splashButton.setIcon(splashImage);
	}
	// setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	setLayout(new BorderLayout(0, 0));
	//splashButton.setMargin(new Insets(0, 0, 0, 0));
	// JPanel main = new JPanel(new BorderLayout());
	// setBorder(new EtchedBorder(EtchedBorder.RAISED));
	add(top, BorderLayout.NORTH);
	add(splashButton, BorderLayout.CENTER);
	// add(_statusBar, BorderLayout.SOUTH);
    }

    public ImageIcon getImage() {
	return splashImage;
    }

} /* end class SplashPanel */
