package org.argouml.uml.diagram.ui;

import org.argouml.uml.diagram.static_structure.ui.FigCompartment;

/**
 * A specialist for displaying the model element name and stereotype.
 * @author Bob Tarling
 */
public class FigNameCompartment extends FigCompartment {
    
    private FigStereotype stereotype;
    private FigName name;
    public FigNameCompartment(int x, int y, int w, int h, boolean expandOnly) {
        stereotype = new FigStereotype(x, y, w, h/2 ,expandOnly);
        name = new FigName(x, y+h/2, w, h/2 ,expandOnly);
        addFig(stereotype);
        addFig(name);
    }
    
    public void setStereotype(String stereotype) {
        this.stereotype.setText(stereotype);
    }
    
    public String getStereotype() {
        return stereotype.getText();
    }
    
    public void setName(String name) {
        this.name.setText(name);
    }
    
    public String getName() {
        return name.getText();
    }
    
    public Object clone() {
        FigNameCompartment clone = (FigNameCompartment)super.clone();
        clone.stereotype = (FigStereotype)clone.getFigAt(0);
        clone.name = (FigName)clone.getFigAt(1);
        return clone;
    }
}
