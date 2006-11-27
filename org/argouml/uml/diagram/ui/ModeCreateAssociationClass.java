// $Id: FigStereotype.java 11516 2006-11-25 04:30:15Z tfmorris $
// Copyright (c) 2006 The Regents of the University of California. All
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

import java.awt.Rectangle;

import org.apache.log4j.Logger;
import org.argouml.ui.ProjectBrowser;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Layer;
import org.tigris.gef.base.ModeCreatePolyEdge;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;

public class ModeCreateAssociationClass extends ModeCreatePolyEdge {

    private static final long serialVersionUID = -8656139458297932182L;
    
    private static final Logger LOG =
        Logger.getLogger(ModeCreateAssociationClass.class);

    private static final int DISTANCE = 80;
    
    protected void endAttached() {
        Layer lay = editor.getLayerManager().getActiveLayer();
        FigAssociationClass thisFig =
            (FigAssociationClass) lay.presentationFor(getNewEdge());
        createMemento(thisFig);
        buildParts(editor, thisFig, lay);
    }
    
    public static void buildParts(Editor editor, FigAssociationClass thisFig,
            Layer lay) {
        
        thisFig.removePathItem(thisFig.getMiddleGroup());

        GraphModel graphModel = editor.getGraphModel();

        MutableGraphModel mutableGraphModel =
            (MutableGraphModel) graphModel;
        mutableGraphModel.addNode(thisFig.getOwner());

        Rectangle drawingArea =
            ProjectBrowser.getInstance()
                .getEditorPane().getBounds();
        
        thisFig.makeEdgePort();
        FigEdgePort tee = thisFig.getEdgePort();
        thisFig.calcBounds();

        int x = tee.getX();
        int y = tee.getY();

        LOG.info("Creating Class box for association class");
        FigClassAssociationClass figNode =
            new FigClassAssociationClass(thisFig.getOwner());
        y = y - DISTANCE;
        if (y < 0) {
            y = tee.getY() + figNode.getHeight() + DISTANCE;
        }
        if (x + figNode.getWidth() > drawingArea.getWidth()) {
            x = tee.getX() - DISTANCE;
        }
        figNode.setLocation(x, y);
        lay.add(figNode);

        FigEdgeAssociationClass dashedEdge =
            new FigEdgeAssociationClass(figNode, thisFig);
        dashedEdge.setOwner(thisFig.getOwner());
        lay.add(dashedEdge);

        dashedEdge.damage();
        figNode.damage();
    }
}
