// $Id$
// Copyright (c) 2002-2003 The Regents of the University of California. All
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
package org.argouml.ui.targetmanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.model.uml.UmlFactory;

import junit.framework.TestCase;

/**
 * @author gebruiker
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestTargetManager extends TestCase {

    private boolean targetAddedCalled;
    private Object  targetAddedTarget;
    private Object  targetAddedObjects[];
    private boolean targetSetCalled;
    private Object  targetSetTarget;
    private Object  targetSetObjects[];
    private boolean targetRemovedCalled;
    private Object  targetRemovedTarget;
    private Object  targetRemovedObjects[];

    private class TestTargetListener implements TargetListener {

	/* (non-Javadoc)
	 * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
	 */
	public void targetAdded(TargetEvent e) {
	    targetAddedCalled = true;
	    targetAddedTarget = TargetManager.getInstance().getTarget();
	    targetAddedObjects = e.getNewTargets();
	}

	/* (non-Javadoc)
	 * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
	 */
	public void targetRemoved(TargetEvent e) {
	    targetRemovedCalled = true;
	    targetRemovedTarget = TargetManager.getInstance().getTarget();
	    targetRemovedObjects = e.getNewTargets();
	}

	/* (non-Javadoc)
	 * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
	 */
	public void targetSet(TargetEvent e) {
	    targetSetCalled = true;
	    targetSetTarget = TargetManager.getInstance().getTarget();
	    targetSetObjects = e.getNewTargets();
	}

    }

    /**
     * @param arg0
     */
    public TestTargetManager(String arg0) {
	super(arg0);
    }

    public void testGetInstance() {

	Runnable[] runnables = new Runnable[50];
	final TargetManager manager = TargetManager.getInstance();
	for (int i = 0; i < 50; i++) {
	    runnables[i] = new Runnable() 
		{
		    public void run() {
			assertEquals(manager, TargetManager.getInstance());
		    }
		};

	}
	Thread[] threads = new Thread[50];
	for (int i = 0; i < 50; i++) {
	    threads[i] = new Thread(runnables[i]);
	    threads[i].start();
	}
	for (int i = 0; i < 50; i++) {
	    try {
		threads[i].join();
	    } catch (InterruptedException e) {
	    }
	}
	assertTrue(TargetManager.getInstance() instanceof TargetManager);
    }

    public void testSetTarget() {
	Object test = new Object();
	assertEquals(null, TargetManager.getInstance().getTarget());
	TargetManager.getInstance().setTarget(test);
	assertEquals(test, TargetManager.getInstance().getTarget());
	TargetManager.getInstance().setTarget(null);
	assertEquals(null, TargetManager.getInstance().getTarget());
	TargetListener listener = new TestTargetListener();
	targetSetCalled = false;
	targetSetObjects = null;
	TargetManager.getInstance().addTargetListener(listener);
	TargetManager.getInstance().setTarget(test);
	assertTrue(targetSetCalled);
	assertEquals(new Object[] {test}, targetSetObjects);

	TargetManager.getInstance().removeTargetListener(listener);
    }

    public void testGetTarget() {
	TargetListener listener = new TestTargetListener();
	assertEquals(null, TargetManager.getInstance().getTarget());
	Object test = new Object();
	TargetManager.getInstance().setTarget(test);
	assertEquals(test, TargetManager.getInstance().getTarget());

	TargetManager.getInstance().addTargetListener(listener);

	targetSetTarget = null;
	TargetManager.getInstance().setTarget(null);
	assertEquals(null, targetSetTarget);

	targetSetTarget = null;
	TargetManager.getInstance().setTarget(test);
	assertEquals(test, targetSetTarget);

	TargetManager.getInstance().setTarget(null);
	targetAddedTarget = null;
	TargetManager.getInstance().addTarget(test);
	assertEquals(test, targetAddedTarget);
	targetRemovedTarget = null;
	TargetManager.getInstance().removeTarget(test);
	assertEquals(null, targetRemovedTarget);

	TargetManager.getInstance().removeTargetListener(listener);
    }

    public void testGetTargets() {
	Collection coll, coll2;
	Object test =  new Object();
	Object test2 = new Object();
	HashSet set1 = new HashSet();     set1.add(test);
	HashSet set2 = new HashSet(set1); set2.add(test2);

	TargetManager.getInstance().setTarget(null);
	assertTrue(TargetManager.getInstance().getTargets().isEmpty());
	TargetManager.getInstance().setTarget(test);
	assertEquals(new HashSet(TargetManager.getInstance().getTargets()), set1);
	TargetManager.getInstance().setTarget(null);
	assertTrue(TargetManager.getInstance().getTargets().isEmpty());

	TargetManager.getInstance().setTargets(set2);
	coll = TargetManager.getInstance().getTargets();
	coll2 = new HashSet(coll);
	assertTrue(coll.size() == set2.size());
	assertEquals(coll2, set2);

	TargetManager.getInstance().setTargets(set1);
	coll = TargetManager.getInstance().getTargets();
	coll2 = new HashSet(coll);
	assertTrue(coll.size() == set1.size());
	assertEquals(coll2, set1);

	TargetManager.getInstance().setTargets(new HashSet());
	assertTrue(TargetManager.getInstance().getTargets().isEmpty());

	TargetManager.getInstance().addTarget(null);
	assertTrue(TargetManager.getInstance().getTargets().isEmpty());

	TargetManager.getInstance().addTarget(test);
	coll = TargetManager.getInstance().getTargets();
	coll2 = new HashSet(coll);
	assertTrue(coll.size() == set1.size());
	assertEquals(coll2, set1);

	TargetManager.getInstance().addTarget(test2);
	coll = TargetManager.getInstance().getTargets();
	coll2 = new HashSet(coll);
	assertTrue(coll.size() == set2.size());
	assertEquals(coll2, set2);

	TargetManager.getInstance().removeTarget(null);
	coll = TargetManager.getInstance().getTargets();
	coll2 = new HashSet(coll);
	assertTrue(coll.size() == set2.size());
	assertEquals(coll2, set2);

	TargetManager.getInstance().removeTarget(test2);
	coll = TargetManager.getInstance().getTargets();
	coll2 = new HashSet(coll);
	assertTrue(coll.size() == set1.size());
	assertEquals(coll2, set1);

	TargetManager.getInstance().removeTarget(test);
	assertTrue(TargetManager.getInstance().getTargets().isEmpty());
    }

    public void testSetTargets() {
	List test2 = new ArrayList();
	test2.add(new Object());
	test2.add(null);
	test2.add(new Object());
	TargetManager.getInstance().setTargets(test2);
	assertTrue(TargetManager.getInstance().getTargets().contains(test2.get(0)));
	assertTrue(!TargetManager.getInstance().getTargets().contains(null));
	assertTrue(TargetManager.getInstance().getTargets().contains(test2.get(2)));

	List test3 = new ArrayList();
	test3.add(new Object());
	test3.add(test3.get(0));
	TargetManager.getInstance().setTargets(test3);
	assertTrue(TargetManager.getInstance().getTargets().contains(test3.get(0)));
	assertTrue(TargetManager.getInstance().getTargets().size() == 1);

	List test = new ArrayList();
	for (int i = 0; i < 10; i++) {
	    test.add(new Object());
	}
	TargetManager.getInstance().setTargets(test);
	assertEquals(test.toArray(), TargetManager.getInstance().getTargets().toArray());
	TargetManager.getInstance().setTargets(null);
        List expectedValue = new ArrayList();
	assertEquals(expectedValue, TargetManager.getInstance().getTargets());

	TargetListener listener = new TestTargetListener();
	targetSetCalled = false;
	targetSetTarget = null;
	targetSetObjects = null;
	TargetManager.getInstance().addTargetListener(listener);
	TargetManager.getInstance().setTargets(test);
	assertTrue(targetSetCalled);
	assertEquals(test.get(0), targetSetTarget);
	assertEquals(test.toArray(), targetSetObjects);

	TargetManager.getInstance().removeTargetListener(listener);
    }

    public void testAddTarget() {
	List testList = new ArrayList();
	for (int i = 0; i < 10; i++) {
	    testList.add(new Object());
	}
	Object testObject = new Object();
	TargetManager.getInstance().addTarget(testObject);
	assertTrue(TargetManager.getInstance().getTargets().contains(testObject));
	TargetManager.getInstance().setTargets(testList);
	assertTrue(!TargetManager.getInstance().getTargets().contains(testObject));
	TargetManager.getInstance().addTarget(testObject);
	assertTrue(TargetManager.getInstance().getTargets().contains(testObject));
	TargetManager.getInstance().addTarget(null);
	assertTrue(TargetManager.getInstance().getTargets().contains(testObject));
	assertTrue(!TargetManager.getInstance().getTargets().contains(null));

	TargetListener listener = new TestTargetListener();
	TargetManager.getInstance().addTargetListener(listener);
	TargetManager.getInstance().setTargets(testList);
	Object oldTarget = TargetManager.getInstance().getTarget();
	List newList = new ArrayList(testList);
	newList.add(testObject);
	targetAddedCalled = false;
	targetAddedTarget = null;
	targetAddedObjects = null;
	TargetManager.getInstance().addTarget(testObject);
	assertTrue(targetAddedCalled);
	assertEquals(newList.toArray(), targetAddedObjects);
	assertEquals(oldTarget, targetAddedTarget);
	assertEquals(oldTarget, TargetManager.getInstance().getTarget());
	targetAddedCalled = false;
	TargetManager.getInstance().addTarget(testObject);
	assertTrue(!targetAddedCalled);

	TargetManager.getInstance().removeTargetListener(listener);
    }

    public void testRemoveTarget() {
	List testList = new ArrayList();
	for (int i = 0; i < 10; i++) {
	    testList.add(new Object());
	}
	Object testObject = new Object();
	TargetManager.getInstance().setTarget(testObject);
	assertTrue(TargetManager.getInstance().getTargets().contains(testObject));
	TargetManager.getInstance().removeTarget(null);
	assertTrue(TargetManager.getInstance().getTargets().contains(testObject));
	TargetManager.getInstance().removeTarget(testObject);
	assertTrue(TargetManager.getInstance().getTargets().isEmpty());
	TargetManager.getInstance().removeTarget(null);
	assertTrue(TargetManager.getInstance().getTargets().isEmpty());

	testList.add(testObject);
	TargetManager.getInstance().setTargets(testList);
	assertTrue(TargetManager.getInstance().getTargets().contains(testObject));
	TargetManager.getInstance().removeTarget(testObject);
	assertTrue(!TargetManager.getInstance().getTargets().contains(testObject));

	TargetListener listener = new TestTargetListener();
	TargetManager.getInstance().addTargetListener(listener);
	TargetManager.getInstance().setTargets(testList);

	Object oldTarget = TargetManager.getInstance().getTarget();
	targetRemovedCalled = false;
	targetRemovedTarget = null;
	TargetManager.getInstance().removeTarget(testObject);
	assertTrue(targetRemovedCalled);
	assertEquals(oldTarget, TargetManager.getInstance().getTarget());
	assertEquals(oldTarget, targetRemovedTarget);

	TargetManager.getInstance().setTarget(testObject);
	targetRemovedTarget = null;
	targetRemovedObjects = null;
	TargetManager.getInstance().removeTarget(testObject);
	assertEquals(null, targetRemovedTarget);
	assertEquals(new Object[] {}, targetRemovedObjects);

	List testList2 = new ArrayList();
	testList2.add(new Object());
	testList2.add(testObject);
	TargetManager.getInstance().setTargets(testList2);
	targetRemovedTarget = null;
	targetRemovedObjects = null;
	TargetManager.getInstance().removeTarget(testObject);
	assertEquals(testList2.get(0), targetRemovedTarget);
	assertEquals(new Object[] {testList2.get(0)}, targetRemovedObjects);
	targetRemovedCalled = false;
	TargetManager.getInstance().removeTarget(testObject);
	assertTrue(!targetRemovedCalled);

	TargetManager.getInstance().removeTargetListener(listener);
    }

    public void testTransaction() {
	class Listener implements TargetListener {
	    int counter = 0;
	    List list = new ArrayList();

	    /* (non-Javadoc)
	     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
	     */
	    public void targetAdded(TargetEvent e) {
		counter++;
		TargetManager.getInstance().addTarget(new Object());
		TargetManager.getInstance().setTarget(new Object());
		list.add(new Object());
		TargetManager.getInstance().setTargets(list);
		TargetManager.getInstance().setTarget(new Object());

	    }

	    /* (non-Javadoc)
	     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
	     */
	    public void targetRemoved(TargetEvent e) {
		counter++;
		TargetManager.getInstance().addTarget(new Object());
		TargetManager.getInstance().setTarget(new Object());
		list.add(new Object());
		TargetManager.getInstance().setTargets(list);
		TargetManager.getInstance().setTarget(new Object());

	    }

	    /* (non-Javadoc)
	     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
	     */
	    public void targetSet(TargetEvent e) {
		counter++;
		TargetManager.getInstance().addTarget(new Object());
		TargetManager.getInstance().setTarget(new Object());
		list.add(new Object());
		TargetManager.getInstance().setTargets(list);
		TargetManager.getInstance().setTarget(new Object());

	    }

	}
	Listener listener = new Listener();
	TargetManager.getInstance().addTargetListener(listener);
	TargetManager.getInstance().addTarget(new Object());
	assertEquals(1, listener.counter);
	assertEquals(1, listener.counter);
	listener = new Listener();
	TargetManager.getInstance().addTargetListener(listener);
	TargetManager.getInstance().setTarget(new Object());
	assertEquals(1, listener.counter);
	listener = new Listener();
	List list = new ArrayList();
	list.add(new Object());
	TargetManager.getInstance().addTargetListener(listener);
	TargetManager.getInstance().setTargets(list);
	list.add(new Object());
	assertEquals(1, listener.counter);
	listener = new Listener();
	TargetManager.getInstance().addTargetListener(listener);
	TargetManager.getInstance().removeTarget(list.get(0));
	assertEquals(1, listener.counter);
    }
    
    public void testNavigate() {
        TargetManager.getInstance().cleanHistory();
        int numtargets = 10;
        Object[] targets = new Object[numtargets];
        for (int i = 0; i < numtargets; i++) {
            targets[i] = new Object();
            TargetManager.getInstance().setTarget(targets[i]);
        }
        assertTrue(TargetManager.getInstance().navigateBackPossible());
        assertEquals(false, TargetManager.getInstance().navigateForwardPossible());
        try {        
            TargetManager.getInstance().navigateForward();
            fail();
        }
        catch (IllegalStateException e) {
        }
        TargetManager.getInstance().navigateBackward();
        assertEquals(targets[8], TargetManager.getInstance().getTarget());
        assertTrue(TargetManager.getInstance().navigateBackPossible());
        assertTrue(TargetManager.getInstance().navigateForwardPossible());
        for (int i = 7; i > 0; i--) {
            TargetManager.getInstance().navigateBackward();
            assertEquals(targets[i], TargetManager.getInstance().getTarget());
            assertTrue(TargetManager.getInstance().navigateBackPossible());
            assertTrue(TargetManager.getInstance().navigateForwardPossible());
        }
        TargetManager.getInstance().navigateBackward();
        assertTrue(TargetManager.getInstance().navigateForwardPossible());
        assertEquals(false, TargetManager.getInstance().navigateBackPossible());
        try {
            TargetManager.getInstance().navigateBackward();
            fail(); 
        } catch (IllegalStateException e) {
        }
        TargetManager.getInstance().navigateForward();
        assertEquals(targets[1], TargetManager.getInstance().getTarget());
        assertTrue(TargetManager.getInstance().navigateBackPossible());
        assertTrue(TargetManager.getInstance().navigateForwardPossible());
        TargetManager.getInstance().setTarget(targets[9]);
        assertTrue(TargetManager.getInstance().navigateBackPossible());
        assertEquals(false, TargetManager.getInstance().navigateForwardPossible());
        try {
            TargetManager.getInstance().navigateForward();
            fail();
        } catch (IllegalStateException e) {
        }
        TargetManager.getInstance().navigateBackward();
        assertTrue(TargetManager.getInstance().navigateBackPossible());
        assertTrue(TargetManager.getInstance().navigateForwardPossible());
        TargetManager.getInstance().navigateBackward();
        assertTrue(TargetManager.getInstance().navigateForwardPossible());
        assertEquals(false, TargetManager.getInstance().navigateBackPossible());
        
        
    }

    protected void setUp() {
        ArgoSecurityManager.getInstance().setAllowExit(true);
        UmlFactory.getFactory().setGuiEnabled(false);
	TargetManager.getInstance().setTarget(null);
        
    }

    protected void tearDown() {
	TargetManager.getInstance().setTarget(null);
    }
}
