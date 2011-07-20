/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
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

package org.argouml.notation.providers.uml;

import junit.framework.TestCase;

import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.init.InitProfileSubsystem;

/**
 * Tests for the notation of an ActionState.
 *
 * @author Michiel
 */
public class TestActionStateNotationUml extends TestCase {
    private Object aClass;
    private Object aStateMachine;
    private Object aOper;
    private Object aState;
    private Object aActionState;
    private Object aUninterpretedAction;

    /**
     * The constructor.
     * 
     * @param arg0 the name
     */
    public TestActionStateNotationUml(String arg0) {
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
        aClass = Model.getCoreFactory().buildClass(model);
        aStateMachine =
            Model.getStateMachinesFactory().buildStateMachine(aClass);
        Object top = Model.getFacade().getTop(aStateMachine);
        aState =
            Model.getStateMachinesFactory().buildCompositeState(top);
        aActionState =
            Model.getActivityGraphsFactory().createActionState();
        Model.getStateMachinesHelper().setContainer(aActionState, top);
    }

    
    @Override
    protected void tearDown() throws Exception {
        ProfileFacade.reset();
        super.tearDown();
    }
    
    /**
     * An extra setup method.
     */
    protected void setUp2() {
        String language = "testLanguage";
        String script = "testScript";

        aUninterpretedAction = Model.getCommonBehaviorFactory()
            .buildUninterpretedAction(aActionState);
        Object actionExpression = Model.getDataTypesFactory()
            .createActionExpression(language, script);
        Model.getCommonBehaviorHelper().setScript(aUninterpretedAction, 
                actionExpression);
    }

    /**
     * Test if the string for an absent entry-action is correct.
     */
    public void testStringGenerationNoAction() {
        ActionStateNotationUml notation = 
            new ActionStateNotationUml(aActionState);
        String notationStr = notation.toString(aActionState, 
                NotationSettings.getDefaultSettings());
        assertEquals("Notation not correctly generated "
        		+ "(for absent entry-action)", "", notationStr);
    }
    
    /**
     * Test if the string for an absent expression is correct.
     */
    public void testStringGenerationNoScript() {
        aUninterpretedAction = Model.getCommonBehaviorFactory()
            .buildUninterpretedAction(aActionState);
        ActionStateNotationUml notation = 
            new ActionStateNotationUml(aActionState);
        String notationStr = notation.toString(aActionState, 
                NotationSettings.getDefaultSettings());
        assertEquals("Notation not correctly generated "
        		+ "(for absent script)", "", notationStr);
    }

    /**
     * Test if the string generated for a action state with action 
     * with script is correct.
     */
    public void testStringGenerationWithAction() {
        setUp2();
        ActionStateNotationUml notation = 
            new ActionStateNotationUml(aActionState);
        String notationStr = notation.toString(aActionState, 
                NotationSettings.getDefaultSettings());
        assertEquals("Notation not correctly generated", "testScript",
                notationStr);
    }

    /**
     * Test generating the modelelements from a string, and then 
     * re-creating the string from the modelelements. 
     * The resulting string should be equal to the original.
     */
    public void testGenerateRoundTrip() {
        ActionStateNotationUml notation = 
            new ActionStateNotationUml(aActionState);
        notation.parse(aActionState, "testRoundTrip");

        Object entry = Model.getFacade().getEntry(aActionState); 
        assertNotNull("No entry action generated", entry);
        
        Object actionExpr = Model.getFacade().getScript(entry);
        assertNotNull("No script generated for the entry action", actionExpr);

        String notationStr = notation.toString(aActionState, 
                NotationSettings.getDefaultSettings());
        assertEquals("Notation not correctly generated", "testRoundTrip",
                notationStr);
        
        notation.parse(aActionState, "otherExpression");
        assertEquals("Entry Action not reused", entry, 
                Model.getFacade().getEntry(aActionState));
    }

    /**
     * Test if the correct action types are generated.
     */
    public void testActionType() {
        ActionStateNotationUml notation = 
            new ActionStateNotationUml(aActionState);
        Object returnType = null;
        aOper = Model.getCoreFactory().buildOperation2(aClass, returnType,
            "testUA");
        notation.parse(aActionState, "testUA()");
        Object entry = Model.getFacade().getEntry(aActionState); 
        assertNotNull("No entry action generated", entry);
        assertTrue("No UninterpretedAction generated", Model.getFacade()
                .isAUninterpretedAction(entry));
        /* If there is no script, then the language should not be maintained. */
        Model.getCommonBehaviorHelper().setScript(entry, null);
        notation.parse(aActionState, "testUA()");
        Object script = Model.getFacade().getScript(entry);
        assertTrue("Language not reset",
                "".equals(Model.getDataTypesHelper().getLanguage(script)));
    }

    /**
     * Test if a null body returns an empty string instead of null.
     */
    public void testNullBody() {
        setUp2();
        Object script = Model.getFacade().getScript(aUninterpretedAction);
        Model.getDataTypesHelper().setBody(script, null);
        ActionStateNotationUml notation = 
            new ActionStateNotationUml(aActionState);
        String result = notation.toString(aActionState, 
                NotationSettings.getDefaultSettings());
        assertTrue("Null body did not return empty string", "".equals(result));
    }
    
    /**
     * Test if help is correctly provided.
     */
    public void testGetHelp() {
        ActionStateNotationUml notation = 
            new ActionStateNotationUml(aActionState);
        String help = notation.getParsingHelp();
        assertTrue("No help at all given", help.length() > 0);
        assertTrue("Parsing help not conform for translation", 
                help.startsWith("parsing."));
    }
    
    /**
     * Test if the notationProvider refuses to instantiate 
     * without showing it the right UML element.
     */
    public void testValidObjectCheck() {
        try {
            new ActionStateNotationUml(aStateMachine);
            fail("The NotationProvider did not throw for a wrong UML element.");
        } catch (IllegalArgumentException e) {
            /* Everything fine... */
        } 
    }
}
