// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

/*
 * ActionAddAssociation.java
 *
 * Created on 15 February 2003, 01:01
 */
package org.argouml.uml.diagram.ui;


import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;

import org.tigris.gef.base.CmdSetMode;
import org.tigris.gef.base.ModeCreatePolyEdge;

/** 
 * The ActionAddAssociation class is for creating a dummy link with a stimulus and 
 * a given action type. This is done in one step when a new edge between
 * two nodes is instanciated
 *
 * @author Bob Tarling
 */

public class ActionAddAssociation extends CmdSetMode {
    
    /**
     * Construct a new ActionAddAssociation
     * @param aggregation the required aggregation for the association.
     * @param unidirectional true if this is to create a unidirectional 
     *        association
     * @param name the action description
     */
    public ActionAddAssociation(MAggregationKind aggregation, boolean unidirectional, String name) {
        super(ModeCreatePolyEdge.class, "edgeClass", MAssociation.class, name);
        _modeArgs.put("aggregation", aggregation);
        _modeArgs.put("unidirectional", new Boolean(unidirectional));
    }
}


