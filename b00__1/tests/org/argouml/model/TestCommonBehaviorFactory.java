// $Id:TestCommonBehaviorFactory.java 12576 2007-05-09 14:19:16Z tfmorris $
// Copyright (c) 2002-2007 The Regents of the University of California. All
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	"Action", // abstract
	"ActionSequence",
	"Argument",
	"AttributeLink",
	"CallAction",
	"ComponentInstance",
	"CreateAction",
	"DataValue",
	"DestroyAction",
	"Exception",
	"Instance", // abstract
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
     * @return concrete ModelElement types
     */
    static List<String> getTestableModelElements() {
        ArrayList c = new ArrayList(Arrays.asList(allModelElements));
        c.remove("Action");
        c.remove("Instance");
        return c;
    }

    /**
     * @return Returns the allModelElements.
     */
    static String[] getAllModelElements() {
        return allModelElements;
    }

    /**
     * Test for creation.
     */
    public void testCreates() {
        CheckUMLModelHelper.createAndRelease(
                Model.getCommonBehaviorFactory(), getTestableModelElements());
    }

}
