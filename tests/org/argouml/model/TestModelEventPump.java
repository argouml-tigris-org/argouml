// $Id$
// Copyright (c) 2004 The Regents of the University of California. All
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;

import junit.framework.TestCase;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.model.uml.foundation.core.CoreFactory;

import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MOperationImpl;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.core.MParameterImpl;
import ru.novosoft.uml.model_management.MModel;

/**
 * This test case is initially very much a copy of the applicable parts
 * of the TestUmlModelEventPump test case.
 * 
 * TODO: For the proof of concept purpose it would be valuable if this 
 * test case could be rewritten without importing any of the ru.novosoft
 * things.
 * 
 * TODO: We have not documented exactly what events arrive and when.
 * 
 * @author Linus Tolke
 */
public class TestModelEventPump extends TestCase {
    private MClass elem;
    private boolean eventcalled;
    private TestListener listener;

    /**
     * A mock listener used for the tests.
     */
    private class TestListener implements PropertyChangeListener {
        /**
         * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
         */
        public void propertyChange(PropertyChangeEvent e) {
            // TODO: This log entry tells us what events actually arrive 
            // to the listener. I (Linus) suggest we let it in there until 
            // all event types and contents are fully documented.
            System.out.println("Received event " + e.getSource()
                    	       + "[" + e.getSource().getClass() + "], " 
                    	       + e.getPropertyName() + ", " 
                    	       + e.getOldValue() + ", " 
                    	       + e.getNewValue());
            eventcalled = true;
        }
    }

    /**
     * Constructor for TestUmlModelEventPump.
     *
     * @param arg0 is the name of the test case.
     */
    public TestModelEventPump(String arg0) {
        super(arg0);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        elem = CoreFactory.getFactory().createClass();
        Project project = ProjectManager.getManager().getCurrentProject();
        MModel model = (MModel) project.getRoot();
        model.addOwnedElement(elem);
        eventcalled = false;
        listener = new TestListener();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        if (elem != null) {
            UmlFactory.getFactory().delete(elem);
        }
        listener = null;
    }


    /**
     * Tests if the association from a modelelement to the pump is thrown away
     * after deletion of the element.
     */
    public void testCreateDelete() {
        WeakReference ref = new WeakReference(elem);
        UmlFactory.getFactory().delete(elem);
        elem = null;
        System.gc();
        assertNull(ref.get());
    }

    /**
     * Tests if a listener that registred for a PropertySet event on
     * the class level really receive the event.
     */
    public void testPropertySetClass() {
        Model.getPump().addClassModelEventListener(listener,
                				   elem.getClass(),
                				   new String[] {
						       "isRoot" 
        					   });
        elem.setRoot(true);
        assertTrue(eventcalled);
    }

    /**
     * Tests if a listener that registred for a Removed event on the
     * class level really receive the event.
     */
    public void testRemovedClass() {
        Model.getPump().addClassModelEventListener(listener,
                				   elem.getClass(),
                				   new String[] {
						       "remove" 
        					   });
        elem.remove();
        assertTrue(eventcalled);
    }

    /**
     * Tests if a listener that registred for a RoleAdded event on the
     * class level really receive the event.
     */
    public void testRoleAddedSetClass() {
        Model.getPump().addClassModelEventListener(listener,
                				   elem.getClass(),
                				   new String[] {
						       "parameter" 
        					   });
        elem.addParameter(new MParameterImpl());
        assertTrue(eventcalled);
    }

    /**
     * Tests if a listener that registred for a RoleRemoved event on
     * the class level really receive the event.
     */
    public void testRoleRemovedSetClass() {
        MParameter param = new MParameterImpl();
        elem.addParameter(param);
        Model.getPump().addClassModelEventListener(listener,
                				   elem.getClass(),
                				   new String[] {
						       "parameter" 
        					   });
        elem.removeParameter(param);
        assertTrue(eventcalled);
    }

    /**
     * Tests if a non registred listener does not receive any events (never can
     * be too sure :))
     */
    public void testFireNonRegistredListener() {
        MClass elem2 = CoreFactory.getFactory().createClass();
        elem.addParameter(new MParameterImpl());
        assertTrue(!eventcalled);
    }

    /**
     * Tests if a listener that registred for a ListRoleItemSet event really
     * receive the event.
     */
    public void testListRoleItemSet() {
        elem.addFeature(new MOperationImpl());
        Model.getPump().addModelEventListener(listener,
                			      elem,
                			      new String[] {
                				  "feature" 
        				      });
        elem.setFeature(0, new MOperationImpl());
        assertTrue(eventcalled);
    }

    /**
     * Tests if a listener that registred for a PropertySet event really
     * receive the event.
     */
    public void testPropertySet() {
        Model.getPump().addModelEventListener(listener,
                			      elem,
                			      new String[] {
                				  "isRoot" 
        				      });
        elem.setRoot(true);
        assertTrue(eventcalled);
    }

    /**
     * Tests if a listener that registred for a Removed event really
     * receive the event.
     */
    public void testRemoved() {
        Model.getPump().addModelEventListener(listener,
                			      elem,
                			      new String[] {
				       		  UmlModelEventPump.REMOVE 
				   	      });
        elem.remove();
        assertTrue(eventcalled);
    }

    /**
     * Tests if a listener that registred for a RoleAddedSet event really
     * receive the event.
     */
    public void testRoleAddedSet() {
        Model.getPump().addModelEventListener(listener,
                			      elem,
                			      new String[] {
                				  "parameter" 
        				      });
        elem.addParameter(new MParameterImpl());
        assertTrue(eventcalled);
    }

    /**
     * Tests if a listener that registred for a RoleRemovedSet event really
     * receive the event.
     */
    public void testRoleRemovedSet() {
        MParameter param = new MParameterImpl();
        elem.addParameter(param);
        Model.getPump().addModelEventListener(listener,
                			      elem,
                			      new String[] {
                				  "parameter" 
        				      });
        elem.removeParameter(param);
        assertTrue(eventcalled);
    }

    /**
     * Tests if a listener to a class that is legally added and then
     * removed, really is removed.
     */
    public void testRemoveLegalClassListener() {
        Model.getPump().addClassModelEventListener(listener,
                				   elem.getClass(),
                				   new String[] {
						       "isRoot" 
        					   });
        Model.getPump().removeClassModelEventListener(listener,
                				      elem.getClass(),
                				      new String[] {
					       		  "isRoot"
        					      });
        elem.addParameter(new MParameterImpl());
        assertTrue(!eventcalled);
    }

    /**
     * Tests if a listener that is legally added and then removed,
     * really is removed.
     */
    public void testRemoveLegalListener() {
        String[] map = new String[] {
            "isRoot" 
        }; 
        Model.getPump().addModelEventListener(listener, elem, map);
        Model.getPump().removeModelEventListener(listener, elem, map);
        elem.addParameter(new MParameterImpl());
        assertTrue(!eventcalled);
    }
}
