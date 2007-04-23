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
import org.argouml.model.Model;
import org.argouml.notation.providers.uml.CallStateNotationUml;

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
        Object model =
            Model.getModelManagementFactory().createModel();
        Project p = ProjectManager.getManager().getCurrentProject();
        p.setRoot(model);
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
     * An extra setup fiunction.
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
        assertTrue("Notation not correctly generated - "
                + "Resulted in: '" + notationStr + "'", 
                notationStr.equals("myOper"));
    }

    /**
     * Test if the string generated for a named class and operation is correct.
     */
    public void testStringGenerationClassName() {
        setUp2();
        CallStateNotationUml notation = new CallStateNotationUml(aCallState);
        Model.getCoreHelper().setName(aClass, "ClassA");
        String notationStr = notation.toString(aCallState, null);
        assertTrue("Notation not correctly generated - "
                + "Resulted in: '" + notationStr + "'", 
                notationStr.equals("myOper\n(ClassA)"));
    }
    
    /**
     * Test generating the modelelements from a string, and then 
     * re-creating the string from the modelelements. 
     * The resulting string should be equal to the original.
     */
    public void testGenerateRoundTrip() {
        CallStateNotationUml notation = new CallStateNotationUml(aCallState);
        Model.getCoreHelper().setName(aClass, "ClassA");
        try {
            notation.parseCallState(aCallState, "myOper\n(ClassA)");
        } catch (ParseException e) {
            assertTrue("Unexpected exception: " + e.getMessage(), true);
        }
        Object entry = Model.getFacade().getEntry(aCallState); 
        assertTrue("No entry action generated", entry != null);
        Object op = Model.getFacade().getOperation(entry);
        assertTrue("Operation not linked to entry action", op != null);
        String notationStr = notation.toString(aCallState, null);
        assertTrue("Notation not correctly generated - "
                + "Resulted in: '" + notationStr + "'", 
                notationStr.equals("myOper\n(ClassA)"));
    }

    /**
     * Test changing the operation.
     */
    public void testChangeOperation() {
        CallStateNotationUml notation = new CallStateNotationUml(aCallState);
        Model.getCoreHelper().setName(aClass, "ClassA");
        setUp2();
        try {
            notation.parseCallState(aCallState, "myOper2\n(ClassA)");
        } catch (ParseException e) {
            assertTrue("Unexpected exception: " + e.getMessage(), true);
        }
        Object entry = Model.getFacade().getEntry(aCallState); 
        assertTrue("No entry action generated", entry != null);
        Object op = Model.getFacade().getOperation(entry);
        assertTrue("Operation not linked to entry action", op != null);
        String name = Model.getFacade().getName(op);
        assertTrue("Operation name incorrect - resulted in: '" + name + "'", 
                "myOper2".equals(name));
        String notationStr = notation.toString(aCallState, null);
        assertTrue("Notation not correctly generated - "
                + "Resulted in: '" + notationStr + "'", 
                notationStr.equals("myOper2\n(ClassA)"));
    }
    
    /**
     * Test changing the Class and Operation.
     */
    public void testChangeOperationAndClass() {
        CallStateNotationUml notation = new CallStateNotationUml(aCallState);
        Model.getCoreHelper().setName(aClass, "ClassA");
        Model.getCoreHelper().setName(aClass2, "ClassB");
        setUp2();
        try {
            notation.parseCallState(aCallState, "myOperB\n(ClassB)");
        } catch (ParseException e) {
            assertTrue("Unexpected exception: " + e.getMessage(), true);
        }
        Object entry = Model.getFacade().getEntry(aCallState); 
        assertTrue("No entry action generated", entry != null);
        Object op = Model.getFacade().getOperation(entry);
        assertTrue("Operation not linked to entry action", op != null);
        String name = Model.getFacade().getName(op);
        assertTrue("Operation name incorrect - resulted in: '" + name + "'", 
                "myOperB".equals(name));
        String notationStr = notation.toString(aCallState, null);
        assertTrue("Notation not correctly generated - "
                + "Resulted in: '" + notationStr + "'", 
                notationStr.equals("myOperB\n(ClassB)"));
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
            assertTrue("Expected 'wrong brackets' exception did not happen", 
                    true);
        } catch (ParseException e) {
            //ok
        }
        try {
            notation.parseCallState(aCallState, "myOper\nClassA)");
            assertTrue("Expected 'wrong brackets' exception did not happen", 
                    true);
        } catch (ParseException e) {
            //ok
        }
        try {
            notation.parseCallState(aCallState, "myOper\n)ClassA(");
            assertTrue("Expected 'wrong brackets' exception did not happen", 
                    true);
        } catch (ParseException e) {
            //ok
        }
        try {
            notation.parseCallState(aCallState, "myOperX\n(ClassA)");
            assertTrue("Expected 'Operation not found' exception "
                        + "did not happen", true);
        } catch (ParseException e) {
            //ok
        }
        try {
            notation.parseCallState(aCallState, "myOper\n(ClassB)");
            assertTrue("Expected 'Operation not found' exception "
                        + "did not happen", true);
        } catch (ParseException e) {
            //ok
        }
        try {
            notation.parseCallState(aCallState, "myOper\n(ClassX)");
            assertTrue("Expected 'Classifier not found' exception "
                        + "did not happen", true);
        } catch (ParseException e) {
            //ok
        }
        try {
            notation.parseCallState(aCallState, "X");
            assertTrue("Expected exception did not happen", true);
        } catch (ParseException e) {
            //ok
        }
    }
    
    /**
     * The entered string does not have a linefeed.
     */
    public void testGenerateNoLineFeed() {
        CallStateNotationUml notation = new CallStateNotationUml(aCallState);
        Model.getCoreHelper().setName(aClass, "ClassA");
        try {
            notation.parseCallState(aCallState, "myOper(ClassA)");
        } catch (ParseException e) {
            assertTrue("Unexpected exception: " + e.getMessage(), true);
        }
        Object entry = Model.getFacade().getEntry(aCallState); 
        assertTrue("No entry action generated", entry != null);
        Object op = Model.getFacade().getOperation(entry);
        assertTrue("Operation not linked to entry action", op != null);
        String notationStr = notation.toString(aCallState, null);
        assertTrue("Notation not correctly generated - "
                + "Resulted in: '" + notationStr + "'", 
                notationStr.equals("myOper\n(ClassA)"));
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
