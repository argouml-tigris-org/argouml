/* $Id$
 *******************************************************************************
 * Copyright (c) 2010-2013 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris
 *    Bob Tarling
 *******************************************************************************
 *
 * Some portions of this file were previously release using the BSD License:
 */
// $Id$
// Copyright (c) 2007-2009 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import org.argouml.uml.diagram.DiagramSettings;

/**
 * An interface that all ArgoUML Figs are required to implement. It provides a
 * single place to specify behaviors that we want all Figs to have since we
 * don't have access to the GEF class hierarchy (and it is made up of concrete
 * classes instead of interfaces.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 * @since 0.25.4
 */
public interface ArgoFig {

    // TODO: These have been used for most instances of new FigFoo(X0, Y0, ...
    // but additional work is required to extract the constant from
    // calculations, etc.
    
    /** Default X offset of origin used when building figs */
    static final int X0 = 10;

    /** Default Y offset of origin used when building figs */
    static final int Y0 = 10;

    /**
     * min. 17, used to calculate y pos of FigText items in a compartment
     */
    public static final int ROWHEIGHT = 17;
    /**
     * min. 18, used to calculate y pos of stereotype FigText items
     * in a compartment
     */
    public static final int STEREOHEIGHT = 18;


    /**
     * Enable this flag to switch to the debug default color scheme
     */
    static final boolean DEBUG = false;

    /**
     * Default line width in pixels.
     */
    static final int LINE_WIDTH = !DEBUG ? 1 : 20;

    /**
     * Default color for lines. This constant is an interim measure before
     * moving to settable defaults.
     */
    static final Color LINE_COLOR = !DEBUG ? Color.black 
            : new Color(0, 100, 100, 50); // transparent cyan
    
    /**
     * Color for a solid fill.  It's probably always going to be the same as
     * the line color, but we'll identify it separately for flexibility in the
     * future.
     */
    static final Color SOLID_FILL_COLOR = !DEBUG ? LINE_COLOR : new Color(
            LINE_COLOR.getRed(), LINE_COLOR.getGreen(), LINE_COLOR.getBlue(),
            75); // 
    
    /**
     * Default color for filled figures. This is an interim measure before
     * moving to settable defaults.
     */
    static final Color FILL_COLOR = !DEBUG ? Color.white 
            : new Color(255, 255, 100, 100); // yellow

    /**
     * Color for a line which is supposed to blend into the fill.  It's the same
     * color for right now, but we'll keep it separate so we can identify it
     * easily in the future.
     */
    static final Color INVISIBLE_LINE_COLOR = !DEBUG ? FILL_COLOR : new Color(
            FILL_COLOR.getRed(), FILL_COLOR.getGreen(), FILL_COLOR.getBlue(),
            50);

    
    /**
     * Default color for text. This is an interim measure before moving to
     * settable defaults.
     */
    static final Color TEXT_COLOR = !DEBUG ? Color.black 
            : new Color(0, 100, 0, 100); // translucent green
    
    /**
     * The color used for things which shouldn't normally be visible, so we can
     * spot them easily when they are.  Completely transparent when we're not
     * debugging.  Historically this was hardwired to either red or cyan.
     */
    static final Color DEBUG_COLOR = new Color(255, 0, 255, !DEBUG ? 0 : 255);

    /**
     * Rerender the entire fig.
     * <p>
     * This may be an expensive operation for subclasses which are complex,
     * so should be used sparingly.  It is only intended to be used when 
     * some global change to the rendering defaults is made at the ArgoDiagram
     * level.
     */
    public void renderingChanged();

    /**
     * @return the rendering settings for the Fig
     */
    public DiagramSettings getSettings();
    
    /**
     * Set the rendering settings to be used for this fig. Currently this
     * normally will be a diagram-wide or project-wide settings object that is
     * shared by all Figs.
     * 
     * @param settings the rendering settings to use
     */
    public void setSettings(DiagramSettings settings);
}
