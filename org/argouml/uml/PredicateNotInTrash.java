/*
 * Created on 21.10.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.argouml.uml;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.tigris.gef.util.Predicate;

/** A Predicate to determine if a given object is in the Project Trash or not.
 * Required so that the GoListToOfffenderItem does not display offenders,
 * which are already in the trash bin.
 **/
public class PredicateNotInTrash implements Predicate {
    private Project p; 
    public boolean predicate(Object obj) {
        p = ProjectManager.getManager().getCurrentProject();
        if (p == null) return true;
        if (p.isInTrash(obj)) return false;
        return true;
    }
}

