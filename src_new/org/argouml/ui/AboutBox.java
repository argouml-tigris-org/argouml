// $Id$
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

import java.awt.*;
import java.awt.event.*;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.ListIterator;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.tree.*;
import org.argouml.application.api.AboutTabPanel;
import org.argouml.application.api.Argo;
import org.argouml.application.api.PluggableAboutTab;
import org.argouml.util.Tools;

/** This is what you see after you click the About
 * button in the toolbar.
 */
public class AboutBox extends JDialog {

    ////////////////////////////////////////////////////////////////
    // instance variables

    JTabbedPane _tabs = new JTabbedPane();

    /** Shared splash panel */
    SplashPanel _splashPanel = null;

    ////////////////////////////////////////////////////////////////
    // constructor
    public AboutBox() {
	this((Frame) null, false);
    }

    public AboutBox(Frame owner) {
	this(owner, false);
    }

    public AboutBox(Frame owner, boolean modal) {
	super(owner, modal);
	this.setTitle("About Argo/UML");
	_splashPanel = new SplashPanel("Splash");
	int imgWidth = _splashPanel.getImage().getIconWidth();
	int imgHeight = _splashPanel.getImage().getIconHeight();
	Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
	setLocation(scrSize.width / 2 - imgWidth / 2,
		    scrSize.height / 2 - imgHeight / 2);
	getContentPane().setLayout(new BorderLayout(0, 0));

	Font ctrlFont = MetalLookAndFeel.getControlTextFont();

	StringBuffer versionBuf = new StringBuffer();
	versionBuf.append("\n--- Generated version information: ---\n");
	versionBuf.append(Tools.getVersionInfo());
	versionBuf.append(
			  "\n" +
			  "Intended for use with:\n" +
			  "* JDK 1.2 or higher plus\n" +
			  "* GEF Graph Editing Framework (gef.tigris.org)\n" +
			  "  including GIF generation code " +
			     "from www.acme.com\n" +
			  "* A JAXP 1.0.1 compatible parser\n" +
			  "  [Xerces-J 1.2.2 or later recommended, " +
			     "(xml.apache.org), it's just great!]\n" +
			  "* Novosoft's NSUML 0.4.19 or higher " +
			     "(nsuml.sourceforge.net)\n" +
			  "* Frank Finger's (TU-Dresden) OCL-Compiler " +
			     "(dresden-ocl.sourceforge.net)\n" +
			  "* ANTLR (www.antlr.org) version 2.7\n" +
			  "\n");

	versionBuf.append("\n");
	versionBuf.append("The ArgoUML developers would like to thank "
			  + "all those broad-minded people\n");
	versionBuf.append("who spend their valuable time in contributing "
			  + "to the projects ArgoUML\n");
	versionBuf.append("depends on! We wouldn't be here without your "
			  + "work!\n");
	versionBuf.append("\n");

	InputStreamReader isr = null;

	_tabs.addTab("Splash", _splashPanel);

	try {
	    JTextArea a = new JTextArea();
	    a.setEditable(false);
	    a.read(new StringReader(versionBuf.toString()), null);
	    _tabs.addTab("Version", new JScrollPane(a));
	}
	catch (Exception e) {
	    Argo.log.error("Unable to read version information", e);
	}

	try {
	    JTextArea a = new JTextArea();
	    a.read(new InputStreamReader(
		           getClass().getResourceAsStream(Argo.RESOURCEDIR 
							  + "credits.about")),
		   null);
	    a.setEditable(false);
	    _tabs.addTab("Credits", new JScrollPane(a));
	}
	catch (Exception e) {
	    Argo.log.error("Unable to read 'credits.about'", e);
	}

	try {
	    JTextArea a = new JTextArea();
	    a.setEditable(false);
	    a.read(new InputStreamReader(getClass().
			   getResourceAsStream(Argo.RESOURCEDIR
					       + "contacts.about")),
		   null);
	    _tabs.addTab("Contact Info", new JScrollPane(a));
	}
	catch (Exception e) {
	    Argo.log.error("Unable to read 'contacts.about'", e);
	}


	try {
	    JTextArea a = new JTextArea();
	    a.setEditable(false);
	    a.read(new InputStreamReader(getClass().
			  getResourceAsStream(Argo.RESOURCEDIR
					      + "bugreport.about")),
		   null);
	    _tabs.addTab("Report bugs", new JScrollPane(a));
	}
	catch (Exception e) {
	    Argo.log.error("Unable to read 'bugreport.about'", e);
	}


	try {
	    JTextArea a = new JTextArea();
	    a.setEditable(false);
	    a.read(new InputStreamReader(getClass().
					 getResourceAsStream(Argo.RESOURCEDIR
							     + "legal.about")),
		   null);
	    _tabs.addTab("Legal", new JScrollPane(a));
	}
	catch (Exception e) {
	    Argo.log.error("Unable to read 'legal.about'", e);
	}

	// Add the about tabs from the modules.
	ArrayList list = Argo.getPlugins( PluggableAboutTab.class);
	ListIterator iterator = list.listIterator();
	while (iterator.hasNext()) {
	    Object o = iterator.next();
	    AboutTabPanel atp = ((PluggableAboutTab) o).getAboutTabPanel();
	
	    _tabs.addTab(Argo.localize(atp.getTabResourceBundleKey(),
				       atp.getTabKey()),
			 atp.getTabPanel());
	}

	getContentPane().setLayout(new BorderLayout(0, 0));
	getContentPane().add(_tabs, BorderLayout.CENTER);

	// TODO: 10 and 120 were found by trial and error.  Calculate them.
	setSize(imgWidth + 10, imgHeight + 120);
	//pack();
    }

} /* end class AboutBox */
