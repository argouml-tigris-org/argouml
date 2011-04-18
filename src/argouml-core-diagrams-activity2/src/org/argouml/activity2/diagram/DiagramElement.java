package org.argouml.activity2.diagram;

import java.awt.Dimension;

interface DiagramElement extends org.argouml.uml.diagram.DiagramElement {

    Dimension getMinimumSize();
}
