/* $Id$
 *******************************************************************************
 * Copyright (c) 2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *******************************************************************************
 */

package org.argouml.notation2;

class UmlNotationTextEvent implements NotationTextEvent {

    private final String text;
    private final boolean underlined;
    private final boolean italic;
    private final boolean bold;
    
    UmlNotationTextEvent(
            final String text,
            final boolean italic,
            final boolean underlined,
            final boolean bold) {
        this.text = text;
        this.italic = italic;
        this.underlined = underlined;
        this.bold = bold;
    }
    
    public String getText() {
        return text;
    }

    public boolean isUnderlined() {
        return underlined;
    }

    public boolean isItalic() {
        return italic;
    }

    public boolean isBold() {
        return bold;
    }
}
