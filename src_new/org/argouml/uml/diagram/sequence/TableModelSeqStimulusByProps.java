
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


// File: TableModelSeqStimulusByProps.java
// Classes: TableModelSeqStimulusByProps
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$


package org.argouml.uml.diagram.sequence;

import java.util.*;
import java.beans.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import org.argouml.uml.*;
import org.argouml.uml.diagram.sequence.ui.*;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.Globals;



public class TableModelSeqStimulusByProps extends TableModelComposite {
    ////////////////
    // constructor
    public TableModelSeqStimulusByProps() { }

    public void initColumns() {
	addColumn(ColumnDescriptor.Name);
	addColumn(ColumnDescriptor.Action);
	addColumn(ColumnDescriptor.MStereotype);
    }

    public Vector rowObjectsFor(Object t) {
	if (!(t instanceof UMLSequenceDiagram || org.argouml.model.ModelFacade.isALink(t)))
	    return new Vector();
	if (t instanceof UMLSequenceDiagram) {
	    Editor _editor = Globals.curEditor();
	    Layer lay = _editor.getLayerManager().getActiveLayer();
	    Vector contents = lay.getContents();
	    int size = contents.size();
	    Vector res = new Vector();
	    for (int i = 0; i < size; i++) {
		Object figure = contents.elementAt(i);
		if (figure instanceof FigSeqStimulus) {
		    FigSeqStimulus figSti = (FigSeqStimulus) figure;
		    MStimulus sti = (MStimulus) figSti.getOwner();
		    res.addElement(sti);
		}
	    }
	    return res;
	}
	else {
	    MLink ml = (MLink) t;
	    Vector res = new Vector();
	    Collection stimuli = ml.getStimuli();
	    Iterator it = stimuli.iterator();
	    while (it.hasNext()) {
		MStimulus sti = (MStimulus) it.next();
		res.addElement(sti);
	    }
	    return res;
	}
    }

    public String toString() { return "SeqStimuli vs. Properties"; }
} /* end class TableModelSeqStimulusByProps */
