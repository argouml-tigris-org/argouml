package org.argouml.model.uml;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

import org.apache.log4j.Category;

public class ModelListener implements MElementListener {

    private static ModelListener SINGLETON = new ModelListener();

    Category logger = null;

    public static ModelListener getInstance() {
        return SINGLETON;
    }

    /** Don't allow instantiation.
     * Create the logger.
     */
    private ModelListener() {
        logger = Category.getInstance("org.argouml.model.uml.listener");
    }

    private void report(String caption, MElementEvent mee) {
        logger.debug("ModelListener." + caption + "(" + mee + ")");
	// 
        // logger.debug("            name: '" + mee.getName() + "'");
        // logger.debug("        oldValue: '" + mee.getOldValue() + "'");
        // logger.debug("        newValue: '" + mee.getNewValue() + "'");
        // logger.debug("        position: '" + mee.getPosition() + "'");
        // logger.debug("    removedValue: '" + mee.getRemovedValue() + "'");
        // logger.debug("            type: '" + mee.getType() + "'");
        // logger.debug("");
    }

    public void listRoleItemSet (MElementEvent mee) {
        // logger.debug("listRoleItemSet(" + mee + ")");
        report("listRoleItemSet", mee);
    }

    public void propertySet (MElementEvent mee) {
        report("propertySet", mee);
	if (! mee.getNewValue().equals(mee.getOldValue())) {
	    notifyModelChanged(mee);
	}
    }

    public void recovered (MElementEvent mee) {
        report("recovered", mee);
    }

    public void removed (MElementEvent mee) {
        report("removed", mee);
    }

    public void roleAdded (MElementEvent mee) {
        report("roleAdded", mee);
	notifyModelChanged(mee);
    }

    public void roleRemoved (MElementEvent mee) {
        report("roleRemoved", mee);
	notifyModelChanged(mee);
    }

    protected void notifyModelChanged(MElementEvent mee) {
	logger.debug ("MODEL CHANGED");
	// TODO: Mark the project as needing a save.

	// TODO: post an event of some type.
	//
	// Should this be a property change event?
	//
    }
}

