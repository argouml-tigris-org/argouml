// Copyright (c) 1996-2003 The Regents of the University of California. All
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

// $Id$
package org.argouml.uml.ui;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.uml.diagram.static_structure.ui.FigClass;
import org.tigris.gef.presentation.Fig;

/**
 * An abstract test class that forms the basis for testing actions subclassed
 * from UMLAction. It provides several helper functions and templates for testing.
 * @author jaap.branderhorst@xs4all.nl
 * Jul 20, 2003
 */
public abstract class AbstractTestUMLAction extends TestCase {

    /**
     * The action to test
     */
    private UMLAction _action;
    
    /**
     * @param arg0
     */
    public AbstractTestUMLAction(String arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }
    
    
    /**
     * Tests if a non-null fig target for shouldBeEnabled leads to a correct
     * value of shouldBeEnabled 
     *
     */
    public void testShouldBeEnabledOneFigTarget() {
        Fig fig = new FigClass();
        fig.setOwner(CoreFactory.getFactory().buildClass());
        assertTrue(getAction().shouldBeEnabled(new Object[] {fig}));        
    }
    
    /**
     * Must return true if shouldBeEnabled of the to be tested action should return
     * true when the target is a fig. Otherwise must return false.
     * @return true if shouldBeEnabled must return true when a fig is the target.
     */
    public abstract boolean getValueShouldBeEnabledOneFigTarget();

    /**
     * Tests if a non-null modelelement target for shouldBeEnabled leads to a correct
     * value of shouldBeEnabled.
     *
     */
    public void testShouldBeEnabledOneModelTarget() {
        Object modelElem = CoreFactory.getFactory().buildClass();
        assertTrue(getAction().shouldBeEnabled(new Object[] {modelElem}));        
    }
    
    /**
     * Must return true if shouldBeEnabled of the to be tested action should return
     * true when the target is a modelelement. Otherwise must return false.
     * @return true if shouldBeEnabled must return true when a modelelement is the target.
     */
    public abstract boolean getValueShouldBeEnabledOneModelTarget();
    
    /**
    * Tests if a non-null modelelement target for shouldBeEnabled leads to a correct
    * value of shouldBeEnabled.
    *
    */
   public void testShouldBeEnabledNullTarget() {
       Object modelElem = CoreFactory.getFactory().buildClass();
       assertTrue(getAction().shouldBeEnabled(new Object[] {null}));        
   }

   /**
    * Must return true if shouldBeEnabled of the to be tested action should return
    * true when the target is null. Otherwise must return false.
    * @return true if shouldBeEnabled must return true when the target is null.
    */
   public abstract boolean getValueShouldBeEnabledNullTarget();

    
    
    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {       
        super.setUp();
        ArgoSecurityManager.getInstance().setAllowExit(true);
        UmlFactory.getFactory().setGuiEnabled(false);
        _action = createAction();                
    }

    /**
     * Method must return the action to be tested.
     * @return
     */
    protected abstract UMLAction createAction();


    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {        
        super.tearDown();
        _action = null;
    }

    /**
     * @return
     */
    public UMLAction getAction() {
        return _action;
    }

    /**
     * @param action
     */
    public void setAction(UMLAction action) {
        _action = action;
    }

}
