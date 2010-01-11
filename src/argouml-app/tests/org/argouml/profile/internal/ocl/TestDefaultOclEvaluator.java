/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    maurelio1234
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.profile.internal.ocl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.profile.internal.ocl.uml14.Bag;
import org.argouml.profile.internal.ocl.uml14.Uml14ModelInterpreter;

/**
 * Tests for the EvaluateExpression class.
 * 
 * @author maurelio1234
 */
public class TestDefaultOclEvaluator extends TestCase {
    
    @SuppressWarnings("unused")
    private class DummyModelInterpreter implements ModelInterpreter {

        /**
         * @see org.argouml.profile.internal.ocl.ModelInterpreter#getBuiltInSymbol(java.lang.String)
         */
        public Object getBuiltInSymbol(String sym) {
            return null;
        }

        public Object invokeFeature(HashMap<String, Object> vt, Object subject,
                 String feature, String type, Object[] parameters) {
            return null;
        }

        public Object invokeFeature(Map<String, Object> vt, Object subject,
                String feature, String type, Object[] parameters) {
            return null;
        }
        
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
    }

    /**
     * Test the basic values and types (section 8.4)
     * 
     * @throws Exception if something goes wrong
     */
    public void testBasicValuesAndTypes() throws Exception {
        assertEquals(DefaultOclEvaluator.getInstance()
                .evaluate(null, null, "true"), true);
        assertEquals(DefaultOclEvaluator.getInstance()
                .evaluate(null, null, "false"), false);
        assertEquals(DefaultOclEvaluator.getInstance()
                .evaluate(null, null, "5"), 5);        
        assertEquals(DefaultOclEvaluator.getInstance()
                .evaluate(null, null, "-2"), -2);

        // TODO real numbers not yet supported
        //assertEquals(DefaultOclEvaluator.getInstance()
                //.evaluate(null, null, "1.5"), 1.5);
        
        // TODO write test for enumerations, although they are not yet supported
        assertEquals(DefaultOclEvaluator.getInstance().evaluate(null, null,
                "'to be or not to be'"), "to be or not to be");
    }
    
    /**
     * Test the let expressions (section 8.4.3)
     * 
     * @throws Exception if something goes wrong
     */
    public void testLetExpressions() throws Exception {
        assertEquals(DefaultOclEvaluator.getInstance().evaluate(
                new HashMap<String, Object>(), new DummyModelInterpreter(),
                "let x : Integer = 12 in x * 5"), 60);
    }
    
    // TODO do tests for type conformance (8.4.4),
    // even though it is not yet supported
    
    // TODO do tests for infix operators (8.4.7),
    // even though it is not yet supported

    /**
     * Test the undefined values (section 8.4.10)
     * 
     * @throws Exception if something goes wrong
     */
    public void testUndefined() throws Exception {
        HashMap<String, Object> vt = new HashMap<String, Object>();
        ModelInterpreter mi = new DummyModelInterpreter();
        
        assertEquals(DefaultOclEvaluator.getInstance().evaluate(vt, mi,
                "false and x"), false);
        assertEquals(DefaultOclEvaluator.getInstance().evaluate(vt, mi,
                "x and false"), false);
        assertEquals(DefaultOclEvaluator.getInstance().evaluate(vt, mi,
                "true or x"), true);
        assertEquals(DefaultOclEvaluator.getInstance().evaluate(vt, mi,
                "x or true"), true);        
    }
    
    /**
     * Test collections operations in objects (section 8.5.4.2)
     * 
     * @throws Exception if something goes wrong
     */
    public void testObjectAsCollection() throws Exception {
        assertEquals(DefaultOclEvaluator.getInstance().evaluate(null,
                new Uml14ModelInterpreter(), "2->size()"), 1);
    }

    // TODO do tests for navigation to association classes (8.5.5),
    // even though it is not yet supported

