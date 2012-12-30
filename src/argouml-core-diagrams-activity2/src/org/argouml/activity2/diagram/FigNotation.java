/* $Id$
 *****************************************************************************
 * Copyright (c) 2011-2012 Contributors - see below
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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.logging.Logger;

import org.argouml.notation2.NotatedItem;
import org.argouml.notation2.NotationLanguage;
import org.argouml.notation2.NotationManager;
import org.argouml.notation2.NotationTextEvent;
import org.argouml.notation2.NotationType;
import org.argouml.uml.diagram.DiagramSettings;
import org.tigris.gef.presentation.FigGroup;
import org.tigris.gef.presentation.FigText;

/**
 * @author Bob Tarling
 */
class FigNotation extends FigText implements NotatedItem, DiagramElement {

    private static final Logger LOG =
        Logger.getLogger(FigNotation.class.getName());

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

    public void setLineWidth(int lw) {
        super.setLineWidth(0);
    }

    /**
     * Notation is always transparent
     */
    public void setFilled(boolean filled) {
    }

    @Override
    public Dimension getMinimumSize() {

        int w = getFontMetrics().stringWidth(getText());
        int h = getFontMetrics().getHeight();

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
        this.setText(event.getText());
        this.setUnderline(event.isUnderlined());
        this.setBold(event.isBold());
        this.setItalic(event.isItalic());
        if (getMinimumSize().width > getWidth()) {
            setWidth(getMinimumSize().width);
        }
        this.damage();
    }

    public void setText(String s) {
        final String oldText = getText();
        if (s.equals(oldText)) {
            return;
        }
        final Rectangle oldBounds = getBounds();
        super.setText(s);
        // TODO: This should happen in GEF
        firePropChange("text", oldText, s);
        // TODO: setText in GEF should call setBounds instead of directly
        // changing x, y, w, h - then we will have an event generated
        // correctly in GEF
        final FigGroup group = (FigGroup) getGroup();
        if (group != null
                && ( oldBounds.width < getBounds().width
                        || oldBounds.height < getBounds().height)) {
            group.calcBounds();
        }
    }

    /**
     * Prevent underline events if underline does not change.
     * TODO: GEF should manage this after GEF 0.13.4 is included.
     */
    public void setUnderline(boolean u) {
        if (getUnderline() == u) {
            return;
        }
        super.setUnderline(u);
    }


    /**
     * Prevent bold events if bold does not change.
     * TODO: GEF should manage this after GEF 0.13.4 is included.
     */
    public void setBold(boolean b) {
        if (getBold() == b) {
            return;
        }
        super.setBold(b);
    }

    /**
     * Prevent italic events if italic does not change.
     * TODO: GEF should manage this after GEF 0.13.4 is included.
     */
    public void setItalic(boolean i) {
        if (getItalic() == i) {
            return;
        }
        super.setItalic(i);
    }
}
