// $Id$
// Copyright (c) 2002 The Regents of the University of California. All
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

package org.argouml.model.uml.foundation.datatypes;

import java.util.LinkedList;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.model.uml.UmlFactory;
import org.argouml.util.CheckUMLModelHelper;

public class TestDataTypesFactory extends TestCase {

    public TestDataTypesFactory(String n) {
	super(n);
    }

    public void testSingleton() {

	Object o1 = DataTypesFactory.getFactory();

	Object o2 = DataTypesFactory.getFactory();

	assertTrue("Different singletons", o1 == o2);

    }

    public void testCreates() {

	Object[] args = {
	    "language", 
	    "body" 
	};

	String[] objs = {
	    "ActionExpression",
	    "ArgListsExpression",
	    "BooleanExpression",
	    "Expression",
	    "IterationExpression",
	    "MappingExpression",
	    "ObjectSetExpression",
	    "ProcedureExpression",
	    "TimeExpression",
	    "TypeExpression",
	    null 
	};

	CheckUMLModelHelper.createAndRelease(
					     this,
					     DataTypesFactory.getFactory(),
					     objs,
					     args);

	// Multiplicity

	CheckUMLModelHelper.createAndRelease(
					     this,
					     DataTypesFactory.getFactory().createMultiplicity(1, 10));

	CheckUMLModelHelper.createAndRelease(
					     this,
					     DataTypesFactory.getFactory().createMultiplicity(new LinkedList()));

	CheckUMLModelHelper.createAndRelease(
					     this,
					     DataTypesFactory.getFactory().createMultiplicity("1..10"));

	// Do we need to add tests for the following?
	//
	// AggregationKind
	// ChangeableKind
	// MessageDirectionKind
	// OrderingKind
	// ParameterDirectionKind
	// PseudostateKind
	// ScopeKind
	// VisibilityKind
	//
    }
    /*
      public void testRemoves() {
      String [] objs = {
      "ActionExpression",
      "ArgListsExpression",
      "BooleanExpression",
      "Expression",
      "IterationExpression",
      "MappingExpression",
      "ObjectSetExpression",
      "ProcedureExpression",
      "TimeExpression",
      "TypeExpression"
      };
	
      CheckUMLModelHelper.testRemove(this, 
      DataTypesFactory.getFactory(), objs);
      }
    */

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
	super.setUp();
	ArgoSecurityManager.getInstance().setAllowExit(true);
        UmlFactory.getFactory().setGuiEnabled(false);
    }
}