    /**
     * Test predefined properties on all objects (section 8.5.10)
     * 
     * @throws Exception if something goes wrong
     */
    public void testPredefinedPropertiesAllObjects() throws Exception {
        Object iface = Model.getCoreFactory().buildInterface();
        
        HashMap<String, Object> vt = new HashMap<String, Object>();
        ModelInterpreter mi = new Uml14ModelInterpreter();
        
        vt.put("x", iface);

        assertEquals(DefaultOclEvaluator.getInstance().evaluate(vt, mi, "x"),
                iface);
        
        assertEquals(DefaultOclEvaluator.getInstance().evaluate(vt, mi,
            "x.oclIsTypeOf(Interface)"), true);
        
//        assertEquals(DefaultOclEvaluator.getInstance().evaluate(vt, mi,
//                "x.oclIsKindOf(Classifier)"), true);               
    }

    // TODO do tests for allInstances (8.5.11),
    // even though it is not yet supported

    // TODO do tests for ranges in collection definition (8.5.12),
    // even though it is not yet supported

    /**
     * Test set constructor (section 8.5.12)
     * 
     * @throws Exception if something goes wrong
     */
    @SuppressWarnings("unchecked")
    public void testSetConstructors() throws Exception {
        HashMap<String, Object> vt = new HashMap<String, Object>();
        ModelInterpreter mi = new Uml14ModelInterpreter();
        
        Object obj = DefaultOclEvaluator.getInstance().evaluate(vt, mi,
                "Set{1,2,5,88}");

        assertTrue(obj instanceof Set);
        
        Set<Object> oset = (Set<Object>) obj;
        assertTrue(oset.size() == 4);
        assertTrue(oset.contains(1));
        assertTrue(oset.contains(2));
        assertTrue(oset.contains(5));        
        assertTrue(oset.contains(88));

    }
    
    /**
     * Test bag constructor (section 8.5.12)
     * 
     * @throws Exception if something goes wrong
     */
    @SuppressWarnings("unchecked")
    public void testBagConstructors() throws Exception {
        HashMap<String, Object> vt = new HashMap<String, Object>();
        ModelInterpreter mi = new Uml14ModelInterpreter();
        
        Object obj = DefaultOclEvaluator.getInstance().evaluate(vt, mi,
                "Bag{1,3,4,3,5}");

        assertTrue(obj instanceof Bag);
        
        Bag<Object> obag = (Bag<Object>) obj;
        assertTrue(obag.size() == 5);
        assertTrue(obag.contains(1));
        assertTrue(obag.contains(3));
        assertTrue(obag.contains(4));        
        assertTrue(obag.contains(5));
        assertTrue(obag.count(1) == 1);
        assertTrue(obag.count(3) == 2);
    }

    /**
     * Test sequence constructor (section 8.5.12)
     * 
     * @throws Exception if something goes wrong
     */
    @SuppressWarnings("unchecked")
    public void testSequenceConstructors() throws Exception {
        HashMap<String, Object> vt = new HashMap<String, Object>();
        ModelInterpreter mi = new Uml14ModelInterpreter();
        
        Object obj = DefaultOclEvaluator.getInstance().evaluate(vt, mi,
                "Sequence{'ape', 'nut'}");

        assertTrue(obj instanceof List);
        
        List<Object> oseq = (List<Object>) obj;
        assertTrue(oseq.size() == 2);
        assertTrue(oseq.contains("ape"));
        assertTrue(oseq.contains("nut"));
        assertTrue(oseq.get(0).equals("ape"));
        assertTrue(oseq.get(1).equals("nut"));
    }

    // TODO do tests for collections of collections (8.5.13),
    // even though it is not yet supported 
    // question: should this happen with all collections or only 
    // the constructed ones?

