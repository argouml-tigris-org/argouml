/*
 * ModeCreateMessage.java
 *
 * Created on 12. Februar 2003, 20:21
 */

package org.argouml.uml.diagram.sequence.specification_level.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Rectangle;

import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;

import org.tigris.gef.base.Editor;
import org.tigris.gef.base.ModeCreateFigLine;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigLine;

import ru.novosoft.uml.behavior.common_behavior.MCreateAction;

/**
 *
 * @author  Administrator
 */
public class ModeCreateMessage extends ModeCreateFigLine implements MouseListener, FigSeqConstants {

    protected UMLSequenceDiagram diagram;
    protected Class action;
    protected Fig _source;
    protected Fig _dest;

    /** Creates a new instance of ModeCreateMessage */
    public ModeCreateMessage() {
        super();
    }
//    public ModeCreateMessage(Editor par) {
//        super(par); 
//    }


    /** On mouse down, make a new Fig in memory. */
    public void mousePressed(MouseEvent me) {
        int x = me.getX();
        int y = me.getY();
        Fig f = editor.hit(x, y, 1, 1);
        if (f == null) {
            f = editor.hit(x - 4, y - 4, 8, 8);
        }
        Class ec = (Class)_args.get("edgeClass");
        Class xc = (ec == MCreateAction.class) ? FigSeqClRoleObject.class : FigSeqClRolePort.class;
        if (f == null || !(f instanceof FigSeqClRolePort)) {
            me.consume();
            return;
        } else {
            FigSeqClRolePort p = (FigSeqClRolePort)f;
            boolean connectable = p.isConnectable(xc, true);
            if (!connectable) {
                me.consume();
                return;
            }
        }
        _source = f;
        super.mousePressed(me);
    }

    /** On mouse up, officially add the new item to the parent Editor
     *  and select it. Then exit this mode. */
    public void mouseReleased(MouseEvent me) {
        int x = me.getX();
        int y = me.getY();
        Fig f = editor.hit(x, y);
        if (f == null) {
            f = editor.hit(x - 4, y - 4, 8, 8);
        }
        Class ec = (Class)_args.get("edgeClass");
        Class xc = (ec == MCreateAction.class) ? FigSeqClRoleObject.class : FigSeqClRolePort.class;
        if (f == null || f.getClass() != xc) {
            done();
            me.consume();
            return;
        }
        if (f instanceof FigSeqClRolePort) {
            FigSeqClRolePort p = (FigSeqClRolePort)f;
            boolean connectable = p.isConnectable(xc, false);
            if (!connectable) {
                done();
                me.consume();
                return;
            }
        }
        _dest = f;
        FigLine line = (FigLine)_newItem;
        super.mouseReleased(me);
        ((SequenceDiagramLayout)((UMLSequenceDiagram)getArg("diagram")).getLayer()).addMessage((Class)getArg("action"), _dest, _source);
    }

    public void done() {
        super.done();
        _source = null;
        _dest = null;
    }
}
