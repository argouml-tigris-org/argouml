// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.argouml.application.api.AboutTabPanel;
import org.argouml.application.api.Argo;
import org.argouml.application.api.PluggableAboutTab;
import org.argouml.i18n.Translator;
import org.argouml.util.Tools;

/**
 * This is what you see after you activate the "Help->About ArgoUML" menu-item.
 *
 */
public class AboutBox extends JDialog {

    /**
     * Insets in pixels.
     */
    private static final int INSET_PX = 3;

    ////////////////////////////////////////////////////////////////
    // instance variables

    private JTabbedPane tabs = new JTabbedPane();

    /**
     * Shared splash panel.
     */
    private SplashPanel splashPanel;

    ////////////////////////////////////////////////////////////////
    // constructor
    /**
     * Class constructor.
     */
    public AboutBox() {
	this((Frame) null, false);
    }

    /**
     * Class constructor.
     *
     * @param owner     the frame from which the dialog is displayed
     */
    public AboutBox(Frame owner) {
	this(owner, false);
    }

    /**
     * @see Translator#localize(String)
     *
     * @param str The key to localize.
     * @return The localized String.
     */
    private String localize(String str) {
	return Translator.localize(str);
    }

    /**
     * Creates a JScrollPane from the text.
     *
     * @return          the JScrollPane
     * @param text      the text to represent
     */
    private JScrollPane createPane(String text) {
	JTextArea a = new JTextArea();
	a.setEditable(false);
	a.setLineWrap(true);
	a.setWrapStyleWord(true);
	a.setMargin(new Insets(INSET_PX, INSET_PX, INSET_PX, INSET_PX));
	a.setText(text);
	a.setCaretPosition(0);
	return new JScrollPane(a);
    }

