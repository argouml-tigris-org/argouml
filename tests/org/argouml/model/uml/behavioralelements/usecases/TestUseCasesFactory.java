
// Copyright (c) 2002 The Regents of the University of California. All

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



package org.argouml.model.uml.behavioralelements.usecases;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.util.CheckUMLModelHelper;

import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.behavior.use_cases.MUseCase;



public class TestUseCasesFactory extends TestCase {
    
    static String[] allModelElements = {
        "Actor",
        "Extend",
        "ExtensionPoint",
        "Include",
        "UseCase",
        "UseCaseInstance",
    }; 

    public TestUseCasesFactory(String n) { super(n); }



    public void testSingleton() {

	Object o1 = UseCasesFactory.getFactory();

	Object o2 = UseCasesFactory.getFactory();

	assertTrue("Different singletons", o1 == o2);

    }



    public void testCreates() {

	String [] objs = {

	    "Actor",

	    "Extend",

	    "ExtensionPoint",

	    "Include",

	    "UseCase",

	    "UseCaseInstance",

	    null

	};



	CheckUMLModelHelper.createAndRelease(this, 

	    UseCasesFactory.getFactory(), objs);

    }
    
    public void testDeleteComplete() {
        CheckUMLModelHelper.deleteComplete(this, 
        UseCasesFactory.getFactory(), 
        allModelElements);
    }
    
    public void testBuildExtend1() {
        MUseCase base = UseCasesFactory.getFactory().createUseCase();
        MUseCase extension = UseCasesFactory.getFactory().createUseCase();
        MExtensionPoint point = UseCasesFactory.getFactory().buildExtensionPoint(base);
        MExtend extend = UseCasesFactory.getFactory().buildExtend(base, extension, point);
        assertTrue("extensionpoint not added to base", !base.getExtensionPoints().isEmpty());
        assertTrue("extend not added to base", !base.getExtends2().isEmpty());
        assertTrue("extend not added to extension", !extension.getExtends().isEmpty());
        assertTrue("extend not added to correct extensionpoint", 
            extend.getExtensionPoints().contains(point) && 
                extend.getExtensionPoints().size() == 1);
    }
    
    protected void setUp() {
        ArgoSecurityManager.getInstance().setAllowExit(true);
    }
       
}


