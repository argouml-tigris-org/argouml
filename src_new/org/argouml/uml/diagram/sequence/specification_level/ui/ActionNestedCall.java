/* 
 * The ActionAddMessage class is for creating a dummy link with a stimulus and 
 * a given action type. This is done in one step when a new edge between
 * two nodes is instanciated
 */

// file: ActionAddMessage.java 
// author: 5kanzler@informatik.uni-hamburg.de
 

package org.argouml.uml.diagram.sequence.specification_level.ui;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MAction;

import org.tigris.gef.base.CmdSetMode;
import org.tigris.gef.base.ModeCreatePolyEdge;

public class ActionNestedCall extends CmdSetMode {

    public ActionNestedCall(Class actionClass) {
        super(ModeCreatePolyEdge.class, "edgeClass", MMessage.class, "NestedCall");
        putValue(Action.NAME, "NestedCall");
        _modeArgs.put("action", actionClass);
    }
}