// $Id$
// Copyright (c) 2006-2007 The Regents of the University of California. All
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

package org.argouml.notation.providers.uml;

import java.text.ParseException;

import junit.framework.TestCase;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.profile.InitProfileSubsystem;

/**
 * Test the notation for a CallState.
 *
 * @author Michiel
 */
public class TestCallStateNotationUml extends TestCase {
    private Object aClass, aClass2;
    private Object aStateMachine;
    private Object aOper, aOper2, aOperB;
    private Object aState;
    private Object aCallState;
    private Object aCallAction;

    /**
     * The constructor.
     * 
     * @param arg0 the name
     */
    public TestCallStateNotationUml(String arg0) {
        super(arg0);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() {
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
        Object model =
            Model.getModelManagementFactory().createModel();
        Project p = ProjectManager.getManager().getCurrentProject();
        p.addModel(model);
        aClass = Model.getCoreFactory().buildClass(model);
        aClass2 = Model.getCoreFactory().buildClass(model);
        Object returnType = p.getDefaultReturnType();
        aOper = Model.getCoreFactory().buildOperation2(aClass, returnType,
                "myOper");
        aOper2 = Model.getCoreFactory().buildOperation2(aClass, returnType,
                "myOper2");
        aOperB = Model.getCoreFactory().buildOperation2(aClass2, returnType,
                "myOperB");
        aStateMachine =
            Model.getStateMachinesFactory().buildStateMachine(aClass);
        Object top = Model.getFacade().getTop(aStateMachine);
        aState =
            Model.getStateMachinesFactory().buildCompositeState(top);
        aCallState =
            Model.getActivityGraphsFactory().createCallState();
        Model.getStateMachinesHelper().setContainer(aCallState, top);
    }
    
    /**
     * An extra setup method.
     */
    protected void setUp2() {
        aCallAction = 
            Model.getCommonBehaviorFactory().buildCallAction(aOper, "myAction");
        Model.getStateMachinesHelper().setEntry(aCallState, aCallAction);
    }
    
    /**
     * Test if the string for an un-named class is correct.
     */
    public void testStringGenerationNoClassName() {
        setUp2();
        CallStateNotationUml notation = new CallStateNotationUml(aCallState);
        String notationStr = notation.toString(aCallState, null);
        assertEquals("Notation not correctly generated", "myOper", notationStr);
    }

    /**
     * Test if the string generated for a named class and operation is correct.
     */
    public void testStringGenerationClassName() {
        setUp2();
        CallStateNotationUml notation = new CallStateNotationUml(aCallState);
        Model.getCoreHelper().setName(aClass, "ClassA");
        String notationStr = notation.toString(aCallState, null);
        assertEquals("Notation not correctly generated", "myOper\n(ClassA)",
                notationStr);
    }
    
    /**
     * Test generating the modelelements from a string, and then 
     * re-creating the string from the modelelements. 
     * The resulting string should be equal to the original.
     */
    public void testGenerateRoundTrip() throws ParseException {
        CallStateNotationUml notation = new CallStateNotationUml(aCallState);
        Model.getCoreHelper().setName(aClass, "ClassA");
        notation.parseCallState(aCallState, "myOper\n(ClassA)");
        Object entry = Model.getFacade().getEntry(aCallState); 
        assertNotNull("No entry action generated", entry);
        Object op = Model.getFacade().getOperation(entry);
        assertNotNull("Operation not linked to entry action", op);
        String notationStr = notation.toString(aCallState, null);
        assertEquals("Notation not correctly generated", "myOper\n(ClassA)",
                notationStr);
    }

    /**
     * Test changing the operation.
     * @throws ParseException on failure
     */
    public void testChangeOperation() throws ParseException {
        CallStateNotationUml notation = new CallStateNotationUml(aCallState);
        Model.getCoreHelper().setName(aClass, "ClassA");
        setUp2();
        notation.parseCallState(aCallState, "myOper2\n(ClassA)");
        Object entry = Model.getFacade().getEntry(aCallState); 
        assertNotNull("No entry action generated", entry);
        Object op = Model.getFacade().getOperation(entry);
        assertNotNull("Operation not linked to entry action", op);
        String name = Model.getFacade().getName(op);
        assertEquals("Operation name incorrect", "myOper2", name);
        String notationStr = notation.toString(aCallState, null);
        assertEquals("Notation not correctly generated", "myOper2\n(ClassA)",
                notationStr);
    }
    
    /**
     * Test changing the Class and Operation.
     * @throws ParseException on failure
     */
    public void testChangeOperationAndClass() throws ParseException {
        CallStateNotationUml notation = new CallStateNotationUml(aCallState);
        Model.getCoreHelper().setName(aClass, "ClassA");
        Model.getCoreHelper().setName(aClass2, "ClassB");
        setUp2();
        notation.parseCallState(aCallState, "myOperB\n(ClassB)");
        Object entry = Model.getFacade().getEntry(aCallState); 
        assertNotNull("No entry action generated", entry);
        Object op = Model.getFacade().getOperation(entry);
        assertNotNull("Operation not linked to entry action", op);
        String name = Model.getFacade().getName(op);
        assertEquals("Operation name incorrect" ,"myOperB", name);
        String notationStr = notation.toString(aCallState, null);
        assertEquals("Notation not correctly generated", "myOperB\n(ClassB)", notationStr);
    }
    
    /**
     * Test is some mistakes in the string do generate 
     * exceptions as they should.
     */
    public void testExceptionalCases() {
        CallStateNotationUml notation = new CallStateNotationUml(aCallState);
        Model.getCoreHelper().setName(aClass, "ClassA");
        try {
            notation.parseCallState(aCallState, "myOper\n(ClassA");
            fail("Expected 'wrong brackets' exception did not happen");
        } catch (ParseException e) {
            //ok
        }
        try {
            notation.parseCallState(aCallState, "myOper\nClassA)");
            fail("Expected 'wrong brackets' exception did not happen");
        } catch (ParseException e) {
            //ok
        }
        try {
            notation.parseCallState(aCallState, "myOper\n)ClassA(");
            fail("Expected 'wrong brackets' exception did not happen");
        } catch (ParseException e) {
            //ok
        }
        try {
            notation.parseCallState(aCallState, "myOperX\n(ClassA)");
            fail("Expected 'Operation not found' exception "
                        + "did not happen");
        } catch (ParseException e) {
            //ok
        }
        try {
            notation.parseCallState(aCallState, "myOper\n(ClassB)");
            fail("Expected 'Operation not found' exception "
                        + "did not happen");
        } catch (ParseException e) {
            //ok
        }
        try {
            notation.parseCallState(aCallState, "myOper\n(ClassX)");
            fail("Expected 'Classifier not found' exception "
                        + "did not happen");
        } catch (ParseException e) {
            //ok
        }
        try {
            notation.parseCallState(aCallState, "X");
            fail("Expected exception did not happen");
        } catch (ParseException e) {
            //ok
        }
    }
    
    /**
     * The entered string does not have a newline.
     */
    public void testGenerateNoLineFeed() throws ParseException {
        CallStateNotationUml notation = new CallStateNotationUml(aCallState);
        Model.getCoreHelper().setName(aClass, "ClassA");
        notation.parseCallState(aCallState, "myOper(ClassA)");
        Object entry = Model.getFacade().getEntry(aCallState);
        assertNotNull("No entry action generated", entry);
        Object op = Model.getFacade().getOperation(entry);
        assertNotNull("Operation not linked to entry action", op);
        String notationStr = notation.toString(aCallState, null);
        assertEquals("Notation not correctly generated", "myOper\n(ClassA)",
                notationStr);
    }
    
    /**
     * Test if help is correctly provided.
     */
    public void testGetHelp() {
        CallStateNotationUml notation = new CallStateNotationUml(aCallState);
        String help = notation.getParsingHelp();
        assertTrue("No help at all given", help.length() > 0);
        assertTrue("Parsing help not conform for translation", 
                help.startsWith("parsing."));
    }
}
