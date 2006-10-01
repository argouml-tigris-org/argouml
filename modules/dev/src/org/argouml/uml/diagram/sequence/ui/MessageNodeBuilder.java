// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.sequence.ui;

import javax.swing.tree.DefaultMutableTreeNode;

import org.argouml.uml.diagram.sequence.CallerListNodeBuilder;
import org.argouml.uml.diagram.sequence.MessageNode;

public class MessageNodeBuilder {

    public static void addNodeTree(
            DefaultMutableTreeNode treeNode,
            FigClassifierRole fcr) {
        int nodeCount = fcr.getNodeCount();
        for (int i = 0; i < nodeCount; ++i) {
            MessageNode mn = fcr.getNode(i);
            String descr =
            	"MessageNode y=" + 
            	fcr.getYCoordinate(mn) + 
            	" " + 
            	mn.getState();
            FigMessagePort fmp = mn.getFigMessagePort();
            if (fmp != null) {
                descr += " FigMessagePort registered";
            }
            DefaultMutableTreeNode tn = new DefaultMutableTreeNode(descr);
            treeNode.add(tn);
            CallerListNodeBuilder.addNodeTree(tn, mn);
        }
    }
}