    /**
     * Test select operation (section 8.6.1)
     * 
     * @throws Exception if something goes wrong
     */
    @SuppressWarnings("unchecked")
    public void testSelectOperation() throws Exception {
        HashMap<String, Object> vt = new HashMap<String, Object>();
        ModelInterpreter mi = new Uml14ModelInterpreter();
        
        Object obj = DefaultOclEvaluator.getInstance().evaluate(vt, mi,
                "Set{1,2,3,4,5}->select(x|x>3)");

        assertTrue(obj instanceof Collection);
        
        Collection<Object> ocol = (Collection<Object>) obj;
        assertTrue(ocol.size() == 2);
        assertTrue(ocol.contains(4));
        assertTrue(ocol.contains(5));
    }
  
    /**
     * Test reject operation (section 8.6.1)
     * 
     * @throws Exception if something goes wrong
     */
    @SuppressWarnings("unchecked")
    public void testRejectOperation() throws Exception {
        HashMap<String, Object> vt = new HashMap<String, Object>();
        ModelInterpreter mi = new Uml14ModelInterpreter();
        
        Object obj = DefaultOclEvaluator.getInstance().evaluate(vt, mi,
                "Set{1,2,3,4,5}->reject(x|x>3)");

        assertTrue(obj instanceof Collection);
        
        Collection<Object> ocol = (Collection<Object>) obj;
        assertTrue(ocol.size() == 3);
        assertTrue(ocol.contains(1));
        assertTrue(ocol.contains(2));
        assertTrue(ocol.contains(3));
    }

    /**
     * Test collect operation (section 8.6.2 & 8.6.2.1)
     * 
     * @throws Exception if something goes wrong
     */
    @SuppressWarnings("unchecked")
    public void testCollectOperation() throws Exception {        
        HashMap<String, Object> vt = new HashMap<String, Object>();
        ModelInterpreter mi = new Uml14ModelInterpreter();

        Set<Object> classes = new HashSet<Object>();
        classes.add(Model.getCoreFactory().buildClass("class1"));
        classes.add(Model.getCoreFactory().buildClass("class2"));
        classes.add(Model.getCoreFactory().buildClass("class2"));
        
        vt.put("x", classes);
        
        Object obj1 = DefaultOclEvaluator.getInstance().evaluate(vt, mi,
                "x.name");
        assertTrue(obj1 instanceof Collection);        
        assertTrue(((Collection<Object>) obj1).size() == 3);

        Object obj2 = DefaultOclEvaluator.getInstance().evaluate(vt, mi,
                "x->collect(x|x.name)");
        assertTrue(obj2 instanceof Collection);        
        assertTrue(((Collection<Object>) obj2).size() == 3);
        assertEquals(obj1, obj2);
    }

    /**
     * Test forAll operation (section 8.6.3)
     * 
     * @throws Exception if something goes wrong
     */
    @SuppressWarnings("unchecked")
    public void testForAllOperation() throws Exception {
        HashMap<String, Object> vt = new HashMap<String, Object>();
        ModelInterpreter mi = new Uml14ModelInterpreter();
        
        assertEquals(DefaultOclEvaluator.getInstance().evaluate(vt, mi,
                "Set{1,2,3,4,5}->forAll(x|x>0)"), true);
        assertEquals(DefaultOclEvaluator.getInstance().evaluate(vt, mi,
                "Set{1,2,3,4,5}->forAll(x|x<4)"), false);
        
    }

    /**
     * Test exists operation (section 8.6.4)
     * 
     * @throws Exception if something goes wrong
     */
    @SuppressWarnings("unchecked")
    public void testExistsOperation() throws Exception {
        HashMap<String, Object> vt = new HashMap<String, Object>();
        ModelInterpreter mi = new Uml14ModelInterpreter();
        
        assertEquals(DefaultOclEvaluator.getInstance().evaluate(vt, mi,
                "Set{1,2,3,4,5}->exists(x|x>3)"), true);
        assertEquals(DefaultOclEvaluator.getInstance().evaluate(vt, mi,
                "Set{1,2,3,4,5}->exists(x|x>40)"), false);
        
    }
        
}

