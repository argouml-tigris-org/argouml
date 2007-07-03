// $Id: UUIDHelper.java 12908 2007-06-24 18:22:05Z mvw $
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

package org.argouml.uml;

import org.argouml.model.Model;
import org.tigris.gef.presentation.Fig;

/**
 * @stereotype utility
 */
public final class UUIDHelper {

    /**
     * Hide constructor for the UUIDManager. This is private to make sure that
     * we are a utility.
     */
    private UUIDHelper() { }

    /**
     * Return the UUID of the element.
     *
     * @param base A model element or a Fig representing a model element
     * @return UUID
     */
    public static String getUUID(Object base) {
        if (base instanceof Fig) {
            base = ((Fig) base).getOwner();
        }
        if (base == null) return null;
        if (base instanceof CommentEdge) {
            return (String) ((CommentEdge) base).getUUID();
        }
        return Model.getFacade().getUUID(base);
    }

    /**
     * @return a new UUID
     */
    public static String getNewUUID() {
        return org.argouml.model.UUIDManager.getInstance().getNewUUID();
    }

} /* end class UUIDManager */
