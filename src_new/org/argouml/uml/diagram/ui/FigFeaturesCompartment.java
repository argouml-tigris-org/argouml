package org.argouml.uml.diagram.ui;

import java.awt.Color;

import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;

/**
 * Presentation logic which is common to both an operations
 * compartment and an attributes compartment.
 * @author Bob Tarling
 */
public abstract class FigFeaturesCompartment extends FigCompartment {
    
    private Fig bigPort;

    public FigFeaturesCompartment(int x, int y, int w, int h) {
        bigPort = new FigRect(x, y, w, h, Color.black, Color.white);
        bigPort.setFilled(true);
        bigPort.setLineWidth(1);
        setFilled(true);
        setLineWidth(1);
        addFig(bigPort);
    }
    
    public Fig getBigPort() {
        return bigPort;
    }
}
