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

package org.argouml.util;

import java.util.Collection;
import java.util.List;

/**
 * Some helper methods for dealing with collections.
 *
 * @author Bob Tarling
 * @stereotype utility
 */
public final class CollectionUtil {

    /**
     * Can't construct a utility.
     */
    private CollectionUtil() {
    }

    /**
     * Return the first item from a collection using the most efficient
     * method possible.
     *
     * @param c The Collection.
     * @return the first element of a Collection.
     * @throws java.util.NoSuchElementException if the collection is empty.
     */
    public static Object getFirstItem(Collection c) {
        if (c instanceof List) {
            return ((List) c).get(0);
        }
        return c.iterator().next();
    }

    /**
     * Return the first item from a collection using the most efficient
     * method possible. Returns null for an empty collection.
     *
     * @param c The Collection.
     * @return the first element of a Collection.
     */
    public static Object getFirstItemOrNull(Collection c) {
        if (c.size() == 0) {
            return null;
        }
        return getFirstItem(c);
    }
}
