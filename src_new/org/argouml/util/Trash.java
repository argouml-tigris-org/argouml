// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.util;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.model.ModelFacade;
import org.tigris.gef.base.Diagram;

/**
 * In the future this will be a trash can icon in the project
 * browser.  Deleting an object moves it to the trash.  You can move
 * things back out of the trash if you like.  Eventually you empty the
 * trash.  Critics check for relationships between things that will
 * break when the trash is empty.  E.g., Class X's superclass is in
 * the trash, you must fix this before you empty the trash.
 *
 * TODO: Move to the Model component.
 * Problem: there are public static attributes so it is hard to make a proxy
 */
public final class Trash {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(Trash.class);

    /**
     * The SINGLETON trashcan.
     */
    public static final Trash SINGLETON = new Trash();

    /**
     * Keys are model objects, values are TrashItems with recovery info.
     */
    private Vector contents = new Vector();

    /**
     * Constructor to forbid creating this class.
     */
    private Trash() {
        // Do nothing.
    }

    /**
     * @param obj the object to be added
     * @param places currently <code>null</code> in all calls to this function
     */
    public void addItemFrom(Object obj, Vector places) {
	if (obj == null) {
	    LOG.warn("tried to add null to trash!");
	    return;
	}
	if (ModelFacade.isAModelElement(obj)) {
	    //MModelElement me = (MModelElement) obj;
	    TrashItem ti = new TrashItem(obj, places);
	    contents.addElement(ti);

	    // next two lines give runtime exceptions.
	    // Remove should be done properly
	    // me.setNamespace(null);
	    // me.setNamespace(Trash_Model);
	    LOG.debug("added " + obj + " to trash");
	}
	if (obj instanceof Diagram) {
	    TrashItem ti = new TrashItem(obj, places);
	    contents.addElement(ti);
	}
	//TODO: trash diagrams
    }

    /**
     * @param obj the object we look for
     * @return true if the object is present in the trashcan
     */
    public boolean contains(Object obj) {
	int size = contents.size();
	for (int i = 0; i < size; i++) {
	    TrashItem ti = (TrashItem) contents.elementAt(i);
	    if (ti.getItem() == obj) {
	        return true;
	    }
	}
	return false;
    }

    /**
     * @param obj the object to be recovered, i.e. put it back into the model
     */
    public void recoverItem(Object obj) {
	LOG.debug("TODO: recover from trash");
	// if (ModelFacade.isAModelElement(obj)) {
	    // TrashItem ti = null; //TODO: find in trash
	    //((MModelElement)obj).recoverFromTrash(ti);
	// }
    }

    /**
     * @param obj the object to be removed from the trashcan, i.e. nuke it
     */
    public void removeItem(Object obj) {
	if (obj == null) {
	    LOG.debug("tried to remove null from trash!");
	    return;
	}
	TrashItem ti = null; //TODO: find in trash
	contents.removeElement(ti);
    }

    /**
     * Empty the trashcan.
     */
    public void emptyTrash() {
	LOG.debug("TODO: emptyTheTrash not implemented yet");
	if (LOG.isDebugEnabled()) {
	    StringBuffer buf = new StringBuffer("Trash contents:");
	    buf.append("\n");
	    java.util.Enumeration keys = contents.elements();
	    while (keys.hasMoreElements()) {
		Object k = keys.nextElement();
		buf.append("| " + ((TrashItem) k).getItem() + "\n");
	    }
	    LOG.debug(buf.toString());
	}
    }

    /**
     * @return the number of items in the trashcan
     */
    public int getSize() { return contents.size(); }

} /* end class Trash */

////////////////////////////////////////////////////////////////

class TrashItem {

    private Object item;
    private Vector places;

    /**
     * Constructor.
     *
     * @param theItem The item.
     * @param thePlaces ???
     */
    TrashItem(Object theItem, Vector thePlaces) {
	item = theItem;
	places = thePlaces;
	//if (item instanceof MModelElement) {
	// this can't work with nsuml. Toby
	/*      try {
		recoveryInfo = ((MModelElement)item).prepareForTrash();
		}
		catch (PropertyVetoException pve) { }
	*/
	//}
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
	if (o instanceof TrashItem) {
	    TrashItem ti = (TrashItem) o;
	    return ti.item == item;
	}
	return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return item.hashCode();
    }

    /**
     * @return Returns the _item.
     */
    Object getItem() {
        return item;
    }

} /* end class TrashItem */
