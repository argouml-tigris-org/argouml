/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
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

// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram;

import junit.framework.TestCase;

import org.argouml.uml.diagram.DiagramSettings.StereotypeStyle;


/**
 * Tests for the DiagramSettings.
 *
 * TODO: Test chaining of settings.
 *
 * @author Tom Morris (based on TestProjectSettings by Michiel)
 */
public class TestDiagramSettings extends TestCase {

    private DiagramSettings settings;

    /**
     * Constructor.
     *
     * @param name is the name of the test case.
     */
    public TestDiagramSettings(String name) {
        super(name);
    }

    /**
     * Test the default shadow width.
     */
    public void testDefaultShadowWidth() {
        assertEquals("Default shadow width wrong",
                0, settings.getDefaultShadowWidth());
        settings.setDefaultShadowWidth(4);
        assertEquals("Shadow width setting incorrect",
                4, settings.getDefaultShadowWidth());
    }

    /**
     * Test the default stereotype view settings, including compatibility
     * methods.
     */
    public void testStereotypeView() {
        assertEquals("Default stereotype view incorrect",
                StereotypeStyle.TEXTUAL,
                settings.getDefaultStereotypeView());
        assertEquals("Default stereotype view wrong",
                DiagramAppearance.STEREOTYPE_VIEW_TEXTUAL,
                settings.getDefaultStereotypeViewInt());
        settings.setDefaultStereotypeView(
                DiagramAppearance.STEREOTYPE_VIEW_BIG_ICON);
        assertEquals("stereotype view setting incorrect",
                StereotypeStyle.BIG_ICON,
                settings.getDefaultStereotypeView());
        assertEquals("stereotype view setting incorrect",
                DiagramAppearance.STEREOTYPE_VIEW_BIG_ICON,
                settings.getDefaultStereotypeViewInt());

        settings.setDefaultStereotypeView(StereotypeStyle.SMALL_ICON);
        assertEquals("stereotype view setting incorrect",
                StereotypeStyle.SMALL_ICON,
                settings.getDefaultStereotypeView());
        assertEquals("stereotype view setting incorrect",
                DiagramAppearance.STEREOTYPE_VIEW_SMALL_ICON,
                settings.getDefaultStereotypeViewInt());

        settings.setDefaultStereotypeView(StereotypeStyle.TEXTUAL);
        assertEquals("stereotype view setting incorrect",
                StereotypeStyle.TEXTUAL,
                settings.getDefaultStereotypeView());
        assertEquals("stereotype view setting incorrect",
                DiagramAppearance.STEREOTYPE_VIEW_TEXTUAL,
                settings.getDefaultStereotypeViewInt());
    }


    public void testShowBidirectionalArrows() {
        assertFalse("BidirectionalArrows is not correct",
                settings.isShowBidirectionalArrows());
        settings.setShowBidirectionalArrows(true);
        assertTrue("BidirectionalArrows is not correct",
                settings.isShowBidirectionalArrows());
        settings.setShowBidirectionalArrows(false);
        assertFalse("BidirectionalArrows is not correct",
                settings.isShowBidirectionalArrows());
    }

    public void testShowBoldNames() {
        assertFalse("BoldNames is not correct",
                settings.isShowBoldNames());
        settings.setShowBoldNames(true);
        assertTrue("BoldNames is not correct",
                settings.isShowBoldNames());
        settings.setShowBoldNames(false);
        assertFalse("BoldNames is not correct",
                settings.isShowBoldNames());
    }



    public void testFont() {
        assertEquals("Font default is not correct",
                "Dialog", settings.getFontName());
        settings.setFontName("Courier");
        assertEquals("Font is not correct",
                "Courier", settings.getFontName());
        assertEquals("Font size default is not correct",
                10, settings.getFontSize());
        settings.setFontSize(20);
        assertEquals("Font size is not correct",
                20, settings.getFontSize());

        assertTrue("Bold font is incorrect",
                settings.getFontBold().isBold());

        assertTrue("Italic font is incorrect",
                settings.getFontItalic().isItalic());
    }


    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        settings = new DiagramSettings();
    }


}
