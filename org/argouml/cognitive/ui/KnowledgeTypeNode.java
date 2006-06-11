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

package org.argouml.cognitive.ui;

import java.util.Vector;
import org.argouml.cognitive.critics.Critic;


/**
 * This class represents a knowledgetype, which is a classification for critics.
 *
 */
public class KnowledgeTypeNode {

    ////////////////////////////////////////////////////////////////
    // static variables and methods
    private static Vector types = null;

    ////////////////////////////////////////////////////////////////
    // instance variables

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
     */
    public static Vector getTypes() {
        if (types == null) {
            types = new Vector();
            types.addElement(new KnowledgeTypeNode(Critic.KT_DESIGNERS));
            types.addElement(new KnowledgeTypeNode(Critic.KT_CORRECTNESS));
            types.addElement(new KnowledgeTypeNode(Critic.KT_COMPLETENESS));
            types.addElement(new KnowledgeTypeNode(Critic.KT_CONSISTENCY));
            types.addElement(new KnowledgeTypeNode(Critic.KT_SYNTAX));
            types.addElement(new KnowledgeTypeNode(Critic.KT_SEMANTICS));
            types.addElement(new KnowledgeTypeNode(Critic.KT_OPTIMIZATION));
            types.addElement(new KnowledgeTypeNode(Critic.KT_PRESENTATION));
            types.addElement(new KnowledgeTypeNode(Critic.KT_ORGANIZATIONAL));
            types.addElement(new KnowledgeTypeNode(Critic.KT_EXPERIENCIAL));
            types.addElement(new KnowledgeTypeNode(Critic.KT_TOOL));
        }
    return types;
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @return the name of the knowledgetype
     */
    public String getName() { return name; }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() { return getName(); }

} /* end class KnowledgeTypeNode */
