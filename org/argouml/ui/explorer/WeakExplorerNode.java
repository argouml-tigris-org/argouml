// $Id$
// Copyright (c) 2004 The Regents of the University of California. All
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

package org.argouml.ui.explorer;

/**
 * Interface for adding weak nodes to the explorer, in this case nodes that
 * can yield to nodes already in the explorer. This may typically look like:
 *
 * <pre>
 * public class SomeNode implements WeakExplorerNode {
 *     Object parent;
 *
 *     public SomeNode(Object parent) {
 *         this.parent = parent;
 *     }
 *
 *     public boolean subsumes(Object obj) {
 *         return obj instanceof SomeNode;
 *     }
 * }
 * </pre>
 *
 * @author d00mst
 * @since 0.16.alpha1
 */
public interface WeakExplorerNode {

    /**
     * This method is called by ExplorerTreeModel to check if this
     * WeakExplorerNode subsumes another WeakExplorerNode, ie if this
     * node should be preserved rather than adding the other node.
     * This only comes into play if this instance and the other
     * sorts equal, since otherwise there will anyway be tree
     * modifications and then it doesn't matter.
     *
     * @param obj another WeakExplorerNode
     * @return true if this node subsumes obj, otherwise false.
     */
    public boolean subsumes(Object obj);
}

