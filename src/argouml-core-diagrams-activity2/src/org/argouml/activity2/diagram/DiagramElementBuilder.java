/* $Id: $
 *****************************************************************************
 * Copyright (c) 2010 Contributors - see below
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

import org.argouml.uml.diagram.DiagramSettings;

class DiagramElementBuilder {

    private static final int WIDTH = 90;
    private static final int HEIGHT = 25;
    
    static void buildDiagramElement(
            final FigBaseNode fig, String style, Object owner, DiagramSettings settings) {
        if (style.equals("compartmentbox")) {
            
        } else if (style.equals("rect")) {
            fig.setDisplayState(
                    new RectDisplayState(
                            fig.getX(), 
                            fig.getX(), 
                            WIDTH, 
                            HEIGHT, 
                            fig.getLineColor(), 
                            fig.getFillColor(),
                            owner,
                            settings));
        } else if (style.equals("rrect")) {
            fig.setDisplayState(
                    new RRectDisplayState(
                            fig.getX(), 
                            fig.getX(), 
                            WIDTH, 
                            HEIGHT, 
                            fig.getLineColor(), 
                            fig.getFillColor(),
                            owner,
                            settings));
        } else if (style.equals("pentagon")) {
            fig.setDisplayState(
                    new PentagonDisplayState(
                            fig.getX(), 
                            fig.getX(), 
                            WIDTH, 
                            HEIGHT, 
                            fig.getLineColor(), 
                            fig.getFillColor(),
                            owner,
                            settings));
        } else if (style.equals("concave-pentagon")) {
            fig.setDisplayState(
                    new ConcavePentagonDisplayState(
                            fig.getX(), 
                            fig.getX(), 
                            WIDTH, 
                            HEIGHT, 
                            fig.getLineColor(), 
                            fig.getFillColor(),
                            owner,
                            settings));
        }
    }

}
