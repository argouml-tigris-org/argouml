package org.argouml.activity2.diagram;

import java.awt.Rectangle;

/**
 * This is the interface for all visual nodes. When mature the intention is to
 * move to the diagrams module
 * @author Bob Tarling
 */
public interface DiagramNode extends DiagramElement {

    Rectangle getBounds();
    void setNameDiagramElement(DiagramElement name);
}
