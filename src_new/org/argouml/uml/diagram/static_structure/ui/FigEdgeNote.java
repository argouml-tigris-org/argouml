// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.beans.VetoableChangeListener;

import org.argouml.i18n.Translator;
import org.argouml.kernel.DelayedVChangeListener;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.uml.UUIDManager;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.tigris.gef.base.Layer;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.FigPoly;

import ru.novosoft.uml.MElementEvent;


/** 
 * Class to display a UML note connection to a
 * annotated model element.<p>
 * <p>
 * The owner of this fig is allways CommentEdge
 * </p>
 *
 * @author Andreas Rueckert <a_rueckert@gmx.net>
 * @author jaap.branderhorst@xs4all.nl
 */
public class FigEdgeNote
    extends FigEdgeModelElement
    implements VetoableChangeListener,
	       DelayedVChangeListener,
	       MouseListener,
	       KeyListener,
	       PropertyChangeListener
{
    private Object _owner;

    
    /** 
     * Construct a new note connection. Use the same layout as for
     * other edges.
     */
    public FigEdgeNote() {
        super();
	setBetweenNearestPoints(true);
	((FigPoly) _fig).setRectilinear(false);
	setDashed(true);	
    }      
    
    public FigEdgeNote(Object owner, Layer layer) {
        this(((CommentEdge)owner).getSource(), ((CommentEdge)owner).getDestination());
        setOwner(owner);
    }
    
    /**
     * Constructs a new figedgenote from some object to another
     * object. The objects must have a representation on the given
     * layer.<p>
     *
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
    
    public String toString() {
        return Translator.localize("misc.comment-edge");
    }
    
    
    
    

    /**
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#modelChanged(ru.novosoft.uml.MElementEvent)
     */
    protected void modelChanged(MElementEvent e) {        
    }
    /**
     * @see org.tigris.gef.presentation.Fig#setOwner(java.lang.Object)
     */
    public void setOwner(Object newOwner) {
        // hack to avoid loading problems since we cannot store the whole model yet in XMI
        if (newOwner == null) {
            newOwner = new CommentEdge(getSourceFigNode(), getDestFigNode());
        }
       _owner = newOwner;
       if (ModelFacade.getUUID(newOwner) == null) {
           ModelFacade.setUUID(newOwner,
				UUIDManager.getInstance().getNewUUID());
	}
    }
    
    /**
     * @see org.tigris.gef.presentation.Fig#getOwner()
     */
    public Object getOwner() {
        return _owner;
    }
    
    
    /**
     * @see org.tigris.gef.presentation.Fig#postLoad()
     */
    public void postLoad() {       
        super.postLoad();
        CommentEdge owner = (CommentEdge)getOwner();
        owner.setDestination(getDestFigNode().getOwner());
        owner.setSource(getSourceFigNode().getOwner());
    }
} /* end class FigEdgeNote */
