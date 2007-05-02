// $Id$
// Copyright (c) 2002-2006 The Regents of the University of California. All
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
import java.util.Vector;

import junit.framework.TestCase;


/**
 * Test the CommonBehaviorFactory.
 */
public class TestCommonBehaviorFactory extends TestCase {

    /**
     * All the ModelElements that we will test.
     */
    private static String[] allModelElements =
    {
	"Action",
	"ActionSequence",
	"Argument",
	"AttributeLink",
	"CallAction",
	"ComponentInstance",
	"CreateAction",
	"DataValue",
	"DestroyAction",
	"Exception",
	"Instance",
	"Link",
	"LinkEnd",
	"LinkObject",
	"NodeInstance",
	"Object",
	"Reception",
	"ReturnAction",
	"SendAction",
	"Signal",
	"Stimulus",
	"TerminateAction",
	"UninterpretedAction",
    };

    /**
     * The constructor.
     *
     * @param n the name
     */
    public TestCommonBehaviorFactory(String n) {
	super(n);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
        InitializeModel.initializeDefault();
    }

    /**
     * Test for creation.
     */
    public void testCreates() {

	Collection objs = new Vector();

        // Action is abstract
	objs.add("ActionSequence");
	objs.add("Argument");
	objs.add("AttributeLink");
	objs.add("CallAction");
	objs.add("ComponentInstance");
	objs.add("CreateAction");
	objs.add("DataValue");
	objs.add("DestroyAction");
	objs.add("Exception");
        // Instance is abstract
	objs.add("Link");
	objs.add("LinkEnd");
	objs.add("NodeInstance");
	objs.add("Object");
	objs.add("Reception");
	objs.add("ReturnAction");
	objs.add("SendAction");
	objs.add("Signal");
	objs.add("Stimulus");
	objs.add("TerminateAction");
	objs.add("UninterpretedAction");

	CheckUMLModelHelper.createAndRelease(
	        Model.getCommonBehaviorFactory(),
	        // +1 in array size because we also test the null value
	        (String[]) objs.toArray(new String[objs.size() + 1]));

    }

    /**
     * @return Returns the allModelElements.
     */
    static String[] getAllModelElements() {
        return allModelElements;
    }
}
