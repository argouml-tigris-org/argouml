// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.uml.diagram.activity.ui;

import java.util.Collection;

import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.tigris.gef.presentation.Fig;

/**
 * This utility has 1 method that deals with all elements 
 * that can be enclosed by a Partition 
 * (i.e. the swimlane on an activity diagram).
 *
 * @author Michiel
 */
public class PartitionUtility {

    /**
     * This method handles 3 cases: 
     * moving a element from the diagram on a partition, 
     * from a partition onto the diagram, 
     * and from one partition to another.
     * 
     * @param oldEncloser the old encloser fig (or null)
     * @param newEncloser the new encloser fig (or null)
     * @param owner the owner of the fig that is moved
     */
    public static void handleEnclosing (Fig oldEncloser, Fig newEncloser, 
            Object owner) {
        // remove from old partition
        if (newEncloser == null || newEncloser != oldEncloser) {
            if (oldEncloser != null) {
                Object partition = oldEncloser.getOwner();
                if (partition != null 
                        && Model.getFacade().isAPartition(partition)) {
                    try {
                        Collection c = Model.getFacade().getContents(partition);
                        c.remove(owner);
                        Model.getActivityGraphsHelper().setContents(
                                partition, c);
                    } catch (InvalidElementException e) {
                        // This just happens sometimes when deleting elements...
                    }
                }
            }
        }
        
        // add to new partition
        if (newEncloser != null) {
            Object partition = newEncloser.getOwner();
            if (Model.getFacade().isAPartition(partition)) {
                Collection c = Model.getFacade().getContents(partition);
                c.add(owner);
                Model.getActivityGraphsHelper().setContents(partition, c);
            }
        }
    }
}
