// $Id$
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

package org.argouml.ui.targetmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import junit.framework.TestCase;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.uml.diagram.ArgoDiagram;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigRect;

/**
 * @author gebruiker
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TestTargetManager extends TestCase {

    private boolean targetAddedCalled;
    private Object  targetAddedTarget;
    private Fig     targetAddedFigTarget;
    private Object  targetAddedModelTarget;
    private Object  targetAddedObjects[];
    private boolean targetSetCalled;
    private Object  targetSetTarget;
    private Fig     targetSetFigTarget;
    private Object  targetSetModelTarget;
    private Object  targetSetObjects[];
    private boolean targetRemovedCalled;
    private Object  targetRemovedTarget;
    private Fig     targetRemovedFigTarget;
    private Object  targetRemovedModelTarget;
    private Object  targetRemovedObjects[];

    private class TestTargetListener implements TargetListener {

	/*
	 * @see TargetListener#targetAdded(TargetEvent)
	 */
	public void targetAdded(TargetEvent e) {
	    targetAddedCalled = true;
	    targetAddedTarget = TargetManager.getInstance().getTarget();
	    targetAddedFigTarget = TargetManager.getInstance().getFigTarget();
	    targetAddedModelTarget =
		TargetManager.getInstance().getModelTarget();
	    targetAddedObjects = e.getNewTargets();
	}

	/*
	 * @see TargetListener#targetRemoved(TargetEvent)
	 */
	public void targetRemoved(TargetEvent e) {
	    targetRemovedCalled = true;
	    targetRemovedTarget = TargetManager.getInstance().getTarget();
	    targetRemovedFigTarget = TargetManager.getInstance().getFigTarget();
	    targetRemovedModelTarget =
		TargetManager.getInstance().getModelTarget();
	    targetRemovedObjects = e.getNewTargets();
	}

	/*
	 * @see TargetListener#targetSet(TargetEvent)
	 */
	public void targetSet(TargetEvent e) {
	    targetSetCalled = true;
	    targetSetTarget = TargetManager.getInstance().getTarget();
	    targetSetFigTarget = TargetManager.getInstance().getFigTarget();
	    targetSetModelTarget = TargetManager.getInstance().getModelTarget();
	    targetSetObjects = e.getNewTargets();
	}

    }

    /**
     * @param arg0 is the name of the test case.
     */
    public TestTargetManager(String arg0) {
	super(arg0);
    }

    /**
     * Test getInstance().
     */
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
	assertTrue(TargetManager.getInstance() != null);
    }

    /**
     * Test setTarget().
     */
    public void testSetTarget() {
	Object test = new Object();
	assertEquals(null, TargetManager.getInstance().getTarget());
	TargetManager.getInstance().setTarget(test);
	assertEquals(test, TargetManager.getInstance().getTarget());
	TargetManager.getInstance().setTarget(null);
	assertEquals(null, TargetManager.getInstance().getTarget());

	TargetListener listener = new TestTargetListener();
	TargetManager.getInstance().addTargetListener(listener);
	targetSetCalled = false;
	targetSetObjects = null;
	TargetManager.getInstance().setTarget(test);
	assertTrue(targetSetCalled);
	assertTrue(Arrays.equals(new Object[] {test}, targetSetObjects));
	targetSetCalled = false;
	TargetManager.getInstance().setTarget(test);
	assertTrue(!targetSetCalled);

	TargetManager.getInstance().removeTargetListener(listener);
    }

    /**
     * Test getTarget().
     */
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

    /**
     * Test getTargets().
     */
    public void testGetTargets() {
	Collection<Object> coll, coll2;
	Object test =  new Object();
	Object test2 = new Object();
	HashSet<Object> set1 = new HashSet<Object>();
        set1.add(test);
	HashSet<Object> set2 = new HashSet<Object>(set1);
        set2.add(test2);

	TargetManager.getInstance().setTarget(null);
	assertTrue(TargetManager.getInstance().getTargets().isEmpty());
	TargetManager.getInstance().setTarget(test);
	assertEquals(
                new HashSet<Object>(TargetManager.getInstance().getTargets()),
		set1);
	TargetManager.getInstance().setTarget(null);
	assertTrue(TargetManager.getInstance().getTargets().isEmpty());

	TargetManager.getInstance().setTargets(set2); //TabProps gets selected!
        TargetManager.getInstance().setTargets(set2); //So, 2nd time right
	coll = TargetManager.getInstance().getTargets();
	assertTrue(coll.size() == set2.size());
        coll2 = new HashSet<Object>(coll);
	assertEquals(coll2, set2);

	TargetManager.getInstance().setTargets(set1);
	coll = TargetManager.getInstance().getTargets();
	coll2 = new HashSet<Object>(coll);
	assertTrue(coll.size() == set1.size());
	assertEquals(coll2, set1);

	TargetManager.getInstance().setTargets(new HashSet<Object>());
	assertTrue(TargetManager.getInstance().getTargets().isEmpty());

	TargetManager.getInstance().addTarget(null);
	assertTrue(TargetManager.getInstance().getTargets().isEmpty());

	TargetManager.getInstance().addTarget(test);
	coll = TargetManager.getInstance().getTargets();
	coll2 = new HashSet<Object>(coll);
	assertTrue(coll.size() == set1.size());
	assertEquals(coll2, set1);

	TargetManager.getInstance().addTarget(test2);
	coll = TargetManager.getInstance().getTargets();
	coll2 = new HashSet<Object>(coll);
	assertTrue(coll.size() == set2.size());
	assertEquals(coll2, set2);

	TargetManager.getInstance().removeTarget(null);
	coll = TargetManager.getInstance().getTargets();
	coll2 = new HashSet<Object>(coll);
	assertTrue(coll.size() == set2.size());
	assertEquals(coll2, set2);

	TargetManager.getInstance().removeTarget(test2);
	coll = TargetManager.getInstance().getTargets();
	coll2 = new HashSet<Object>(coll);
	assertTrue(coll.size() == set1.size());
	assertEquals(coll2, set1);

	TargetManager.getInstance().removeTarget(test);
	assertTrue(TargetManager.getInstance().getTargets().isEmpty());
    }

    /**
     * Test setTargets().
     */
    public void testSetTargets() {
	List<Object> test2 = new ArrayList<Object>();
	test2.add(new Object());
	test2.add(null);
	test2.add(new Object());
	TargetManager.getInstance().setTargets(test2);
	assertTrue(TargetManager.getInstance()
		   .getTargets().contains(test2.get(0)));
	assertTrue(!TargetManager.getInstance().getTargets().contains(null));
	assertTrue(TargetManager.getInstance()
		   .getTargets().contains(test2.get(2)));

	List<Object> test3 = new ArrayList<Object>();
	test3.add(new Object());
	test3.add(test3.get(0));
	TargetManager.getInstance().setTargets(test3);
	assertTrue(TargetManager.getInstance()
		   .getTargets().contains(test3.get(0)));
	assertTrue(TargetManager.getInstance().getTargets().size() == 1);

	List<Object> test4 = new ArrayList<Object>();
	test4.add(test2.get(0));
	test4.add(test2.get(2));

	List<Object> test5 = new ArrayList<Object>();
	test5.add(test2.get(2));
	test5.add(test2.get(0));

	List<Object> test = new ArrayList<Object>();
	for (int i = 0; i < 10; i++) {
	    test.add(new Object());
	}
	TargetManager.getInstance().setTargets(test);
	assertTrue(Arrays.equals(test.toArray(),
				 (TargetManager.getInstance()
				  .getTargets().toArray())));
	TargetManager.getInstance().setTargets(null);
        List expectedValue = new ArrayList();
	assertTrue(Arrays.equals(expectedValue.toArray(),
				 (TargetManager.getInstance()
				  .getTargets().toArray())));

	TargetListener listener = new TestTargetListener();
	TargetManager.getInstance().addTargetListener(listener);
	targetSetCalled = false;
	targetSetTarget = null;
	targetSetObjects = null;
	TargetManager.getInstance().setTargets(test);
	assertTrue(targetSetCalled);
	assertEquals(test.get(0), targetSetTarget);
	assertTrue(Arrays.equals(test.toArray(), targetSetObjects));
	targetSetCalled = false;
	TargetManager.getInstance().setTargets(test);
	assertTrue(!targetSetCalled);
	test.remove(1);
	TargetManager.getInstance().setTargets(test);
	assertTrue(targetSetCalled);

	TargetManager.getInstance().setTargets(test2);
	targetSetCalled = false;
	TargetManager.getInstance().setTargets(test4);
	assertTrue(!targetSetCalled);
	TargetManager.getInstance().setTargets(test5);
	assertTrue(targetSetCalled);
	assertEquals(test5.get(0), targetSetTarget);

	TargetManager.getInstance().removeTargetListener(listener);
    }

    /**
     * Test addTarget().
     */
    public void testAddTarget() {
	List<Object> testList = new ArrayList<Object>();
	for (int i = 0; i < 10; i++) {
	    testList.add(new Object());
	}
	Object testObject = new Object();
	TargetManager.getInstance().addTarget(testObject);
	assertTrue(TargetManager.getInstance()
		   .getTargets().contains(testObject));
	TargetManager.getInstance().setTargets(testList);
	assertTrue(!TargetManager.getInstance()
		   .getTargets().contains(testObject));
	TargetManager.getInstance().addTarget(testObject);
	assertTrue(TargetManager.getInstance()
		   .getTargets().contains(testObject));
	TargetManager.getInstance().addTarget(null);
	assertTrue(TargetManager.getInstance()
	        .getTargets().contains(testObject));
	assertTrue(!TargetManager.getInstance().getTargets().contains(null));

	TargetListener listener = new TestTargetListener();
	TargetManager.getInstance().addTargetListener(listener);
	TargetManager.getInstance().setTargets(testList);
	Object oldTarget = TargetManager.getInstance().getTarget();
	List<Object> newList = new ArrayList<Object>(testList);
	newList.add(testObject);
	targetAddedCalled = false;
	targetAddedTarget = null;
	targetAddedObjects = null;
	TargetManager.getInstance().addTarget(testObject);
	assertTrue(targetAddedCalled);
	assertTrue(newList.containsAll(Arrays.asList(targetAddedObjects))
            && newList.size() == targetAddedObjects.length);
	assertTrue(TargetManager.getInstance().getTarget() == testObject);
	targetAddedCalled = false;
	TargetManager.getInstance().addTarget(testObject);
	assertTrue(!targetAddedCalled);

	TargetManager.getInstance().removeTargetListener(listener);
    }

    /**
     * Test removeTarget().
     */
    public void testRemoveTarget() {
	List<Object> testList = new ArrayList<Object>();
	for (int i = 0; i < 10; i++) {
	    testList.add(new Object());
	}
	Object testObject = new Object();
	TargetManager.getInstance().setTarget(testObject);
	assertTrue(TargetManager.getInstance()
		   .getTargets().contains(testObject));
	TargetManager.getInstance().removeTarget(null);
	assertTrue(TargetManager.getInstance()
		   .getTargets().contains(testObject));
	TargetManager.getInstance().removeTarget(testObject);
	assertTrue(TargetManager.getInstance().getTargets().isEmpty());
	TargetManager.getInstance().removeTarget(null);
	assertTrue(TargetManager.getInstance().getTargets().isEmpty());

	testList.add(testObject);
	TargetManager.getInstance().setTargets(testList);
	assertTrue(TargetManager.getInstance()
		   .getTargets().contains(testObject));
	TargetManager.getInstance().removeTarget(testObject);
	assertTrue(!TargetManager.getInstance()
		   .getTargets().contains(testObject));

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
	assertTrue(Arrays.equals(new Object[] {}, targetRemovedObjects));

	List<Object> testList2 = new ArrayList<Object>();
	testList2.add(new Object());
	testList2.add(testObject);
	TargetManager.getInstance().setTargets(testList2);
	targetRemovedTarget = null;
	targetRemovedObjects = null;
	TargetManager.getInstance().removeTarget(testObject);
	assertEquals(testList2.get(0), targetRemovedTarget);
	assertTrue(Arrays.equals(new Object[] {testList2.get(0)},
				 targetRemovedObjects));
	targetRemovedCalled = false;
	TargetManager.getInstance().removeTarget(testObject);
	assertTrue(!targetRemovedCalled);

	TargetManager.getInstance().removeTargetListener(listener);
    }

    /**
     * Test getFigTarget().
     */
    public void testGetFigTarget() {
	final Object owner = new Object();
	final Fig fig = new FigRect(0, 0, 12, 12); fig.setOwner(owner);
	Object test = new Object();
	ArgoDiagram diag = new ArgoDiagram() {
	    public Fig getContainingFig(Object obj) {
		if (obj == owner)
		    return fig;
		return null;
	    }
	};
	Project p = ProjectManager.getManager().getCurrentProject();
	p.addDiagram(diag);
	TargetManager.getInstance().setTarget(diag);
	List<Object> list1 = new ArrayList<Object>();
	List<Object> list2 = new ArrayList<Object>();
        list2.add(test);
	List<Object> list3 = new ArrayList<Object>();
        list3.add(owner);
        list3.add(test);

	TargetListener listener = new TestTargetListener();
	TargetManager.getInstance().addTargetListener(listener);

	targetSetFigTarget = null;
	TargetManager.getInstance().setTarget(null);
	assertEquals(null, TargetManager.getInstance().getFigTarget());
	assertEquals(null, targetSetFigTarget);

	targetSetFigTarget = null;
	TargetManager.getInstance().setTarget(test);
	assertEquals(null, TargetManager.getInstance().getFigTarget());
	assertEquals(null, targetSetFigTarget);

	targetSetFigTarget = null;
	TargetManager.getInstance().setTarget(owner);
	assertEquals(fig, TargetManager.getInstance().getFigTarget());
	assertEquals(fig, targetSetFigTarget);

	targetRemovedCalled = false;
	TargetManager.getInstance().removeTarget(fig);
	assertEquals(null, TargetManager.getInstance().getFigTarget());
	assertTrue(targetRemovedCalled);

	targetSetFigTarget = null;
	TargetManager.getInstance().setTarget(test);
	assertEquals(null, TargetManager.getInstance().getFigTarget());
	assertEquals(null, targetSetFigTarget);

	TargetManager.getInstance().setTarget(null);
	targetAddedFigTarget = null;
	TargetManager.getInstance().addTarget(test);
	assertEquals(null, TargetManager.getInstance().getFigTarget());
	assertEquals(null, targetAddedFigTarget);

	TargetManager.getInstance().setTarget(null);
	targetAddedFigTarget = null;
	TargetManager.getInstance().addTarget(owner);
	assertEquals(fig, TargetManager.getInstance().getFigTarget());
	assertEquals(fig, targetAddedFigTarget);

	targetRemovedCalled = false;
	TargetManager.getInstance().removeTarget(null);
	assertEquals(fig, TargetManager.getInstance().getFigTarget());
	assertTrue(!targetRemovedCalled);

	targetRemovedFigTarget = null;
	TargetManager.getInstance().removeTarget(test);
	assertEquals(fig, TargetManager.getInstance().getFigTarget());

	TargetManager.getInstance().addTarget(test);
	targetRemovedFigTarget = null;
	TargetManager.getInstance().removeTarget(owner);
	assertEquals(null, TargetManager.getInstance().getFigTarget());
	assertEquals(null, targetRemovedFigTarget);

	targetSetFigTarget = null;
	TargetManager.getInstance().setTargets(list1);
	assertEquals(null, TargetManager.getInstance().getFigTarget());
	assertEquals(null, targetSetFigTarget);

	targetSetFigTarget = null;
	TargetManager.getInstance().setTargets(list2);
	assertEquals(null, TargetManager.getInstance().getFigTarget());
	assertEquals(null, targetSetFigTarget);

	targetSetFigTarget = null;
	TargetManager.getInstance().setTargets(list3);
	assertEquals(fig, TargetManager.getInstance().getFigTarget());
	assertEquals(fig, targetSetFigTarget);

	targetSetFigTarget = null;
	TargetManager.getInstance().setTargets(list2);
	assertEquals(null, TargetManager.getInstance().getFigTarget());
	assertEquals(null, targetSetFigTarget);

	TargetManager.getInstance().removeTargetListener(listener);
    }

    /**
     * Test getModelTarget().
     */
    public void testGetModelTarget() {
	Object owner = Model.getCoreFactory().buildClass();
	Fig fig = new FigRect(0, 0, 12, 12); fig.setOwner(owner);
	Object test = new Object();

	List<Object> list1 = new ArrayList<Object>();
	List<Object> list2 = new ArrayList<Object>(); list2.add(test);
	List<Object> list3 = new ArrayList<Object>(); list3.add(fig); list3.add(test);

	TargetListener listener = new TestTargetListener();
	TargetManager.getInstance().addTargetListener(listener);

	targetSetModelTarget = null;
	TargetManager.getInstance().setTarget(null);
	assertEquals(null, TargetManager.getInstance().getModelTarget());
	assertEquals(null, targetSetModelTarget);

	targetSetModelTarget = null;
	TargetManager.getInstance().setTarget(test);
	assertEquals(null, TargetManager.getInstance().getModelTarget());
	assertEquals(null, targetSetModelTarget);

	targetSetModelTarget = null;
	TargetManager.getInstance().setTarget(fig);
	assertEquals(owner, TargetManager.getInstance().getModelTarget());
	assertEquals(owner, targetSetModelTarget);

	targetRemovedCalled = false;
	TargetManager.getInstance().removeTarget(owner);
	assertEquals(null, TargetManager.getInstance().getModelTarget());
	assertTrue(targetRemovedCalled);

	targetSetModelTarget = null;
	TargetManager.getInstance().setTarget(test);
	assertEquals(null, TargetManager.getInstance().getModelTarget());
	assertEquals(null, targetSetModelTarget);

	TargetManager.getInstance().setTarget(null);
	targetAddedModelTarget = null;
	TargetManager.getInstance().addTarget(test);
	assertEquals(null, TargetManager.getInstance().getModelTarget());
	assertEquals(null, targetAddedModelTarget);

	TargetManager.getInstance().setTarget(null);
	targetAddedModelTarget = null;
	TargetManager.getInstance().addTarget(fig);
	assertEquals(owner, TargetManager.getInstance().getModelTarget());
	assertEquals(owner, targetAddedModelTarget);

	targetAddedModelTarget = null;
	TargetManager.getInstance().addTarget(test);
	assertEquals(null, TargetManager.getInstance().getModelTarget());
	assertEquals(null, targetAddedModelTarget);

	targetRemovedCalled = false;
	TargetManager.getInstance().removeTarget(null);
	assertEquals(null, TargetManager.getInstance().getModelTarget());
	assertTrue(!targetRemovedCalled);

	targetRemovedModelTarget = null;
	TargetManager.getInstance().removeTarget(test);
	assertEquals(owner, TargetManager.getInstance().getModelTarget());
	assertEquals(owner, targetRemovedModelTarget);

	TargetManager.getInstance().addTarget(test);
	targetRemovedModelTarget = null;
	TargetManager.getInstance().removeTarget(fig);
	assertEquals(null, TargetManager.getInstance().getModelTarget());
	assertEquals(null, targetRemovedModelTarget);

	targetSetModelTarget = null;
	TargetManager.getInstance().setTargets(list1);
	assertEquals(null, TargetManager.getInstance().getModelTarget());
	assertEquals(null, targetSetModelTarget);

	targetSetModelTarget = null;
	TargetManager.getInstance().setTargets(list2);
	assertEquals(null, TargetManager.getInstance().getModelTarget());
	assertEquals(null, targetSetModelTarget);

	targetSetModelTarget = null;
	TargetManager.getInstance().setTargets(list3);
	assertEquals(owner, TargetManager.getInstance().getModelTarget());
	assertEquals(owner, targetSetModelTarget);

	targetSetModelTarget = null;
	TargetManager.getInstance().setTargets(list2);
	assertEquals(null, TargetManager.getInstance().getModelTarget());
	assertEquals(null, targetSetModelTarget);

	TargetManager.getInstance().removeTargetListener(listener);
    }

    /**
     * Testing to {@link TargetManager#addTarget(Object) add},
     * {@link TargetManager#setTarget(Object) set}, and
     * {@link TargetManager#removeTarget(Object) remove}
     * a target from within the {@link TargetListener} calls
     * ({@link TargetListener#targetAdded(TargetEvent)},
     * {@link TargetListener#targetRemoved(TargetEvent)}, and
     * {@link TargetListener#targetSet(TargetEvent)}).
     */
    public void testTransaction() {
	class Listener implements TargetListener {
	    private int counter = 0;
	    private List<Object> list = new ArrayList<Object>();

	    /*
	     * @see TargetListener#targetAdded(TargetEvent)
	     */
	    public void targetAdded(TargetEvent e) {
		counter++;
		TargetManager.getInstance().addTarget(new Object());
		TargetManager.getInstance().setTarget(new Object());
		list.add(new Object());
		TargetManager.getInstance().setTargets(list);
		TargetManager.getInstance().removeTarget(e.getNewTarget());
	    }

	    /*
	     * @see TargetListener#targetRemoved(TargetEvent)
	     */
	    public void targetRemoved(TargetEvent e) {
		counter++;
		TargetManager.getInstance().addTarget(new Object());
		TargetManager.getInstance().setTarget(new Object());
		list.add(new Object());
		TargetManager.getInstance().setTargets(list);
		TargetManager.getInstance().removeTarget(e.getNewTarget());
	    }

	    /*
	     * @see TargetListener#targetSet(TargetEvent)
	     */
	    public void targetSet(TargetEvent e) {
		counter++;
		TargetManager.getInstance().addTarget(new Object());
		TargetManager.getInstance().setTarget(new Object());
		list.add(new Object());
		TargetManager.getInstance().setTargets(list);
		TargetManager.getInstance().removeTarget(e.getNewTarget());
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
	List<Object> list = new ArrayList<Object>();
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

    /**
     * Test navigate().
     */
    public void testNavigate() {
        TargetManager.getInstance().cleanHistory();
        int numtargets = 10;
        Object[] targets = new Object[numtargets];
        for (int i = 0; i < numtargets; i++) {
            targets[i] = new Object();
            TargetManager.getInstance().setTarget(targets[i]);
        }
        assertTrue(TargetManager.getInstance().navigateBackPossible());
        assertEquals(false,
		     TargetManager.getInstance().navigateForwardPossible());
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
        assertEquals(false,
		     TargetManager.getInstance().navigateForwardPossible());
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

    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() {
        InitializeModel.initializeDefault();

	TargetManager.getInstance().setTarget(null);
    }

    /*
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() {
	TargetManager.getInstance().setTarget(null);
    }
}
