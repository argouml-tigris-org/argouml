/*
 * Created on 22.10.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.argouml.ui.cmd;

import java.awt.event.ActionEvent;

import org.argouml.cognitive.ToDoItem;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLAction;
import org.argouml.util.osdep.StartBrowser;

/**
 * ActionOpenBrowser opens a web browser, using the current target if it is a ToDoItem.
 * Enhance the actionPerformed method for better functionality.
 * 
 * @author MarkusK
 *
 */
public class ActionOpenBrowser extends UMLAction {
    public ActionOpenBrowser() { super("action.open-browser", NO_ICON); }
    
    public void actionPerformed(ActionEvent ae) {
        Object target = TargetManager.getInstance().getTarget();
        if (target instanceof ToDoItem) {
            ToDoItem item = (ToDoItem) target;
            StartBrowser.openUrl(item.getMoreInfoURL());
        }
    }
    
}
