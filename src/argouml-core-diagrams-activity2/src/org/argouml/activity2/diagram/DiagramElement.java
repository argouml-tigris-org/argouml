package org.argouml.activity2.diagram;

import java.awt.Dimension;
import java.awt.Rectangle;

interface DiagramElement extends org.argouml.uml.diagram.DiagramElement {

    Dimension getMinimumSize();
    Rectangle getBounds();
    void setBounds(Rectangle rect);
    void setLocation(int x, int y);
}
