// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import org.argouml.uml.diagram.ui.*;
import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import java.awt.event.*;
import java.util.*;

/**
 * A class to perform the action of changing the unidirectional or
 * bidirectional navigation of an association.
 *
 * @author  Bob Tarling
 */
public class ActionNavigability extends UMLAction {
    final public static int BIDIRECTIONAL = 0;
    final public static int STARTTOEND = 1;
    final public static int ENDTOSTART = 2;

    int nav = BIDIRECTIONAL;
    MAssociationEnd start = null;
    MAssociationEnd end = null;

    /**
     * The <code>ActionNavigability</code> constructor.
     *
     * @param start a <code>MAssociationEnd</code> object at the start
     * of an association.
     * @param end a <code>MAssociationEnd</code> object at the end of
     * an association.
     * @param nav the type of navigation required in the association
     * being either <ul> <li>BIDIRECTIONAL <li>STARTTOEND
     * <li>ENDTOSTART </ul>
     */

    public static ActionNavigability newActionNavigability(MAssociationEnd start,
							   MAssociationEnd end,
							   int nav) {
        return new ActionNavigability(getDescription(start, end, nav),
				      start,
				      end,
				      nav);
    }

    static private String getDescription(MAssociationEnd start,
					 MAssociationEnd end,
					 int nav) {
        String startName = start.getType().getName();
        String endName = end.getType().getName();

        if (startName == null || startName.length() == 0) startName = "anon";
        if (endName == null || endName.length() == 0) endName = "anon";

        if (nav == STARTTOEND) {
            return startName + " to " + endName;
        }
        else if (nav == ENDTOSTART) {
            return endName + " to " + startName;
        }
        else {
            return "Bidirectional";
        }
    }

    protected ActionNavigability(String label,
				 MAssociationEnd start,
				 MAssociationEnd end,
				 int nav) {
        super(label, NO_ICON);

        this.nav = nav;
        this.start = start;
        this.end = end;
    }

    /**
     * To perform the action of changing navigability
     */
    public void actionPerformed(ActionEvent ae) {
	start.setNavigable(nav == BIDIRECTIONAL || nav == ENDTOSTART);
        end.setNavigable(nav == BIDIRECTIONAL || nav == STARTTOEND);
    }

    /**
     * The is action is always enabled
     */
    public boolean shouldBeEnabled() {
	return true;
    }
} /* end class ActionNavigability */
