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

/** In the furture this will be a trash can icon in the project
 * browser.  Deleting an object moves it to the trash.  You can move
 * things back out of the trash if you like.  Eventually you empty the
 * trash.  Critics check for relationships between things that will
 * break when the trash is empty.  E.g., Class X's superclass is in
 * the trash, you must fix this before you empty the trash. 
 */
public class Trash {
    protected static Logger cat = 
        Logger.getLogger(Trash.class);
    
    public static Trash SINGLETON = new Trash();

    /** Keys are model objects, values are TrashItems with recovery info */
    public Vector _contents = new Vector();

    protected Trash() { }

    public void addItemFrom(Object obj, Vector places) {
	if (obj == null) {
	    cat.warn("tried to add null to trash!");
	    return;
	}    
	if (ModelFacade.isAModelElement(obj)) {
	    //MModelElement me = (MModelElement) obj;
	    TrashItem ti = new TrashItem(obj, places);
	    _contents.addElement(ti);
      
	    // next two lines give runtime exceptions. Remove should be done properly
	    //me.setNamespace(null);
	    // me.setNamespace(Trash_Model);
	    cat.debug("added " + obj + " to trash");
	}
	//TODO: trash diagrams
    }

    public boolean contains(Object obj) {
	int size = _contents.size();
	for (int i = 0; i < size; i++) {
	    TrashItem ti = (TrashItem) _contents.elementAt(i);
	    if (ti._item == obj) return true;
	}
	return false;
    }
  
    public void recoverItem(Object obj) {
	cat.debug("TODO: recover from trash");
	if (ModelFacade.isAModelElement(obj)) {
	    TrashItem ti = null; //TODO: find in trash
	    //((MModelElement)obj).recoverFromTrash(ti);
	}
    }

    public void removeItem(Object obj) {
	if (obj == null) {
	    cat.debug("tried to remove null from trash!");
	    return;
	}
	TrashItem ti = null; //TODO: find in trash
	_contents.removeElement(ti);
    }

    public void emptyTrash() {
	cat.debug("TODO: emptyTheTrash not implemented yet");
	if (cat.isDebugEnabled()) {
	    StringBuffer buf = new StringBuffer("Trash contents:");
	    buf.append("\n");
	    java.util.Enumeration keys = _contents.elements();
	    while (keys.hasMoreElements()) {
		Object k = keys.nextElement();
		buf.append("| " + ((TrashItem) k)._item + "\n");
	    }
	    cat.debug(buf.toString());
	}
    
    }

    public int getSize() { return _contents.size(); }

} /* end class Trash */

////////////////////////////////////////////////////////////////

class TrashItem {

    Object _item;
    Object _recoveryInfo = null;
    Vector _places;

    TrashItem(Object item, Vector places) {
	_item = item;
	_places = places;
	//if (item instanceof MModelElement) {
	// this can't work with nsuml. Toby
	/*      try {
		_recoveryInfo = ((MModelElement)item).prepareForTrash();
		}
		catch (PropertyVetoException pve) { }
	*/
	//}
    }

    public boolean equals(Object o) {
	if (o instanceof TrashItem) {
	    TrashItem ti = (TrashItem) o;
	    return ti._item == _item;
	}
	return false;
    }

    public int hashCode() { return _item.hashCode(); }

} /* end class TrashItem */
