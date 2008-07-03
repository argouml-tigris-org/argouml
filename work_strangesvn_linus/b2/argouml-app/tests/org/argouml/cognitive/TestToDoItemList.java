// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.cognitive;

import junit.framework.TestCase;

import org.argouml.model.InitializeModel;

/**
 * Testing the creation of a ToDoItemList.
 */
public class TestToDoItemList extends TestCase {

    /**
     * The constructor.
     *
     * @param name the name of the test.
     */
    public TestToDoItemList(String name) {
        super(name);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        // This test calls ToDoList.addElement which requires the
        // Model.  We are able to get away with using the Mock model.
        InitializeModel.initializeMock();
    }


    /**
     * Test constructor and some basic methods.
     */
    public void testBasics() {
        
        // initialize test data
        int priority = ToDoItem.HIGH_PRIORITY;          
        String headline = "Test Headline";
        String description = "Test Description";
        String moreInfo = "http://argouml.tigris.org/test";
        Critic critic = new Critic();
        critic.setHeadline(headline);           
        ToDoItem item = new ToDoItem(critic, headline, priority, description,
                moreInfo);
        
        // initialize the ToDoList
        ToDoList list = new ToDoList();
                                
        // add a ToDoItem
        list.addElement(item);
                
        // redundantly add item again which should be ignored
        list.addElement(item);

        // size should be 1 since addition of second item would have been ignored
        assertTrue("ToDoList.size() is incorrect", list.size() == 1);

        // retrieve item
        assertTrue("ToDoItem.get(0) did not return correct item",
                list.size() > 0 && list.get(0) == item);

        // remove item
        assertTrue("ToDoItem.removeElement(item) failed to remove item", list
                .removeElement(item));

        // size should be 0 since item was removed
        assertTrue("ToDoList.size() is incorrect", list.size() == 0);
    }
}
