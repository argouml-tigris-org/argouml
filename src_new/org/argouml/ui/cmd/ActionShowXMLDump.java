// $Id$
// Copyright (c) 2004-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.persistence.PersistenceManager;
import org.argouml.ui.ArgoFrame;
import org.argouml.util.UIUtils;

/**
 * Action that shows an XML dump of the current project contents.
 */
public class ActionShowXMLDump extends AbstractAction {
    
    private static final long serialVersionUID = -7942597779499060380L;

    /**
     * Insets in pixels.
     */
    private static final int INSET_PX = 3;

    /**
     * Constructor.
     */
    public ActionShowXMLDump() {
        super(Translator.localize("action.show-saved"));
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
	Project project = ProjectManager.getManager().getCurrentProject();

	String data =
	    PersistenceManager.getInstance().getQuickViewDump(project);

	JDialog pw = new JDialog(ArgoFrame.getInstance(), 
                Translator.localize("action.show-saved"), 
                false);

	JTextArea a = new JTextArea(data, 50, 80);
	a.setEditable(false);
	a.setLineWrap(true);
	a.setWrapStyleWord(true);
	a.setMargin(new Insets(INSET_PX, INSET_PX, INSET_PX, INSET_PX));
	a.setCaretPosition(0);

	pw.getContentPane().add(new JScrollPane(a));

	pw.setSize(400, 500);

	pw.setLocationRelativeTo(ArgoFrame.getInstance());
        
        init(pw);
        
	pw.setVisible(true);
    }

    private void init(JDialog pw) {
        UIUtils.loadCommonKeyMap(pw);
    }
}

