// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
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
import java.awt.Toolkit;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ApplicationVersion;

/**
 * This is what you see after you activate the "Help->Help" menu-item.
 */
public class HelpBox extends JFrame implements HyperlinkListener {

    private static final Logger LOG = Logger.getLogger(HelpBox.class);

    /**
     * A tabbed pane to display several pages simultaneously.
     */
    private JTabbedPane tabs = new JTabbedPane();

    /**
     * The panes to display the various help pages.
     */
    private JEditorPane[] panes = null;

    /**
     * The names and URLs for the pages.
     */
    private String pages[][] = {{"Manual",
            ApplicationVersion.getOnlineManual() ,
            "The ArgoUML online manual"},
        {"Support",
            ApplicationVersion.getOnlineSupport(),
            "The ArgoUML support page" }};


    /**
    * Class constructor.
    *
    * @param title      the title of the help window.
    */
    public HelpBox( String title) {
	super( title);
	Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
	setLocation(scrSize.width / 2 - 400, scrSize.height / 2 - 300);

	getContentPane().setLayout(new BorderLayout(0, 0));
	setSize( 800, 600);

	panes = new JEditorPane [ pages.length];
	for ( int i = 0; i < pages.length; i++) {
            panes[i] = new JEditorPane();
            panes[i].setEditable( false);
            panes[i].setSize( 780, 580);
            panes[i].addHyperlinkListener( this);

	    URL paneURL = null;
            try {
                paneURL = new URL( pages[i][1]);
            } catch ( MalformedURLException e) {
                LOG.warn( pages[i][0] + " URL malformed: " + pages[i][1]);
            }

            if ( paneURL != null) {
                try {
                    panes[i].setPage( paneURL);
                } catch ( IOException e) {
                    LOG.warn("Attempted to read a bad URL: " + paneURL);
                }
            } else {
                LOG.warn("Couldn't find " + pages[i][0]);
            }

            // Put the current pane in a scroll pane.
            JScrollPane paneScrollPane = new JScrollPane( panes[i]);
            paneScrollPane.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            paneScrollPane.setPreferredSize(new Dimension(800, 600));
            paneScrollPane.setMinimumSize(new Dimension(400, 300));

	    tabs.addTab( pages[i][0], null, paneScrollPane, pages[i][2]);
        }
        getContentPane().add( tabs, BorderLayout.CENTER);
    }

    public void hyperlinkUpdate(HyperlinkEvent event) {
	if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	    JEditorPane pane = (JEditorPane) event.getSource();
            try {
                pane.setPage(event.getURL());
            } catch (IOException ioe) {
                LOG.warn( "Could not fetch requested URL");
            }
	}
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 0L;

}