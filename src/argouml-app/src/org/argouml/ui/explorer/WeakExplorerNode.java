// $Id$
// Copyright (c) 2004-2006 The Regents of the University of California. All
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
 * About subsumption:
 * <ul>
 * <li>Weak nodes must not rely on that the Explorer preserves the oldest one.
 * <li>The Explorer will not check if several PerspectiveRules has returned
 *     nodes that subsume eachother. Thus it is up to the PerspectiveRules to
 *     make sure that they will not suggest nodes that subsume eachother.
 * <li>The Explorer assumes that they have done this, and can thus draw two
 *     conclusions:
 *     <ul>
 *     <li>If the same WeakExplorerNode instance is returned by any
 *         PerspectiveRule then it will not subsume any other WeakExplorerNode.
 *     <li>If a WeakExplorerNode has subsumed a new WeakExplorerNode then
 *         it will not subsume another (if it would, then those would be
 *         expected to subsume eachother and thus the PerspectiveRules has
 *         failed).
 *     </ul>
 * </ul>
 *
 * @author d00mst
 * @since 0.16.alpha1
 */
public interface WeakExplorerNode {

    /**
     * This method is called by ExplorerTreeModel to check if this
     * WeakExplorerNode subsumes another WeakExplorerNode, ie if this
     * node should be preserved rather than adding the other node.<p>
     *
     * This relation should be reflexive, so that if <code>a</code>
     * is a WeakExplorerNode then <code>a.subsumes(a) == true</code>.<p>
     *
     * This relation should be symmetric, so that if a and b are
     * WeakExplorerNodes and <code>a.subsumes(b) == true</code> then
     * <code>b.subsumes(a) == true</code>.<p>
     *
     * This relation should be transitive, so that if a, b and c are
     * WeakExplorerNodes, <code>a.subsumes(b) == true</code> and
     * <code>b.subsumes(c) == true</code> then
     * <code>a.subsumes(c) == true</code>.<p>
     *
     * Note: While this means that only other WeakExplorerNodes can be
     * subsumed, the argument is still of Object type. This is just since
     * there is no particular point in getting a WeakExplorerNode reference,
     * you would either have to down-cast it further or wouldn't use it more
     * than as an Object pointer.
     *
     * @param obj another WeakExplorerNode
     * @return true if this node subsumes obj, otherwise false.
     */
    boolean subsumes(Object obj);
}

