// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.diagram.ui;

import java.awt.Color;
import java.beans.PropertyChangeEvent;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.ProjectSettings;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;

/**
 * Fig to show one stereotype within a FigStereotypeCompartment.
 *
 * @author Bob Tarling
 */
public class FigStereotype extends CompartmentFigText {

    /**
     * The UID.
     */
    private static final long serialVersionUID = -6174252286709779782L;

    /**
    * Constructor for FigFeature.
    * @param x x
    * @param y x
    * @param w w
    * @param h h
    * @param figCompartment the fig
    * @param owner the UML element
    */
    public FigStereotype(int x, int y, int w, int h, Fig figCompartment, 
            Object owner) {
        super(x, y, w, h, figCompartment, "name");

        setFilled(false);
        setLineWidth(0);
        setTextColor(Color.black);
        setTextFilled(false);
        setJustification(FigText.JUSTIFY_LEFT);
        setReturnAction(FigText.END_EDITING);
        setRightMargin(3);
        setLeftMargin(3);
        setOwner(owner);
    }

    public void propertyChange(PropertyChangeEvent event) {
        super.propertyChange(event);
        if (event instanceof AttributeChangeEvent) {
            if (event.getPropertyName().equals("name")) {
                damage();
            }
        }
    }
    
    protected void setText() {
        setText(Model.getFacade().getName(getOwner()));
    }
    
    /**
     * Add guillemots to any text set to this Fig.
     * {@inheritDoc}
     */
    public void setText(String text) {
        Project project = 
            ProjectManager.getManager().getCurrentProject();
        ProjectSettings ps = project.getProjectSettings();
        super.setText(ps.getLeftGuillemot() + text + ps.getRightGuillemot());
    }
    
}
