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

/**
 * The event fired whenever notation text should change display
 *
 * @author Bob Tarling
 */
public interface NotationTextEvent {
    /**
     * Provides the new text to display
     * @return text
     */
    String getText();
    /**
     * Indicate if the text should be underlined
     * @return text
     */
    boolean isUnderlined();
    /**
     * Indicate if the text should be underlined
     * @return text
     */
    boolean isItalic();
    /**
     * Indicate if the text should be bold
     * @return text
     */
    boolean isBold();
}
