// $Id: eclipse-argo-codetemplates.xml,v 1.2 2006/05/25 12:58:04 linus Exp $
// Copyright (c) 2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.model.mdr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.omg.uml.behavioralelements.statemachines.State;

/**
 * Static utility methods for dealing with collections.
 * 
 * @author Tom Morris
 */
public class CollectionHelper {

    /**
     * Update a collection using a minimal update strategy and updating the
     * collection in place. This can be used with the JMI "live" semantic
     * collections to update the model in place. Order of processing is to first
     * remove all obsolete elements from the collection and then add all new
     * elements.
     * 
     * @param base
     *            the base collection to be updated
     * @param updates
     *            desired end state of collection
     */
    static void update(Collection base, Collection updates) {
        if (base instanceof List || updates instanceof List) {
            throw new IllegalArgumentException("Lists are not supported");
        }
        if (updates == null) {
            base.clear();
            return;
        }
        Collection toBeRemoved = new ArrayList();
        Collection toBeAdded = new ArrayList();

        Iterator oldIt = base.iterator();
        while (oldIt.hasNext()) {
            State s = (State) oldIt.next();
            if (!updates.contains(s)) {
                toBeRemoved.add(s);
            }
        }
        Iterator newIt = updates.iterator();
        while (newIt.hasNext()) {
            State s = (State) newIt.next();
            if (!base.contains(s)) {
                toBeAdded.add(s);
            }
        }
        
        base.removeAll(toBeRemoved);
        base.addAll(toBeAdded);
    }

}
