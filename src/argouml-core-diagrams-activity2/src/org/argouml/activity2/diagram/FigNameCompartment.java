package org.argouml.activity2.diagram;

import java.awt.Rectangle;

import org.argouml.model.Model;
import org.argouml.notation2.NotationType;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.Fig;

public class FigNameCompartment extends FigComposite {

    public FigNameCompartment(Object owner, Rectangle bounds,
            DiagramSettings settings) {
        super(owner, settings);
        addFig(new FigCompartment(owner, bounds, settings, Model.getMetaTypes().getStereotype()));
        Fig nameDisplay = new FigNotation(
                owner,
                new Rectangle(0, 0, 0, 0),
                settings,
                NotationType.NAME);
        addFig(nameDisplay);
    }
}
