// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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
import java.util.ArrayList;
import java.util.ListIterator;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JPanel; 
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;
import org.argouml.application.api.AboutTabPanel;
import org.argouml.application.api.Argo;
import org.argouml.application.api.PluggableAboutTab;
import org.argouml.i18n.Translator;
import org.argouml.util.Tools;

/** This is what you see after you click the About
 * button in the toolbar.
 */
public class AboutBox extends JDialog {

    /** logger */
    private static Logger _Log = Logger.getLogger(AboutBox.class);

    ////////////////////////////////////////////////////////////////
    // instance variables

    JTabbedPane _tabs = new JTabbedPane();
    int i = 3; // insets in pixels

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

    private final String localize(String str) {
	return Translator.localize(str);
    }

    /** Create a JScrollPane from the text
     *
     * @returns the JScrollPane
     * @param the text to represent
     */
    private JScrollPane createPane(String text) {
	JTextArea a = new JTextArea();
	a.setEditable(false);
	a.setLineWrap(true);
	a.setWrapStyleWord(true);
	a.setMargin(new Insets(i, i, i, i));
	a.setText(text);
	a.setCaretPosition(0);
	return new JScrollPane(a);
    }


    public AboutBox(Frame owner, boolean modal) {
	super(owner, modal);
        // TODO: i18n
	this.setTitle(localize("aboutbox.aboutbox-title"));
	_splashPanel = new SplashPanel("Splash");
	int imgWidth = _splashPanel.getImage().getIconWidth();
	int imgHeight = _splashPanel.getImage().getIconHeight();
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
	versionBuf.append("* Xerces-J 1.2.3\n");
	versionBuf.append("* NSUML 0.4.19 (nsuml.sourceforge.net)\n");
	versionBuf.append("* TU-Dresden OCL-Compiler " +
			  "(dresden-ocl.sourceforge.net)\n");
	versionBuf.append("* ANTLR 2.7.2 (www.antlr.org)\n");
	// Library maintainers! Add and update information here above!

	versionBuf.append("\n\n");

	versionBuf.append(localize("aboutbox.thanks"));
	versionBuf.append("\n");

        /** MVW: Added the inset JPanel, so that the image width is also
        applied to the "ArgoUML Vx,xx.x" part */
        JPanel myInsetPanel = new JPanel(); 
        /* top, left, bottom, right */
        myInsetPanel.setBorder(new EmptyBorder(30, 40, 40, 40));
        /* This gives some more space to the row of tabs, 
           so that there will not be 2 rows of tabs
           See issue 2365, remark 3 from Jeremy.         */
        imgWidth  += 40 + 40; 
        /* It looks better if the height increases, too */
        imgHeight += 40 + 40; 
        myInsetPanel.add(_splashPanel); 
        _tabs.addTab("Splash", myInsetPanel); 
	//_tabs.addTab("Splash", _splashPanel);

	_tabs.addTab("Version", createPane(versionBuf.toString()));
	_tabs.addTab("Credits", 
		     createPane(localize("aboutbox.credits")));
	_tabs.addTab("Contact Info", 
		     createPane(localize("aboutbox.contacts")));
	_tabs.addTab("Report bugs", 
		     createPane(localize("aboutbox.bugreport")));
	_tabs.addTab("Legal", 
		     createPane(localize("aboutbox.legal")));

	// Add the about tabs from the modules.
	ArrayList list = Argo.getPlugins( PluggableAboutTab.class);
	ListIterator iterator = list.listIterator();
	while (iterator.hasNext()) {
	    Object o = iterator.next();
	    AboutTabPanel atp = ((PluggableAboutTab) o).getAboutTabPanel();
	
	    _tabs.addTab(Translator.localize(atp.getTabResourceBundleKey(),
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
