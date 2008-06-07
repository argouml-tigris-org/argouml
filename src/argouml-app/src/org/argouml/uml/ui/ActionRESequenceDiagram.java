// $Id$
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
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.sequence.SequenceDiagramGraphModel;
import org.argouml.uml.diagram.sequence.ui.FigMessage;
import org.argouml.uml.reveng.ui.RESequenceDiagramDialog;
import org.tigris.gef.base.Globals;

/**
 * Action to reverse engineer a sequence diagram from the operation bodies.
 */
public class ActionRESequenceDiagram extends AbstractAction {

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

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        final Object target = TargetManager.getInstance().getTarget();
        if (Model.getFacade().isAOperation(target)) {
            RESequenceDiagramDialog dialog =
                new RESequenceDiagramDialog(target);
            dialog.setVisible(true);
        } else if (Model.getFacade().isAMessage(target) && messageFig != null) {
            final Object action = Model.getFacade().getAction(target);
            Object operation =
                Model.getFacade().isACallAction(action)
                ? Model.getFacade().getOperation(action)
                        : null;
            final SequenceDiagramGraphModel sequenceDiagramGraphModel = 
                (SequenceDiagramGraphModel) Globals.curEditor().getGraphModel();
            final Project project =
                ProjectManager.getManager().getCurrentProject();
            ArgoDiagram diagram = null;
            Iterator<ArgoDiagram> iter = project.getDiagramList().iterator();
            while (iter.hasNext()) {
                diagram = iter.next();
                if (sequenceDiagramGraphModel == diagram.getGraphModel()) {
                    break;
                }
            }
            if (operation != null) {
                // it is highly desirable that the message action
                // already knows it's operation
                // TODO: There is a cyclic dependency between 
                // ActionRESequenceDiagram and FigMessage
                RESequenceDiagramDialog dialog = new RESequenceDiagramDialog(
                        operation, 
                        (FigMessage) messageFig, 
                        diagram);
                dialog.setVisible(true);
            } else {
                // the hard way: try to determine the operation
                // from the message name
                Object receiver = Model.getFacade().getReceiver(target);
                // TODO: Do we really need to test for null here? I would
                // expect an empty array which is safe.
                Collection c =
                    receiver != null
                    ? Model.getFacade().getBases(receiver)
                            : null;
                Object cls =
                    c != null && !c.isEmpty() ? c.iterator().next() : null;
                if (cls != null && Model.getFacade().isAClassifier(cls)) {
                    // too primitive (just gets the first method
                    // with matching name)
                    String opName = Model.getFacade().getName(target);
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
                    final Iterator it = 
                        Model.getCoreHelper().getOperationsInh(cls).iterator();
                    while (it.hasNext()) {
                        operation = it.next();
                        if (opName.equals(
                                Model.getFacade().getName(operation))) {
                            // TODO: There is a cyclic dependency between 
                            // ActionRESequenceDiagram and FigMessage
                            RESequenceDiagramDialog dialog =
                                new RESequenceDiagramDialog(
                                        operation,
                                        (FigMessage) messageFig,
                                        diagram);
                            dialog.setVisible(true);
                            break;
                        }
                    }
                }
            }
        }
    }

    /*
     * @see javax.swing.Action#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        Object target = TargetManager.getInstance().getModelTarget();
        return Model.getFacade().isAOperation(target)
            || Model.getFacade().isAMessage(target);
    }

    // TODO: We later cast this to FigMessage so why define as Object here?
    private Object messageFig;

    /**
     * The UID.
     */
    private static final long serialVersionUID = 2915509413708666273L;
} 
