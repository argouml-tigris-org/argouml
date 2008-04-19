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

/**
 * Tests initialization of ChecklistStatus.
 * 
 * @author Scott Roberts
 */
public class TestChecklistStatus extends TestCase {

    /**
     * The constructor.
     *
     * @param name the name of the test.
     */
    public TestChecklistStatus(String name) {
        super(name);
    }

    /**
     * Test constructor and some basic methods.
     */
    public void testBasics() {

        String category = "";
        String description = "";

        // initialize the ChecklistStatus
        ChecklistStatus list = new ChecklistStatus();

        // add some test data
        list.addItem("Test1");
        list.addItem("Test2");
        list.setNextCategory("Test Category");
        list.addItem("Test3");
        list.addItem("Test4");

        // verify the size of the list
        assertTrue("ChecklistStatus.size() is incorrect", list.size() == 4);

        // verify the items were correctly added
        int iter = 0;
        for (CheckItem item : list.getCheckItemList()) {

            switch (++iter)
            {
            case 1:
                category = "General";
                description = "Test1";
                break;

            case 2:
                category = "General";
                description = "Test2";
                break;

            case 3:
                category = "Test Category";
                description = "Test3";
                break;

            case 4:
                category = "Test Category";
                description = "Test4";
                break;
            }

            // test that the category and description are correct
            assertTrue("ChecklistStatus items incorrectly added (category: "
                    + category + ", description: )" + description,
                    category == item.getCategory()
                    && description == item.getDescription());
        }

        // create a new list to test the addAll
        ChecklistStatus list2 = new ChecklistStatus();

        // add all of the test items from the first list
        list2.addAll(list);

        // verify the size of the new list
        assertTrue("ChecklistStatus.addAll(Checklist) failed", 
                list2.size() == 4);
    }
}