    /**
    * Class constructor.
    *
    * @param owner      the frame from which the dialog is displayed
    * @param modal      true for a modal dialog, false for one that allows
    *                   other windows to be active at the same time
    */
    public AboutBox(Frame owner, boolean modal) {
	super(owner, modal);
        // TODO: i18n
	this.setTitle(localize("aboutbox.aboutbox-title"));
	splashPanel = new SplashPanel("Splash");
	int imgWidth = splashPanel.getImage().getIconWidth();
	int imgHeight = splashPanel.getImage().getIconHeight();
	Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
	setLocation(scrSize.width / 2 - imgWidth / 2,
		    scrSize.height / 2 - imgHeight / 2);
	getContentPane().setLayout(new BorderLayout(0, 0));

	StringBuffer versionBuf = new StringBuffer();
	versionBuf.append(localize("aboutbox.generated-version-header"));
	versionBuf.append(Tools.getVersionInfo());
	versionBuf.append(localize("aboutbox.used-tools-header"));
	// Not localized:
	versionBuf.append("* GEF (gef.tigris.org)\n");
	versionBuf.append("* Xerces-J 2.6.2\n");
	versionBuf.append("* NSUML (nsuml.sourceforge.net)\n");
	versionBuf.append("* TU-Dresden OCL-Compiler "
	        	  + "(dresden-ocl.sourceforge.net)\n");
	versionBuf.append("* ANTLR 2.7.2 (www.antlr.org)\n");
	// Library maintainers! Add and update information here above!

	versionBuf.append("\n\n");

	versionBuf.append(localize("aboutbox.thanks"));
	versionBuf.append("\n");

        /* MVW: Added the inset JPanel, so that the image width is also
        applied to the "ArgoUML Vx.xx.x" part */
        JPanel myInsetPanel = new JPanel();
        /* top, left, bottom, right */
        myInsetPanel.setBorder(new EmptyBorder(30, 40, 40, 40));
        /* This gives some more space to the row of tabs,
           so that there will not be 2 rows of tabs
           See issue 2365, remark 3 from Jeremy.         */
        imgWidth  += 40 + 40;
        /* It looks better if the height increases, too */
        imgHeight += 40 + 40;
        myInsetPanel.add(splashPanel);
        tabs.addTab("Splash", myInsetPanel);

	tabs.addTab("Version", createPane(versionBuf.toString()));

	StringBuffer creditsBuf = new StringBuffer();
	creditsBuf.append(localize("aboutbox.developed-by"));
	creditsBuf.append("\n\n");
	creditsBuf.append(localize("aboutbox.project-leader"));
	creditsBuf.append("Linus Tolke (linus@tigris.org)");
	creditsBuf.append("\n\n");
	creditsBuf.append(localize("aboutbox.module-owners"));
	creditsBuf.append(":\n");

	creditsBuf.append("+ Diagrams, GUI, Persistence: Bob Tarling\n");
	creditsBuf.append("+ Sequence Diagrams: Michael A. MacDonald\n");
	creditsBuf.append("+ C++: Luis Sergio Oliveira\n");
	creditsBuf.append("+ Csharp: Jan Magne Andersen\n");
	creditsBuf.append("+ PHP 4/5: Kai Schroeder\n");
	creditsBuf.append("+ Cognitive support: Markus Klink\n");
	creditsBuf.append("+ User Manual: Michiel van der Wulp (mvw@tigris.org)\n");
	creditsBuf.append("+ Localization French: Jean-Hugues de Raigniac\n");
	creditsBuf.append("+ Localization Russian: Alexey Aphanasyev\n");
	creditsBuf.append("+ Localization German: Harald Braun\n");
	creditsBuf.append("+ Localization Spanish: Stewart Munoz\n");
	creditsBuf.append("+ Localization British English: Alex Bagehot\n");
	creditsBuf.append("+ Localization Norwegian (bokmål): Hans Fredrik Nordhaug\n");
	creditsBuf.append("+ Localization Chinese: Jeff Liu\n\n");

	creditsBuf.append(Translator.messageFormat(
	        "aboutbox.contrib-developers-for-release",
	        new Object[] {
	            "0.20",
		}));
	creditsBuf.append("\n");

	final String cpbi =
	    	", Polytechnic of Bandung Indonesia"
	    	+ ", Computer Engineering Departement";
	// Alphabetic order!
	creditsBuf.append("+ Bob Tarling\n");
	creditsBuf.append("+ Decki" + cpbi + "\n");
	creditsBuf.append("+ Endi" + cpbi + "\n");
	creditsBuf.append("+ Hans Fredrik Nordhaug\n");
	creditsBuf.append("+ Jan Magne Andersen\n");
	creditsBuf.append("+ Jeff Liu\n");
	creditsBuf.append("+ Kai Schroeder\n");
	creditsBuf.append("+ Linus Tolke\n");
	creditsBuf.append("+ Luis Sergio Oliveira\n");
	creditsBuf.append("+ Ludovic Maitre\n");
	creditsBuf.append("+ Michael A. MacDonald\n");
	creditsBuf.append("+ Markus Klink\n");
	creditsBuf.append("+ Michiel van der Wulp\n");
	creditsBuf.append("+ Thomas Neustupny\n");
	creditsBuf.append("+ Tom Morris\n");
	creditsBuf.append("+ Yayan" + cpbi + "\n\n");

	creditsBuf.append(Translator.messageFormat(
	        "aboutbox.contrib-developers-for-release",
	        new Object[] {
	            "0.18",
	        }));

	creditsBuf.append("\n");

	// Alphabetic order!
	creditsBuf.append("+ Harald Braun\n");
	creditsBuf.append("+ Michael Stockman\n");
	creditsBuf.append("+ Jaap Branderhorst\n");
	creditsBuf.append("+ Stewart Munoz\n\n");

	creditsBuf.append(localize("aboutbox.past-developers"));
	creditsBuf.append("\n");

	// Alphabetic order!
	creditsBuf.append("+ Adam Gauthier\n");
	creditsBuf.append("+ Adam Bonner\n");
	creditsBuf.append("+ Alex Bagehot\n");
	creditsBuf.append("+ Alexander Lepekhine\n");
	creditsBuf.append("+ Alexey Aphanasyev\n");
	creditsBuf.append("+ Andreas Rueckert (a_rueckert@gmx.net) (Java RE)\n");
	creditsBuf.append("+ Clemens Eichler\n");
	creditsBuf.append("+ Curt Arnold\n");
	creditsBuf.append("+ David Glaser\n");
	creditsBuf.append("+ David Hilbert\n");
	creditsBuf.append("+ David Redmiles\n");
	creditsBuf.append("+ Dennis Daniels (denny_d@hotmail.com)\n");
	creditsBuf.append("+ Donat Wullschleger\n");
	creditsBuf.append("+ Edwin Park\n");
	creditsBuf.append("+ Eric Lefevre\n");
	creditsBuf.append("+ Eugenio Alvarez\n");
	creditsBuf.append("+ Frank Finger\n");
	creditsBuf.append("+ Frank Wienberg\n");
	creditsBuf.append("+ Grzegorz Prokopski\n");
	creditsBuf.append("+ Jason Robbins (Project founder, researcher)\n");
	creditsBuf.append("+ Jean-Hugues de Raigniac\n");
	creditsBuf.append("+ Jeremy Jones\n");
	creditsBuf.append("+ Jim Holt\n");
	creditsBuf.append("+ Luc Maisonobe\n");
	creditsBuf.append("+ Marcus Andersson\n");
	creditsBuf.append("+ Marko Boger (GentleWare) (UML Diagrams)\n");
	creditsBuf.append("+ Nick Santucci\n");
	creditsBuf.append("+ Phil Sager\n");
	creditsBuf.append("+ Philippe Vanpeperstraete (User Manual)\n");
	creditsBuf.append("+ Piotr Kaminski\n");
	creditsBuf.append("+ Scott Guyer\n");
	creditsBuf.append("+ Sean Chen\n");
	creditsBuf.append("+ Steffen Zschaler\n");
	creditsBuf.append("+ Steve Poole\n");
	creditsBuf.append("+ Stuart Zakon\n");
	creditsBuf.append("+ Thierry Lach\n");
	creditsBuf.append("+ Thomas Schaumburg\n");
	creditsBuf.append("+ Thorsten Sturm (thorsten.sturm@gentleware.de) (GEF)\n");
	creditsBuf.append("+ Toby Baier (UML Metamodel, XMI, Project leader)\n");
	creditsBuf.append("+ Will Howery\n");
	creditsBuf.append("+ ICS 125 team Spring 1996\n");
	creditsBuf.append("+ ICS 125 teams Spring 1998\n");

	tabs.addTab("Credits", createPane(creditsBuf.toString()));

	tabs.addTab("Contact Info",
		     createPane(localize("aboutbox.contacts")));
	tabs.addTab("Report bugs",
		     createPane(localize("aboutbox.bugreport")));
	tabs.addTab("Legal",
		     createPane(localize("aboutbox.legal")));

	// Add the About tabs from the modules.
	ArrayList list = Argo.getPlugins(PluggableAboutTab.class);
	ListIterator iterator = list.listIterator();
	while (iterator.hasNext()) {
	    Object o = iterator.next();
	    AboutTabPanel atp = ((PluggableAboutTab) o).getAboutTabPanel();

	    tabs.addTab(Translator.localize(atp.getTabKey()),
			atp.getTabPanel());
	}

	getContentPane().setLayout(new BorderLayout(0, 0));
	getContentPane().add(tabs, BorderLayout.CENTER);

	// TODO: 10 and 120 were found by trial and error.  Calculate them.
	setSize(imgWidth + 10, imgHeight + 120);
	//pack();
    }

} /* end class AboutBox */
