/*
 * Created on Aug 8, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.argouml.model.uml;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author lacht
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AllModelUmlTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.argouml.model.uml");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(NsumlModelFacadeTest.class));
		suite.addTest(new TestSuite(NsumlObjectFactoryTest.class));
		suite.addTest(new TestSuite(ModelElementTest.class));
		suite.addTest(new TestSuite(NamespaceTest.class));
		suite.addTest(new TestSuite(ModelTest.class));
		suite.addTest(new TestSuite(UseCaseTest.class));
		suite.addTest(new TestSuite(ActorTest.class));
		//$JUnit-END$
		return suite;
	}
}
