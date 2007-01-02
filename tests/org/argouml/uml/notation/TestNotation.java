// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.uml.notation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import junit.framework.TestCase;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;

/**
 * Test the Notation class.
 *
 * @author Michiel
 */
public class TestNotation extends TestCase {

    public void testConfiguredNotation() {
        NotationName name = Notation.getConfiguredNotation();
        assertTrue("No configured notation available or generated", 
                name != null);
        NotationName nn = Notation.makeNotation("Test", null, 
                ResourceLoaderWrapper.lookupIconResource("JavaNotation"));
        Notation.setDefaultNotation(nn);
        assertTrue("Default notation not changed", 
                nn.sameNotationAs(Notation.getConfiguredNotation()));
    }
    
    public void testAddingAndRemovingNotations() {
        int oldSize = Notation.getAvailableNotations().size();
        NotationName name = Notation.makeNotation("TestNotation", 
                null, ResourceLoaderWrapper
                .lookupIconResource("JavaNotation"));
        int newSize = Notation.getAvailableNotations().size();
        assertTrue("Adding a notation failed", newSize == oldSize + 1);
        assertTrue("Not able to remove notation", 
                Notation.removeNotation(name));
        int originalSize = Notation.getAvailableNotations().size();
        assertTrue("Adding then removing a notation failed", 
                oldSize == originalSize);
    }
    
    public void testNoDefaultNotation() {
        List nots = new ArrayList(Notation.getAvailableNotations());
        Iterator i = nots.iterator();
        while (i.hasNext()) {
            NotationName nn = (NotationName) i.next();
            assertTrue("Not able to remove notation", 
                    Notation.removeNotation(nn));
        }
        assertTrue("Notations list not empty", 
                Notation.getAvailableNotations().size() == 0);
        Notation.makeNotation("UML 1.4", null, 
                ResourceLoaderWrapper.lookupIconResource("UmlNotation"));
        NotationName name = Notation.getConfiguredNotation();
        assertTrue("No configured notation generated", 
                name != null);
    }
    
    public void testNotationIcons() {
        ImageIcon uml = 
            ResourceLoaderWrapper.lookupIconResource("UmlNotation");
        assertTrue("No UML icon found", uml != null);
        ImageIcon java = 
            ResourceLoaderWrapper.lookupIconResource("JavaNotation");
        assertTrue("No Java icon found", java != null);
    }
}
