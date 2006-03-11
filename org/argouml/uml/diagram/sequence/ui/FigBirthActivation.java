package org.argouml.uml.diagram.sequence.ui;

import java.awt.Color;

public class FigBirthActivation extends FigActivation {
    
    private static final long serialVersionUID = -686782941711592971L;

    FigBirthActivation(int x, int y) {
        super(x, y, FigLifeLine.WIDTH, SequenceDiagramLayer.LINK_DISTANCE / 4);
        setFillColor(Color.black);
    }
}
