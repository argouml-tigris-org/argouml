// Copyright (c) 1996-2001 The Regents of the University of California. All
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
import org.argouml.application.api.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.tigris.gef.util.*;

/** This is what you see after you click the About
 * button in the toolbar.
 */
public class AboutBox extends JFrame {

  ////////////////////////////////////////////////////////////////
  // instance variables

  JTabbedPane _tabs = new JTabbedPane();
  JLabel _splashButton = new JLabel("");
  /** Version information. This is calling on
   * a simple test file located in the resources
   * file. One day this will be an xml file.
   */  
  JTextArea _version = new JTextArea();
  /** This is calling on
   * a simple test file located in the resources
   * file. One day this will be an xml file.
   * Get your names in lights! Help make Argo the best
   * choice for UML design and development.
   */  
  JTextArea _credits = new JTextArea();
  /** This is calling on
   * a simple test file located in the resources
   * file. One day this will be an xml file.
   * This one day will allow users to send emails
   * directly from the java window.
   * Also see email expert.
   */  
  JTextArea _contact = new JTextArea();
  /** The small print.
   * This is calling on
   * a simple test file located in the resources
   * file. One day this will be an xml file.
   */  
  JTextArea _legal = new JTextArea();

  ////////////////////////////////////////////////////////////////
  // constructor
  public AboutBox() {
    super("About Argo/UML");
    String iconName = "Splash";
    ImageIcon splashImage = ResourceLoader.lookupIconResource(iconName, iconName);
    int imgWidth = splashImage.getIconWidth();
    int imgHeight = splashImage.getIconHeight();
    Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(scrSize.width/2 - imgWidth/2,
		       scrSize.height/2 - imgHeight/2);
    //setSize(new Dimension(imgWidth + 10, imgHeight + 40));
    getContentPane().setLayout(new BorderLayout(0, 0));

    //_splashButton.setMargin(new Insets(0, 0, 0, 0));
    _splashButton.setIcon(splashImage);


    Font ctrlFont = MetalLookAndFeel.getControlTextFont();
//     _version.setFont(ctrlFont);
//     _credits.setFont(ctrlFont);
//     _legal.setFont(ctrlFont);
//     _contact.setFont(ctrlFont);

    StringBuffer versionBuf = new StringBuffer();
    versionBuf.append("\n--- Generated version information: ---\n");
    versionBuf.append(org.argouml.util.Tools.getVersionInfo());
    versionBuf.append(
		     "\n"+
		     "Intended for use with:\n"+
		     "  JDK 1.2 only plus\n"+
		     "    GEF Graph Editing Framework (gef.tigris.org)\n"+
		     "      including GIF generation code from www.acme.com\n"+
		     "    A JAXP 1.0.1 compatible parser\n" +
                     "       [Xerces-J 1.2.2 or later recommended, (xml.apache.org), it's just great!]\n"+
		     "    Novosoft's NSUML 0.4.19 or higher (nsuml.sourceforge.net)\n"+
		     "    Frank Finger's (TU-Dresden) OCL-Compiler (dresden-ocl.sourceforge.net)\n"+
		     "    ANTLR (www.antlr.org) version 2.7\n"+
		     "\n");

	versionBuf.append("\n");
	versionBuf.append("The ArgoUML developers would like to thank all those broad-minded people\n");
	versionBuf.append("who spend their valuable time in contributing to the projects ArgoUML\n");
	versionBuf.append("depends on! We wouldn't be here without your work!\n");
	versionBuf.append("\n");

    InputStreamReader isr = null;

    _tabs.addTab("Splash", _splashButton);

    try {
        _version.read(new StringReader(versionBuf.toString()), null);
        _tabs.addTab("Version", new JScrollPane(_version));
    }
    catch (Exception e) {
        Argo.log.error("Unable to read version information", e);
    }

    try {
        _credits.read(new InputStreamReader(getClass().getResourceAsStream(Argo.RESOURCEDIR + "credits.about")), null);
        _tabs.addTab("Credits", new JScrollPane(_credits));
    }
    catch (Exception e) {
        Argo.log.error("Unable to read 'credits.about'", e);
    }

    try {
        _contact.read(new InputStreamReader(getClass().getResourceAsStream(Argo.RESOURCEDIR + "contacts.about")), null);
        _tabs.addTab("Contact Info", new JScrollPane(_contact));
    }
    catch (Exception e) {
        Argo.log.error("Unable to read 'contacts.about'", e);
    }


    try {
        _legal.read(new InputStreamReader(getClass().getResourceAsStream(Argo.RESOURCEDIR + "legal.about")), null);
        _tabs.addTab("Legal", new JScrollPane(_legal));
    }
    catch (Exception e) {
        Argo.log.error("Unable to read 'legal.about'", e);
    }

    getContentPane().setLayout(new BorderLayout(0, 0));
    getContentPane().add(_tabs, BorderLayout.CENTER);
    // add preloading progress bar?
    setSize(imgWidth + 20, imgHeight + 50);
    //pack();
  }

} /* end class AboutBox */
