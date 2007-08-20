// $Id:SelectionNodeClarifiers.java 12612 2007-05-13 18:30:40Z tfmorris $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import javax.swing.Icon;

import org.tigris.gef.presentation.Fig;

/**
 * Class to decorate or paint "clarifiers" for a figure.
 * 
 * @deprecated for 0.25.2 by tfmorris - use {@link SelectionNodeClarifiers2} for
 *             new implementations
 */
public class SelectionNodeClarifiers extends SelectionNodeClarifiers2 {
    

    /** Construct a new SelectionNodeClarifiers for the given Fig
     *
     * @param f the given Fig
     */
    public SelectionNodeClarifiers(Fig f) {
        super(f);
    }

    @Override
    protected Icon[] getIcons() {
        return null;
    }

    @Override
    protected String getInstructions(int index) {
        return null;
    }

    @Override
    protected Object getNewNodeType(int index) {
        return null;
    }

    @Override
    protected Object getNewEdgeType(int index) {
        return null;
    }

    @Override
    protected boolean isReverseEdge(int index) {
        return false;
    }

} 

