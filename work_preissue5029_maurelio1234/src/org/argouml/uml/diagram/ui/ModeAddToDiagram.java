// $Id: ModeAddToDiagram.java 14844 2008-05-31 12:15:11Z bobtarling $
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.FigModifyingModeImpl;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.undo.Memento;
import org.tigris.gef.undo.UndoManager;

/**
 * This is the mode the editor is in having selected a number of model elements
 * and executing Add To Diagram.
 */

public class ModeAddToDiagram extends FigModifyingModeImpl {

    private static final long serialVersionUID = 8861862975789222877L;

    /**
     * The nodes to be added.
     */
    private final Collection<Object> modelElements;

    private final boolean addRelatedEdges = true;

    private final String instructions; 

    private static final Logger LOG = Logger.getLogger(ModeAddToDiagram.class);

    /**
     * Create a mode to add the given elements to the diagram
     * @param modelElements the model elements to add
     * @param instructions the instruction to place in status bar
     */
    public ModeAddToDiagram(
            final Collection<Object> modelElements, 
            final String instructions) {
        this.modelElements = modelElements;
        if (instructions == null) {
            this.instructions = "";
        } else {
            this.instructions = instructions;
        }
    } 

    ////////////////////////////////////////////////////////////////
    // user feedback

    /**
     * @return A string to be shown in the status bar of the Editor when this
     * mode is on top of the ModeManager.
     */
    @Override
    public String instructions() {
        return instructions;
    }

    /**
     * @return a cross hair cursor
     */
    public Cursor getInitialCursor() {
        return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    }

    /**
     * On mouse release add the elements to the diagram
     * @param me the mouse event
     */
    @Override
    public void mouseReleased(final MouseEvent me) {
    	if (me.isConsumed()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("MouseReleased but rejected as already consumed");
            }
            return;
        }
        UndoManager.getInstance().addMementoLock(this);
        start();
    	MutableGraphModel gm = (MutableGraphModel) editor.getGraphModel();
        
        final int x = me.getX();
        final int y = me.getY();
        editor.damageAll();
        final Point snapPt = new Point(x, y);
        editor.snap(snapPt);
        editor.damageAll();
        int count = 0;
        
        Layer lay = editor.getLayerManager().getActiveLayer();
        GraphNodeRenderer renderer = editor.getGraphNodeRenderer();
        
        final List<FigNode> placedFigs =
            new ArrayList<FigNode>(modelElements.size());
        
        for (final Object node : modelElements) {
            if (gm.canAddNode(node)) {
                final FigNode pers =
                    renderer.getFigNodeFor(gm, lay, node, null);
                pers.setLocation(snapPt.x + (count++ * 100), snapPt.y);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("mouseMoved: Location set ("
                            + pers.getX() + "," + pers.getY() + ")");
                }
                UndoManager.getInstance().startChain();
                editor.add(pers);
                gm.addNode(node);
                if (addRelatedEdges) {
                    gm.addNodeRelatedEdges(node);
                }

                Fig encloser = null;
                final Rectangle bbox = pers.getBounds();
                final List<Fig> otherFigs = lay.getContents();
                for (final Fig otherFig : otherFigs) {
                    if (!(otherFig.getUseTrapRect())) {
                        continue;
                    }
                    if (!(otherFig instanceof FigNode)) {
                        continue;
                    }
                    if (!otherFig.isVisible()) {
                        continue;
                    }
                    if (otherFig.equals(pers)) {
                        continue;
                    }
                    final Rectangle trap = otherFig.getTrapRect();
                    if (trap != null
                            && trap.contains(bbox.x, bbox.y)
                            && trap.contains(
                                    bbox.x + bbox.width, 
                                    bbox.y + bbox.height)) {
                        encloser = otherFig;
                    }
                }
                pers.setEnclosingFig(encloser);
                
                placedFigs.add(pers);
            }
        }
        
        UndoManager.getInstance().removeMementoLock(this);
        if (UndoManager.getInstance().isGenerateMementos()) {
            AddToDiagramMemento memento =
                new AddToDiagramMemento(editor, placedFigs);
            UndoManager.getInstance().addMemento(memento);
        }
        UndoManager.getInstance().addMementoLock(this);
        editor.getSelectionManager().select(placedFigs);
        
        done();
        me.consume();
    }

    public void keyTyped(KeyEvent ke) {
        if (ke.getKeyChar() == KeyEvent.VK_ESCAPE) {
            LOG.debug("ESC pressed");
            leave();
        }
    }
}

class AddToDiagramMemento extends Memento {
    
    private final List<FigNode> nodesPlaced;
    private final Editor editor;
    private final MutableGraphModel mgm;
    
    AddToDiagramMemento(final Editor ed, final List<FigNode> nodesPlaced) {
        this.nodesPlaced = nodesPlaced;
        this.editor = ed;
        this.mgm = (MutableGraphModel) editor.getGraphModel();
    }
    
    public void undo() {
    	UndoManager.getInstance().addMementoLock(this);
        for (FigNode figNode : nodesPlaced) {
            mgm.removeNode(figNode.getOwner());
            editor.remove(figNode);
        }
        UndoManager.getInstance().removeMementoLock(this);
    }
    public void redo() {
        UndoManager.getInstance().addMementoLock(this);
        for (FigNode figNode : nodesPlaced) {
            editor.add(figNode);
            mgm.addNode(figNode.getOwner());
        }
        UndoManager.getInstance().removeMementoLock(this);
    }
    public void dispose() {
    }
    
    public String toString() {
        return (isStartChain() ? "*" : " ")
            + "AddToDiagramMemento";
    }
}
