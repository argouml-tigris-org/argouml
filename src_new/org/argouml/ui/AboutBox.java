// Copyright (c) 1996-99 The Regents of the University of California. All
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

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.tigris.gef.util.*;

public class AboutBox extends JFrame {

  ////////////////////////////////////////////////////////////////
  // instance varaibles

  JTabbedPane _tabs = new JTabbedPane();
  JLabel _splashButton = new JLabel("");
  JTextArea _version = new JTextArea();
  JTextArea _credits = new JTextArea();
  JTextArea _contact = new JTextArea();
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

    _version.setText(versionBuf.toString());

    _credits.setText("ArgoUML was developed by the following:\n"+
		     "Project Lead:\n"+
		     "  Jason Robbins (Collab.net)\n"+
		     "  \n"+
		     "Version 0.9 release managers:\n"+
		     "  Toby Baier (University of Hamburg, Germany)\n"+
		     "  Marko Boger (GentleWare)\n"+
		     "  \n"+
		     "Module Owners (contact these people for contributions):\n"+
		     "  GEF: Edwin Park (esp@parkplace.dhs.org)\n"+
		     "  UML Diagrams: Marko Boger (boger@informatik.uni-hamburg.de)\n"+
		     "  UML Metamodel, XMI: Toby Baier (Toby.Baier@gmx.net)\n"+
		     "  Plugin-support: Sean Chen (schen@bw.webex.net)\n"+
		     "  Java RE: Andreas Rueckert (a_rueckert@gmx.net)\n"+
		     "  Knowledge support: Jason Robbins (jrobbins@collab.net)\n"+
		     "  User manual: Philippe Vanpeperstraete (Philippe.Vanpeperstraete@skynet.be)\n"+
		     "  \n"+
		     "Contributing Developers (in no special order):\n"+
		     "  Jim Holt\n"+
		     "  Thomas Schaumburg\n"+
		     "  David Glaser\n"+
		     "  Toby Baier\n"+
		     "  Eugenio Alvarez\n"+
		     "  Clemens Eichler\n"+
		     "  Curt Arnold\n"+
		     "  Andreas Rueckert\n"+
		     "  Frank Finger\n"+
		     "  Stuart Zakon\n"+
		     "  Frank Wienberg\n"+
		     "  Marcus Andersson\n"+

		     "\n"+
		     "Credits for previous versions:\n"+
		     "\nResearchers:  \n"+
		     "  Jason Robbins\n"+
		     "  David Redmiles\n"+
		     "  David Hilbert\n"+
		     "\nDevelopers and Testers:  \n"+
		     "  Jason Robbins\n"+
		     "  Adam Gauthier\n"+
		     "  Adam Bonner\n"+
		     "  David Hilbert\n"+
		     "  ICS 125 team Spring 1996\n"+
		     "  ICS 125 teams Spring 1998\n"+
		     "\nContributing Developers:\n"+
		     "  Scott Guyer\n"+
		     "  Piotr Kaminski\n"+
		     "  Nick Santucci\n"+
		     "  Eric Lefevre\n"+
		     "  Sean Chen\n" +
		     "  Jim Holt\n" +
		     "  Steve Poole\n"
		     );

    _contact.setText("For more information on the Argo project:\n"+
		     " + Visit our web site:\n"+
		     "   http://www.ArgoUML.org\n"+
		     " + Send email to Jason Robbins at:\n"+
		     "   jrobbins@collab.net\n"+
		     " + Send email to the developers mailing-list at:\n"+
		     "   dev@argouml.tigris.org\n"+
		     "   (subscribe by sending a message to dev-subscribe@argouml.tigris.org\n"+
		     " + Read our conference and journal papers:\n"+
		     "   (list of publications: KBSE'96, IUI'98, ICSE'98, etc.)"
		     );

    String s = "";
    s+="Copyright (c) 1996-99 The Regents of the University of California.\n";
    s+="All Rights Reserved.  Permission to use, copy, modify, and distribute\n";
    s+="this software and its documentation without fee, and without a written\n";
    s+="agreement is hereby granted, provided that the above copyright notice\n";
    s+="and this paragraph appear in all copies.  This software program and\n";
    s+="documentation are copyrighted by The Regents of the University of\n";
    s+="California.  The software program and documentation are supplied ''as\n";
    s+="is'', without any accompanying services from The Regents.  The Regents\n";
    s+="do not warrant that the operation of the program will be uninterrupted\n";
    s+="or error-free.  The end-user understands that the program was\n";
    s+="developed for research purposes and is advised not to rely exclusively\n";
    s+="on the program for any reason.  IN NO EVENT SHALL THE UNIVERSITY OF\n";
    s+="CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT, SPECIAL,\n";
    s+="INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS, ARISING\n";
    s+="OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE\n";
    s+="UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH\n";
    s+="DAMAGE.  THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY\n";
    s+="WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF\n";
    s+="MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE\n";
    s+="PROVIDED HEREUNDER IS ON AN ''AS IS'' BASIS, AND THE UNIVERSITY OF\n";
    s+="CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,\n";
    s+="UPDATES, ENHANCEMENTS, OR MODIFICATIONS.\n";
    _legal.setText(s);

    _tabs.addTab("Splash", _splashButton);
    _tabs.addTab("Version", new JScrollPane(_version));
    _tabs.addTab("Credits", new JScrollPane(_credits));
    _tabs.addTab("Contact Info", new JScrollPane(_contact));
    _tabs.addTab("Legal", new JScrollPane(_legal));

    getContentPane().setLayout(new BorderLayout(0, 0));
    getContentPane().add(_tabs, BorderLayout.CENTER);
    // add preloading progress bar?
    setSize(imgWidth + 20, imgHeight + 50);
    //pack();
  }

} /* end class AboutBox */
