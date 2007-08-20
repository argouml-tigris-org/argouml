// $Id:KnowledgeTypeNode.java 12950 2007-07-01 08:10:04Z tfmorris $
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

package org.argouml.cognitive.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.argouml.cognitive.Critic;


/**
 * This class represents a knowledgetype, which is a classification for critics.
 *
 */
public class KnowledgeTypeNode {

    private static List<KnowledgeTypeNode> types = null;

    private String name;

    /**
     * The constructor.
     *
     * @param n the name for the knowledgetype
     */
    public KnowledgeTypeNode(String n) {
	name = n;
    }

    /**
     * @return a list of all the types
     * @deprecated for 0.25.4 by tfmorris. Use {@link #getTypeList()}.
     */
    @Deprecated
    public static Vector<KnowledgeTypeNode> getTypes() {
        return new Vector<KnowledgeTypeNode>(getTypeList());
    }

    /**
     * @return a list of all the types
     */
    public static List<KnowledgeTypeNode> getTypeList() {
        if (types == null) {
            types = new ArrayList<KnowledgeTypeNode>();
            types.add(new KnowledgeTypeNode(Critic.KT_DESIGNERS));
            types.add(new KnowledgeTypeNode(Critic.KT_CORRECTNESS));
            types.add(new KnowledgeTypeNode(Critic.KT_COMPLETENESS));
            types.add(new KnowledgeTypeNode(Critic.KT_CONSISTENCY));
            types.add(new KnowledgeTypeNode(Critic.KT_SYNTAX));
            types.add(new KnowledgeTypeNode(Critic.KT_SEMANTICS));
            types.add(new KnowledgeTypeNode(Critic.KT_OPTIMIZATION));
            types.add(new KnowledgeTypeNode(Critic.KT_PRESENTATION));
            types.add(new KnowledgeTypeNode(Critic.KT_ORGANIZATIONAL));
            types.add(new KnowledgeTypeNode(Critic.KT_EXPERIENCIAL));
            types.add(new KnowledgeTypeNode(Critic.KT_TOOL));
        }
        return types;
    }

    /**
     * @return the name of the knowledgetype
     */
    public String getName() {
        return name;
    }

    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getName();
    }

}
