// $Id: OutgoingDependencyNode.java 11516 2006-11-25 04:30:15Z tfmorris $
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

package org.argouml.ui.explorer.rules;

import org.argouml.ui.explorer.WeakExplorerNode;

/**
 * This class is a support class for The Navigation panel Go Rules.
 * Don't confuse it with anything to do with GEF nodes or the like.
 *
 * @author  alexb, d00mst
 * @since argo 0.13.4, Created on 21 March 2003, 23:18
 */
public class OutgoingDependencyNode implements WeakExplorerNode {
    /**
     * The parent.
     */
    private Object parent;

    /**
     * Creates a new instance of AssociationsNode.
     *
     * @param p the parent
     */
    public OutgoingDependencyNode(Object p) {
        parent = p;
    }

    /**
     * @return the parent
     */
    public Object getParent() {
	return parent;
    }

    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
	return "Outgoing Dependencies";
    }

    /*
     * @see org.argouml.ui.explorer.WeakExplorerNode#subsumes(java.lang.Object)
     */
    public boolean subsumes(Object obj) {
	return obj instanceof OutgoingDependencyNode;
    }
}

