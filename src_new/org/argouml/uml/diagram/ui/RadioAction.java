
/*
 * RadioAction.java
 *
 * Created on 20 July 2003, 02:12
 */

package org.argouml.uml.diagram.ui;

import javax.swing.Action;
import javax.swing.JButton;
import org.argouml.kernel.ProjectManager;

import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.toolbutton.AbstractButtonAction;

/**
 * A wrapper around a standard action to indicate that any buttons created
 * from this actions should act like radio buttons.
 *
 * @author Bob Tarling
 */
public class RadioAction extends AbstractButtonAction {

    Action realAction;

    public RadioAction(Action action) {
        super(new JButton(action).getName(), new JButton(action).getIcon());
        realAction = action;
    }

    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        super.actionPerformed(actionEvent);
        realAction.actionPerformed(actionEvent);
        // TODO Change this to ArgoDiagram
        UMLDiagram diagram = (UMLDiagram)ProjectManager.getManager().getCurrentProject().getActiveDiagram();
        diagram.deselectOtherTools(this);
        Globals.setSticky(isDoubleClick());
        if (!isDoubleClick()) {
            Editor ce = Globals.curEditor();
            if (ce != null) {
                ce.finishMode();
            }
        }
    }

    public Action getAction() {
        return realAction;
    }
}