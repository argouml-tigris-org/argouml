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

/// Copyright (c) 2008 Tom Morris and other contributors. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Contributors.
// The software program and documentation are supplied "AS
// IS", without any accompanying services from The Contributors. The 
// Contributors do not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// CONTRIBUTORS BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE CONTRIBUTORS HAVE BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE CONTRIBUTORS SPECIFICALLY DISCLAIM ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE CONTRIBUTORS
// HAVE NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.notation;

import junit.framework.TestCase;

/**
 * Tests for the NotationSettings class.
 */
public class TestNotationSettings extends TestCase {

    private NotationSettings settings;
    
    protected void setUp() throws Exception {
        super.setUp();
        settings = new NotationSettings();
    }

    public void testNotationLanguage() {
        assertEquals("NotationLanguage default is not correct",
                Notation.DEFAULT_NOTATION, 
                settings.getNotationLanguage());
        // The following should fail
        assertFalse(settings.setNotationLanguage("foo"));
        // and leave things unchanged
        assertEquals(Notation.DEFAULT_NOTATION, settings.getNotationLanguage());
        // This one should work except Java is not registered by default
//        assertTrue(settings.setNotationLanguage("Java"));
//        assertEquals("Java", settings.getNotationLanguage());
    }

    /**
     * Test that default settings are available.
     */
    public void testGetDefaultSettings() {
        assertNotNull(NotationSettings.getDefaultSettings());
    }


    public void testShowPath() {
        assertFalse("Path is not correct", settings.isShowPaths());
        settings.setShowPaths(true);
        assertTrue("Path is not correct", settings.isShowPaths());
        settings.setShowPaths(false);
        assertFalse("Path is not correct", settings.isShowPaths());
    }


    public void testFullyHandleStereotypes() {
        assertFalse("FullyHandleStereotypes is not correct",
                settings.isFullyHandleStereotypes());
        settings.setFullyHandleStereotypes(true);
        assertTrue("FullyHandleStereotypes is not correct",
                settings.isFullyHandleStereotypes());
        settings.setFullyHandleStereotypes(false);
        assertFalse("FullyHandleStereotypes is not correct",
                settings.isFullyHandleStereotypes());
    }


    /**
     * Test the project setting for showing Association names.
     */
    public void testAssociationNames() {
        assertTrue("Association names not correct",
                settings.isShowAssociationNames());
        settings.setShowAssociationNames(false);
        assertFalse("Association names not correct",
                settings.isShowAssociationNames());
        settings.setShowAssociationNames(true);
        assertTrue("Association names not correct",
                settings.isShowAssociationNames());
    }


    public void testShowInitialValues() {
        assertFalse("InitialValue is not correct",
                settings.isShowInitialValues());
        settings.setShowInitialValues(true);
        assertTrue("InitialValue is not correct",
                settings.isShowInitialValues());
        settings.setShowInitialValues(false);
        assertFalse("InitialValue is not correct",
                settings.isShowInitialValues());        
        
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
    
    public void testShowMultiplicities() {
        assertFalse("Multiplicities is not correct",
                settings.isShowMultiplicities());
        settings.setShowMultiplicities(true);
        assertTrue("Multiplicities is not correct",
                settings.isShowMultiplicities());
        settings.setShowMultiplicities(false);
        assertFalse("Multiplicities is not correct",
                settings.isShowMultiplicities());        
        
    }
    
    public void testSingularMultiplicities() {
        assertTrue("ShowSingularMultiplicities is not correct",
                settings.isShowSingularMultiplicities());
        settings.setShowSingularMultiplicities(false);
        assertFalse("SingularMultiplicities is not correct",
                settings.isShowSingularMultiplicities());
        settings.setShowSingularMultiplicities(true);
        assertTrue("ShowSingularMultiplicities is not correct",
                settings.isShowSingularMultiplicities());
    }
    
    public void testShowTypes() {
        assertTrue("Types is not correct", settings.isShowTypes());
        settings.setShowTypes(false);
        assertFalse("Types is not correct", settings.isShowTypes());
        settings.setShowTypes(true);
        assertTrue("Types is not correct", settings.isShowTypes());
    }
    
    /**
     * Test the project setting for showing Multiplicities.
     */
    public void testMultiplicities() {
        assertFalse("Multiplicities not correct",
                settings.isShowMultiplicities());
        settings.setShowMultiplicities(true);
        assertTrue("Multiplicities not correct",
                settings.isShowMultiplicities());
        settings.setShowMultiplicities(false);
        assertFalse("Multiplicities not correct",
                settings.isShowMultiplicities());
        
    }
    
    /**
     * Test the project setting for showing Visibility.
     */
    public void testVisibilities() {
        assertFalse("Visibility not correct",
                settings.isShowVisibilities());
        
        settings.setShowVisibilities(true);
        assertTrue("Visibility not correct",
                settings.isShowVisibilities());
        
        settings.setShowVisibilities(false);
        assertFalse("Visibility not correct",
                settings.isShowVisibilities());
    }

    /**
     * Test the use of Guillemets.
     */
    public void testUseGuillemets() {
        assertFalse("Guillemets not correct",
                settings.isUseGuillemets());
        settings.setUseGuillemets(true);
        assertTrue("Guillemets not correct",
                settings.isUseGuillemets());
        settings.setUseGuillemets(false);
        assertFalse("Guillemets not correct",
                settings.isUseGuillemets());
    }


}
