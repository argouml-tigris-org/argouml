// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: ActionCreatePseudostate.java
// Classes: ActionCreatePseudostate
// Original Author: jrobbins@ics.uci.edu

package org.argouml.uml.diagram.state.ui;

import java.util.Hashtable;

import org.argouml.ui.CmdCreateNode;
import org.argouml.model.ModelFacade;

/**  */
public class ActionCreatePseudostate extends CmdCreateNode {

    ////////////////////////////////////////////////////////////////
    // constructors

    /** Construct a new Cmd with the given classes for the NetNode
     *  and its FigNode. */
    public ActionCreatePseudostate(Object kind, String name) {
	super(new Hashtable(), name);
        
        if(!ModelFacade.isAPseudostateKind(kind))
            throw new IllegalArgumentException();
        
	setArg("className", (Class)ModelFacade.PSEUDOSTATE);
	setArg("kind", kind);
    }

    ////////////////////////////////////////////////////////////////
    // Cmd API

    /** Actually instanciate the NetNode and FigNode objects and
     * set the global next mode to ModePlace */
    // TODO: should call super, reduce code volume!
    public Object makeNode() {
	Object newNode = super.makeNode();
	Object kind = _args.get("kind");
	ModelFacade.setKind(newNode, kind);
    
	return newNode;
    }

} /* end class ActionCreatePseudostate */