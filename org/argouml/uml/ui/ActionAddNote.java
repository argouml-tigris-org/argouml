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

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.static_structure.ui.FigComment;
import org.argouml.uml.diagram.static_structure.ui.FigEdgeNote;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigEdge;
import org.tigris.gef.presentation.FigNode;

import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MComment;
import ru.novosoft.uml.foundation.core.MModelElement;




public class ActionAddNote extends UMLChangeAction {
    
    protected static final int DISTANCE = 80;

    ////////////////////////////////////////////////////////////////
    // static variables
    
    public static ActionAddNote SINGLETON = new ActionAddNote(); 


    ////////////////////////////////////////////////////////////////
    // constructors

    public ActionAddNote() { super("Note"); }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent ae) {
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        Object target = pb.getDetailsTarget();
        if (target == null) {
            target = pb.getTarget();
            if (target == null || !(target instanceof MModelElement)) return;
        }
        MModelElement elem = (MModelElement)target;
        MComment comment = CoreFactory.getFactory().buildComment(elem);
        
        // calculate the position of the comment
        Fig elemFig = pb.getActiveDiagram().presentationFor(elem);
        if (elemFig == null) return;
        int x = 0;
        int y = 0;
        Diagram diagram = pb.getActiveDiagram();
        Layer lay = diagram.getLayer();
        Rectangle drawingArea = pb.getEditorPane().getBounds();
        FigComment fig = new FigComment(diagram.getGraphModel(), comment);
        if (elemFig instanceof FigNode) {
            x = elemFig.getX() + elemFig.getWidth() + DISTANCE;
            y = elemFig.getY();
            if (x + fig.getWidth() > drawingArea.getX()) {
                x = elemFig.getX() - fig.getWidth() - DISTANCE ;
                if (x < 0) {
                    x = elemFig.getX();
                    y = elemFig.getY() - fig.getHeight() - DISTANCE;
                    if (y < 0) {
                        y = elemFig.getY() + elemFig.getHeight() + DISTANCE;
                        if (y + fig.getHeight() > drawingArea.getHeight()) {
                            comment.remove();
                            return;
                        }
                    }
                }
            }
        } else
        if (elemFig instanceof FigEdge) {
            // we cannot do this yet since we have to modify all our edges probably
            /*
            Point startPoint = new Point(elemFig.getX(), elemFig.getY());
            Point endPoint = new Point(elemFig.getX() + elemFig.getWidth(), 
                elemFig.getY() + elemFig.getHeight());
            */
            comment.remove();
            return;
        }
        fig.setLocation(x, y);
        lay.add(fig);
        FigEdgeNote edge = new FigEdgeNote(elem, comment);
        lay.add(edge);
        lay.sendToBack(edge);
        edge.damage();
        fig.damage();
        elemFig.damage();
        
        
        
           
        /*
         * this needs refactoring badly
	ProjectBrowser pb = ProjectBrowser.TheInstance;
	Object target = pb.getDetailsTarget();
	Object d = pb.getTarget();
	MModelElement objectWithNote = null;
    // if the target is neither of a Classifier nor a state do nothing
	if (!(target instanceof MClassifier || 
          target instanceof MStateVertex)) return;
    
    if (target instanceof MClassifier)
        objectWithNote = (MClassifier)target;
    else if (target instanceof MStateVertex) 
        objectWithNote = (MStateVertex)target;
    
	Editor ce = Globals.curEditor();
	GraphModel gm = ce.getGraphModel();
	GraphNodeRenderer renderer = ce.getGraphNodeRenderer();
	Layer lay = ce.getLayerManager().getActiveLayer();

	// There are 2 FigNote classes in the project now, so I have to
	// use the fully qualified typename here.
	org.argouml.uml.diagram.static_structure.ui.FigComment note = new org.argouml.uml.diagram.static_structure.ui.FigComment(objectWithNote);

	// place the note a few pixels right of the selected figure
	Fig f = null;  // The figure for the associated object.
	Rectangle targetBounds=null;  // The bounds of this figure.
        Vector figs = ce.getSelectionManager().getFigs();  // Get all the figures of the current diagram.
        int size = figs.size();
        for (int i = 0; i < size; i++) {  // Now search the figure for the active element
	    f = (Fig)figs.elementAt(i);  
	    Object owner = f.getOwner();  // Get the owner of the current figure.
	    if((owner instanceof MModelElement) && (owner == target)) {  // If this is the figure,
		targetBounds = f.getBounds();   // get the bounds of it.

		// Place the note right of the figure,
		note.setLocation(targetBounds.x + targetBounds.width + 80, targetBounds.y);

		// And finish the search.
		break;
	    }
        }
	lay.add(note);  // Add the figure for for the note to the current layer.

	// Add a link from note to associated figure
	//

	Rectangle noteBounds = note.getBounds();  // Get the bounds of the note.

	// Simulate a mouseclick in the middle of the figure to get a port to connect
	// the link to.
	Object startPort = note.deepHitPort( noteBounds.x + noteBounds.width/2, noteBounds.y + noteBounds.height/2);      
	Fig startPortFigure = note.getPortFig(startPort);

	MutableGraphModel mgm = (MutableGraphModel)gm;

	if (f instanceof FigNode) {
	    FigNode destFigNode = (FigNode)f;

	    // Place the port in the middle of the figure.
	    Object foundPort = destFigNode.deepHitPort(targetBounds.x + targetBounds.width/2, targetBounds.y + targetBounds.height/2);

	    if (foundPort != null) {
		Fig destPortFig = destFigNode.getPortFig(foundPort);

		FigEdgeNote fe = new FigEdgeNote();

		fe.setSourcePortFig( startPortFigure);
		fe.setSourceFigNode( note);
		fe.setDestPortFig( destPortFig);
		fe.setDestFigNode( (FigNode)f);

		// Compute the route for this edge. That methods basically adds a snap point at
		// the edge of the figures, so you can mode the edge at the edge of the figures.
		fe.computeRoute();

		lay.add(fe);  // Add the edge to the current layer.
		lay.sendToBack(fe);

		// set the new edge in place
		note.addFigEdge(fe);
		destFigNode.addFigEdge(fe);
		note.updateEdges();
		destFigNode.updateEdges();   
		ce.damaged(fe);
		note.damage();
		f.damage();
	    }
	}
    */
	super.actionPerformed(ae);
    
    }

    public boolean shouldBeEnabled() {
	ProjectBrowser pb = ProjectBrowser.TheInstance;
	Object target = pb.getDetailsTarget();
	return super.shouldBeEnabled() && 
        (target instanceof MModelElement) && (pb.getActiveDiagram().presentationFor(target) instanceof FigNode);
    }
} /* end class ActionAddNote */
