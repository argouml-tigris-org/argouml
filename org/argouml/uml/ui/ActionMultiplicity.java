// Copyright (c) 1996-01 The Regents of the University of California. All
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


public class ActionMultiplicity extends UMLAction {
    String str = "";
    MMultiplicity mult = null;


    ////////////////////////////////////////////////////////////////
    // static variables

    // multiplicity
    public static UMLAction SrcMultOne = new ActionMultiplicity(MMultiplicity.M1_1, "src");
    public static UMLAction DestMultOne = new ActionMultiplicity(MMultiplicity.M1_1, "dest");

    public static UMLAction SrcMultZeroToOne = new ActionMultiplicity(MMultiplicity.M0_1, "src");
    public static UMLAction DestMultZeroToOne = new ActionMultiplicity(MMultiplicity.M0_1, "dest");

    public static UMLAction SrcMultZeroToMany = new ActionMultiplicity(MMultiplicity.M0_N, "src");
    public static UMLAction DestMultZeroToMany = new ActionMultiplicity(MMultiplicity.M0_N, "dest");

    public static UMLAction SrcMultOneToMany = new ActionMultiplicity(MMultiplicity.M1_N, "src");
    public static UMLAction DestMultOneToMany = new ActionMultiplicity(MMultiplicity.M1_N, "dest");


    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionMultiplicity(MMultiplicity m, String s) {
	//super(m.getLower() + ".." + m.getUpper(), NO_ICON);
	super(m.toString(), NO_ICON);
	str = s;
	mult = m;
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent ae) {
	Vector sels = Globals.curEditor().getSelectionManager().selections();
	if( sels.size() == 1 ) {
	    Selection sel = (Selection) sels.firstElement();
	    Fig f = sel.getContent();
	    Object owner = ((FigEdgeModelElement) f).getOwner();
	    java.util.List ascEnds = ((MAssociation) owner).getConnections();
	    MAssociationEnd ascEnd = null;
	    if(str.equals("src"))
		ascEnd = (MAssociationEnd) ascEnds.get(0);
	    else
		ascEnd = (MAssociationEnd) ascEnds.get(ascEnds.size()-1);
	    ascEnd.setMultiplicity(mult);
	}
    }

    public boolean shouldBeEnabled() { 
	return true; 
    }
} /* end class ActionSrcMultOneToMany */
