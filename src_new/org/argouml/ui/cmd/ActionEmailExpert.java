// $Id$
// Copyright (c) 2004-2006 The Regents of the University of California. All
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

import java.awt.event.ActionEvent;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.ToDoItem;
import org.argouml.ui.EmailExpertDialog;
import org.argouml.util.osdep.OsUtil;



/**
 * The action to send an email to an expert.
 *
 */
public class ActionEmailExpert extends ToDoItemAction {

    /**
     * The constructor.
     */
    public ActionEmailExpert() {
        super("action.send-email-to-expert", true);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
    	super.actionPerformed(ae);
        if (OsUtil.isWin32()) {
            ToDoItem target = (ToDoItem) getRememberedTarget();
            Poster p = target.getPoster();
            String to = p.getExpertEmail();
            String subject = target.getHeadline().trim();
            /* The replaceAll function is only supported in Java 1.4 and up.
             * Once we stop supporting Java 1.3,
             * we can reintroduce this clean solution!
             * subject = subject.replaceAll("\\s", "%20");
             */
            int i;
            while ((i = subject.indexOf(" ")) >= 0) {
                StringBuffer s = new StringBuffer(subject);
                subject = s.replace(i, i + 1, "%20").toString();
            }

            Designer dsgr = Designer.theDesigner();
            try {
                //MVW: This works under MSWindows only, I guess.
                Runtime.getRuntime().exec(
                    "cmd /c start mailto:" + to
                    + "?subject=" + subject
                    + "&body=" + dsgr);
            } catch (Exception ex) {
                /*ignore for now*/
            }
        } else {
            EmailExpertDialog dialog = new EmailExpertDialog();
            dialog.setTarget(getRememberedTarget());
            dialog.setVisible(true);
        }
    }

    /**
     * @see org.argouml.ui.cmd.ToDoItemAction#isEnabled(java.lang.Object)
     */
    public boolean isEnabled(Object target) {
        return getRememberedTarget() != null
            && getRememberedTarget() instanceof ToDoItem;
    }

} /* end class ActionEmailExpert */

