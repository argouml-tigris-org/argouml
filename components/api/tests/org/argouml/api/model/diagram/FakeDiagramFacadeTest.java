// Copyright (c) 2003 The Regents of the University of California. All
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

package org.argouml.api.model.diagram;

import org.argouml.api.FacadeManager;

import junit.framework.TestCase;

/**
 * Test cases for FakeDiagramFacadeTest
 * 
 * @author Thierry Lach
 */
public class FakeDiagramFacadeTest extends TestCase
{

	private DiagramFacade facade;

	/**
	 * Constructor for FakeDiagramFacadeTest.
	 * @param arg0
	 */
	public FakeDiagramFacadeTest(String arg0)
	{
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();

		Object o = FacadeManager.getDiagramFacade();
		assertNotNull("Unable to get Diagram facade", o);
		assertEquals("Didn't get the correct Diagram facade",
					 o.getClass(), FakeDiagramFacade.class);
		facade = (DiagramFacade)o;

	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testIsADiagram()
	{
		try {
			facade.isADiagram(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) { }
	}

	public void testGetName()
	{
		try {
			String name = facade.getName(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) { }
	}

}
