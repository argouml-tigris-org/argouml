/* $Id: FigNotation.java $
 *****************************************************************************
 * Copyright (c) 20011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 */

package org.argouml.activity2.diagram;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

import org.argouml.notation2.NotatedItem;
import org.argouml.notation2.NotationLanguage;
import org.argouml.notation2.NotationManager;
import org.argouml.notation2.NotationTextEvent;
import org.argouml.notation2.NotationType;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.FigText;

/**
 * @author Bob Tarling
 */
public class FigNotation extends FigText implements NotatedItem, DiagramElement {

    private final NotationType notationType;
    
    /**
     * Construct the notation fig
     * 
     * @param owner owning UML element
     * @param bounds position and size
     * @param settings diagram settings
     * @param notationType the notation type to show
     */
    public FigNotation(
            final Object owner,
            final Rectangle bounds,
            final DiagramSettings settings,
            final NotationType notationType) {
        super(bounds.x, bounds.y, bounds.width, bounds.height , true);
        setOwner(owner);
        setFont(settings.getFontPlain());
        this.notationType = notationType;
        setTabAction(FigText.END_EDITING);
        setReturnAction(FigText.END_EDITING);
        setLineWidth(0);
        super.setFilled(true);
        NotationManager.getInstance().addListener(this);
    }

    /**
     * Notation is always transparent
     */
    public void setFilled(boolean filled) {
    }
    
    @Override
    public Dimension getMinimumSize() {
        
//        int w = getFontMetrics().stringWidth(getText());
//        int h = getFontMetrics().getHeight();
        int w = 0;
        int h = getFont().getSize();
        
        final int minWidth = 
            w
            + getLeftMargin()
            + getRightMargin()
            + 2 * getLineWidth();
        final int minHeight = 
            h
            + getTopMargin()
            + getBotMargin()
            + 2 * getLineWidth();
        return new Dimension(minWidth, minHeight);
    }

    public Object getMetaType() {
        return getOwner().getClass();
    }

    public NotationLanguage getNotationLanguage() {
        return null;
    }

    public NotationType getNotationType() {
        return notationType;
    }

    public void notationTextChanged(NotationTextEvent event) {
        System.out.println("Notation=" + event.getText());
        this.setText(event.getText());
        this.setUnderline(event.isUnderlined());
        this.setUnderline(event.isBold());
        this.setItalic(event.isBold());
    }
}
