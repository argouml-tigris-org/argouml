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
    
    public static void buildParts(Editor editor, FigAssociationClass thisFig, Layer lay) {
        
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
