/* 
 * The ActionAddLink class is for creating a dummy link with a stimulus and 
 * a given action type. This is done in one step when a new edge between
 * two nodes is instanciated
 */

// file: ActionAddLink.java 
// author: 5kanzler@informatik.uni-hamburg.de
 

package org.argouml.uml.diagram.sequence.ui;

import java.awt.event.ActionEvent;

import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MAction;

import org.tigris.gef.base.CmdSetMode;
import org.tigris.gef.base.ModeCreatePolyEdge;

public class ActionAddLink extends CmdSetMode {
   public ActionAddLink(Class actionClass, String name) {
    super(ModeCreatePolyEdge.class,"edgeClass", MLink.class,name);
    _modeArgs.put("action", actionClass);
  }




}


