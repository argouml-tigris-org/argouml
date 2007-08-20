// $Id:ActionRESequenceDiagram.java 11516 2006-11-25 04:30:15Z tfmorris $
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.AbstractAction;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.sequence.ui.FigMessage;
import org.argouml.uml.reveng.ui.RESequenceDiagramDialog;

/**
 * Action to reverse engineer a sequence diagram from the operation bodies.
 */
public class ActionRESequenceDiagram extends AbstractAction {

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor. If a figure is given, then it is invoked inside of a
     * sequence diagram, so it will work with this diagram. If figure is null,
     * then this causes the creation of a new sequence diagram.
     *
     * @param fig the figure the action is performed on
     */
    public ActionRESequenceDiagram(Object fig) {
        super(Translator.localize("action.reverse-engineer-sequence-diagram"));
        messageFig = fig;
    }

    /**
     * The constructor. Invoked from the explorer, so no figure is available.
     */
    public ActionRESequenceDiagram() {
        this(null);
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Object obj = TargetManager.getInstance().getTarget();
        if (Model.getFacade().isAOperation(obj)) {
            RESequenceDiagramDialog dialog = new RESequenceDiagramDialog(obj);
            dialog.setVisible(true);
        } else if (Model.getFacade().isAMessage(obj) && messageFig != null) {
            Object ac = Model.getFacade().getAction(obj);
            Object op =
                Model.getFacade().isACallAction(ac)
                ? Model.getFacade().getOperation(ac)
                        : null;
            if (op != null) {
                // it is highly desirable that the message action
                // already knows it's operation
                RESequenceDiagramDialog dialog =
                    new RESequenceDiagramDialog(op, (FigMessage) messageFig);
                dialog.setVisible(true);
            } else {
                // the hard way: try to determine the operation
                // from the message name
                Object receiver = Model.getFacade().getReceiver(obj);
                Collection c =
                    receiver != null
                    ? Model.getFacade().getBases(receiver)
                            : null;
                Object cls =
                    c != null && !c.isEmpty() ? c.iterator().next() : null;
                if (cls != null && Model.getFacade().isAClassifier(cls)) {
                    // too primitive (just gets the first method
                    // with matching name)
                    String opName = Model.getFacade().getName(obj);
                    int pos1 = opName.lastIndexOf(".");
                    int pos2 = opName.lastIndexOf("(");
                    if (pos1 == -1) {
                        pos1 = opName.lastIndexOf("new ");
                        pos1 = pos1 != -1 ? pos1 + 4 : 0;
                    } else {
                        pos1++;
                    }
                    pos2 = pos2 != -1 ? pos2 : opName.length();
                    opName = opName.substring(pos1, pos2);
                    c = Model.getCoreHelper().getOperationsInh(cls);
                    Iterator iter = c != null ? c.iterator() : null;
                    while (iter != null && iter.hasNext()) {
                        op = iter.next();
                        if (opName.equals(Model.getFacade().getName(op))) {
                            RESequenceDiagramDialog dialog =
                                new RESequenceDiagramDialog(op,
                                        (FigMessage) messageFig);
                            dialog.setVisible(true);
                            break;
                        }
                    }
                }
            }
            // Model.getCoreHelper().setName(
            //     ((FigEdgeModelElement) messageFig).getOwner(),
            //     "Hello World");
        }
    }

    /*
     * @see javax.swing.Action#isEnabled()
     */
    public boolean isEnabled() {
        Object target = TargetManager.getInstance().getModelTarget();
        return Model.getFacade().isAOperation(target)
            || Model.getFacade().isAMessage(target);
    }

    private Object messageFig;

    /**
     * The UID.
     */
    private static final long serialVersionUID = 2915509413708666273L;
} /* end class ActionRESequenceDiagram */
