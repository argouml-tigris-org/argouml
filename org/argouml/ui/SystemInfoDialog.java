// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

/*
 * SystemInfoDialog.java
 */

package org.argouml.ui;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

/**
 * Display System Information (JDK Version, JDK Vendor, etc).
 * A Copy to System Clipboard button is provided to help generate bug reports.
 *
 * @author Eugenio Alvarez
 */
public class SystemInfoDialog extends JDialog {

    ////////////////////////////////////////////////////////////////
    // instance varaibles

    JTabbedPane _tabs = new JTabbedPane();
    JTextArea   _info = new JTextArea();
    JButton     _runGCButton = new JButton("Run GC");
    JButton     _copyButton = new JButton("Copy Information to System Clipboard");
    JButton     _cancelButton = new JButton("Cancel");
    JPanel      _buttons = new JPanel();

    ////////////////////////////////////////////////////////////////
    // constructors

    public SystemInfoDialog() {
	this((Frame) null, false);
    }

    public SystemInfoDialog(Frame owner) {
	this(owner, false);
    }

    public SystemInfoDialog(Frame owner, boolean modal) {
	super(owner, modal);
	this.setTitle("System Information");
	this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

	_info.setEditable(false);

	_tabs.addTab("System Information", new JScrollPane(_info));

	_runGCButton.addActionListener(new java.awt.event.ActionListener() 
	    {
		public void actionPerformed(ActionEvent e) {
		    runGC_actionPerformed(e);
		}
	    });
	_copyButton.addActionListener(new java.awt.event.ActionListener() 
	    {
		public void actionPerformed(ActionEvent e) {
		    copy_actionPerformed(e);
		}
	    });
	_cancelButton.addActionListener(new java.awt.event.ActionListener() 
	    {
		public void actionPerformed(ActionEvent e) {
		    cancel_actionPerformed(e);
		}
	    });
	getContentPane().setLayout(new BorderLayout(0, 0));
	getContentPane().add(_tabs, BorderLayout.CENTER);
	_buttons.add(_runGCButton);
	_buttons.add(_copyButton);
	_buttons.add(_cancelButton);
	getContentPane().add(_buttons, BorderLayout.SOUTH);
	updateInfo();
	addWindowListener(new WindowAdapter() 
	    {
		public void windowActivated(WindowEvent e) {
		    updateInfo();
		} // end windowActivated()
	    });
	pack();
    } // end SystemInfoDialog()

    void runGC_actionPerformed(ActionEvent e) {
	Runtime.getRuntime().gc();
	updateInfo();
    } // end runGC_actionPerformed()

    void copy_actionPerformed(ActionEvent e) {
	String info = _info.getText();
	StringSelection contents = new StringSelection(info);
	Clipboard clipboard = getToolkit().getSystemClipboard();
	clipboard.setContents(contents, defaultClipboardOwner);
    } // end copy_actionPerformed()

    void cancel_actionPerformed(ActionEvent e) {
	this.setVisible(false);
    } // end cancel_actionPerformed()

    void updateInfo() {
	StringBuffer info = new StringBuffer();
	info.append("Java Version		: ");
	info.append(System.getProperty("java.version", "") + "\n" );
	info.append("Java Vendor		: ");
	info.append(System.getProperty("java.vendor", "") + "\n" );
	info.append("Java Vendor URL	: ");
	info.append(System.getProperty("java.vendor.url", "") + "\n" );
	info.append("Java Home Directory	: ");
	info.append(System.getProperty("java.home", "") + "\n" );
	info.append("Java Classpath		: ");
	info.append(System.getProperty("java.class.path", "") + "\n" );
	info.append("Operation System	: ");
	info.append(System.getProperty("os.name", ""));
	info.append(", Version ");
	info.append(System.getProperty("os.version", "") + "\n" );
	info.append("Architecture		: ");
	info.append(System.getProperty("os.arch", "") + "\n" );
	info.append("User Name		: ");
	info.append(System.getProperty("user.name", "") + "\n" );
	info.append("User Home Directory	: ");
	info.append(System.getProperty("user.home", "") + "\n" );
	info.append("Current Directory	: ");
	info.append(System.getProperty("user.dir", "") + "\n" );
	info.append("JVM Total Memory	: ");
	info.append(String.valueOf(Runtime.getRuntime().totalMemory()) + "\n" );
	info.append("JVM Free Memory	: ");
	info.append(String.valueOf(Runtime.getRuntime().freeMemory()) + "\n" );
    
	_info.setText(info.toString());
    } //end updateInfo()

    private static ClipboardOwner defaultClipboardOwner = new ClipboardObserver();

    static class ClipboardObserver implements ClipboardOwner {
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
	}
    }

} /* end class SystemInfoDialog */
