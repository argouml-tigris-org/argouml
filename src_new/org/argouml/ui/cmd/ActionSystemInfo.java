// $Id$
// Copyright (c) 2004-2005 The Regents of the University of California. All
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

package org.argouml.ui.cmd;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import org.tigris.swidgets.ActionUtilities;
import org.argouml.ui.SystemInfoDialog;
import org.argouml.uml.ui.UMLAction;

/**
 * System information dialog.
 */
class ActionSystemInfo extends UMLAction {

    public ActionSystemInfo() {
        super("action.system-information", HAS_ICON);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	JFrame jFrame = (JFrame) ActionUtilities.getActionRoot(ae);
	SystemInfoDialog sysInfoDialog = new SystemInfoDialog(jFrame, true);
	Dimension siDim = sysInfoDialog.getSize();
	Dimension pbDim = jFrame.getSize();

	if (siDim.width > pbDim.width / 2) {
	    sysInfoDialog.setSize(pbDim.width / 2, siDim.height + 45);
	} else {
	    sysInfoDialog.setSize(siDim.width, siDim.height + 45);
	}

	sysInfoDialog.setLocationRelativeTo(jFrame);
	sysInfoDialog.setVisible(true);
    }

} /* end class ActionSystemInfo */

