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

package org.argouml.model.uml.foundation.core;
import junit.framework.*;

import org.argouml.util.*;

public class TestCoreFactory extends TestCase {
    public TestCoreFactory(String n) { super(n); }

    public void testSingleton() {
	Object o1 = CoreFactory.getFactory();
	Object o2 = CoreFactory.getFactory();
	assert("Different singletons", o1 == o2);
    }

    public void testCreates() {

        // Do not test BehavioralFeature, Feature, PresentationElement,
        //    StructuralFeature yet.
        // NSUML does not have create method.
        //
        // Never test for ModelElement.

	String [] objs = {
	    "Abstraction",
	    "Association",
	    "AssociationClass",
	    "AssociationEnd",
	    "Attribute",
	    // "BehavioralFeature",
	    "Binding",
	    "Class",
	    "Classifier",
	    "Comment",
	    "Component",
	    "Constraint",
	    "DataType",
	    "Dependency",
	    // "Element",
	    "ElementResidence",
	    // "Feature",
	    "Flow",
	    "Generalization",
	    "Interface",
	    "Method",
	    // "ModelElement",
	    "Namespace",
	    "Node",
	    "Operation",
	    "Parameter",
	    "Permission",
	    // "PresentationElement",
	    "Relationship",
	    // "StructuralFeature",
	    "TemplateParameter",
	    "Usage",
	    null
	};

	CheckUMLModelHelper.createAndRelease(this, 
	    CoreFactory.getFactory(),
	    objs);
    }
    
}
