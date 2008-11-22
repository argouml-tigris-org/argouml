// $Id$
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

import org.argouml.uml.diagram.DiagramSettings.StereotypeStyle;

import junit.framework.TestCase;


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

    /**
     * Test the project setting for showing Association names.
     */
    public void testAssociationNames() {
        assertFalse("Association names not correct",
                settings.isShowAssociationNames());
        settings.setShowAssociationNames(true);
        assertTrue("Association names not correct",
                settings.isShowAssociationNames());
        settings.setShowAssociationNames(false);
        assertFalse("Association names not correct",
                settings.isShowAssociationNames());
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

    public void testShowInitialValue() {
        assertFalse("InitialValue is not correct",
                settings.isShowInitialValue());
        settings.setShowInitialValue(true);
        assertTrue("InitialValue is not correct",
                settings.isShowInitialValue());
        settings.setShowInitialValue(false);
        assertFalse("InitialValue is not correct",
                settings.isShowBoldNames());        
        
    }

    public void testShowProperties() {
        assertFalse("Properties is not correct",
                settings.isShowProperties());
        settings.setShowProperties(true);
        assertTrue("Properties is not correct",
                settings.isShowProperties());
        settings.setShowProperties(false);
        assertFalse("Properties is not correct",
                settings.isShowProperties());
    }
    
    public void testShowMultiplicity() {
        assertFalse("Multiplicities is not correct",
                settings.isShowMultiplicity());
        settings.setShowMultiplicity(true);
        assertTrue("Multiplicity is not correct",
                settings.isShowMultiplicity());
        settings.setShowMultiplicity(false);
        assertFalse("Multiplicity is not correct",
                settings.isShowMultiplicity());        
        
    }
    
    public void testSingularMultiplicities() {
        assertFalse("ShowSingularMultiplicities is not correct",
                settings.isShowSingularMultiplicities());
        settings.setShowSingularMultiplicities(true);
        assertTrue("ShowSingularMultiplicities is not correct",
                settings.isShowSingularMultiplicities());
        settings.setShowSingularMultiplicities(false);
        assertFalse("SingularMultiplicities is not correct",
                settings.isShowSingularMultiplicities());
    }
    
    public void testShowStereotypes() {
        assertTrue("Stereotypes is not correct",
                settings.isShowStereotypes());
        settings.setShowStereotypes(true);
        assertTrue("Stereotypes is not correct",
                settings.isShowStereotypes());
        settings.setShowStereotypes(false);
        assertFalse("Stereotypes is not correct",
                settings.isShowStereotypes());        

    }
    
    public void testShowTypes() {
        assertFalse("Types is not correct",
                settings.isShowTypes());
        settings.setShowTypes(true);
        assertTrue("Types is not correct",
                settings.isShowTypes());
        settings.setShowTypes(false);
        assertFalse("Types is not correct",
                settings.isShowTypes());        

    }
    
    public void testNotationLanguage() {
        assertEquals("NotationLanguage default is not correct",
                "UML", settings.getNotationLanguage());
        
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
    
    /**
     * Test the project setting for showing Multiplicity.
     */
    public void testMultiplicity() {
        assertFalse("Multiplicity not correct",
                settings.isShowMultiplicity());
        
        settings.setShowMultiplicity(true);
        assertTrue("Multiplicity not correct",
                settings.isShowMultiplicity());
        
        settings.setShowMultiplicity(false);
        assertFalse("Multiplicity not correct",
                settings.isShowMultiplicity());
        
    }
    
    /**
     * Test the project setting for showing Visibility.
     */
    public void testVisibility() {
        assertFalse("Visibility not correct",
                settings.isShowVisibility());
        
        settings.setShowVisibility(true);
        assertTrue("Visibility not correct",
                settings.isShowVisibility());
        
        settings.setShowVisibility(false);
        assertFalse("Visibility not correct",
                settings.isShowVisibility());
    }

    /**
     * Test the use of Guillemets.
     */
    public void testUseGuillemets() {
        assertFalse("Guillemots not correct",
                settings.isUseGuillemets());
        settings.setUseGuillemets(true);
        assertTrue("Guillemots not correct",
                settings.isUseGuillemets());
        settings.setUseGuillemets(false);
        assertFalse("Guillemots not correct",
                settings.isUseGuillemets());
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        settings = new DiagramSettings();
    }


}
