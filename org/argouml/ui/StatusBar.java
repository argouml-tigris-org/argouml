// $Id$
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
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class StatusBar extends JPanel implements Runnable {
    ////////////////////////////////////////////////////////////////
    // instance variables
    protected JLabel _msg = new JLabel();
    protected JProgressBar _progress = new JProgressBar();
    protected String _statusText;
  
    ////////////////////////////////////////////////////////////////
    // constructor
    public StatusBar() {
	_progress.setMinimum(0);
	_progress.setMaximum(100);
	_progress.setMinimumSize(new Dimension(100, 20));
	_progress.setSize(new Dimension(100, 20));

	_msg.setMinimumSize(new Dimension(300, 20));
	_msg.setSize(new Dimension(300, 20));
	_msg.setFont(new Font("Dialog", Font.PLAIN, 10));
	_msg.setForeground(Color.black);

	setLayout(new BorderLayout());
	setBorder(new EtchedBorder(EtchedBorder.LOWERED));
	add(_msg, BorderLayout.CENTER);
	add(_progress, BorderLayout.EAST);
    }

    public void showStatus(String s) {
	_msg.setText(s);
	paintImmediately(getBounds());
    }

    public void showProgress(int percent) {
	_progress.setValue(percent);
    }

    public void incProgress(int delataPercent) {
	_progress.setValue(_progress.getValue() + delataPercent);
    }

    public synchronized void doFakeProgress(String s, int work) {
	_statusText = s;
	showStatus(_statusText + "... not implemented yet ...");
	_progress.setMaximum(work);
	_progress.setValue(0);
	Thread t = new Thread(this);
	t.start();
    }

    public synchronized void run() {
	int work = _progress.getMaximum();
	for (int i = 0; i < work; i++) {
	    _progress.setValue(i);
	    repaint();
	    try { wait(10); }
	    catch (Exception ex) { }
	}
	showStatus(_statusText + "... done.");
	repaint();
	try { wait(1000); }
	catch (Exception ex) { }
	_progress.setValue(0);
	showStatus("");
	repaint();
    }


    //   public boolean isOptimizedDrawingEnabled() {
    //     return false;
    //   }
  
} /* end class StatusBar */
