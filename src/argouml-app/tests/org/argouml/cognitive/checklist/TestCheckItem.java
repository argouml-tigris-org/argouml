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

package org.argouml.cognitive.checklist;

import junit.framework.TestCase;
import org.tigris.gef.util.PredicateTrue;

/**
 * Tests initialization of CheckItem.
 * 
 * @author Scott Roberts
 */
public class TestCheckItem extends TestCase {

    /**
     * The constructor.
     *
     * @param name the name of the test.
     */
    public TestCheckItem(String name) {
        super(name);
    }


    /**
     * Test constructor and some basic methods.
     */
    public void testBasics() {

        // initialize test data
        String category = "TestCheckItem Category";
        String description = "TestCheckItem Description";
        String moreInfo = "http://argouml.tigris.org/test";

        // Construct a CheckItem
        CheckItem item = new CheckItem(category, description,
                moreInfo, PredicateTrue.theInstance());

        // (1) test that the properties were correctly initialized
        assertTrue("CheckItem.getDescription() incorrect "
                       + "after initialization (1)",
                item.getDescription() == description);
        assertTrue("CheckItem.getMoreInfoURL() incorrect "
                       + "after initialization (1)",
                item.getMoreInfoURL() == moreInfo);
        assertTrue("CheckItem.getCategory() incorrect after "
                       + "initialization (1)",
                item.getCategory() == category);
        assertTrue("CheckItem.getPredicate() incorrect after "
                + "initialization (1)",
                item.getPredicate() == PredicateTrue.theInstance());

        // reset the values to something different
        category += "- Set Test";
        description += "- Set Test";
        moreInfo += "- Set Test";

        // update the values
        item.setCategory(category);
        item.setDescription(description);
        item.setMoreInfoURL(moreInfo);
        item.setPredicate(null);

        assertTrue("CheckItem.getDescription() incorrect after set",
                item.getDescription() == description);
        assertTrue("CheckItem.getMoreInfoURL() incorrect after set",
                item.getMoreInfoURL() == moreInfo);
        assertTrue("CheckItem.getCategory() incorrect after set",
                item.getCategory() == category);
        assertTrue("CheckItem.getPredicate() incorrect after set",
                item.getPredicate() == null);

        // (2) Construct another CheckItem
        CheckItem item2 = new CheckItem(category, description);

        // test that the properties were correctly initialized
        assertTrue("CheckItem.getDescription() incorrect "
                       + "after initialization (2)",
                item.getDescription() == description);
        assertTrue("CheckItem.getMoreInfoURL() incorrect "
                       + "after initialization (2)",
                item.getMoreInfoURL() == moreInfo);
        assertTrue("CheckItem.getCategory() incorrect "
                       + "after initialization (2)",
                item.getCategory() == category);

        // verify that the equivalence test works
        assertTrue("CheckItem.equals(o) incorrect", item2.equals(item));

    }
}
