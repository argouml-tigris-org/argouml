// $Id$
// Copyright (c) 2004 The Regents of the University of California. All
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ArgoDialog;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.TabResults;
import org.argouml.uml.ui.UMLAction;

////////////////////////////////////////////////////////////////
// items on view menu

class ActionGotoDiagram extends UMLAction {

    public ActionGotoDiagram() { super("action.goto-diagram", NO_ICON); }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	ProjectBrowser pb = ProjectBrowser.getInstance();
	Project p = ProjectManager.getManager().getCurrentProject();

	final TabResults allDiagrams = new TabResults(false); // no related
	allDiagrams.setResults(p.getDiagrams(), p.getDiagrams());

	// TabResults has really large preferred height, so divide in
	// half to reduce size of dialog which will be sized based on
	// this preferred size.
	allDiagrams.
	    setPreferredSize(new Dimension(
	            allDiagrams.getPreferredSize().width,
		    allDiagrams.getPreferredSize().height / 2));
	allDiagrams.selectResult(0);

	//TODO: class TearOffHostFrame and TearOffManager.
	//idea: pop-up on tab that lists docking locations, new window.
	ArgoDialog f =
	    new ArgoDialog(pb, "Goto Diagram...",
			   ArgoDialog.OK_CANCEL_OPTION, false) {
		public void actionPerformed(ActionEvent e) {
		    if (e.getSource() == getOkButton()) {
			allDiagrams.doDoubleClick();
		    }
		    else {
			super.actionPerformed(e);
		    }
		}

		protected void nameButtons() {
		    super.nameButtons();
		    nameButton(getOkButton(), "button.go-to-selection");
		    nameButton(getCancelButton(), "button.close");
		}
	    };
	JPanel mainPanel = new JPanel(new BorderLayout());
	//JTabbedPane tabs = new JTabbedPane();
	//mainPanel.add(tabs, BorderLayout.CENTER);
	//tabs.addTab("All Diagrams", allDiagrams);
	mainPanel.add(allDiagrams, BorderLayout.CENTER);
	f.setContent(mainPanel);
	//TODO: tabs for class, state, usecase, help
	f.setVisible(true);
    }
} /* end class ActionGotoDiagram */

