// $Id$
// Copyright (c) 2003-2004 The Regents of the University of California. All
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

package org.argouml.model;

import java.util.Collection;

import junit.framework.TestCase;

import org.argouml.model.uml.CoreFactory;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.behavior.activity_graphs.MActivityGraph;
import ru.novosoft.uml.behavior.activity_graphs.MPartition;

/**
 * Tests some specific methods in ModelFacade.<p>
 *
 * This is a complement to the tests in {@link TestModelFacade} and 
 * {@link TestModelFacade3} that makes a lot more general tests.<p>
 *
 * As opposed to the tests in {@link TestModelFacade} and 
 * {@link TestModelFacade3} that are run on a whole set of objects,
 * these tests are maintained manually.
 * 
 * @author Linus Tolke
 */
public class TestModelFacade2 extends TestCase {

    /**
     * Constructor for TestModelFacade2.
     *
     * @param arg0 name of test case
     */
    public TestModelFacade2(String arg0) {
	super(arg0);
    }

    /**
     * Test that the correct error is thrown for isAsynchronous.
     */
    public void testErrorThrownInIsAsynchronous() {
	try {
	    ModelFacade.isAsynchronous(new Object());
	    assertTrue("Error was not thrown", false);
	} catch (IllegalArgumentException e) {
	    // We expected an error to be thrown.
	}
    }
    
    /**
     * Test that the correct error is thrown for a setName with illegal name.
     */
    public void testSetName() {
        Object ob = CoreFactory.getFactory().buildClass("initial");
        ModelFacade.setName(ob, "correct");
        ModelFacade.setName(ob, new String(new char[] {
            'i', 'n', 'c', 'o', 'r', 'r', 'e', 'c', 't', ':',
            Character.MAX_VALUE,
        }));
        assertTrue("0xFFFF is not filtered out of the name of a modelelement", 
                ModelFacade.getName(ob).indexOf(0xffff) == -1);
    }
    
   

    /**
     * Test for setModelElementContainer.
     */
    public void testSetModelElementContainer() {
	UmlFactory fy = UmlFactory.getFactory();

	MActivityGraph container =
	    fy.getActivityGraphs().createActivityGraph();
	MPartition partition = fy.getActivityGraphs().createPartition();

	ModelFacade.setModelElementContainer(partition, container);

	Collection collection = container.getPartitions();
	assertTrue(collection.contains(partition));
    }

    /**
     * Test some Tagged Value functions.
     */
    public void testTaggedValue() {
	UmlFactory fy = UmlFactory.getFactory();
	Object cls = fy.getCore().buildClass();

	assertNull(ModelFacade.getTaggedValue(cls, "fooValue"));
	ModelFacade.setTaggedValue(cls, "fooValue", "foo");
	assertEquals(ModelFacade.getValueOfTag(
		ModelFacade.getTaggedValue(cls, "fooValue")), "foo");
	ModelFacade.removeTaggedValue(cls, "fooValue");
	ModelFacade.removeTaggedValue(cls, "nonExistingValue");
	assertNull(ModelFacade.getTaggedValue(cls, "fooValue"));
    }
}
