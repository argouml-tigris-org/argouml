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



// File: FigEdgeNote.java
// Classes: FigEdgeNote
// Original Author: Andreas Rueckert <a_rueckert@gmx.net>
// $Id$

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;

import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.kernel.ProjectManager;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;


/** 
 * Class to display a UML note connection to a
 * annotated model element.
 */
public class FigEdgeNote
    extends FigEdgeModelElement
    implements VetoableChangeListener,
	       DelayedVChangeListener,
	       MouseListener,
	       KeyListener,
	       PropertyChangeListener
{

    ////////////////////////////////////////////////////////////////     
    // constants

    ////////////////////////////////////////////////////////////////
    // constructors
    
    /** 
     * Construct a new note connection. Use the same layout as for
     * other edges.
     */
    public FigEdgeNote() {
	setBetweenNearestPoints(true);
	((FigPoly) _fig).setRectilinear(false);
	setDashed(true);
    }
    
    /**
     * Constructs a new figedgenote from some object to another object. The
     * objects must have a representation on the given layer.
     * @param lay
     * @param fromNode
     * @param toNode
     */
    public FigEdgeNote(Object fromNode, Object toNode) {
        this();
        Layer lay =
	    ProjectManager.getManager().getCurrentProject()
	    .getActiveDiagram().getLayer();
        setLayer(lay);
        Fig destFig = lay.presentationFor(toNode);
        Fig sourceFig = lay.presentationFor(fromNode);
        if (destFig == null || sourceFig == null)
	    throw new IllegalStateException("No destfig or sourcefig while "
					    + "creating FigEdgeNode");
        setDestFigNode((FigNode) destFig);
        setDestPortFig(destFig);
        setSourceFigNode((FigNode) sourceFig);
        setSourcePortFig(sourceFig);
        computeRoute();
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public void setFig(Fig f) {
	super.setFig(f);
	_fig.setDashed(true);
    }    

    protected boolean canEdit(Fig f) { return false; }

} /* end class FigEdgeNote */
