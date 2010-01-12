/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.ui;

import java.awt.Font;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalTheme;

/**
 * This class defines a variation on the default Metal Theme.
 */
public class JasonsHugeTheme extends MetalTheme {

    private final ColorUIResource primary1 = new ColorUIResource(102, 102, 153);
    private final ColorUIResource primary2 = new ColorUIResource(153, 153, 204);
    private final ColorUIResource primary3 = new ColorUIResource(204, 204, 255);

    private final ColorUIResource secondary1 =
	new ColorUIResource(102, 102, 102);
    private final ColorUIResource secondary2 =
	new ColorUIResource(153, 153, 153);
    private final ColorUIResource secondary3 =
	new ColorUIResource(204, 204, 204);

    private final FontUIResource controlFont =
	new FontUIResource("SansSerif", Font.BOLD, 16);
    private final FontUIResource systemFont =
	new FontUIResource("Dialog", Font.PLAIN, 16);
    private final FontUIResource windowTitleFont =
	new FontUIResource("SansSerif", Font.BOLD, 16);
    private final FontUIResource userFont =
	new FontUIResource("SansSerif", Font.PLAIN, 16);
    private final FontUIResource smallFont =
	new FontUIResource("Dialog", Font.PLAIN, 14);

    /*
     * @see javax.swing.plaf.metal.MetalTheme#getName()
     */
    public String getName() { return "Very Large Fonts"; }

    // these are blue in Metal Default Theme
    /*
     * @see javax.swing.plaf.metal.MetalTheme#getPrimary1()
     */
    protected ColorUIResource getPrimary1() { return primary1; }

    /*
     * @see javax.swing.plaf.metal.MetalTheme#getPrimary2()
     */
    protected ColorUIResource getPrimary2() { return primary2; }
    /*
     * @see javax.swing.plaf.metal.MetalTheme#getPrimary3()
     */
    protected ColorUIResource getPrimary3() { return primary3; }

    // these are gray in Metal Default Theme
    /*
     * @see javax.swing.plaf.metal.MetalTheme#getSecondary1()
     */
    protected ColorUIResource getSecondary1() { return secondary1; }

    /*
     * @see javax.swing.plaf.metal.MetalTheme#getSecondary2()
     */
    protected ColorUIResource getSecondary2() { return secondary2; }

    /*
     * @see javax.swing.plaf.metal.MetalTheme#getSecondary3()
     */
    protected ColorUIResource getSecondary3() { return secondary3; }

    /*
     * @see javax.swing.plaf.metal.MetalTheme#getControlTextFont()
     */
    public FontUIResource getControlTextFont() { return controlFont; }

    /*
     * @see javax.swing.plaf.metal.MetalTheme#getSystemTextFont()
     */
    public FontUIResource getSystemTextFont() { return systemFont; }

    /*
     * @see javax.swing.plaf.metal.MetalTheme#getUserTextFont()
     */
    public FontUIResource getUserTextFont() { return userFont; }

    /*
     * @see javax.swing.plaf.metal.MetalTheme#getMenuTextFont()
     */
    public FontUIResource getMenuTextFont() { return controlFont; }

    /*
     * @see javax.swing.plaf.metal.MetalTheme#getSubTextFont()
     */
    public FontUIResource getSubTextFont() { return smallFont; }

    /*
     * @see javax.swing.plaf.metal.MetalTheme#getWindowTitleFont()
     */
    public FontUIResource getWindowTitleFont() { return windowTitleFont; }
}
