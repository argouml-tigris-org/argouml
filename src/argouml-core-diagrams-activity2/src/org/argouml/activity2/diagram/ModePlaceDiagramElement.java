package org.argouml.activity2.diagram;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.base.FigModifyingModeImpl;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.di.GraphNode;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphNodeHooks;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.undo.UndoManager;

public class ModePlaceDiagramElement extends FigModifyingModeImpl {

    private static final Logger LOG = Logger.getLogger(ModePlaceDiagramElement.class);
    private final Object metaType;
    private final String style;
    private final String instructions;
    private final ArgoDiagram diagram;
    
    private Object modelElement;
    private GraphNode graphNode;

    private static final int WIDTH = 90;
    private static final int HEIGHT = 25;
    
    public ModePlaceDiagramElement(
            ArgoDiagram diagram,
            Object metaType,
            String style,
            String instructions) {
        this.diagram = diagram;
        this.metaType = metaType;
        this.style = style;
        if (instructions == null) {
            this.instructions = "";
        } else {
            this.instructions = instructions;
        }
    }

    public String instructions() {
        return instructions;
    }

    public Cursor getInitialCursor() {
        return Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    }

    public void mousePressed(MouseEvent me) {
        if (me.isConsumed()) {
            return;
        }
        UndoManager.getInstance().addMementoLock(this);
        // TODO: Something might go wrong during processing. We don't really
        // want to create the model element until the user releases the mouse
        // in the place expected.
        modelElement = Model.getUmlFactory().buildNode(metaType);
        //
        start();
        editor = Globals.curEditor();
        Layer lay = editor.getLayerManager().getActiveLayer();
        graphNode = createDiagramElement(lay, modelElement, diagram.getDiagramSettings());
        mouseMoved(me);
        me.consume();
    }
    
    private GraphNode createDiagramElement(Layer lay, Object owner, DiagramSettings settings) {
        FigBaseNode fig = new FigBaseNode(modelElement, new Rectangle(0, 0, 0, 0), settings);
        fig.setLayer(lay);
        DiagramElementBuilder.buildDiagramElement(fig, style, owner, settings);
        return fig;
    }

    public void mouseExited(MouseEvent me) {
        editor.damageAll();
        me.consume();
    }

    public void mouseMoved(MouseEvent me) {
        mouseDragged(me);
    }

    public void mouseDragged(MouseEvent me) {
        if (me.isConsumed()) {
            return;
        }
        if (graphNode == null) {
            me.consume();
            return;
        }
        editor.damageAll();
        Point snapPt = new Point(me.getX(), me.getY());
        editor.snap(snapPt);
        ((Fig) graphNode).setLocation(snapPt.x, snapPt.y);
        editor.damageAll();
        me.consume();
    }

    public void mouseEntered(MouseEvent me) {
        me.consume();
    }

    public void mouseReleased(MouseEvent me) {
        if (me.isConsumed()) {
            return;
        }

        LOG.info("Mouse released");
        MutableGraphModel mgm = (MutableGraphModel) editor.getGraphModel();
        UndoManager.getInstance().startChain();
        editor.add((Fig) graphNode);
        mgm.addNode(modelElement);

        editor.getSelectionManager().select((Fig) graphNode);
        LOG.info("The diagram element " + graphNode + " was added");
        done();
        me.consume();
    }

    public void keyTyped(KeyEvent ke) {
        if (ke.getKeyChar() == KeyEvent.VK_ESCAPE) {
            leave();
        }
    }

    public void paint(Graphics g) {
        if (graphNode != null) {
            ((Fig) graphNode).paint(g);
        }
    }
}
